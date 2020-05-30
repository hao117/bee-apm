package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.HeaderKey;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import org.apache.http.HttpRequest;

/**
 * Created by yuan on 2018/8/14.
 */
public class HttpClient4xHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(HttpClient4xHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments,Object[] extVal) {
        try {
            for (int i = 0; i < allArguments.length; i++) {
                if (allArguments[i] instanceof HttpRequest) {
                    HttpRequest req = (HttpRequest) allArguments[i];
                    if(req.getLastHeader(HeaderKey.GID) == null){
                        req.setHeader(HeaderKey.GID, BeeTraceContext.getGId());
                        req.setHeader(HeaderKey.PID,BeeTraceContext.getCurrentId());
                        req.setHeader(HeaderKey.CTAG,BeeTraceContext.getCTag());
                        req.setHeader(HeaderKey.SRC_APP, BeeConfig.me().getApp());
                        req.setHeader(HeaderKey.SRC_INST, BeeConfig.me().getInst());
                    }
                }
            }
        }catch (Exception e){
            log.warn("",e);
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        return result;
    }
}
