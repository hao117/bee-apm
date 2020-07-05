package net.beeapm.agent.plugin.thread;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuan
 * @date 2020/07/05
 */
public class ThreadConfig extends AbstractBeeConfig {
    private static ThreadConfig config;
    private Boolean enable;
    private List<String> excludeClassNameStartsWithList;
    private List<String> excludeClassNameContainsList;

    public static ThreadConfig me() {
        if (config == null) {
            synchronized (ThreadConfig.class) {
                if (config == null) {
                    config = new ThreadConfig();
                    BeeConfigFactory.me().registryConfig("thread", config);
                }
            }
        }
        return config;
    }

    private ThreadConfig() {
        initConfig();
    }

    @Override
    public void clear() {
        config = null;
    }

    @Override
    public void initConfig() {
        enable = ConfigUtils.me().getBoolean("plugins.thread.enable", true);
        excludeClassNameStartsWithList = ConfigUtils.me().getList("plugins.thread.excludeClass.nameStartsWith");
        excludeClassNameContainsList = ConfigUtils.me().getList("plugins.thread.excludeClass.nameContains");
    }

    public List<String> getExcludeClassNameStartsWithList() {
        if(excludeClassNameStartsWithList == null){
            return new ArrayList<>(1);
        }
        return excludeClassNameStartsWithList;
    }

    public List<String> getExcludeClassNameContainsList() {
        if(excludeClassNameContainsList == null){
            return new ArrayList<>(1);
        }
        return excludeClassNameContainsList;
    }

    public Boolean isEnable() {
        return enable;
    }
}
