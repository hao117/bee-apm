package net.beeapm.agent.common.ids;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuanlong.chen
 * @date 2021/04/17
 */
enum UuidIdGenerator implements IdGenerator {
    INSTANCE;
    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String id() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
