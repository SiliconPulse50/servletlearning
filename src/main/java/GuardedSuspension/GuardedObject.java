package GuardedSuspension;



class GuardedObject {
    // 结果（如果为 null，说明还没拿到结果）
    private Object response;

//    // 获取结果的方法（消费者/等待者调用）
//    public Object get() {
//        synchronized (this) {
//            // 没有结果，就一直等（必须用 while，防止虚假唤醒和错失唤醒）
//            while (response == null) {
//                try {
//                    this.wait(); // 释放锁，进入等待
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            // 走到这里，说明 response 已经有值了，直接返回
//            return response;
//        }
//    }
    //  修改版get() :不但要防止死等，还要防止虚假唤醒导致的时间计算错误
public Object get(long timeout) {
    synchronized (this) {
        // 1. 按下秒表，记下现在的时间（比如 15:00:00）
        long begin = System.currentTimeMillis();
        // 2. 刚开始，已经花掉的时间肯定是 0
        long passedTime = 0;

        // 3. 只要外卖还没到，我就要继续等（或者继续去确认）
        while (response == null) {

            // 4. 【核心逻辑】：算一下，我的底线时间减去已经花掉的时间，我还能等多久？
            long waitTime = timeout - passedTime;

            // 5. 如果发现，已经花掉的时间 >= 底线时间，说明超时了！
            if (passedTime >= timeout) {
                break; // 不等了，直接掀桌子走人（跳出循环）
            }

            try {
                // 6. 去椅子上打盹，最多只睡 waitTime 这么长的时间
                this.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 7. 【醒来后，第一件事】：重新算一下，从按秒表开始到现在，到底过了多久？
            // (假设被提前叫醒了，或者时间到了自然醒，都要重新算一遍)
            passedTime = System.currentTimeMillis() - begin;
        }

        // 8. 走到这里有两种可能：要么外卖等到了，要么时间超了没等到（response 是 null）
        return response;
    }
}

    // 产生结果的方法（生产者/唤醒者调用）
    public void complete(Object response) {
        synchronized (this) {
            // 1. 给结果成员变量赋值
            this.response = response;
            // 2. 唤醒所有在这个信箱上等待的线程（你之前学过，用 notifyAll 最稳）
            this.notifyAll();
        }
    }

}