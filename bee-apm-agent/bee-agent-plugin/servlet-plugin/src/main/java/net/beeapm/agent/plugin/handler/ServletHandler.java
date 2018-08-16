package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.*;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.transmit.TransmitterFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ServletHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments) {
        Span currSpan = SpanManager.getCurrentSpan();
        if(currSpan == null || !currSpan.getType().equals(SpanType.REQUEST)){
            HttpServletRequest request = (HttpServletRequest)allArguments[0];
            BeeTraceContext.setGId(request.getHeader(HeaderKey.GID));
            BeeTraceContext.setPId(request.getHeader(HeaderKey.PID));
            Span span = SpanManager.createEntrySpan(SpanType.REQUEST);
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments,Object result, Throwable t) {
        Span currSpan = SpanManager.getCurrentSpan();
        if(currSpan!=null && currSpan.getType().equals(SpanType.REQUEST)) {
            Span span = SpanManager.getExitSpan();
            HttpServletRequest request = (HttpServletRequest) allArguments[0];
            HttpServletResponse response = (HttpServletResponse) allArguments[1];
            span.addTag("url", request.getRequestURL());
            span.addTag("remote", request.getRemoteAddr());
            span.addTag("method", methodName);
            span.addTag("clazz", className);
            response.setHeader(HeaderKey.GID, span.getGid());   //返回gid，用于跟踪
            response.setHeader(HeaderKey.ID, span.getId());     //返回id，用于跟踪
            calculateSpend(span);
            TransmitterFactory.transmit(span);
            return result;
        }
        return null;
    }
}
