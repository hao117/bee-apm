package net.beeapm.demo.service.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuanlong.chen
 * @date 2020/06/07
 */
public class ThreadPoolService {
    private static ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
        private final AtomicInteger threadCounter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("bee-thread-demo" + threadCounter.incrementAndGet());
            t.setDaemon(true);
            return t;
        }
    });

    public void hello(String name) {
        executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Class<?> cls = this.getClass();
                System.out.println(cls.getName());
                HelloThreadService hello = new HelloThreadService();
                hello.sayHelloThread3("apple");
                return null;
            }
        });
    }

    public void hello2(String name) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Class<?> cls = this.getClass();
                System.out.println("do----------------->"+cls.getName());
                HelloThreadService hello = new HelloThreadService();
                hello.sayHelloThread3("tomato");
            }
        });
    }

}
