
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaitNotifyDemo {

    static ReentrantLock lock = new ReentrantLock();
    static Condition waitCigaretteQueue = lock.newCondition();
    static Condition waitBreakfastQueue = lock.newCondition();

    // 状态标志位，初始为 false
    static volatile boolean hasCigarette = false;
    static volatile boolean hasBreakfast = false;

    public static void main(String[] args) {

        // ---------------- 线程 1：等烟 ----------------
        new Thread(() -> {
            lock.lock(); // 1. 抢锁
            try {
                // 2. 判断条件：只要没有烟，就循环等待（防止虚假唤醒）
                while (!hasCigarette) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 没烟，进入等待...");
                        waitCigaretteQueue.await(); // 3. 释放锁并挂起
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 4. 跳出 while 循环，说明条件满足了，执行核心逻辑
                System.out.println(Thread.currentThread().getName() + " 等到了它的烟");
            } finally {
                lock.unlock(); // 5. 必须在 finally 中释放锁
            }
        }, "等烟线程").start();


        // ---------------- 线程 2：等早餐 ----------------
        new Thread(() -> {
            lock.lock();
            try {
                while (!hasBreakfast) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 没早餐，进入等待...");
                        waitBreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " 等到了它的早餐");
            } finally {
                lock.unlock();
            }
        }, "等早餐线程").start();


        // ---------------- 主线程：负责送餐 ----------------
        try {
            Thread.sleep(1000); // 主线程先睡1秒，保证两个子线程先拿到锁并进入 await 状态
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 1. 先送早餐
        sendBreakfast();

        try {
            Thread.sleep(1000); // 主线程再睡1秒，观察效果
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. 再送烟
        sendCigarette();
    }

    // 送烟的方法
    private static void sendCigarette() {
        lock.lock();
        try {
            System.out.println("主线程：送烟来了！");
            hasCigarette = true; // 修改状态
            waitCigaretteQueue.signal(); // 精准唤醒“等烟线程”
        } finally {
            lock.unlock();
        }
    }

    // 送早餐的方法
    private static void sendBreakfast() {
        lock.lock();
        try {
            System.out.println("主线程：送早餐来了！");
            hasBreakfast = true; // 修改状态
            waitBreakfastQueue.signal(); // 精准唤醒“等早餐线程”
        } finally {
            lock.unlock();
        }
    }
}