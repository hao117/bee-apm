package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/7/30.
 * 注意：实例方法使用@Advice.This注解，静态方法使用@Advice.Origin 两者不能混用
 */
public class BeeInstanceAdvice{
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Origin Method method,
                             @Advice.This Object instance,
                             @Advice.AllArguments Object[] allArguments){
        //TODO This is a demo !
    }

    /**
     * 如果需要返回值，在方法里添加注解和参数@Advice.Return(readOnly = false) Object result,result的类型要和实际返回值类型一致,需要修改参数readOnly置为false
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Local("handler") IHandler handler,
                            @Advice.Origin Method method,
                            @Advice.This Object instance,
                            @Advice.AllArguments Object[] allArguments,
                            @Advice.Thrown Throwable t){
        //TODO This is a demo !
    }
}
