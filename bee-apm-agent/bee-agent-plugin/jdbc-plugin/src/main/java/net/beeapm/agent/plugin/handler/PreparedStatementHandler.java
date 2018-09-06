package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.JdbcConfig;
import net.beeapm.agent.plugin.common.JdbcContext;
import net.beeapm.agent.transmit.TransmitterFactory;
import org.apache.commons.lang3.ClassUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class PreparedStatementHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ConnectionHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal) {
        if(!JdbcConfig.me().isEnable()){
            return null;
        }
        Span span = JdbcContext.getJdbcSpan();
        // 参数处理
        if(span != null && methodName.startsWith("set") && allArguments.length > 1 && JdbcConfig.me().isEnableParam()) {
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
        //发生异常或者执行完sql，清除线程变量
        if(t != null && methodName.startsWith("execute")){
            JdbcContext.remove();
        }
        if(!JdbcConfig.me().isEnable()){
            return null;
        }

        if(methodName.startsWith("execute") && span != null) {
            if(t != null){
                span.addTag("status","1");
            }
            calculateSpend(span);
            if(span.getSpend() > JdbcConfig.me().getSpend()) {
                Map<String,Object> params = (Map<String,Object>)span.getTags().get("params");
                span.removeTag("params");
                if(params != null){
                    Span paramSpan = new Span(SpanType.SQL_PARAM);
                    paramSpan.setTime(null);
                    paramSpan.setId(span.getId());
                    paramSpan.addTag("args", JSON.toJSONString(params.values()));
                    TransmitterFactory.transmit(paramSpan);
                }
                TransmitterFactory.transmit(span);
            }
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
