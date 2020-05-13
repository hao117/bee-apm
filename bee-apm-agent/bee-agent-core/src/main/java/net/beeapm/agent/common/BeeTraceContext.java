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
        traceContext.remove();;
    }

    public static String getPId() {
        return getAndCreate().getPid();
    }

    public static void setPId(String pId) {
        getAndCreate().setPid(pId);
    }

    public static String getGId() {
        TraceContextModel model = getAndCreate();
        String gid = model.getGid();
        if (gid == null) {
            gid = IdHelper.id();
            model.setGid(gid);
        }
        return gid;
    }

    public static void setGId(String gId) {
        getAndCreate().setGid(gId);
    }



    /**
     * 采集标识：Y采集，N不采集
     *
     * @return
     */
    public static String getCTag() {
        return getAndCreate().getCTag();
    }

    /**
     * 设置采集标识：Y采集，N不采集
     *
     * @param ctag
     */
    public static void setCTag(String ctag) {
        getAndCreate().setCTag(ctag);
    }


    public static String getCurrentId() {
        Span span = SpanManager.getCurrentSpan();
        if (span == null) {
            return "nvl";
        }
        return span.getId();
    }

    private static TraceContextModel getAndCreate(){
        TraceContextModel model = traceContext.get();
        if(model == null){
            model = new TraceContextModel();
            traceContext.set(model);
        }
        return model;
    }
}
