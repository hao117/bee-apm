package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.SamplingUtil;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.common.SpringTxConfig;
import net.beeapm.agent.plugin.common.SpringTxContext;
import org.springframework.transaction.TransactionDefinition;

public class SpringTxBeginHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(SpringTxBeginHandler.class.getSimpleName());

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        if (t != null || !SpringTxConfig.me().isEnable() || SamplingUtil.NO()) {
            return result;
        }
        Span span = SpanManager.createLocalSpan(SpanType.SPRING_TX);
        TransactionDefinition definition = (TransactionDefinition) allArguments[1];
        span.addTag("point", definition.getName());
        SpringTxContext.setTxSpan(span);
        logEndTrace(className, methodName, span, log);
        return result;
    }
}
