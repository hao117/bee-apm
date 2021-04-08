package net.beeapm.server.core.stream;


import net.beeapm.server.core.common.Stream;

/**
 * @author yuan
 * @date 2018/08/27
 */
public interface IStreamProvider {
    /**
     * 初始化
     */
    void start();

    String write(Stream stream);
}
