package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.bytebuddy.asm.Advice;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yuan on 2018/10/9.
 */
public class RequestReaderAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
    public static boolean enter(@Advice.This Object ths){
        //跳过原始的代码逻辑，执行下面OnMethodExit的代码
        return false;
    }

    @Advice.OnMethodExit()
    public static void exit(@Advice.Return(readOnly = false) BufferedReader reader,
                            @Advice.This Object ths){
        LogImpl log = LogManager.getLog("RequestReaderAdvice");
        try {
            InputStream is = (InputStream) ths.getClass().getMethod("getInputStream").invoke(ths);
            reader = new BufferedReader(new InputStreamReader(is));
        }catch (Exception e){
            log.error("enter:",e);
        }
    }
}
