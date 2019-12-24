package net.beeapm.server.core.common;

/**
 *
 * @author yuan
 * @date 2018/08/27
 */
public class Stream {
    private Object source;

    public Stream(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
