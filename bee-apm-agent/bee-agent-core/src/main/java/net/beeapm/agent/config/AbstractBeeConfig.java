package net.beeapm.agent.config;

/**
 * 配置抽象类
 */
public abstract class AbstractBeeConfig {
    /**
     * 配置初始化
     */
    public abstract void initConfig();

    /**
     * 清除配置
     */
    public abstract void clear();

}
