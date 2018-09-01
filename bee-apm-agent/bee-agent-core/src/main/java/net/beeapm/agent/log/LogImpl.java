package net.beeapm.agent.log;

import net.beeapm.agent.common.BeeConst;
import net.beeapm.agent.config.BeeConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Created by yuan on 2018/3/26.
 */
public class LogImpl {
    private String  targetName;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public LogImpl(String targetName) {
        this.targetName = targetName;
    }
    protected void logger(LogLevel level, String message, Throwable e) {
        String msg = format(level, message, e);
        if(BeeConfig.isLogConsole()){
            System.out.println("---------->"+msg);
        }
        LogWriter.me().writeLog(msg);
    }
    private String replaceParam(String message, Object... parameters) {
        int startSize = 0;
        int parametersIndex = 0;
        int index;
        String tmpMessage = message;
        while ((index = message.indexOf("{}", startSize)) != -1) {
            if (parametersIndex >= parameters.length) {
                break;
            }
            tmpMessage = tmpMessage.replaceFirst("\\{\\}", Matcher.quoteReplacement(String.valueOf(parameters[parametersIndex++])));
            startSize = index + 2;
        }
        return tmpMessage;
    }

    String format(LogLevel level, String message, Throwable t) {
        return StringUtils.join("[", level.name(),"] ",
                dateFormat.format(new Date()),
                " ",
                targetName,
                " : ",
                message,
                t == null ? "" : format(t)
        );
    }

    String format(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BeeConst.LINE_SEPARATOR + expMessage;
    }
    public void info(String format) {
        if (isInfoEnable())
            logger(LogLevel.INFO, format, null);
    }

    public void info(String format, Object... arguments) {
        if (isInfoEnable())
            logger(LogLevel.INFO, replaceParam(format, arguments), null);
    }

    public void warn(String format, Object... arguments) {
        if (isWarnEnable())
            logger(LogLevel.WARN, replaceParam(format, arguments), null);
    }

    public void warn(Throwable e, String format, Object... arguments) {
        if (isWarnEnable())
            logger(LogLevel.WARN, replaceParam(format, arguments), e);
    }

    public void error(String format, Throwable e) {
        if (isErrorEnable())
            logger(LogLevel.ERROR, format, e);
    }

    public void error(Throwable e, String format, Object... arguments) {
        if (isErrorEnable())
            logger(LogLevel.ERROR, replaceParam(format, arguments), e);
    }

    public boolean isDebugEnable() {
        return LogLevel.DEBUG.ordinal() >= BeeConst.LOG_LEVEL.ordinal();
    }

    public static boolean isInfoEnable() {
        return LogLevel.INFO.ordinal() >= BeeConst.LOG_LEVEL.ordinal();
    }

    public boolean isWarnEnable() {
        return LogLevel.WARN.ordinal() >= BeeConst.LOG_LEVEL.ordinal();
    }

    public boolean isErrorEnable() {
        return LogLevel.ERROR.ordinal() >= BeeConst.LOG_LEVEL.ordinal();
    }

    public boolean isTraceEnable() {
        return LogLevel.TRACE.ordinal() >= BeeConst.LOG_LEVEL.ordinal();
    }

    public void debug(String format) {
        if (isDebugEnable()) {
            logger(LogLevel.DEBUG, format, null);
        }
    }

    public void debug(String format, Object... arguments) {
        if (isDebugEnable()) {
            logger(LogLevel.DEBUG, replaceParam(format, arguments), null);
        }
    }

    public void error(String format) {
        if (isErrorEnable()) {
            logger(LogLevel.ERROR, format, null);
        }
    }

    public void trace(String format) {
        if (isTraceEnable()) {
            logger(LogLevel.TRACE, format, null);
        }
    }

    public void trace(String format, Object... arguments) {
        if (isTraceEnable()) {
            logger(LogLevel.TRACE, replaceParam(format, arguments), null);
        }
    }
}
