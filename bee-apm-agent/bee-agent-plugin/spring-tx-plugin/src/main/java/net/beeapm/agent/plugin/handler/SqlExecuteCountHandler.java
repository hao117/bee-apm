package net.beeapm.agent.plugin.handler;


import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.SpringTxContext;


public class SqlExecuteCountHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(SqlExecuteCountHandler.class.getSimpleName());
    private static final String KEY_SQL_COUNT = "count";
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal) {
        Span span = SpringTxContext.getTxSpan();
        if(span != null){
           Integer sqlCount = (Integer)span.getTag(KEY_SQL_COUNT);
           if(sqlCount == null){
               sqlCount = new Integer(0);
           }
           sqlCount++;
           span.addTag(KEY_SQL_COUNT,sqlCount);
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        return result;
    }


}
