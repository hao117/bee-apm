package net.beeapm.agent.boot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BootPluginFactory {
    public static void init(){
        List<AbstractBootPlugin> list = BootPluginLoader.loadPlugins();
        Collections.sort(list, new Comparator<AbstractBootPlugin>() {
            @Override
            public int compare(AbstractBootPlugin p1, AbstractBootPlugin p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
        for(int i = 0; list != null && i < list.size(); i++){
            list.get(i).boot();
        }
    }


}
