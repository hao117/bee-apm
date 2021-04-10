package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.*;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanKind;
import net.beeapm.agent.plugin.MethodConfig;
import net.beeapm.agent.reporter.ReporterFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * @author yuan
 * @date 2018-08-11
 * 关闭process采集，异常也将不采集
 * 如果想仅采集异常而不采集process，可以把采样率调成0，不影响异常采集
 * Created by yuan on 2018/7/31.
 */
public class MethodHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(MethodHandler.class.getSimpleName());
    private static final String KEY_ERROR_THROWABLE = "_ERROR_THROWABLE";
    private static final String KEY_BEE_CHILD_ID = "_BEE_CHILD_ID";
    private static final String KEY_ERROR_POINT = "_ERROR_POINT";
    private static final String KEY_PARAM = "_PARAM";


    @Override
    public Span before(String className, String methodName, Object[] allArgs, Object[] extVal) {
        if (!MethodConfig.me().isEnable()) {
            return null;
        }
        Span span = SpanManager.createEntrySpan(SpanKind.METHOD);
        logBeginTrace(className, methodName, span, log);
        String params = collectParams(allArgs, span.getId(), className + "." + methodName);
        span.addCache(KEY_PARAM, params);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArgs, Object result, Throwable t, Object[] extVal) {
        Span span = SpanManager.getExitSpan();
        if (span == null) {
            return result;
        }
        Throwable childThrowable = (Throwable) span.getCache(KEY_ERROR_THROWABLE);
        String childId = (String) span.getCache(KEY_BEE_CHILD_ID);
        String childErrorPoint = (String) span.getCache(KEY_ERROR_POINT);
        String errorPoint = className + "." + methodName;
        String params = (String) span.getCache(KEY_PARAM);
        //清除暂存的异常信息
        span.clearCache();


        if (!MethodConfig.me().isEnable()) {
            return null;
        }
        calculateDuration(span);
        logEndTrace(className, methodName, span, log);
        //耗时阀值限制
        if (span.getDuration() > MethodConfig.me().getSpend() && SamplingUtil.YES()) {
            sendParams(span.getId(), params);
            span.addAttribute(AttrKey.METHOD_NAME, methodName)
                    .addAttribute(AttrKey.METHOD_CLASS, className);
            handleMethodSignature(span, (String) extVal[0]);
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
        //当前栈顶的为parent，当然span已经被SpanManager.getExitSpan弹出
        Span parentSpan = SpanManager.getCurrentSpan();
        //栈顶
        if (parentSpan == null) {
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
                parentSpan.addCache(KEY_ERROR_THROWABLE, t);
                parentSpan.addCache(KEY_BEE_CHILD_ID, id);
                parentSpan.addCache(KEY_ERROR_POINT, errorPoint);
                return;
            } else if (childThrowable != null && t == null) {
                sendError(childId, childErrorPoint, childThrowable);
            } else {
                parentSpan.addCache(KEY_ERROR_THROWABLE, t);
                parentSpan.addCache(KEY_BEE_CHILD_ID, id);
                parentSpan.addCache(KEY_ERROR_POINT, errorPoint);
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
        Span paramSpan = new Span(SpanKind.PARAM);
        paramSpan.setId(id);
        paramSpan.addAttribute(AttrKey.METHOD_PARAM, params);
        ReporterFactory.report(paramSpan);
    }

    public void sendError(String id, String errorPoint, Throwable t) {
        if (MethodConfig.me().isEnableError() && MethodConfig.me().checkErrorPoint(errorPoint)) {
            Span err = new Span(SpanKind.ERROR);
            err.setId(id);
            err.setTraceId(BeeTraceContext.getTraceId());
            err.addAttribute(AttrKey.METHOD_STACKTRACE, formatThrowable(t));
            ReporterFactory.report(err);
        }
    }

    private void handleMethodSignature(Span span, String sign) {
        if (sign == null || sign.length() < 3) {
            return;
        }
        if (MethodConfig.me().isEnableMethodSign()) {
            span.addAttribute(AttrKey.METHOD_SIGN, sign.substring(1, sign.length() - 1));
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
        if (MethodConfig.me().isEnableParam() && allArgs != null && allArgs.length > 0 && MethodConfig.me().checkParamPoint(point)) {
            Object[] params = new Object[allArgs.length];
            for (int i = 0; i < allArgs.length; i++) {
                if (allArgs[i] != null && MethodConfig.me().isExcludeParamType(allArgs[i].getClass())) {
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
