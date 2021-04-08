package net.beeapm.agent.plugin.jdbc.handler;

import net.beeapm.agent.common.AttrKey;
import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanKind;
import net.beeapm.agent.plugin.handler.AbstractHandler;
import net.beeapm.agent.plugin.jdbc.JdbcConfig;
import net.beeapm.agent.plugin.jdbc.common.JdbcContext;

public class ConnectionHandler extends AbstractHandler {


    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        if (!JdbcConfig.me().isEnable() || SamplingUtil.NO()) {
            return null;
        }
        Span span = JdbcContext.getJdbcSpan();
        String traceId = BeeTraceContext.getTraceId();
        //如果traceId相等，那么调用过名称相同参数签名不一样的方法，属于二次调用,不需要再采集了
        if (span == null || !traceId.equals(span.getTraceId())) {
            span = SpanManager.createLocalSpan(SpanKind.SQL);
            JdbcContext.setJdbcSpan(span);
            span.addAttribute(AttrKey.DB_SQL, allArguments[0]);
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        if (t != null) {
            JdbcContext.remove();
        }
        return result;
    }
}
