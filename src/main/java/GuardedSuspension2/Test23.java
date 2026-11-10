package GuardedSuspension2;
//这是lockInterruptibly的使用示例，演示了如何在等待锁时响应中断。
import java.util.concurrent.locks.ReentrantLock;

public class Test23 {
    public  static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t0 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("t0 获取锁，工作 10 秒");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread t1 = new Thread(() -> {
            try {
                System.out.println("t1 等待锁");
                lock.lockInterruptibly();

                try {
                    System.out.println("t1 获取锁，进入临界区");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println("t1 被打断，没有进入临界区");
            }
        });

        t0.start();
        Thread.sleep(100);
        t1.start();

        Thread.sleep(1000);
        t1.interrupt();
    }
    }

