package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.*;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.ProcessConfig;
import net.beeapm.agent.reporter.ReporterFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;


/**
 * @author yuan
 * @date 2018-08-11
 * 关闭process采集，异常也将不采集
 * 如果想仅采集异常而不采集process，可以把采样率调成0，不影响异常采集
 * Created by yuan on 2018/7/31.
 */
public class ProcessHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ProcessHandler.class.getSimpleName());
    private static final String KEY_ERROR_THROWABLE = "_ERROR_THROWABLE";
    private static final String KEY_BEE_CHILD_ID = "_BEE_CHILD_ID";
    private static final String KEY_ERROR_POINT = "_ERROR_POINT";
    private static final String KEY_PARAM = "_PARAM";

    @Override
    public Span before(String className, String methodName, Object[] allArgs, Object[] extVal) {
        if (!ProcessConfig.me().isEnable()) {
            return null;
        }
        Span span = SpanManager.createEntrySpan(SpanType.PROCESS);
        logBeginTrace(className, methodName, span, log);
        String params = collectParams(allArgs, span.getId(), className + "." + methodName);
        span.addTag(KEY_PARAM, params);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArgs, Object result, Throwable t, Object[] extVal) {
        Span span = SpanManager.getExitSpan();
        if (span == null) {
            return result;
        }
        Throwable childThrowable = (Throwable) span.getTag(KEY_ERROR_THROWABLE);
        String childId = (String) span.getTag(KEY_BEE_CHILD_ID);
        String childErrorPoint = (String) span.getTag(KEY_ERROR_POINT);
        String errorPoint = className + "." + methodName;
        String params = (String) span.getTag(KEY_PARAM);
        //清除暂存的异常信息
        span.removeTag(KEY_ERROR_THROWABLE);
        span.removeTag(KEY_BEE_CHILD_ID);
        span.removeTag(KEY_ERROR_POINT);
        span.removeTag(KEY_PARAM);

        if (!ProcessConfig.me().isEnable()) {
            return null;
        }
        calculateSpend(span);
        logEndTrace(className, methodName, span, log);
        //耗时阀值限制
        if (span.getSpend() > ProcessConfig.me().getSpend() && SamplingUtil.YES()) {
            sendParams(span.getId(), params);
            span.addTag("method", methodName).addTag("clazz", className);
            handleMethodSignature(span, (String) extVal[0]);
            span.fillEnvInfo();
            ReporterFactory.report(span);
        }
        //异常处理
        handleError(span.getId(), errorPoint, t, childId, childErrorPoint, childThrowable);
        return result;
    }

    public void handleError(String id, String errorPoint, Throwable t, String childId, String childErrorPoint, Throwable childThrowable) {
        if (t == null && childThrowable == null) {
            return;
        }
        Span parentSpan = SpanManager.getCurrentSpan(); //当前栈顶的为parent，当然span已经被SpanManager.getExitSpan弹出
        if (parentSpan == null) {      //栈顶
            if (t == null && childThrowable != null) {
                sendError(childId, childErrorPoint, childThrowable);
            } else if (t != null && childThrowable == null) {
                sendError(id, errorPoint, t);
            } else {
                if (t == childThrowable || t.getCause() == childThrowable) {
                    sendError(id, errorPoint, t);
                } else {
                    sendError(id, errorPoint, t);
                    sendError(childId, childErrorPoint, childThrowable);
                }
            }
        } else {
            if (childThrowable == null && t != null) {
                //暂存异常信息
                parentSpan.addTag(KEY_ERROR_THROWABLE, t);
                parentSpan.addTag(KEY_BEE_CHILD_ID, id);
                parentSpan.addTag(KEY_ERROR_POINT, errorPoint);
                return;
            } else if (childThrowable != null && t == null) {
                sendError(childId, childErrorPoint, childThrowable);
            } else {
                parentSpan.addTag(KEY_ERROR_THROWABLE, t);
                parentSpan.addTag(KEY_BEE_CHILD_ID, id);
                parentSpan.addTag(KEY_ERROR_POINT, errorPoint);
                if (t != childThrowable && t.getCause() != childThrowable) {
                    sendError(childId, childErrorPoint, childThrowable);
                }
            }
        }
    }

    private void sendParams(String id, String params) {
        if (params == null) {
            return;
        }
        Span paramSpan = new Span(SpanType.PARAM);
        paramSpan.setId(id);
        paramSpan.addTag("param", params);
        ReporterFactory.report(paramSpan);
    }

    public void sendError(String id, String errorPoint, Throwable t) {
        if (ProcessConfig.me().isEnableError() && ProcessConfig.me().checkErrorPoint(errorPoint)) {
            Span err = new Span(SpanType.ERROR);
            err.fillEnvInfo();
            err.setId(id);
            err.setGid(BeeTraceContext.getGId());
            err.addTag("desc", formatThrowable(t));
            ReporterFactory.report(err);
        }
    }

    private void handleMethodSignature(Span span, String sign) {
        if (sign == null || sign.length() < 3) {
            return;
        }
        if (ProcessConfig.me().isEnableMethodSign()) {
            span.addTag("msign", sign.substring(1, sign.length() - 1));
        }
    }


    private String formatThrowable(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
        }
        return expMessage;
    }

    private String collectParams(Object[] allArgs, String id, String point) {
        if (ProcessConfig.me().isEnableParam() && allArgs != null && allArgs.length > 0 && ProcessConfig.me().checkParamPoint(point)) {
            Object[] params = new Object[allArgs.length];
            for (int i = 0; i < allArgs.length; i++) {
                if (allArgs[i] != null && ProcessConfig.me().isExcludeParamType(allArgs[i].getClass())) {
                    params[i] = "--";
                } else {
                    params[i] = allArgs[i];
                }
            }
            return JSON.toJSONString(params);
        }
        return null;
    }

}
