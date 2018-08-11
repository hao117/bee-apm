package net.beeapm.agent.plugin.handler;

import java.lang.reflect.Method;

public class HandlerUtils {
    private static Method getMethod(Object handler,String name){
        Method[] methods = handler.getClass().getDeclaredMethods();
        for(int i = 0; i < methods.length; i++){
            if(methods[i].getName().equals(name)){
                return methods[i];
            }
        }
        return null;
    }
    public static Object doBefore(Object handler,Method m, Object[] allArgs){
        try {
            return getMethod(handler, "before").invoke(handler, new Object[]{m, allArgs});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object doAfter(Object handler,Method method, Object[] allArgs, Object result,Throwable t){
        try {
            return getMethod(handler, "after").invoke(handler, new Object[]{method,allArgs,result,t});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
