package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import java.lang.reflect.Method;

public class HandlerUtils {
    private static final LogImpl log = LogManager.getLog(HandlerUtils.class.getSimpleName());
    private static Method getMethod(Object handler,String name){
        Method[] methods = handler.getClass().getDeclaredMethods();
        for(int i = 0; i < methods.length; i++){
            if(methods[i].getName().equals(name)){
                return methods[i];
            }
        }
        return null;
    }
    public static Object doBefore(Object handler,String className,String methodName, Object[] allArgs){
        try {
            return getMethod(handler, "before").invoke(handler, new Object[]{className,methodName, allArgs});
        }catch (Exception e){
            log.warn("",e);
        }
        return null;
    }

    public static Object doAfter(Object handler,String className,String methodName, Object[] allArgs, Object result,Throwable t){
        try {
            return getMethod(handler, "after").invoke(handler, new Object[]{className,methodName,allArgs,result,t});
        }catch (Exception e){
            log.warn("",e);
        }
        return null;
    }


}
