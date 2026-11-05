package GuardedSuspension2;


import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Test22")
public class Test22 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        // 【新加一个线程 t0】：先抢到锁，然后死死抱住不放手
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("t0 拿到了锁，并在里面睡了10秒...");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t0").start();

        // 主线程睡个 0.5 秒，确保 t0 先拿到锁
        Thread.sleep(500);

        // 你原本的 t1
        Thread t1 = new Thread(() -> {
            try {
                log.debug("尝试获取锁");
                // t1 此时会卡在这里，因为 t0 没放锁
                lock.lockInterruptibly();
                log.debug("获取到锁"); // 这句不会执行
            } catch (InterruptedException e) {
                log.debug("没有获得锁，被打断了，返回");
                return;
            }

            try {
                log.debug("t1 开始干活...");
            } finally {
                lock.unlock();
            }
        }, "t1");
        t1.start();

        // 主线程睡个 1 秒，觉得 t1 等得太可怜了，决定打断它
        Thread.sleep(1000);
        log.debug("主线程：t1 别等了，赶紧回来！");
        t1.interrupt(); // 尝试打断 t1 的等待状态
    }
}