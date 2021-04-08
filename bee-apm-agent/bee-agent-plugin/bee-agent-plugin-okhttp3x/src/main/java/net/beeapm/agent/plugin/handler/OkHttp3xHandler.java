package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.HeaderKey;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/8/14.
 * @date 2018/8/14
 * @author yuan
 */
public class OkHttp3xHandler extends AbstractHandler {
    private static final ILog log = LogFactory.getLog(OkHttp3xHandler.class.getSimpleName());

    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        try {
            if (allArguments[0] != null && allArguments[0] instanceof okhttp3.Request.Builder) {
                okhttp3.Request.Builder builder = (okhttp3.Request.Builder) allArguments[0];
                builder.header(HeaderKey.TRACE_ID, BeeTraceContext.getTraceId());
                builder.header(HeaderKey.PARENT_ID, BeeTraceContext.getCurrentId());
                builder.header(HeaderKey.SRC_APP, BeeConfig.me().getApp());
                builder.header(HeaderKey.SRC_INST, BeeConfig.me().getInst());
                if (BeeTraceContext.getSampled() != null) {
                    builder.header(HeaderKey.SAMPLED, BeeTraceContext.getSampled());
                }
            }
        } catch (Exception e) {
            log.warn("set header failed", e);
        }
        return null;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        return result;
    }
}
