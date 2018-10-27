package net.beeapm.agent.plugin.jdbc.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.CollectRatio;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.handler.AbstractHandler;
import net.beeapm.agent.plugin.jdbc.JdbcConfig;
import net.beeapm.agent.plugin.jdbc.common.JdbcContext;
import net.beeapm.agent.transmit.TransmitterFactory;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

public class PreparedStatementExecuteHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ConnectionHandler.class.getSimpleName());


    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal) {
        if(!JdbcConfig.me().isEnable() || CollectRatio.NO()){
            return null;
        }
        Span span = JdbcContext.getJdbcSpan();
        if(span == null){
            span = SpanManager.createLocalSpan(SpanType.SQL);
            JdbcContext.setJdbcSpan(span);
        }else{
           span.setTime(new Date());
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        Span span = JdbcContext.getJdbcSpan();
        JdbcContext.remove();
        if(!JdbcConfig.me().isEnable() || span == null || CollectRatio.NO()){
            return null;
        }
        if(t != null){
            span.addTag("status","1");
        }
        calculateSpend(span);
        if(span.getSpend() > JdbcConfig.me().getSpend()) {
            Map<String,Object> params = (Map<String,Object>)span.getTags().get("params");
            span.removeTag("params");
            if(params != null){
                Span paramSpan = new Span(SpanType.SQL_PARAM);
                paramSpan.setId(span.getId());
                paramSpan.addTag("args", JSON.toJSONString(params.values()));
                TransmitterFactory.transmit(paramSpan);
            }
            span.addTag("count",calcResultCount(result));
            span.fillEnvInfo();
            TransmitterFactory.transmit(span);
        }
        return result;
    }

    public Long calcResultCount(Object result){
        if(result == null){
            return null;
        }
        Long count = null;
        if(result instanceof  ResultSet) {
            ResultSet rs = (ResultSet) result;
            if (rs != null) {
                try {
                    rs.last();
                    count = new Long(rs.getRow());
                } catch (Exception e) {
                    log.warn("ResultSet::last exception");
                } finally {
                    try {
                        rs.absolute(0);//移动到第一行前
                    } catch (Exception e) {
                        log.warn("ResultSet::absolute exception");
                    }
                }
            }
        }
        if(result instanceof Long){
            count = (Long)result;
        }
        if(result instanceof Integer){
            count = new Long((Integer)result);
        }
        return count;
    }
}
