package net.beeapm.agent.plugin.thread.interceptor;

import net.beeapm.agent.common.HandlerContext;
import net.beeapm.agent.log.LogUtil;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.beeapm.agent.plugin.thread.common.ThreadConst;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.util.concurrent.FutureTask;

/**
 * @author yuan
 * @date 2020/05/28
 */
public class ThreadAdvice {

    @Advice.OnMethodEnter()
    public static void enter(@Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.Argument(value = 0, readOnly = false, typing = Assigner.Typing.DYNAMIC) Object task) {
        //入参是FutureTask通过拦截FutureTask的构造函数进行处理,这里跳过
        if (task instanceof FutureTask) {
            return;
        }
        IHandler handler = HandlerLoader.load("net.beeapm.agent.plugin.thread.handler.ThreadHandler");
        HandlerContext.remove();
        Span span = handler.before(className, methodName, new Object[]{task}, null);
        if (span != null) {
            Object ret = span.getCache(ThreadConst.CACHE_KEY_TASK);
            span.clearCache();
            if (ret != null) {
                //修改入参
                task = ret;
            }
        }
    }
}
