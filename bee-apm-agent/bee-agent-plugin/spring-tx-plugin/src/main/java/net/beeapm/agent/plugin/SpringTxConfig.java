package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;

public class SpringTxConfig extends AbstractBeeConfig {
    private static SpringTxConfig config;
    private Boolean enable;
    private long spend;

    public static SpringTxConfig me(){
        if(config == null){
            synchronized (SpringTxConfig.class){
                if(config == null){
                    config = new SpringTxConfig();
                    BeeConfigFactory.me().registryConfig("springTx",config);
                }
            }
        }
        return config;
    }

    private SpringTxConfig(){
        initConfig();
    }

    @Override
    public void initConfig() {
        enable = ConfigUtils.me().getBoolean("plugins.springTx.enable",true);
        spend = ConfigUtils.me().getInt("plugins.springTx.spend",-1);
    }

    public long getSpend() {
        return spend;
    }

    public Boolean isEnable() {
        return enable;
    }
}
