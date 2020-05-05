package net.beeapm.agent.plugin.jdbc;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;

public class JdbcConfig extends AbstractBeeConfig {
    private static JdbcConfig config;
    private Boolean enableParam;
    private Boolean enable;
    private long spend;

    public static JdbcConfig me() {
        if (config == null) {
            synchronized (JdbcConfig.class) {
                if (config == null) {
                    config = new JdbcConfig();
                    BeeConfigFactory.me().registryConfig("jdbc", config);
                }
            }
        }
        return config;
    }

    @Override
    public void clear() {
        config = null;
    }

    private JdbcConfig() {
        initConfig();
    }

    @Override
    public void initConfig() {
        enableParam = ConfigUtils.me().getBoolean("plugins.jdbc.enableParam", true);
        enable = ConfigUtils.me().getBoolean("plugins.jdbc.enable", true);
        spend = ConfigUtils.me().getInt("plugins.jdbc.spend", -1);
    }

    public boolean isEnableParam() {
        return enableParam;
    }

    public long getSpend() {
        return spend;
    }

    public Boolean isEnable() {
        return enable;
    }
}
