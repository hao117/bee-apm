package net.beeapm.agent.plugin.thread.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.handler.AbstractHandler;
import net.beeapm.agent.plugin.thread.common.ThreadConst;
import net.beeapm.agent.plugin.thread.wrapper.BeeCallableWrapper;
import net.beeapm.agent.plugin.thread.wrapper.BeeRunnableWrapper;
import net.beeapm.agent.plugin.thread.wrapper.BeeForkJoinTaskWrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinTask;

/**
 * @author yuan
 * @date 2018/08/05
 */
public class ThreadHandler extends AbstractHandler {

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        Object task = allArguments[0];
        Span span = new Span("t");
        if (task instanceof BeeRunnableWrapper || task instanceof BeeCallableWrapper || task instanceof BeeForkJoinTaskWrapper) {
            return null;
        }
        //修改入参
        if (task instanceof Runnable) {
            task = new BeeRunnableWrapper((Runnable) task, BeeTraceContext.getOrNew().copy());
        } else if (task instanceof Callable) {
            task = new BeeCallableWrapper((Callable) task, BeeTraceContext.getOrNew().copy());
        } else if (task instanceof BeeForkJoinTaskWrapper) {
            task = new BeeForkJoinTaskWrapper((ForkJoinTask) task, BeeTraceContext.getOrNew().copy());
        }
        span.addTag(ThreadConst.KEY_TASK, task);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {

        return null;
    }

}
