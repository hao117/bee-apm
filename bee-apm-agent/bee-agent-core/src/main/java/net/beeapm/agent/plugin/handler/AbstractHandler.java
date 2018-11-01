package net.beeapm.agent.plugin.handler;


import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.model.Span;

import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/7/30.
 */
public abstract class AbstractHandler implements IHandler {

    public void logBeginTrace(String className,String methodName, Span span, LogImpl log){
        log.trace("[begin]{}.{} type={}", className, methodName, span.getType());
    }
    public void logEndTrace(String className,String methodName, Span span,LogImpl log){
        log.trace("[end]{}.{} type={}", className, methodName, span.getType());
    }

    public void calculateSpend(Span span){
        if(span != null){
            span.setSpend(System.currentTimeMillis() - span.getTime().getTime());
        }
    }

}
