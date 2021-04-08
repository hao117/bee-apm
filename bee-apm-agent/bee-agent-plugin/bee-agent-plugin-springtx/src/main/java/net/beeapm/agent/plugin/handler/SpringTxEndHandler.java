package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.SpringTxConfig;
import net.beeapm.agent.plugin.common.SpringTxContext;
import net.beeapm.agent.reporter.ReporterFactory;

/**
 * @date 2018/9/20
 * @author yuan
 */
public class SpringTxEndHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(SpringTxEndHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal)  {
        if(!SpringTxConfig.me().isEnable() || SamplingUtil.NO()){
            return null;
        }
        Span span = SpringTxContext.getTxSpan();
        SpringTxContext.remove();
        if(span != null) {
            calculateDuration(span);
            if(span.getDuration() > SpringTxConfig.me().getSpend()) {
                ReporterFactory.report(span);
            }
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal){
        return result;
    }
}
