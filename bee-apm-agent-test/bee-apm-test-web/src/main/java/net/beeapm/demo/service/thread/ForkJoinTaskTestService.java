package net.beeapm.demo.service.thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author yuan
 * @date 2020/05/29
 */
public class ForkJoinTaskTestService {
    public void hello() {
        System.out.println("ForkJoinTaskTestService------------------hello");
    }

    public void test() {
        System.out.println("ForkJoinTaskTestService------------------test");
        hello();
        ForkJoinPool.commonPool().submit(new ForkJoinTask<Object>() {
            @Override
            public Object getRawResult() {
                return null;
            }

            @Override
            protected void setRawResult(Object value) {

            }

            @Override
            protected boolean exec() {
                System.out.println("ForkJoinTaskTestService:ForkJoinTask------------------exec");
                HelloThreadService service = new HelloThreadService();
                service.sayHelloThread("tom");
                return false;
            }
        });

    }
}
