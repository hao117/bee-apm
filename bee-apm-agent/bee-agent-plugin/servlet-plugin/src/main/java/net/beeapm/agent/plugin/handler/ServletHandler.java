package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.*;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.ServletConfig;
import net.beeapm.agent.transmit.TransmitterFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ServletHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments,Object[] extVal) {
        if(!ServletConfig.me().isEnable()){
            return null;
        }
        Span currSpan = SpanManager.getCurrentSpan();
        if(currSpan == null || !currSpan.getType().equals(SpanType.REQUEST)){
            HttpServletRequest request = (HttpServletRequest)allArguments[0];
            BeeTraceContext.setGId(request.getHeader(HeaderKey.GID));
            BeeTraceContext.setPId(request.getHeader(HeaderKey.PID));
            BeeTraceContext.setCTag(request.getHeader(HeaderKey.CTAG));
            Span span = SpanManager.createEntrySpan(SpanType.REQUEST);
            span.addTag("sc",request.getHeader(HeaderKey.SRC_CLUSTER));
            span.addTag("ss",request.getHeader(HeaderKey.SRC_SERVER));
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments,Object result, Throwable t,Object[] extVal) {
        Span currSpan = SpanManager.getCurrentSpan();
        if(!ServletConfig.me().isEnable() || CollectRatio.NO()){
            return null;
        }
        if(currSpan!=null && currSpan.getType().equals(SpanType.REQUEST)) {
            Span span = SpanManager.getExitSpan();
            HttpServletRequest request = (HttpServletRequest) allArguments[0];
            HttpServletResponse response = (HttpServletResponse) allArguments[1];
            span.addTag("url", request.getRequestURL());
            span.addTag("remote", request.getRemoteAddr());
            span.addTag("method", methodName);
            span.addTag("clazz", className);
            calculateSpend(span);
            if(span.getSpend() > ServletConfig.me().getSpend() && CollectRatio.YES()) {
                response.setHeader(HeaderKey.GID, span.getGid());   //返回gid，用于跟踪
                response.setHeader(HeaderKey.ID, span.getId());     //返回id，用于跟踪
                TransmitterFactory.transmit(span);
                if(ServletConfig.me().isEnableParam()){
                    Span paramSpan = new Span(SpanType.REQUEST_PARAM);
                    paramSpan.setId(span.getId());
                    paramSpan.setIp(null);
                    paramSpan.setPort(null);
                    paramSpan.setServer(null);
                    paramSpan.setCluster(null);
                    paramSpan.addTag("param", JSON.toJSONString(request.getParameterMap()));
                    TransmitterFactory.transmit(paramSpan);
                }
            }
            return result;
        }
        return null;
    }

}
