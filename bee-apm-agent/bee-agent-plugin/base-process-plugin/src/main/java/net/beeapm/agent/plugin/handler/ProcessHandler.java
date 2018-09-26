package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.*;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.ProcessConfig;
import net.beeapm.agent.transmit.TransmitterFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;


/**
 * Created by yuan on 2018/7/31.
 */
public class ProcessHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ProcessHandler.class.getSimpleName());
    private static final String KEY_ERROR_THROWABLE = "_ERROR_THROWABLE";
    private static final String KEY_BEE_CHILD_ID = "_BEE_CHILD_ID";
    @Override
    public Span before(String className,String methodName, Object[] allArgs,Object[] extVal) {
        if(!ProcessConfig.me().isEnable()){
            return null;
        }
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

        Throwable childThrowable = (Throwable)span.getTags().get(KEY_ERROR_THROWABLE);
        span.removeTag(KEY_ERROR_THROWABLE);
        String childId = (String)span.getTags().get(KEY_BEE_CHILD_ID);
        span.removeTag(KEY_BEE_CHILD_ID);

        if(!ProcessConfig.me().isEnable()){
            return null;
        }

        calculateSpend(span);
        logEndTrace(className, methodName, span, log);
        //耗时阀值限制
        if(span.getSpend() > ProcessConfig.me().getSpend() && CollectRatio.YES()) {
            TransmitterFactory.transmit(span);
            collectParams(allArgs, span.getId());
        }
        //异常处理
        handleError(span.getId(),t,childId,childThrowable);
        return result;
    }

    public void handleError(String id,Throwable t,String childId,Throwable childThrowable){
        if(t == null && childThrowable == null){
            return;
        }
        Span parentSpan = SpanManager.getCurrentSpan();
        if(parentSpan == null){      //栈顶
            if(t == null && childThrowable != null){
                sendError(childId,childThrowable);
            }else if(t != null && childThrowable == null){
                sendError(id,t);
            }else{
                if(t == childThrowable || t.getCause() == childThrowable){
                    sendError(id,t);
                }else {
                    sendError(id, t);
                    sendError(childId, childThrowable);
                }
            }
        }else{
            if (childThrowable == null && t != null) {
                parentSpan.addTag(KEY_ERROR_THROWABLE, t);
                parentSpan.addTag(KEY_BEE_CHILD_ID, id);
                return;
            }else if(childThrowable != null && t == null){
                sendError(childId,childThrowable);
            }else{
                parentSpan.addTag(KEY_ERROR_THROWABLE, t);
                parentSpan.addTag(KEY_BEE_CHILD_ID, id);
                if(t != childThrowable && t.getCause() != childThrowable){
                    sendError(childId,childThrowable);
                }
            }
        }
    }

    public void sendError(String id,Throwable t){
        Span err = new Span(SpanType.ERROR);
        err.setId(id);
        err.setGid(BeeTraceContext.getGId());
        err.addTag("desc",formatThrowable(t));
    }


    private String formatThrowable(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
        }
        return expMessage;
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
