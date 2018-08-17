package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/3/7.
 */
public interface IHandler {
    /**
     * 方法调用前处理
     * @return
     */
    Span before(String className,String methodName, Object[] allArguments,Object[] extVal);
    /**
     * 方法执行完处理
     * @return
     */
     Object after(String className,String methodName, Object[] allArguments, Object result,Throwable t,Object[] extVal);
}
