package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;
import java.io.BufferedReader;

/**
 * Created by yuan on 2018/10/9.
 */
public class RequestReaderAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    public static boolean enter(@Advice.Local("handler") IHandler handler){
        handler = HandlerLoader.load("net.beeapm.agent.plugin.handler.RequestReaderHandler");
        Span span = handler.before(null,null,null,null);
        //跳过原始的代码逻辑，执行下面OnMethodExit的代码
        return (Boolean) span.getTags().get("skip");
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.This Object ths,
                            @Advice.Return(readOnly = false) BufferedReader reader,
                            @Advice.Thrown Throwable t,
                            @Advice.Local("handler") IHandler handler){
        reader = (BufferedReader)handler.after(null,null,null,reader,t,new Object[]{ths});
    }
}
