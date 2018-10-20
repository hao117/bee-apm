package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuan on 2018/8/5.
 */

/**
 * 注意：实例方法使用@Advice.This注解，静态方法使用@Advice.Origin 两者不能混用
 */
public class ServletAdvice {
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.Argument(value = 1,readOnly = false) HttpServletResponse resp,
                             @Advice.Argument(value = 0) HttpServletRequest req){
        handler = HandlerLoader.load("net.beeapm.agent.plugin.handler.ServletHandler");
//        try {
//            if(!resp.getClass().getSimpleName().equals("BeeHttpResponseWrapper")) {
//                resp = (HttpServletResponse) HandlerLoader.loadClass("net.beeapm.agent.plugin.BeeHttpResponseWrapper").getDeclaredConstructor(HttpServletResponse.class).newInstance(resp);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        Span span = handler.before(className,methodName,new Object[]{req,resp},null);
        if(span != null && span.getTags().get("_respWrapper") != null){
            resp = (HttpServletResponse)span.getTags().get("_respWrapper");
            span.getTags().remove("_respWrapper");
        }
    }

    /**
     * 如果需要返回值，在方法里添加注解和参数@Advice.Return(readOnly = false) Object result,result的类型要和实际返回值类型一致,需要修改参数readOnly置为false
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Local("handler") IHandler handler,
                            @Advice.Origin("#t") String className,
                            @Advice.Origin("#m") String methodName,
                            @Advice.AllArguments Object[] args,
                            @Advice.Thrown Throwable t){
        handler.after(className,methodName,args, null,t,null);
    }

}
