package net.beeapm.agent.plugin.common;

import net.beeapm.agent.common.BeeTraceContext;

public class RequestBodyHolder {
    private static ThreadLocal<ReqBody> BODY_HOLDER = new ThreadLocal<ReqBody>();
    public static String getRequestBody(){
        ReqBody body = BODY_HOLDER.get();
        if(body == null){
            return null;
        }
        return body.body;
    }
    public static void setRequestBody(String body){
        BODY_HOLDER.set(new ReqBody(BeeTraceContext.getGId(),body));
    }

    public static String getGid(){
        ReqBody body = BODY_HOLDER.get();
        if(body == null){
            return null;
        }
        return body.gid;
    }

    public static void remove(){
        BODY_HOLDER.remove();
    }

    private static class ReqBody{
        public String gid;
        public String body;

        public ReqBody(String gid, String body) {
            this.gid = gid;
            this.body = body;
        }
    }
}
