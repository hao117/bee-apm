package net.beeapm.agent.config;

import net.beeapm.agent.log.BeeLog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置工厂
 */
public class BeeConfigFactory {
    private static ConcurrentHashMap<String, AbstractBeeConfig> configMap = new ConcurrentHashMap<String, AbstractBeeConfig>();

    private static BeeConfigFactory inst;

    public static BeeConfigFactory me() {
        if (inst == null) {
            synchronized (BeeConfigFactory.class) {
                if (inst == null) {
                    inst = new BeeConfigFactory();
                }
            }
        }
        return inst;
    }

    public void registryConfig(String name, AbstractBeeConfig config) {
        if (configMap.containsKey(name)) {
            BeeLog.log("------------->注册配置:" + name);
        } else {
            BeeLog.log("------------->重新注册配置:" + name);
        }
        configMap.put(name, config);
    }

    public void refresh() {
        for (Map.Entry<String, AbstractBeeConfig> entry : configMap.entrySet()) {
            BeeLog.log("------------->清除配置:" + entry.getKey());
            entry.getValue().clear();
        }
    }
}
