package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.*;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
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
        if(BeeTraceContext.getAndIncrRequestEntryCounter() != 0){
            return null;
        }
        HttpServletRequest request = (HttpServletRequest)allArguments[0];
        BeeTraceContext.setGId(request.getHeader(HeaderKey.GID));
        BeeTraceContext.setPId(request.getHeader(HeaderKey.PID));
        Span span = SpanManager.createEntrySpan(SpanType.REQUEST);
        return span;
    }

    @Override
    public Object after(Method method, Object[] allArguments, Object result, Throwable t) {
        if(BeeTraceContext.decrAndGetRequestEntryCounter() > 0){
            return null;
        }

        Span span = SpanManager.getExitSpan();
        if(span == null){
            return result;
        }
        HttpServletRequest request = (HttpServletRequest)allArguments[0];
        HttpServletResponse response  = (HttpServletResponse)allArguments[1];
        span.addTag("url",request.getRequestURL());
        span.addTag("remote",request.getRemoteAddr());
        span.addTag("method",getMethodName(method));
        span.addTag("clazz",getClassName(method));
        response.setHeader(HeaderKey.GID,span.getGid());   //返回gid，用于跟踪
        response.setHeader(HeaderKey.ID,span.getId());     //返回id，用于跟踪
        calculateSpend(span);
        TransmitterFactory.transmit(span);
        return result;
    }
}
