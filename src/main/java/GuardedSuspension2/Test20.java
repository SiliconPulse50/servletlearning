package GuardedSuspension2;


import lombok.extern.slf4j.Slf4j;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.Test20")
public class Test20 {
    public static void main(String[] args) throws InterruptedException {
        // 1. 开启 3 个收件人（People）线程
        for (int i = 0; i < 3; i++) {
            new People().start();
        }

        // 2. 主线程睡 1 秒，确保上面 3 个收件人已经把信箱建好了
        Thread.sleep(1000);

        // 3. 获取当前所有正在等待的信箱 id
        Set<Integer> ids = Mailboxes.getIds();
        log.debug("当前有这些编号的信箱在等待: {}", ids);

        // 4. 针对每个等待的信箱，派一个快递员（Postman）去送信
        for (Integer id : ids) {
            new Postman(id, "你好呀，这是信箱[" + id + "]的信！").start();
        }
    }
}

/**
 * 1. 信箱类（保护性暂停对象，带超时逻辑）
 */
class GuardedObject {
    private int id; // 信箱的唯一编号
    private Object response; // 里面的信

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 收件人：取信（带超时）
    public Object get(long timeout) {
        synchronized (this) {
            long begin = System.currentTimeMillis();
            long passedTime = 0;

            while (response == null) {
                long waitTime = timeout - passedTime;
                if (passedTime >= timeout) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    // 快递员：送信
    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}

/**
 * 2. 邮局管理后台类（管理所有信箱）
 */
class Mailboxes {
    // 使用 Hashtable 保证多线程同时存取信箱时的安全
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;

    // 生成唯一信箱编号
    private static synchronized int generateId() {
        return id++;
    }

    // 收件人调用：申请创建一个新信箱
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    // 快递员调用：根据编号找信箱
    public static GuardedObject getGuardedObject(int id) {
        return boxes.get(id);
    }

    // 获取所有信箱编号
    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

    // 清理信箱（防止内存泄漏）
    public static void removeGuardedObject(int id) {
        boxes.remove(id);
    }
}

/**
 * 3. 收件人（People）
 */
@Slf4j(topic = "c.People")
class People extends Thread {
    @Override
    public void run() {
        // 1. 申请信箱
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());

        // 2. 开始等信，最多等 5 秒
        Object mail = guardedObject.get(5000);

        // 3. 打印结果
        log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
    }
}

/**
 * 4. 快递员（Postman）
 */
@Slf4j(topic = "c.Postman")
class Postman extends Thread {
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        // 1. 根据编号去后台找信箱
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);

        // 2. 打印送信日志（老师截图里的那行）
        log.debug("送信 id:{}, 内容:{}", id, mail);

        // 3. 把信塞进去并唤醒收件人
        guardedObject.complete(mail);

        // 4. 送完信后，把信箱从后台删掉
        Mailboxes.removeGuardedObject(id);
    }
}