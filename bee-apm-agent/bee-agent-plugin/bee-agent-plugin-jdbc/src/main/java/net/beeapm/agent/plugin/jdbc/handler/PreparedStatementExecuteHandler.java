package net.beeapm.agent.plugin.jdbc.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanKind;
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
    private static final ILog log = LogFactory.getLog(ConnectionHandler.class.getSimpleName());
    private static final String ATTR_KEY_DB_SQL_STATUS = "db_sql_status";
    private static final String ATTR_KEY_DB_SQL_PARAMS = "db_sql_params";
    private static final String ATTR_KEY_DB_SQL_COUNT = "db_sql_count";
    private static final String CACHE_KEY_SQL_PARAMS = "cache_sql_params";

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        if (!JdbcConfig.me().isEnable() || SamplingUtil.NO()) {
            return null;
        }
        Span span = JdbcContext.getJdbcSpan();
        if (span == null) {
            span = SpanManager.createLocalSpan(SpanKind.SQL);
            JdbcContext.setJdbcSpan(span);
        } else {
            span.setStartTime(System.nanoTime());
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
            span.addAttribute(ATTR_KEY_DB_SQL_STATUS, "Y");
        } else {
            span.addAttribute(ATTR_KEY_DB_SQL_STATUS, "N");
        }
        calculateDuration(span);
        if (span.getDuration() > JdbcConfig.me().getSpend()) {
            Map<String, Object> params = (Map<String, Object>) span.getCache(CACHE_KEY_SQL_PARAMS);
            span.removeCache(CACHE_KEY_SQL_PARAMS);
            if (params != null) {
                Span paramSpan = new Span(SpanKind.SQL_PARAM);
                paramSpan.setId(span.getId());
                paramSpan.addAttribute(ATTR_KEY_DB_SQL_PARAMS, JSON.toJSONString(params.values()));
                ReporterFactory.report(paramSpan);
            }
            //当methodName=execute结果是ResultSet时候result=true，否则为false
            span.addAttribute(ATTR_KEY_DB_SQL_COUNT, calcResultCount(result));
            ReporterFactory.report(span);
        }
        return result;
    }

    /**
     * 获取结果集大小（sql执行结果返回的数据条数）
     *
     * @param result
     * @return
     */
    public String calcResultCount(Object result) {
        if (result == null) {
            return null;
        }
        Object count = null;
        if (result instanceof ResultSet) {
            ResultSet rs = (ResultSet) result;
            if (rs != null) {
                try {
                    ///TODO ？？？是否通过植入方式,当type为TYPE_FORWARD_ONLY修改为TYPE_SCROLL_INSENSITIVE？？？
                    //ResultSet类型为TYPE_FORWARD_ONLY，只能单向，此时不能进行滚动，否则无法回滚回去
                    if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {
                        return "-1";
                    }
                } catch (Exception e) {
                    return "-2";
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
