package net.beeapm.agent.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeeConfigFactory {
    private static ConcurrentHashMap<String, AbstractBeeConfig> configMap = new ConcurrentHashMap<String, AbstractBeeConfig>();

    private static BeeConfigFactory inst;

    public static BeeConfigFactory me(){
        if(inst == null){
            synchronized (BeeConfigFactory.class){
                if(inst == null){
                    inst = new BeeConfigFactory();
                }
            }
        }
        return inst;
    }

    public void registryConfig(String name, AbstractBeeConfig config){
        configMap.put(name,config);
    }

    public void refresh(){
        for(Map.Entry<String, AbstractBeeConfig> entry: configMap.entrySet()){
            entry.getValue().initConfig();
        }
    }
}
