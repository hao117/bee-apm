package net.beeapm.agent.common.ids;

/**
 * @author yuanlong.chen
 * @date 2021/04/17
 */
public interface IdGenerator {
    /**
     * 生成器实例
     * @return
     */
    static IdGenerator generator() {
        //return RandomIdGenerator.INSTANCE;
        //return SimpleIdGenerator.INSTANCE;
        //return UuidIdGenerator.INSTANCE;
        //return ZkIdGenerator.INSTANCE;
        return SimpleIdGenerator.INSTANCE;
    }

    /**
     * 初始化
     */
    void init();

    /**
     * 结束
     */
    void stop();
    /**
     * id生成
     * @return
     */
    String id();
}
