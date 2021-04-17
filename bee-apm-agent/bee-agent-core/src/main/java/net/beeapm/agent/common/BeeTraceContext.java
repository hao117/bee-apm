package net.beeapm.agent.common;

import net.beeapm.agent.common.ids.IdGenerator;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.TraceContextModel;

/**
 * 日志上下文
 *
 * @author yuan
 * @date 2018/8/4
 */
public class BeeTraceContext {
    private static final InheritableThreadLocal<TraceContextModel> traceContext = new InheritableThreadLocal<TraceContextModel>();

    public static void clearAll() {
        traceContext.remove();
    }

    public static void set(TraceContextModel model) {
        traceContext.set(model);
    }

    public static String getParentId() {
        return getOrNew().getParentId();
    }

    public static void setParentId(String pId) {
        getOrNew().setParentId(pId);
    }

    public static String getTraceId() {
        TraceContextModel model = getOrNew();
        String traceId = model.getTraceId();
        if (traceId == null) {
            traceId = IdGenerator.generator().id();
            model.setTraceId(traceId);
        }
        return traceId;
    }

    public static void setTraceId(String traceId) {
        getOrNew().setTraceId(traceId);
    }


    /**
     * 采集标识：Y采集，N不采集
     *
     * @return
     */
    public static String getSampled() {
        return getOrNew().getSampled();
    }

    /**
     * 设置采集标识：Y采集，N不采集
     *
     * @param sampled
     */
    public static void setSampled(String sampled) {
        getOrNew().setSampled(sampled);
    }


    public static String getCurrentId() {
        Span span = SpanManager.getCurrentSpan();
        if (span == null) {
            return "0";
        }
        return span.getId();
    }

    public static TraceContextModel getOrNew() {
        TraceContextModel model = traceContext.get();
        if (model == null) {
            model = new TraceContextModel();
            traceContext.set(model);
        }
        return model;
    }
}
