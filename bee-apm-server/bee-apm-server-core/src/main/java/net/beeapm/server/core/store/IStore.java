package net.beeapm.server.core.store;

/**
 * @author yuan
 * @date 2018/08/27
 */
public interface IStore {
    /**
     * 初始化操作
     */
    void init();

    /**
     * 保存数据
     * @param stream
     */
    void save(Object ... stream);
}
