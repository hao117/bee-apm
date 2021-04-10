package net.beeapm.agent.plugin;

import net.beeapm.agent.loader.AbstractLoader;
import net.beeapm.agent.log.LogUtil;

import java.util.*;

/**
 * 插件加载
 *
 * @author yuan
 */
public class PluginLoader extends AbstractLoader {
    public static List<AbstractPlugin> loadPlugins() {
        Map<String, AbstractPlugin> pluginMap = load(new String[]{"plugins"}, "bee-plugin.def");
        List<AbstractPlugin> pluginList = new ArrayList<>(16);
        for (Map.Entry<String, AbstractPlugin> entry : pluginMap.entrySet()) {
            AbstractPlugin plugin = entry.getValue();
            plugin.setName(entry.getKey());
            pluginList.add(plugin);
            LogUtil.log("plugin : " + plugin.getName() + "=" + plugin.getClass().getName());
        }
        Collections.sort(pluginList, new Comparator<AbstractPlugin>() {
            @Override
            public int compare(AbstractPlugin o1, AbstractPlugin o2) {
                return o2.order() - o1.order();
            }
        });
        return pluginList;
    }
}
