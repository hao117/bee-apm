package net.beeapm.agent.plugin.handler;


import net.beeapm.agent.log.Log;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.SpringTxContext;

/**
 * 统计事务内sql执行次数
 * @author yuan
 * @date 2018-09-22
 */
public class SqlExecuteCountHandler extends AbstractHandler {
    private static final Log log = LogFactory.getLog(SqlExecuteCountHandler.class.getSimpleName());
    private static final String KEY_SQL_COUNT = "count";
    private static final int VAL_STACK_DEPTH = 3;

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        Span span = SpringTxContext.getTxSpan();
        if (span != null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            //排除父子关系调用
            if (stackTrace != null && stackTrace.length > VAL_STACK_DEPTH) {
                String method1 = stackTrace[2].getMethodName();
                String method2 = stackTrace[3].getMethodName();
                log.debug("method1={},method2={}", method1, method2);
                if (method1.equals(method2)) {
                    return span;
                }
            }
            Integer sqlCount = (Integer) span.getTag(KEY_SQL_COUNT);
            if (sqlCount == null) {
                sqlCount = new Integer(0);
            }
            sqlCount++;
            span.addTag(KEY_SQL_COUNT, sqlCount);
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return result;
    }


}
