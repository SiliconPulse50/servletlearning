package GuardedSuspension2;


import lombok.extern.slf4j.Slf4j;
import java.util.LinkedList;
import java.util.Random;

@Slf4j(topic = "c.Test21")
public class Test21 {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个容量为 2 的消息队列
        MessageQueue queue = new MessageQueue(2);

        // 1. 开启 3 个生产者线程，往队列里疯狂塞消息
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            }, "生产者" + i).start();
        }

        // 2. 主线程睡 1 秒，让生产者把队列塞满（容量是2，所以第3个生产者会被卡住）
        Thread.sleep(1000);

        // 3. 开启 1 个消费者线程，每隔 1 秒取一条消息
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // 模拟消费者处理消息很慢
                    Message message = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "消费者").start();
    }
}

/**
 * 消息实体类
 */
class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }
    public int getId() { return id; }
    public Object getValue() { return value; }
    @Override
    public String toString() {
        return "Message{id=" + id + ", value=" + value + "}";
    }
}

/**
 * 消息队列（核心类）
 */
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    // 消息的队列集合（用 LinkedList 因为需要频繁头部删除、尾部添加）
    private LinkedList<Message> list = new LinkedList<>();
    // 队列容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 消费者：获取消息
    public Message take() {
        // 检查队列是否为空
        synchronized (list) {
            // 【重点】：必须用 while，防止虚假唤醒
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者等待...");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 从队列头部获取消息并返回
            Message message = list.removeFirst();
            log.debug("消费者拿走了一条消息: {}", message);

            // 【重点】：唤醒所有在 list 上等待的线程（主要是唤醒生产者，告诉他们有空位了）
            list.notifyAll();
            return message;
        }
    }

    // 生产者：存入消息
    public void put(Message message) {
        synchronized (list) {
            // 检查对象是否已满
            // 【重点】：必须用 while，防止虚假唤醒
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满，生产者等待...");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将消息加入队列尾部
            list.addLast(message);
            log.debug("生产者放入了一条消息: {}", message);

            // 【重点】：唤醒所有在 list 上等待的线程（主要是唤醒消费者，告诉他们有货了）
            list.notifyAll();
        }
    }
}