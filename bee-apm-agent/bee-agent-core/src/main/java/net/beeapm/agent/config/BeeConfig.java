package net.beeapm.agent.config;

import net.beeapm.agent.log.LogLevel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 以后增加配置动态加载
 */
public class BeeConfig {
    private static LogLevel logLevel;
    private static Boolean  isLogConsole;
    private static Boolean enableParam;
    private static Set<String> excludeParamTypes = new HashSet<String>();
    private static Set<String> excludeParamTypePrefix = new HashSet<String>();
    public static void initConfig(){
        String level = ConfigUtils.me().getStr("logger.level","error");
        if(level.equalsIgnoreCase(LogLevel.TRACE.name())){
            logLevel = LogLevel.TRACE;
        }else if(level.equalsIgnoreCase(LogLevel.DEBUG.name())){
            logLevel = LogLevel.DEBUG;
        }else if(level.equalsIgnoreCase(LogLevel.INFO.name())){
            logLevel = LogLevel.INFO;
        }else if(level.equalsIgnoreCase(LogLevel.WARN.name())){
            logLevel = LogLevel.WARN;
        }else if(level.equalsIgnoreCase(LogLevel.ERROR.name())){
            logLevel = LogLevel.ERROR;
        }else if(level.equalsIgnoreCase(LogLevel.OFF.name())){
            logLevel = LogLevel.OFF;
        }
        isLogConsole = ConfigUtils.me().getBoolean("logger.console",false);
        enableParam = ConfigUtils.me().getBoolean("plugins.process.enableParam",true);
        List<String> excludeParamTypesList = ConfigUtils.me().getList("plugins.process.excludeParamTypes");
        List<String> excludeParamTypePrefixList = ConfigUtils.me().getList("plugins.process.excludeParamTypePrefix");
        excludeParamTypes.clear();
        excludeParamTypePrefix.clear();
        if(excludeParamTypesList != null && !excludeParamTypesList.isEmpty()){
            excludeParamTypes.addAll(excludeParamTypesList);
        }
        if(excludeParamTypePrefixList != null && !excludeParamTypePrefixList.isEmpty()){
            excludeParamTypePrefix.addAll(excludeParamTypePrefixList);
        }

    }

    public static LogLevel getLogLevel(){
        return logLevel;
    }

    public static boolean isLogConsole(){
        return isLogConsole;
    }

    public static boolean isEnableParam(){
        return enableParam;
    }

    public static Set<String> getExcludeParamTypes(){
        return  excludeParamTypes;
    }

    public static boolean isExcludeParamType(Class clazz){
        String name = clazz.getName();
        if(excludeParamTypes.contains(name)){
            return true;
        }
        Iterator<String> it = excludeParamTypePrefix.iterator();
        while (it.hasNext()){
            String prefix = it.next();
            if(name.startsWith(prefix)){
                return true;
            }
        }
        return false;
    }

}
