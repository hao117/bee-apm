package net.beeapm.agent.plugin.handler;


import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.SpringTxContext;

/**
 * 统计事务内sql执行次数
 * @author yuan
 * @date 2018-09-22
 */
public class TotalSqlExecuteHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(TotalSqlExecuteHandler.class.getSimpleName());
    private static final String ATTR_KEY_SQL_TOTAL = "db_sql_total";
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
            Integer totalSql = (Integer) span.getAttribute(ATTR_KEY_SQL_TOTAL);
            if (totalSql == null) {
                totalSql = new Integer(0);
            }
            totalSql++;
            span.addAttribute(ATTR_KEY_SQL_TOTAL, totalSql);
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return result;
    }


}
