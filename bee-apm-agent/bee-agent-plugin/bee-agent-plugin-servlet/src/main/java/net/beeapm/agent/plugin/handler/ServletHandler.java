package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.*;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.common.BeeHttpServletRequestWrapper;
import net.beeapm.agent.plugin.common.BeeHttpServletResponseWrapper;
import net.beeapm.agent.plugin.ServletConfig;
import net.beeapm.agent.plugin.common.servlet.Const;
import net.beeapm.agent.reporter.ReporterFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuan
 * @date 2018/08/05
 */
public class ServletHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog("ServletHandler");

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        if (!ServletConfig.me().isEnable()) {
            return null;
        }
        HttpServletRequest request = (HttpServletRequest) allArguments[0];
        HttpServletResponse resp = (HttpServletResponse) allArguments[1];
        String url = request.getRequestURL().toString();
        if (ServletConfig.me().isExcludeUrlSuffix(url)) {
            return null;
        }
        Span currSpan = SpanManager.getCurrentSpan();
        if (currSpan == null || !currSpan.getKind().equals(SpanType.REQUEST)) {
            BeeTraceContext.setTraceId(request.getHeader(HeaderKey.TRACE_ID));
            BeeTraceContext.setParentId(request.getHeader(HeaderKey.PARENT_ID));
            BeeTraceContext.setSampled(request.getHeader(HeaderKey.SAMPLED));
            Span span = SpanManager.createEntrySpan(SpanType.REQUEST);
            String srcApp = request.getHeader(HeaderKey.SRC_APP);
            if (srcApp == null) {
                srcApp = "nvl";
            }
            span.addAttribute("srcApp", srcApp);
            span.addAttribute("srcInst", request.getHeader(HeaderKey.SRC_INST));
            if (ServletConfig.me().isEnableRespBody() && !resp.getClass().getSimpleName().equals(Const.CLASS_BEE_HTTP_SERVLET_RESPONSE_WRAPPER)) {
                BeeHttpServletResponseWrapper wrapper = new BeeHttpServletResponseWrapper(resp);
                //在ServletAdvice里取出来要清除掉
                span.addCache(Const.KEY_RESP_WRAPPER, wrapper);
            }
            if (ServletConfig.me().isEnableReqBody() && !resp.getClass().getSimpleName().equals(Const.CLASS_BEE_HTTP_SERVLET_REQUEST_RAPPER)) {
                BeeHttpServletRequestWrapper wrapper = new BeeHttpServletRequestWrapper(request);
                //在ServletAdvice里取出来要清除掉
                span.addCache(Const.KEY_REQ_WRAPPER, wrapper);
            }
            SpanManager.createTopologySpan(request.getHeader(HeaderKey.SRC_APP), BeeConfig.me().getApp());
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        Span currSpan = SpanManager.getCurrentSpan();
        HttpServletResponse response = (HttpServletResponse) allArguments[1];
        if (!ServletConfig.me().isEnable() || SamplingUtil.NO()) {
            flush(response);
            return null;
        }
        if (currSpan != null && currSpan.getKind().equals(SpanType.REQUEST)) {
            Span span = SpanManager.getExitSpan();
            HttpServletRequest request = (HttpServletRequest) allArguments[0];
            span.addAttribute("url", request.getRequestURL());
            span.addAttribute("remote", request.getRemoteAddr());
            span.addAttribute("method", request.getMethod());
            calculateSpend(span);
            if (span.getDuration() > ServletConfig.me().getSpend() && SamplingUtil.YES()) {
                //返回gid，用于跟踪
                response.setHeader(HeaderKey.TRACE_ID, span.getTraceId());
                //返回id，用于跟踪
                response.setHeader(HeaderKey.ID, span.getId());
                BeeConfig.me().fillEnvInfo(span);
                ReporterFactory.report(span);
                //采集参数
                collectRequestParameter(span, request);
                //采集RequestBody
                collectRequestBody(span, request);
                //采集header
                collectRequestHeader(span, request);
                //采集ResponseBody
                collectResponseBody(span, response);
            }
            flush(response);
            return result;
        }
        flush(response);
        return null;
    }

    private void flush(HttpServletResponse response) {
        try {
            response.flushBuffer();
        } catch (Exception e) {
            log.error("response flush error", e);
        }
    }

    /**
     * 收集请求参数
     *
     * @param span
     * @param request
     */
    private void collectRequestParameter(Span span, HttpServletRequest request) {
        if (ServletConfig.me().isEnableReqParam()) {
            Map<String, String[]> params = request.getParameterMap();
            if (params != null && !params.isEmpty()) {
                Span paramSpan = new Span(SpanType.REQUEST_PARAM);
                paramSpan.setId(span.getId());
                paramSpan.addAttribute("param", JSON.toJSONString(params));
                ReporterFactory.report(paramSpan);
            }
        }
    }

    /**
     * 收集RequestBody
     *
     * @param span
     * @param request
     */
    private void collectRequestBody(Span span, HttpServletRequest request) {
        if (request instanceof BeeHttpServletRequestWrapper) {
            BeeHttpServletRequestWrapper wrapper = (BeeHttpServletRequestWrapper) request;
            Span bodySpan = new Span(SpanType.REQUEST_BODY);
            bodySpan.setId(span.getId());
            String body = new String(wrapper.getBody());
            if (body != null && !body.isEmpty()) {
                bodySpan.addAttribute("body", new String(wrapper.getBody()));
                ReporterFactory.report(bodySpan);
            }
        }
    }

    /**
     * 收集RequestHeader
     *
     * @param span
     * @param request
     */
    private void collectRequestHeader(Span span, HttpServletRequest request) {
        if (ServletConfig.me().isEnableReqHeaders()) {
            Map<String, String> headers = new HashMap<String, String>(8);
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headers.put(key, value);
            }
            if (!headers.isEmpty()) {
                Span headersSpan = new Span(SpanType.REQUEST_HEADERS);
                headersSpan.setId(span.getId());
                headersSpan.addAttribute("headers", JSON.toJSONString(headers));
                ReporterFactory.report(headersSpan);
            }
        }
    }

    /**
     * 收集ResponseBody
     *
     * @param span
     * @param resp
     */
    private void collectResponseBody(Span span, HttpServletResponse resp) {
        if (resp instanceof BeeHttpServletResponseWrapper) {
            BeeHttpServletResponseWrapper beeResp = (BeeHttpServletResponseWrapper) resp;
            //触发原有的输出
            beeResp.writeOriginOutputStream();
            Span respSpan = new Span(SpanType.RESPONSE_BODY);
            respSpan.setId(span.getId());
            byte[] body = beeResp.toByteArray();
            if (body != null && body.length > 0) {
                respSpan.addAttribute("body", new String(body));
                ReporterFactory.report(respSpan);
            }
        }
    }
}
