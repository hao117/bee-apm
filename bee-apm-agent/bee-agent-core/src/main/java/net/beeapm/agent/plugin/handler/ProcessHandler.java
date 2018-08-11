package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.TransmitterFactory;

import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/7/31.
 */
public class ProcessHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ProcessHandler.class.getSimpleName());
    @Override
    public Span before(Method m, Object[] allArgs) {
        Span span = SpanManager.createEntrySpan(SpanType.PROCESS);
        logBeginTrace(m,span,log);
        span.addTag("method",getMethodName(m)).addTag("clazz",getClassName(m));
        return span;
    }

    @Override
    public Object after(Method m, Object[] allArgs, Object result, Throwable t) {
        Span span = SpanManager.getExitSpan();
        if(span == null){
            return result;
        }
        calculateSpend(span);
        logEndTrace(m,span,log);
        TransmitterFactory.transmit(span);
        return result;
    }

}
