package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.SpringTxConfig;
import net.beeapm.agent.plugin.common.SpringTxContext;

public class SpringTxBeginHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(SpringTxBeginHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal)  {
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal){
        if(t == null || !SpringTxConfig.me().isEnable()){
            return result;
        }
        Span span = SpanManager.createLocalSpan(SpanType.SPRING_TX);
        SpringTxContext.setTxSpan(span);
        logEndTrace(className,methodName,null,log);
        return result;
    }
}
