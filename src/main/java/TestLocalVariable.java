import java.util.ArrayList;
import java.util.List;

public class TestLocalVariable {
    public static void main(String[] args) {
        ThreadSafe test = new ThreadSafe();
        // 两个线程同时操作 test 对象
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                test.method1(200);
            }, "Thread" + i).start();
        }
    }
}

class ThreadSafe {
    public final void method1(int loopNumber) {
        // 局部变量：每次进入 method1 都会 new 一个新的 list
        List<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    // 注意：这里把 list 当参数传进去
    private void method2(List<String> list) {
        list.add("1");
    }

    private void method3(List<String> list) {
        list.remove(0);
    }
}
/*
这也是为什么老师图里写的是 private void method2() 和 private void method3()。
如果方法只给当前类自己用，没有暴露给外部或者子类，就尽量用 private。 只要不给外部传参的机会，局部变量就是最安全的。
 */