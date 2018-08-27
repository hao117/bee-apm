package net.beeapm.server.core.handler;

/**
 */
public abstract class AbstractStreamHandler implements IStreamHandler {

    protected IStreamHandler nextStreamHandler = null;

    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public void init() throws Exception{
        doInit();
    }

    public abstract void doInit() throws Exception;

    public void setNextStreamHandler(IStreamHandler handler) {
        this.nextStreamHandler = handler;
    }

    public IStreamHandler getNextStreamHandler() {
        return nextStreamHandler;
    }
}
