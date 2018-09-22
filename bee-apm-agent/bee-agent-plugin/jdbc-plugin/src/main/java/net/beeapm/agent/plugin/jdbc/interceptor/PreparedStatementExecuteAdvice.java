package net.beeapm.agent.plugin.jdbc.interceptor;

import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

public class PreparedStatementExecuteAdvice {
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.AllArguments Object[] allParams){
        handler = HandlerLoader.load("net.beeapm.agent.plugin.jdbc.handler.PreparedStatementExecuteHandler");
        handler.before(className,methodName,allParams,null);
    }

    /**
     * 如果需要返回值，在方法里添加注解和参数@Advice.Return(readOnly = false) Object result,result的类型要和实际返回值类型一致,需要修改参数readOnly置为false
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Local("handler") IHandler handler,
                            @Advice.Origin("#t") String className,
                            @Advice.Origin("#m") String methodName,
                            @Advice.AllArguments Object[] allParams,
                            @Advice.Return Object result,
                            @Advice.Thrown Throwable t){
        handler.after(className,methodName,allParams, result,t,null);
    }
}
