package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.ServletConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestReaderHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog("RequestReaderAdvice");
    @Override
    public Span before(String className, String methodName, Object[] allArguments, Object[] extVal) {
        Span span = new Span("");
        if(ServletConfig.me().isEnableReqBody()){
            span.addTag("skip",false);//跳过原有的代码，不执行
        }else {
            span.addTag("skip", true);//执行原有的
        }
        return span;
    }

    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t, Object[] extVal) {
        try {
            if(ServletConfig.me().isEnableReqBody()) {
                InputStream is = (InputStream) extVal[0].getClass().getMethod("getInputStream").invoke(extVal[0]);
                return new BufferedReader(new InputStreamReader(is));
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }
}
