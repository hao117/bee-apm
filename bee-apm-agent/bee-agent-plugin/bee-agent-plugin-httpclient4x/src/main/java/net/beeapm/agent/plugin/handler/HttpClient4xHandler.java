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
                    if(req.getLastHeader(HeaderKey.TRACE_ID) == null){
                        req.setHeader(HeaderKey.TRACE_ID, BeeTraceContext.getTraceId());
                        req.setHeader(HeaderKey.PARENT_ID,BeeTraceContext.getCurrentId());
                        req.setHeader(HeaderKey.SAMPLED,BeeTraceContext.getSampled());
                        req.setHeader(HeaderKey.SRC_APP, BeeConfig.me().getApp());
                        req.setHeader(HeaderKey.SRC_INST, BeeConfig.me().getInst());
                    }
                }
            }
        }catch (Exception e){
            log.error("add http header fail",e);
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        return result;
    }
}
