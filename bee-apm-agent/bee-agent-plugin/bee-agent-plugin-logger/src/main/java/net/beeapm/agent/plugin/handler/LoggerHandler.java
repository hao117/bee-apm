package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.LoggerConfig;
import net.beeapm.agent.reporter.ReporterFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 * @author yuan
 * @date 2018-08-19
 */
public class LoggerHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(LoggerHandler.class.getSimpleName());
    private static final String VAL_IGNORE_POINT = "org.apache.logging.log4j.status.StatusLogger";

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        String point = (String) extVal[0];
        boolean isCollect = true;
        if (LoggerConfig.me().level(methodName) >= LoggerConfig.LEVEL_ERROR) {
            // 是否error采样
            if (LoggerConfig.me().errorRate()) {
                isCollect = SamplingUtil.YES();
            } else {
                //error不采样，全采集
                isCollect = true;
            }
        }
        if (!LoggerConfig.me().isEnable() || !LoggerConfig.me().checkLevel(point, methodName) || !isCollect) {
            return null;
        }
        //重复打印，排除掉
        if (VAL_IGNORE_POINT.equals(point)) {
            return null;
        }
        Span span = SpanManager.createLocalSpan(SpanType.LOGGER);
        StringBuilder logBuff = new StringBuilder();
        for (int i = 0; i < allArguments.length; i++) {
            Object arg = allArguments[i];
            if (i > 0) {
                logBuff.append(" | ");
            }
            if (arg == null) {
                continue;
            }
            if (arg instanceof Throwable) {
                logBuff.append(parseThrowable((Throwable) arg));
            } else if (BeeUtils.isPrimitive(arg)) {
                logBuff.append(arg);
            } else {
                logBuff.append(JSON.toJSONString(arg));
            }
        }
        span.addTag("point", point + "." + extVal[1]);
        span.addTag("log", logBuff.toString());
        span.addTag("level", methodName);
        BeeConfig.me().fillEnvInfo(span);
        ReporterFactory.report(span);
        return null;
    }


    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return result;
    }

    public static String parseThrowable(Throwable t) {
        StringBuilder builder = new StringBuilder();
        Writer writer = null;
        PrintWriter printWriter = null;
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            t.printStackTrace(printWriter);
            printWriter.flush();
            builder.append(writer.toString());
            printWriter.close();
            writer.close();
        } catch (Exception e) {
            log.error("解析StackTrace异常", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                }
            }
        }
        try {
            Field messageField = Throwable.class.getDeclaredField("detailMessage");
            messageField.setAccessible(true);
            String detailMessage = t.getMessage();
            if (messageField != null) {
                try {
                    messageField.set(t, "[" + BeeTraceContext.getGId() + "]" + detailMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable tt) {
            tt.printStackTrace();
            log.error("解析detailMessage异常", tt);
        }
        return builder.toString();
    }

}
