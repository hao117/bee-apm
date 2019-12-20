package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.SpringTxConfig;
import net.beeapm.agent.plugin.common.SpringTxContext;
import net.beeapm.agent.transmit.TransmitterFactory;

public class SpringTxEndHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(SpringTxEndHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal)  {
        if(!SpringTxConfig.me().isEnable() || SamplingUtil.NO()){
            return null;
        }
        Span span = SpringTxContext.getTxSpan();
        SpringTxContext.remove();
        if(span != null) {
            ///TODO 缺少被事务拦截的方法名称
            calculateSpend(span);
            if(span.getSpend() > SpringTxConfig.me().getSpend()) {
                span.fillEnvInfo();
                TransmitterFactory.transmit(span);
            }
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal){
        return result;
    }
}
