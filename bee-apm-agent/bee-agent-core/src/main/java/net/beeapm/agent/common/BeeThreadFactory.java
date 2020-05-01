package net.beeapm.agent.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuan
 * @date 2018-11-07
 */
public class BeeThreadFactory implements ThreadFactory {
    private final AtomicInteger threadCounter = new AtomicInteger(1);
    private final String namePrefix;

    public BeeThreadFactory(String name) {
        namePrefix = "bee-thread-" + name + "-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(namePrefix + threadCounter.incrementAndGet());
        t.setDaemon(true);
        return t;
    }
}
