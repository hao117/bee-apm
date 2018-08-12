package net.beeapm.agent.plugin;

import net.beeapm.agent.common.AgentClassLoader;
import net.beeapm.agent.model.PluginDefine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PluginLoder {
    public static List<IPlugin> loadPlugins() {
        List<IPlugin> pluginList = new ArrayList<>(16);
        List<PluginDefine> pluginDefList = new ArrayList<>(16);
        AgentClassLoader classLoader = new AgentClassLoader(PluginLoder.class.getClassLoader(),new String[]{"plugins"});
        List<URL> resources = getResources(classLoader);
        for (URL url : resources) {
            try {
                readPluginDefine(url.openStream(),pluginDefList);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        for (PluginDefine pluginDefine : pluginDefList) {
            try {
                IPlugin plugin = (IPlugin)Class.forName(pluginDefine.clazz,true,classLoader).newInstance();
                pluginList.add(plugin);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return pluginList;
    }

    private static List<URL> getResources(ClassLoader classLoader) {
        List<URL> cfgUrlPaths = new ArrayList<URL>();
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources("bee-plugin.def");

            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                cfgUrlPaths.add(pluginUrl);
            }
            return cfgUrlPaths;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void readPluginDefine(InputStream input,List<PluginDefine> pluginDefList) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String pluginDefine = null;
            while ((pluginDefine = reader.readLine()) != null) {
                try {
                    if (pluginDefine == null || pluginDefine.trim().length() == 0 || pluginDefine.startsWith("#")) {
                        continue;
                    }
                    pluginDefine = pluginDefine.trim();
                    String[] defs = pluginDefine.split("=");
                    if (defs.length != 2) {
                        continue;
                    }
                    pluginDefList.add(new PluginDefine(defs[0],defs[1]));
                } catch (Exception e) {
                }
            }
        } finally {
            input.close();
        }
    }


}
