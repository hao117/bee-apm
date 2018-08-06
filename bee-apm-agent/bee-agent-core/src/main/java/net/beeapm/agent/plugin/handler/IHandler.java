package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.model.Span;

import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/3/7.
 */
public interface IHandler {
    /**
     * 方法调用前处理
     * @return
     */
    Span before(Method method, Object[] allArguments);
    /**
     * 方法执行完处理
     * @return
     */
     Object after(Span span, Method method, Object[] allArguments, Object result,Throwable t);
}
