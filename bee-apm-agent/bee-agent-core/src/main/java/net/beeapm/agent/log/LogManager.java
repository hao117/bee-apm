package net.beeapm.agent.log;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuan on 2018/8/4.
 */
public class LogManager {
    private static ConcurrentHashMap<String,LogImpl> logMap = new ConcurrentHashMap<String,LogImpl>();
    public static LogImpl getLog(String className){
        LogImpl log = logMap.get(className);
        if(log == null){
            log = new LogImpl(className);
            logMap.put(className,log);
        }
        return log;
    }

    public static LogImpl getLog(Class<?> clazz){
        return getLog(clazz.getName());
    }
}
