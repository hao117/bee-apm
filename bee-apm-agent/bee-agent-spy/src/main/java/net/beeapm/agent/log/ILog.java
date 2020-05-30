package net.beeapm.agent.log;

/**
 * @author yuanlong.chen
 * @date 2020/05/29
 */
public interface ILog {

    void trace(String format);

    void trace(String format, Object... arguments);

    void debug(String format);

    void debug(String format, Object... arguments);

    void info(String format);

    void info(String format, Object... arguments);

    void warn(String format, Object... arguments);

    void warn(Throwable e, String format, Object... arguments);

    void error(String format);

    void error(String format, Throwable e);

    void error(Throwable e, String format, Object... arguments);

    void exec(String format);

    void exec(String format, Object... arguments);

    void exec(Throwable e, String format, Object... arguments);
}
