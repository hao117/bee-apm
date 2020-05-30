package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.model.Span;

/**
 *
 * @author yuan
 * @date 2018/3/7
 */
public interface IHandler {

    /**
     * 方法调用前处理
     * @param className
     * @param methodName
     * @param allArguments
     * @param extVal
     * @return
     */
    Span before(String className,String methodName, Object[] allArguments,Object[] extVal);

    /**
     * 方法执行完处理
     * @param className
     * @param methodName
     * @param allArguments
     * @param result
     * @param t
     * @param extVal
     * @return
     */
     Object after(String className,String methodName, Object[] allArguments, Object result,Throwable t,Object[] extVal);
}
