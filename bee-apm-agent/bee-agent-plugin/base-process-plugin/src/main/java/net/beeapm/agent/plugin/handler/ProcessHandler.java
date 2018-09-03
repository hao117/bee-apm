package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.ProcessConfig;
import net.beeapm.agent.transmit.TransmitterFactory;
import java.lang.ref.SoftReference;


/**
 * Created by yuan on 2018/7/31.
 */
public class ProcessHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ProcessHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArgs,Object[] extVal) {
        Span span = SpanManager.createEntrySpan(SpanType.PROCESS);
        logBeginTrace(className,methodName,span,log);
        span.addTag("method",methodName).addTag("clazz",className);
        return span;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArgs, Object result, Throwable t,Object[] extVal) {
        Span span = SpanManager.getExitSpan();
        if(span == null){
            return result;
        }
        calculateSpend(span);
        logEndTrace(className, methodName, span, log);
        //耗时阀值限制
        if(span.getSpend() > ProcessConfig.me().getSpend()) {
            TransmitterFactory.transmit(span);
            collectParams(allArgs, span.getId());
        }
        return result;
    }

    private void collectParams(Object[] allArgs,String id){
        if(ProcessConfig.me().isEnableParam() && allArgs != null && allArgs.length > 0) {
            Span paramSpan = new Span(SpanType.PARAM);
            paramSpan.setId(id);
            paramSpan.setTime(null);
            Object[] params = new Object[allArgs.length];
            for(int i = 0; i < allArgs.length; i++){
                if(allArgs[i] != null && ProcessConfig.me().isExcludeParamType(allArgs[i].getClass())){
                    params[i] = "--";
                }else {
                    params[i] = new SoftReference(allArgs[i]);
                }
            }
            paramSpan.addTag("param",params);
            TransmitterFactory.transmit(paramSpan);
        }
    }

}
