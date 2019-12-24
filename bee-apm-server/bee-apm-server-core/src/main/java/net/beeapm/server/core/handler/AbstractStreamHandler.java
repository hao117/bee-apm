package net.beeapm.server.core.handler;


/**
 * @author yuan
 * @date 2018/08/27
 */
public abstract class AbstractStreamHandler implements IStreamHandler {

    protected IStreamHandler nextStreamHandler = null;

    protected String name;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void init() throws Exception {
        doInit();
    }

    /**
     * 初始化
     * @throws Exception
     */
    public abstract void doInit() throws Exception;

    @Override
    public void setNextStreamHandler(IStreamHandler handler) {
        this.nextStreamHandler = handler;
    }

    public IStreamHandler getNextStreamHandler() {
        return nextStreamHandler;
    }
}
