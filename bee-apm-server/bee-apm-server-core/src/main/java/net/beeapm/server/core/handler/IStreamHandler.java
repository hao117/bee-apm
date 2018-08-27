package net.beeapm.server.core.handler;

import net.beeapm.server.core.common.Stream;

public interface IStreamHandler {
    /**
     * 设置处理器名称
     * @param name
     */
    void setName(String name);

    /**
     * 实例始始化工作
     */
    void init() throws Exception;

    /**
     * 数据流处理方法
     * @param stream
     * @return Object
     */
    void handle(Stream stream) throws Exception;

    /**
     * 设置下一个处理器
     * @param handler
     */
    void setNextStreamHandler(IStreamHandler handler);

    /**
     * 获取下一个处理器
     * @return
     */
    IStreamHandler getNextStreamHandler();

}
