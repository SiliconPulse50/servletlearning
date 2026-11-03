package GuardedSuspension;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "c.Test20")
public class Test20 {

    public static void main(String[] args) {
        // 1. 准备一个信箱（保护对象）
        GuardedObject guardedObject = new GuardedObject();

        // 2. 派一个“干活”的线程（比如去下载网页）
        new Thread(() -> {
            log.debug("开始下载内容...");
            try {
                List<String> content = Downloader.download();
                // 下载完了，把结果放进信箱，并叫醒等待的人
                guardedObject.complete(content);
                log.debug("下载完成，已放入信箱！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "干活线程").start();

        // 3. 主线程去信箱拿结果（如果没结果，主线程会卡在这里死等）
        log.debug("主线程去拿结果...");
        Object result = guardedObject.get(1000);

        // 4. 拿到结果，继续干活
        log.debug("主线程拿到了结果，长度是：{}", ((List<String>) result).size());
    }
}