package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.HeaderKey;
import net.beeapm.agent.common.IdHepler;
import net.beeapm.agent.common.SpanType;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.TransmitterFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ServletHandler.class.getSimpleName());
    @Override
    public Span before(Method m, Object[] allArguments) {
        if(!BeeTraceContext.isRequestEnter()){
            return null;
        }
        Span span = new Span(SpanType.REQUEST);
        logBeginTrace(m,span,log);
        HttpServletRequest request = (HttpServletRequest)allArguments[0];
        HttpServletResponse response  = (HttpServletResponse)allArguments[1];
        span.setValue("url",request.getRequestURL());
        span.setValue("remote",request.getRemoteAddr());
        span.setValue("method",getMethodName(m));
        span.setValue("clazz",getClassName(m));
        String pId = request.getHeader(HeaderKey.PID);
        String gId = request.getHeader(HeaderKey.GID);
        //TODO 采样率，预留
        String ctag = request.getHeader(HeaderKey.CTAG);
        if(pId == null || pId.length() == 0){
            pId = "nvl";
        }
        if(gId == null || gId.length() == 0){
            gId = IdHepler.id();
        }
        if(ctag == null || ctag.length() == 0){
            //TODO 需要重算采样
            ctag = "Y";
        }

        String id = IdHepler.id();
        response.setHeader(HeaderKey.GID,gId);   //返回gid，用于跟踪
        response.setHeader(HeaderKey.ID,id);     //返回id，用于跟踪
        span.setId(id).setGid(gId).setPid(pId);
        BeeTraceContext.setCurrentId(span.getId());
        BeeTraceContext.setGId(gId);
        BeeTraceContext.setPId(pId);
        BeeTraceContext.setCTag(ctag);

        return span;
    }

    @Override
    public Object after(Span span, Method method, Object[] allArguments, Object result, Throwable t) {
        if(!BeeTraceContext.isRequestEnter()){
            return result;
        }
        calculateSpend(span);
        BeeTraceContext.clearAll();
        TransmitterFactory.transmit(span);
        return result;
    }
}
