import java.util.Random;

public class ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                 a.transferSync(b, randomAmount()); // 测试笔记里的错误改法（会出错）
                // a.transferClass(b, randomAmount()); // 测试全局锁
                //a.transferSafe(b, randomAmount());     // 测试排序锁
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transferSync(a, randomAmount());
                // b.transferClass(a, randomAmount());
                //  b.transferSafe(a, randomAmount());
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 查看转账 2000 次后的总金额（必须是 2000）
        System.out.println("total: " + (a.getMoney() + b.getMoney()));
    }

    static Random random = new Random();
    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }
}

class Account {
    private int money;

    public Account(int money) { this.money = money; }
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }

    // 【错误版】笔记里的改法：加 synchronized，锁的是 this
    public synchronized void transferSync(Account target, int amount) {
        if (this.money > amount) {
            this.setMoney(this.getMoney() - amount);
            target.setMoney(target.getMoney() + amount);
        }
    }

    // 【正确版 1】全局锁
    public void transferClass(Account target, int amount) {
        synchronized (Account.class) {
            if (this.money > amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }

    // 【正确版 2】排序加锁（推荐）
    public void transferSafe(Account target, int amount) {
        if (this == target) return;
        Account left = this;
        Account right = target;
        if (this.hashCode() > target.hashCode()) {
            left = target;
            right = this;
        }
        synchronized (left) {
            synchronized (right) {
                if (this.money > amount) {
                    this.setMoney(this.getMoney() - amount);
                    target.setMoney(target.getMoney() + amount);
                }
            }
        }
    }
}