package net.beeapm.agent.config;

import net.beeapm.agent.log.BeeLogUtil;

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
            BeeLogUtil.log("------------->注册配置:" + name);
        } else {
            BeeLogUtil.log("------------->重新注册配置:" + name);
        }
        configMap.put(name, config);
    }

    public void refresh() {
        BeeLogUtil.log("------------->刷新配置");
        for (Map.Entry<String, AbstractBeeConfig> entry : configMap.entrySet()) {
            BeeLogUtil.log("------------->清除配置:" + entry.getKey());
            entry.getValue().clear();
        }
    }
}
