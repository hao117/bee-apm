package net.beeapm.agent.log;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工厂
 * @date 2018/8/4
 * @author yuan
 */
public class LogFactory {
    private static ConcurrentHashMap<String, Log> logMap = new ConcurrentHashMap<String, Log>();
    public static Log getLog(String className){
        Log log = logMap.get(className);
        if(log == null){
            log = new Log(className);
            logMap.put(className,log);
        }
        return log;
    }

    public static Log getLog(Class<?> clazz){
        return getLog(clazz.getName());
    }
}
