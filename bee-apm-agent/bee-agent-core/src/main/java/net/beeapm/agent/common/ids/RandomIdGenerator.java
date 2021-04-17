package net.beeapm.agent.common.ids;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yuanlong.chen
 * @date 2021/04/17
 */
enum RandomIdGenerator implements IdGenerator {
    INSTANCE;
    private static final long INVALID_ID = 0;

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String id() {
        long id;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        do {
            id = random.nextLong();
        } while (id == INVALID_ID);
        return String.valueOf(id);
    }
}
