package net.beeapm.agent.common;


/**
 * Created by yuan on 2018/8/4.
 */
public class BeeTraceContext {
    private static final ThreadLocal<String> localGId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localPId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localCTag = new ThreadLocal<String>();
    private static final ThreadLocal<Integer> localRequestEntryCounter = new ThreadLocal<Integer>();

    public static void clearAll(){
        localGId.remove();
        localPId.remove();
        localCTag.remove();
        localRequestEntryCounter.remove();
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

    public static String getCTag(){
        return localCTag.get();
    }

    public static void setCTag(String ctag){
        localCTag.set(ctag);
    }

    public static int getAndIncrRequestEntryCounter(){
        Integer num = localRequestEntryCounter.get();
        if(num == null){
            num = new Integer(0);
        }
        int ret = num.intValue();
        localRequestEntryCounter.set(num+1);
        return ret;
    }

    public static int decrAndGetRequestEntryCounter(){
        Integer num = localRequestEntryCounter.get();
        if(num == null){
            num = new Integer(0);
        }
        Integer ret = num - 1;
        localRequestEntryCounter.set(ret);
        return ret;
    }




}
