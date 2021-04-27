package net.beeapm.agent.plugin.interceptor;

import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.servlet.Const;
import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 注意：实例方法使用@Advice.This注解，静态方法使用@Advice.Origin 两者不能混用
 *
 * @author yuan
 * @date 2018/08/05
 */
public class ServletAdvice {
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Local("flag") AtomicBoolean flag,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.Argument(value = 0, readOnly = false, typing = Assigner.Typing.DYNAMIC) Object req,
                             @Advice.Argument(value = 1, readOnly = false, typing = Assigner.Typing.DYNAMIC) Object resp) {
        if (req.getClass().getSimpleName().equals("BeeHttpServletRequestWrapper")) {
            return;
        }
        flag = new AtomicBoolean(false);
        handler = HandlerLoader.load("net.beeapm.agent.plugin.handler.ServletHandler");
        Span span = handler.before(className, methodName, new Object[]{req, resp}, new Object[]{flag});
        if (span != null && span.getCache(Const.KEY_RESP_WRAPPER) != null) {
            //修改resp
            resp = span.getCache(Const.KEY_RESP_WRAPPER);
            span.removeCache(Const.KEY_RESP_WRAPPER);
        }
        if (span != null && span.getCache(Const.KEY_REQ_WRAPPER) != null) {
            //修改req
            req = span.getCache(Const.KEY_REQ_WRAPPER);
            span.removeCache(Const.KEY_REQ_WRAPPER);
        }
    }

    /**
     * 如果需要返回值，在方法里添加注解和参数@Advice.Return(readOnly = false) Object result,result的类型要和实际返回值类型一致,需要修改参数readOnly置为false
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Local("handler") IHandler handler,
                            @Advice.Local("flag") AtomicBoolean flag,
                            @Advice.Origin("#t") String className,
                            @Advice.Origin("#m") String methodName,
                            @Advice.AllArguments Object[] args,
                            @Advice.Thrown Throwable t) {
        if (flag != null && flag.get()) {
            handler.after(className, methodName, args, null, t, null);
        }
    }

}
