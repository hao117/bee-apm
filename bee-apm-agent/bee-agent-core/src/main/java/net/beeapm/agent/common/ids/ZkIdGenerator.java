package net.beeapm.agent.common.ids;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuanlong.chen
 * @date 2021/04/17
 */
enum ZkIdGenerator implements IdGenerator {
    INSTANCE;
    private static AtomicLong id = new AtomicLong(0);

    @Override
    public void init() {
        ZkIdHelper.init();
    }

    @Override
    public void stop() {
        ZkIdHelper.shutdown();
    }

    @Override
    public String id() {
        return ZkIdHelper.id();
    }
}
