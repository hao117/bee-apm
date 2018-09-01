package net.beeapm.agent.log;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public enum LogThreadFactory implements ThreadFactory {
    INSTANCE;
    private static final AtomicInteger counter = new AtomicInteger(0);

    LogThreadFactory() {
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("bee-logger-"+counter.incrementAndGet());
        t.setDaemon(true);
        return t;
    }
}