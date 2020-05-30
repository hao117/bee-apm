package net.beeapm.agent.boot;

import java.util.List;

public class BootPluginFactory {
    public static void init(){
        List<AbstractBootPlugin> list = BootPluginLoader.loadPlugins();

        for(int i = 0; list != null && i < list.size(); i++){
            list.get(i).boot();
        }
    }


}
