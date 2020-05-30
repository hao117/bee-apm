package net.beeapm.agent.common;

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

    public static String getPId() {
        return getOrNew().getPid();
    }

    public static void setPId(String pId) {
        getOrNew().setPid(pId);
    }

    public static String getGId() {
        TraceContextModel model = getOrNew();
        String gid = model.getGid();
        if (gid == null) {
            gid = IdHelper.id();
            model.setGid(gid);
        }
        return gid;
    }

    public static void setGId(String gId) {
        getOrNew().setGid(gId);
    }


    /**
     * 采集标识：Y采集，N不采集
     *
     * @return
     */
    public static String getCTag() {
        return getOrNew().getCTag();
    }

    /**
     * 设置采集标识：Y采集，N不采集
     *
     * @param ctag
     */
    public static void setCTag(String ctag) {
        getOrNew().setCTag(ctag);
    }


    public static String getCurrentId() {
        Span span = SpanManager.getCurrentSpan();
        if (span == null) {
            return "nvl";
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
