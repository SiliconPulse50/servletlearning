
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketExercise {
/*
* 第一次照着抄，抄完跑通；
第二次闭着眼睛把核心的 sell 逻辑默写出来；
第三次尝试不看任何参考，自己写一个带 synchronized 的安全版。
*/
    public static void main(String[] args) throws InterruptedException {
        // 1. 测试【不安全版】
        System.out.println("========== 开始测试不安全版 ==========");
        UnsafeTicketWindow unsafeWindow = new UnsafeTicketWindow(2000);
        runTest(unsafeWindow::sell, unsafeWindow::getCount);

        // 2. 测试【synchronized安全版】
        System.out.println("\n========== 开始测试 synchronized 安全版 ==========");
        SyncTicketWindow syncWindow = new SyncTicketWindow(2000);
        runTest(syncWindow::sell, syncWindow::getCount);

        // 3. 测试【AtomicInteger (CAS) 安全版】
        System.out.println("\n========== 开始测试 AtomicInteger 安全版 ==========");
        AtomicTicketWindow atomicWindow = new AtomicTicketWindow(2000);
        runTest(atomicWindow::sell, atomicWindow::getCount);
    }

    // 通用测试方法
    private static void runTest(TicketSeller seller, TicketCounter counter) throws InterruptedException {
        List<Thread> list = new ArrayList<>();
        List<Integer> sellCount = new Vector<>(); // Vector 线程安全，用于记录每次卖出的票数

        for (int i = 0; i < 2000; i++) {
            Thread t = new Thread(() -> {
                int count = seller.sell(1); // 每次买 1 张票
                sellCount.add(count);
            });
            list.add(t);
            t.start();
        }

        for (Thread t : list) t.join();

        int totalSelled = sellCount.stream().mapToInt(c -> c).sum();
        System.out.println("总共卖出票数: " + totalSelled + " 张");
        System.out.println("剩余票数: " + counter.getCount() + " 张");

        if (counter.getCount() < 0) {
            System.out.println("❌ 发生超卖！剩余票数为负数！");
        } else {
            System.out.println("✅ 运行正确，没有发生超卖。");
        }
    }

    // 函数式接口，方便传参
    interface TicketSeller { int sell(int amount); }
    interface TicketCounter { int getCount(); }
}

// ================== 1. 不安全版 ==================
class UnsafeTicketWindow {
    private int count;
    public UnsafeTicketWindow(int count) { this.count = count; }
    public int getCount() { return count; }

    public int sell(int amount) {
        if (this.count >= amount) {
            // 【断点调试或休眠可以放大问题】Thread.sleep(1);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.count -= amount;
            return amount;
        }
        return 0;
    }
}

// ================== 2. synchronized 安全版 ==================
class SyncTicketWindow {
    private int count;
    public SyncTicketWindow(int count) { this.count = count; }
    // 加锁保证可见性和原子性
    public synchronized int getCount() { return count; }

    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        }
        return 0;
    }
}

// ================== 3. JUC AtomicInteger 安全版 ==================
class AtomicTicketWindow {
    private AtomicInteger count;
    public AtomicTicketWindow(int count) { this.count = new AtomicInteger(count); }
    public int getCount() { return count.get(); }

    public int sell(int amount) {
        while (true) {
            int current = count.get(); // 获取当前值
            if (current < amount) {
                return 0;
            }
            // CAS 比较并交换：如果当前值是 current，则更新为 current - amount
            if (count.compareAndSet(current, current - amount)) {
                return amount;
            }
            // 如果 CAS 失败，说明有其他线程修改了 count，自旋重试
        }
    }
}