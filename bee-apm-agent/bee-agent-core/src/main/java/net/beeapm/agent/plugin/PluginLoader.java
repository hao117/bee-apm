package net.beeapm.agent.plugin;

import net.beeapm.agent.loader.AbstractLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 插件加载
 *
 * @author yuan
 */
public class PluginLoader extends AbstractLoader {
    public static List<AbstractPlugin> loadPlugins() {
        Map<String, AbstractPlugin> pluginMap = load(new String[]{"plugins", "ext-lib"}, "bee-plugin.def");
        List<AbstractPlugin> pluginList = new ArrayList<AbstractPlugin>(16);
        for (Map.Entry<String, AbstractPlugin> entry : pluginMap.entrySet()) {
            AbstractPlugin plugin = entry.getValue();
            pluginList.add(plugin);
        }
        return pluginList;
    }
}
