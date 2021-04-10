package net.beeapm.demo.service.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author yuanlong.chen
 * @date 2021/04/10
 */
public class BeeFutureTask extends FutureTask {
    public BeeFutureTask(Callable callable) {
        super(callable);
        System.out.println("------------------>" + callable.getClass().getName());
    }

    @Override
    public void run() {
        super.run();
        System.out.println("========================run=======================");
    }
}
