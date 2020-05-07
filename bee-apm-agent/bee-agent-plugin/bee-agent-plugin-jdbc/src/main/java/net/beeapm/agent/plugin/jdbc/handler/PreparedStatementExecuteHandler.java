package net.beeapm.agent.plugin.jdbc.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.Log;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.handler.AbstractHandler;
import net.beeapm.agent.plugin.jdbc.JdbcConfig;
import net.beeapm.agent.plugin.jdbc.common.JdbcContext;
import net.beeapm.agent.reporter.ReporterFactory;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

/**
 * sql内容和sql参数采集
 *
 * @author yuan
 * @date 2018-09-22
 */
public class PreparedStatementExecuteHandler extends AbstractHandler {
    private static final Log log = LogFactory.getLog(ConnectionHandler.class.getSimpleName());


    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        if (!JdbcConfig.me().isEnable() || SamplingUtil.NO()) {
            return null;
        }
        Span span = JdbcContext.getJdbcSpan();
        if (span == null) {
            span = SpanManager.createLocalSpan(SpanType.SQL);
            JdbcContext.setJdbcSpan(span);
        } else {
            span.setTime(new Date());
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        Span span = JdbcContext.getJdbcSpan();
        JdbcContext.remove();
        if (!JdbcConfig.me().isEnable() || span == null || SamplingUtil.NO()) {
            return null;
        }
        //Y成功，N失败
        if (t == null) {
            span.addTag("status", "Y");
        } else {
            span.addTag("status", "N");
        }
        calculateSpend(span);
        if (span.getSpend() > JdbcConfig.me().getSpend()) {
            Map<String, Object> params = (Map<String, Object>) span.getTag("params");

            span.removeTag("params");
            if (params != null) {
                Span paramSpan = new Span(SpanType.SQL_PARAM);
                paramSpan.setId(span.getId());
                paramSpan.addTag("args", JSON.toJSONString(params.values()));
                ReporterFactory.report(paramSpan);
            }
            //当methodName=execute结果是ResultSet时候result=true，否则为false
            span.addTag("count", calcResultCount(result));
            span.fillEnvInfo();
            ReporterFactory.report(span);
        }
        return result;
    }

    public String calcResultCount(Object result) {
        if (result == null) {
            return null;
        }
        Object count = null;
        if (result instanceof ResultSet) {
            ResultSet rs = (ResultSet) result;
            if (rs != null) {
                try {
                    //ResultSet类型为TYPE_FORWARD_ONLY，只能单向，此时不能进行滚动，否则无法回滚回去
                    if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {
                        return "only";
                    }
                } catch (Exception e) {
                    return "error";
                }
                try {
                    rs.last();
                    count = rs.getRow();
                } catch (Exception e) {
                    log.warn("ResultSet::last exception");
                } finally {
                    try {
                        //移动到第一行前
                        rs.first();
                    } catch (Exception e) {
                        log.error("ResultSet::absolute exception", e);
                    }
                }
            }
        } else {
            count = result;
        }
        return count.toString();
    }
}
