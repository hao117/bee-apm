package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProcessConfig extends AbstractBeeConfig {
    private static ProcessConfig config;
    private Boolean enableParam;
    private Boolean enable;
    private Set<String> excludeParamTypes = new HashSet<String>();
    private Set<String> excludeParamTypePrefix = new HashSet<String>();
    private long spend;

    public static ProcessConfig me(){
        if(config == null){
            synchronized (ProcessConfig.class){
                if(config == null){
                    config = new ProcessConfig();
                    BeeConfigFactory.me().registryConfig("process",config);
                }
            }
        }
        return config;
    }

    private ProcessConfig(){
        initConfig();
    }

    @Override
    public void initConfig() {
        enableParam = ConfigUtils.me().getBoolean("plugins.process.enableParam",true);
        enable = ConfigUtils.me().getBoolean("plugins.process.enable",true);
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

        spend = ConfigUtils.me().getInt("plugins.process.spend",-1);
    }
    public boolean isEnableParam(){
        return enableParam;
    }

    public boolean isEnable(){
        return enable;
    }

    public boolean isExcludeParamType(Class clazz){
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

    public long getSpend() {
        return spend;
    }




}
