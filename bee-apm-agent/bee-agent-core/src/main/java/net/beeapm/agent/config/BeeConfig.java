package net.beeapm.agent.config;

import net.beeapm.agent.log.LogLevel;

/**
 * 以后增加配置动态加载
 */
public class BeeConfig {
    private static LogLevel logLevel;
    private static Boolean  isLogConsole;
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
    }

    public static LogLevel getLogLevel(){
        return logLevel;
    }

    public static boolean isLogConsole(){
        return isLogConsole;
    }
}
