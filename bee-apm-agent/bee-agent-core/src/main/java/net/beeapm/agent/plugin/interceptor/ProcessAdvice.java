package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.HandlerUtils;
import net.beeapm.agent.plugin.handler.IHandler;
import net.beeapm.agent.plugin.handler.ProcessHandler;
import net.beeapm.agent.model.Span;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * Created by yuan on 2018/7/29.
 */

/**
 * 注意：实例方法使用@Advice.This注解，静态方法使用@Advice.Origin 两者不能混用
 */
public class ProcessAdvice {
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Local("handler") Object handler,
                             @Advice.Origin Method method,
                             @Advice.AllArguments Object[] allParams){
        handler = HandlerLoader.load(ProcessHandler.class.getName(),method.getDeclaringClass().getClassLoader());
        HandlerUtils.doBefore(handler,method,allParams);
    }

    /**
     * 如果需要返回值，在方法里添加注解和参数@Advice.Return(readOnly = false) Object result,result的类型要和实际返回值类型一致,需要修改参数readOnly置为false
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Local("handler") Object handler,
                            @Advice.Origin Method method,
                            @Advice.AllArguments Object[] allParams,
                            @Advice.Thrown Throwable t){
        HandlerUtils.doAfter(handler,method,allParams, null,t);
    }

}
