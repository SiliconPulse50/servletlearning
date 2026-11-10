package GuardedSuspension2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Test24 {
    private static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {


        Thread t1 = new Thread(() -> {
            try {
                System.out.println("t1 最多等待 3 秒");

                boolean success = lock.tryLock(3, TimeUnit.SECONDS);

                if (!success) {
                    System.out.println("3 秒后仍然没有拿到锁");
                    return;
                }

                try {
                    System.out.println("t1 拿到锁，进入临界区");
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                System.out.println("t1 等待期间被打断");
            }
        });
    }
}
/*
它有三种结果：
1. 在 3 秒内拿到锁
   -> 返回 true
   -> 可以进入临界区

2. 等了 3 秒还没拿到锁
   -> 返回 false
   -> 不进入临界区

3. 等待期间被 interrupt()
   -> 抛出 InterruptedException
   -> 不进入临界区
 */