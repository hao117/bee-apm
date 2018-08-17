package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.common.JdbcContext;
import net.beeapm.agent.transmit.TransmitterFactory;
import org.apache.commons.lang3.ClassUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;

public class PreparedStatementHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ConnectionHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal) {
        Span span = JdbcContext.getJdbcSpan();
        // 参数处理
        if(span != null && methodName.startsWith("set") && allArguments.length > 1) {
            LinkedHashMap<String,Object> params = (LinkedHashMap<String,Object>)span.getTags().get("params");
            if(params == null){
                params = new LinkedHashMap<String,Object>();
                span.addTag("params",params);
            }
            String indexKey = allArguments[0] + "";
            if(!params.containsKey(indexKey)){
                Object val = allArguments[1];
                if(isExcludeType(val)){
                    params.put(indexKey,"{!}");
                }else{
                    params.put(indexKey,val);
                }
            }
        // sql执行处理
        }else if(methodName.startsWith("execute")){
            if(span == null){
                span = SpanManager.createLocalSpan(SpanType.SQL);
                JdbcContext.setJdbcSpan(span);
            }else{
               span.setTime(new Date());
            }
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        Span span = JdbcContext.getJdbcSpan();
        if(t != null || methodName.startsWith("execute")){
            JdbcContext.remove();
        }
        if(methodName.startsWith("execute") && span != null) {
            calculateSpend(span);
            TransmitterFactory.transmit(span);
        }
        return result;
    }

    public boolean isExcludeType(Object val){
        if(val instanceof String
                || ClassUtils.isPrimitiveOrWrapper(val.getClass())
                || val instanceof java.sql.Date
                || val instanceof java.util.Date
                || val instanceof Time
                || val instanceof Timestamp){
            return false;
        }
        return true;
    }
}
