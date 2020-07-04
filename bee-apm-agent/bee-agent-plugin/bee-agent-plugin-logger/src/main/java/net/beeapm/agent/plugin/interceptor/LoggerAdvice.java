package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

/**
 * @author yuan
 * @date 2018/8/19
 */
public class LoggerAdvice {
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.AllArguments Object[] allParams,
                             @Advice.FieldValue("name") String name) {
        IHandler handler = HandlerLoader.load("net.beeapm.agent.plugin.handler.LoggerHandler");
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        String pointMethod = stacks[2].getMethodName();
        handler.before(className, methodName, allParams, new String[]{name, pointMethod});
    }
}
