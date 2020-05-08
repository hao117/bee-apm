package net.beeapm.agent.common;


import net.beeapm.agent.model.Span;

/**
 * 日志上下文
 *
 * @author yuan
 * @date 2018/8/4
 */
public class BeeTraceContext {
    private static final ThreadLocal<String> localGId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localPId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localCTag = new ThreadLocal<String>();

    public static void clearAll() {
        localGId.remove();
        localPId.remove();
        localCTag.remove();
    }

    public static String getPId() {
        return localPId.get();
    }

    public static void setPId(String pId) {
        localPId.set(pId);
    }

    public static String getGId() {
        String gid = localGId.get();
        if (gid == null) {
            gid = IdHelper.id();
            setGId(gid);
        }
        return gid;
    }

    public static void setGId(String gId) {
        localGId.set(gId);
    }

    /**
     * 采集标识：Y采集，N不采集
     *
     * @return
     */
    public static String getCTag() {
        return localCTag.get();
    }

    /**
     * 设置采集标识：Y采集，N不采集
     *
     * @param ctag
     */
    public static void setCTag(String ctag) {
        localCTag.set(ctag);
    }


    public static String getCurrentId() {
        Span span = SpanManager.getCurrentSpan();
        if (span == null) {
            return "nvl";
        }
        return span.getId();
    }
}
