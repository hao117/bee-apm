package net.beeapm.agent.common.ids;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuanlong.chen
 * @date 2021/04/17
 */
enum SimpleIdGenerator implements IdGenerator {
    INSTANCE;
    private static AtomicLong id = new AtomicLong(0);

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String id() {
        return String.valueOf(id.incrementAndGet());
    }
}
