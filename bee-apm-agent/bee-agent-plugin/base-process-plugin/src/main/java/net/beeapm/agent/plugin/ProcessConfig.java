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
    private Boolean enableError;
    private Set<String> includeErrorPointPrefix = new HashSet<String>();
    private Set<String> excludeErrorPointPrefix = new HashSet<String>();

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
        excludeParamTypes.clear();
        excludeParamTypePrefix.clear();
        includeErrorPointPrefix.clear();
        excludeErrorPointPrefix.clear();

        enableParam = ConfigUtils.me().getBoolean("plugins.process.enableParam",true);
        enable = ConfigUtils.me().getBoolean("plugins.process.enable",true);
        List<String> excludeParamTypesList = ConfigUtils.me().getList("plugins.process.excludeParamTypes");
        List<String> excludeParamTypePrefixList = ConfigUtils.me().getList("plugins.process.excludeParamTypePrefix");
        if(excludeParamTypesList != null && !excludeParamTypesList.isEmpty()){
            excludeParamTypes.addAll(excludeParamTypesList);
        }
        if(excludeParamTypePrefixList != null && !excludeParamTypePrefixList.isEmpty()){
            excludeParamTypePrefix.addAll(excludeParamTypePrefixList);
        }

        enableError = ConfigUtils.me().getBoolean("plugins.process.error.enable",true);
        List<String> includeErrorPointPrefix = ConfigUtils.me().getList("plugins.process.error.includeErrorPointPrefix");
        List<String> excludeErrorPointPrefix = ConfigUtils.me().getList("plugins.process.error.excludeErrorPointPrefix");
        if(includeErrorPointPrefix != null && !includeErrorPointPrefix.isEmpty()){
            includeErrorPointPrefix.addAll(includeErrorPointPrefix);
        }
        if(excludeErrorPointPrefix != null && !excludeErrorPointPrefix.isEmpty()){
            excludeErrorPointPrefix.addAll(excludeErrorPointPrefix);
        }

        spend = ConfigUtils.me().getInt("plugins.process.spend",-1);
    }
    public boolean isEnableParam(){
        return enableParam;
    }

    public boolean isEnable(){
        return enable;
    }

    public boolean isEnableError(){
        return enableError;
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

    public boolean checkErrorPoint(String point){
        if(!enableError){
            return false;
        }
        //黑名单，优先级别最高
        Iterator<String> exclude = excludeErrorPointPrefix.iterator();
        while (exclude.hasNext()){
            String prefix = exclude.next();
            if(point.startsWith(prefix)){
                return false;
            }
        }
        //白名单
        Iterator<String> include = includeErrorPointPrefix.iterator();
        while (include.hasNext()){
            String prefix = include.next();
            if(point.startsWith(prefix)){
                return true;
            }
        }
        return false;
    }




}
