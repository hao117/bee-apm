package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;

import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/7/30.
 */
public class EmptyHandler extends AbstractHandler {

    private static final LogImpl log = LogManager.getLog(EmptyHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments) {
        Span span = new Span("empty");
        logBeginTrace(className,methodName,span,log);
        return span;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments, Object result, Throwable t) {
        logEndTrace(className,methodName,new Span("empty"),log);
        return result;
    }

}
