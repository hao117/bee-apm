package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.CollectRatio;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.LoggerConfig;
import net.beeapm.agent.transmit.TransmitterFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

public class LoggerHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(LoggerHandler.class.getSimpleName());
    @Override
    public Span before(String className, String methodName, Object[] allArguments,Object[] extVal) {
        String point = (String) extVal[0];
        boolean isCollect = true;
        if(LoggerConfig.me().level(methodName) >= LoggerConfig.LEVEL_ERROR){
            if(LoggerConfig.me().errorRatio()){// 是否error采样
                isCollect = CollectRatio.YES();
            }else {                            //error不采样，全采集
                isCollect = true;
            }
        }
        if(!LoggerConfig.me().isEnable() || !LoggerConfig.me().checkLevel(point,methodName) || !isCollect){
            return null;
        }
        if("org.apache.logging.log4j.status.StatusLogger".equals(point)){//重复打印，排除掉
            return null;
        }
        Span span = SpanManager.createLocalSpan(SpanType.LOGGER);
        StringBuilder logBuff = new StringBuilder();
        for(int i = 0; i < allArguments.length; i++){
            Object arg = allArguments[i];
            if(i>0){
                logBuff.append(" | ");
            }
            if(arg==null){
                continue;
            }
            if(arg instanceof Throwable){
                logBuff.append(parseThrowable((Throwable)arg));
            }else{
                logBuff.append(JSON.toJSONString(arg));
            }
        }
        span.addTag("point",point);
        span.addTag("log",logBuff.toString());
        span.addTag("level",methodName);
        span.fillEnvInfo();
        TransmitterFactory.transmit(span);
        return null;
    }



    @Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        return result;
    }

    public static String parseThrowable(Throwable t){
        StringBuilder builder = new StringBuilder();
        Writer writer = null;
        PrintWriter printWriter = null;
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            t.printStackTrace(printWriter);
            printWriter.flush();
            builder.append(writer.toString());
            printWriter.close();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                }catch (Exception e){
                }
            }
            if(printWriter != null){
                try {
                    printWriter.close();
                }catch (Exception e){
                }
            }
        }
        try{
            Field messageField = Throwable.class.getDeclaredField("detailMessage");
            messageField.setAccessible(true);
            String detailMessage = t.getMessage();
            if(messageField!=null){
                try{
                    messageField.set(t,"["+ BeeTraceContext.getGId() +"]"+detailMessage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Throwable tt){
            tt.printStackTrace();
        }
        return builder.toString();
    }

}
