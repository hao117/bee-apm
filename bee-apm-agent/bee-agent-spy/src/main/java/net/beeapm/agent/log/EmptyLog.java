package net.beeapm.agent.log;

/**
 * @author yuanlong.chen
 * @date 2020/05/30
 */
public class EmptyLog implements ILog {
    @Override
    public void trace(String format) {

    }

    @Override
    public void trace(String format, Object... arguments) {

    }

    @Override
    public void debug(String format) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void info(String format) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void warn(Throwable e, String format, Object... arguments) {

    }

    @Override
    public void error(String format) {

    }

    @Override
    public void error(String format, Throwable e) {

    }

    @Override
    public void error(Throwable e, String format, Object... arguments) {

    }

    @Override
    public void exec(String format) {

    }

    @Override
    public void exec(String format, Object... arguments) {

    }

    @Override
    public void exec(Throwable e, String format, Object... arguments) {

    }
}
