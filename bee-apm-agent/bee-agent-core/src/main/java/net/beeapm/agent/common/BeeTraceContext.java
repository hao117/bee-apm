package net.beeapm.agent.common;


import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/8/4.
 */
public class BeeTraceContext {
    private static final ThreadLocal<String> localGId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localPId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localCTag = new ThreadLocal<String>();

    public static void clearAll(){
        localGId.remove();
        localPId.remove();
        localCTag.remove();
    }


    public static String getPId(){
        return localPId.get();
    }

    public static void setPId(String pId){
        localPId.set(pId);
    }

    public static String getGId(){
        String gid = localGId.get();
        if(gid == null){
            gid = IdHelper.id();
            setGId(gid);
        }
        return gid;
    }

    public static void setGId(String gId){
        localGId.set(gId);
    }

    public static String getCTag(){
        return localCTag.get();
    }

    public static void setCTag(String ctag){
        localCTag.set(ctag);
    }


    public static String getCurrentId(){
        Span span = SpanManager.getCurrentSpan();
        if(span == null){
            return "nvl";
        }
        return span.getId();
    }



}
