package net.beeapm.agent.common;

/**
 * Created by yuan on 2018/8/4.
 */
public class BeeTraceContext {
    private static final ThreadLocal<String> localCurrentId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localGId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localPId = new ThreadLocal<String>();

    public static void clearAll(){
        localCurrentId.remove();
        localGId.remove();
        localPId.remove();
    }

    public static String getCurrentId(){
        return localCurrentId.get();
    }

    public static void setCurrentId(String currentId){
        localCurrentId.set(currentId);
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
            gid = IdHepler.id();
            setGId(gid);
        }
        return gid;
    }

    public static void setGId(String gId){
        localGId.set(gId);
    }





}
