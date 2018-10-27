package net.beeapm.agent.config;

import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.common.SysPropKey;
import net.beeapm.agent.log.LogLevel;

/**
 * 以后增加配置动态加载
 */
public class BeeConfig extends AbstractBeeConfig {
    private LogLevel logLevel;
    private Boolean  isLogConsole;
    private int ratio;
    private static BeeConfig config;
    private String inst;
    private String app;
    private String env;
    private String ip;
    private String port;

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

        ratio = ConfigUtils.me().getInt("collect.ratio",10000);
        inst = System.getProperty(SysPropKey.BEE_INST,"unknown");
        app = System.getProperty(SysPropKey.BEE_APP,"unknown");
        env = System.getProperty(SysPropKey.BEE_ENV,"unknown");
        port = System.getProperty(SysPropKey.BEE_PORT,"-9999");
        ip = System.getProperty(SysPropKey.BEE_IP);
        if(ip==null){
            ip = BeeUtils.getLocalIp();
        }
    }

    public LogLevel getLogLevel(){
        return logLevel;
    }

    public boolean isLogConsole(){
        return isLogConsole;
    }

    public int getRatio() {
        return ratio;
    }

    public String getInst(){
        return inst;
    }

    public String getApp() {
        return app;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getEnv(){
        return env;
    }
}
