package net.beeapm.agent.config;

import net.beeapm.agent.log.LogLevel;
/**
 * 以后增加配置动态加载
 */
public class BeeConfig extends AbstractBeeConfig {
    private LogLevel logLevel;
    private Boolean  isLogConsole;
    private int rate;
    private static BeeConfig config;

    public static BeeConfig me(){
        if(config == null){
            synchronized (BeeConfig.class){
                if(config == null){
                    config = new BeeConfig();
                    BeeConfigFactory.me().registryConfig("bee",config);
                }
            }
        }
        return config;
    }

    private BeeConfig(){
        initConfig();
    }

    @Override
    public void initConfig(){
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

        rate = ConfigUtils.me().getInt("collect.ratio",10000);

    }

    public LogLevel getLogLevel(){
        return logLevel;
    }

    public boolean isLogConsole(){
        return isLogConsole;
    }

    public int getRate() {
        return rate;
    }
}
