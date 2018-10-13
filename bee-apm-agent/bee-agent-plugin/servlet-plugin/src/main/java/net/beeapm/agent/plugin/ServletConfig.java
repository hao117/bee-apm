package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;

public class ServletConfig extends AbstractBeeConfig {
    private static ServletConfig config;
    private Boolean enable;
    private Boolean enableParam;
    private Boolean enableBody;
    private long spend;

    public static ServletConfig me(){
        if(config == null){
            synchronized (ServletConfig.class){
                if(config == null){
                    config = new ServletConfig();
                    BeeConfigFactory.me().registryConfig("servlet",config);
                }
            }
        }
        return config;
    }

    private ServletConfig(){
        initConfig();
    }

    @Override
    public void initConfig() {
        enable = ConfigUtils.me().getBoolean("plugins.servlet.enable",true);
        enableParam = enable & ConfigUtils.me().getBoolean("plugins.servlet.enableParam",false);
        enableBody = enable & ConfigUtils.me().getBoolean("plugins.servlet.enableBody",false);

        //http入口的耗时要小于等于方法的耗时，否则会造成调用链断开
        long processSpend = ConfigUtils.me().getInt("plugins.process.spend",-1);
        spend = ConfigUtils.me().getInt("plugins.servlet.spend",-1);
        if(spend > processSpend){
            spend = processSpend;
        }
    }

    public long getSpend() {
        return spend;
    }

    public Boolean isEnable() {
        return enable;
    }
    public Boolean isEnableParam() {
        return enableParam;
    }
    public Boolean isEnableBody() {
        return enableBody;
    }
}
