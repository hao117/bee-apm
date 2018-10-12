package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

import javax.servlet.ServletInputStream;

/**
 * Created by yuan on 2018/10/9.
 */
public class RequestInputStreamAdvice {
    @Advice.OnMethodExit()
    public static void exit(@Advice.Return(readOnly = false) ServletInputStream inputStream){
        try {
            IHandler handler = HandlerLoader.load("net.beeapm.agent.plugin.handler.RequestInputStreamHandler");
            inputStream = (ServletInputStream)handler.after(null, null, null, inputStream, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
