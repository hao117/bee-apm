package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.*;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.common.BeeHttpResponseWrapper;
import net.beeapm.agent.plugin.ServletConfig;
import net.beeapm.agent.plugin.common.RequestBodyHolder;
import net.beeapm.agent.reporter.ReporterFactory;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
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
        HttpServletRequest request = (HttpServletRequest)allArguments[0];
        HttpServletResponse resp = (HttpServletResponse) allArguments[1];
        String url = request.getRequestURL().toString();
        if(ServletConfig.me().checkUrlSuffixExclude(url)){
            return null;
        }
        Span currSpan = SpanManager.getCurrentSpan();
        if(currSpan == null || !currSpan.getType().equals(SpanType.REQUEST)){
            BeeTraceContext.setGId(request.getHeader(HeaderKey.GID));
            BeeTraceContext.setPId(request.getHeader(HeaderKey.PID));
            BeeTraceContext.setCTag(request.getHeader(HeaderKey.CTAG));
            Span span = SpanManager.createEntrySpan(SpanType.REQUEST);
            span.addTag("srcApp",request.getHeader(HeaderKey.SRC_APP));
            span.addTag("srcInst",request.getHeader(HeaderKey.SRC_INST));
            if(ServletConfig.me().isEnableRespBody() && !resp.getClass().getSimpleName().equals("BeeHttpResponseWrapper")){
                BeeHttpResponseWrapper wrapper = new BeeHttpResponseWrapper(resp);
                span.addTag("_respWrapper",wrapper);//在ServletAdvice里取出来要清除掉
            }
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments,Object result, Throwable t,Object[] extVal) {
        Span currSpan = SpanManager.getCurrentSpan();
        if(!ServletConfig.me().isEnable() || SamplingUtil.NO()){
            return null;
        }
        if(currSpan!=null && currSpan.getType().equals(SpanType.REQUEST)) {
            Span span = SpanManager.getExitSpan();
            HttpServletRequest request = (HttpServletRequest) allArguments[0];
            HttpServletResponse response = (HttpServletResponse) allArguments[1];
            span.addTag("url", request.getRequestURL());
            span.addTag("remote", request.getRemoteAddr());
            span.addTag("method", request.getMethod());
            //span.addTag("clazz", className);
            calculateSpend(span);
            if(span.getSpend() > ServletConfig.me().getSpend() && SamplingUtil.YES()) {
                response.setHeader(HeaderKey.GID, span.getGid());   //返回gid，用于跟踪
                response.setHeader(HeaderKey.ID, span.getId());     //返回id，用于跟踪
                span.fillEnvInfo();
                ReporterFactory.report(span);
                collectRequestParameter(span,request);//采集参数
                collectRequestBody(span,request);//采集body
                collectRequestHeader(span,request);//采集header
                collectResponseBody(span,response);
            }
            return result;
        }
        return null;
    }

    private void collectRequestParameter(Span span,HttpServletRequest request){
        if(ServletConfig.me().isEnableReqParam()){
            Map<String, String[]> params = request.getParameterMap();
            if(params != null && !params.isEmpty()) {
                Span paramSpan = new Span(SpanType.REQUEST_PARAM);
                paramSpan.setId(span.getId());
                paramSpan.addTag("param", JSON.toJSONString(params));
                ReporterFactory.report(paramSpan);
            }
        }
    }
    private void collectRequestBody(Span span,HttpServletRequest request){
        if(ServletConfig.me().isEnableReqBody()){
            try {
                //触发获取body的代码植入
                request.getInputStream();
            }catch (Exception e){
            }
            if(StringUtils.isNotBlank(RequestBodyHolder.getRequestBody())) {
                Span bodySpan = new Span(SpanType.REQUEST_BODY);
                bodySpan.setId(span.getId());
                bodySpan.addTag("body", RequestBodyHolder.getRequestBody());
                ReporterFactory.report(bodySpan);
            }
        }
    }
    private void collectRequestHeader(Span span,HttpServletRequest request){
        if(ServletConfig.me().isEnableReqHeaders()){
            Map<String, String> headers = new HashMap<String, String>();
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headers.put(key, value);
            }
            if(!headers.isEmpty()) {
                Span headersSpan = new Span(SpanType.REQUEST_HEADERS);
                headersSpan.setId(span.getId());
                headersSpan.addTag("headers", JSON.toJSONString(headers));
                ReporterFactory.report(headersSpan);
            }
        }
    }
    private void collectResponseBody(Span span,HttpServletResponse resp){
        if(ServletConfig.me().isEnableRespBody()){
            BeeHttpResponseWrapper beeResp = (BeeHttpResponseWrapper)resp;
            beeResp.out();//触发原有的输出
            Span respSpan = new Span(SpanType.RESPONSE_BODY);
            respSpan.setId(span.getId());
            String body = new String(beeResp.getBytes());
            if(StringUtils.isNotBlank(body)){
                respSpan.addTag("body", body);
                ReporterFactory.report(respSpan);
            }
        }
    }
}
