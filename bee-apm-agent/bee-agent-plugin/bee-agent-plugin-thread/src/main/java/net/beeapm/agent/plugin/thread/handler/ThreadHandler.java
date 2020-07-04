package net.beeapm.agent.plugin.thread.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.TraceContextModel;
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
    private static final ILog log = LogFactory.getLog(ThreadHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        log.debug(className);
        Object task = allArguments[0];

        if (task instanceof BeeRunnableWrapper
                || task instanceof BeeCallableWrapper
                || task instanceof BeeForkJoinTaskWrapper) {
            return null;
        }
        Span span = new Span("t");
        //修改入参
        TraceContextModel traceContextModel = BeeTraceContext.getOrNew().copy();
        traceContextModel.setPid(BeeTraceContext.getCurrentId());
        if (task instanceof Runnable) {
            task = new BeeRunnableWrapper((Runnable) task, traceContextModel);
        } else if (task instanceof Callable) {
            task = new BeeCallableWrapper((Callable) task, traceContextModel);
        } else if (task instanceof BeeForkJoinTaskWrapper) {
            task = new BeeForkJoinTaskWrapper((ForkJoinTask) task, traceContextModel);
        }
        span.addTag(ThreadConst.KEY_TASK, task);
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return null;
    }

}
