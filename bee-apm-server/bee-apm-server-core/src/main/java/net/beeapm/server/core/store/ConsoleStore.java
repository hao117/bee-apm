package net.beeapm.server.core.store;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;

/**
 * @author yuan
 * @date 2018/11/03
 */
@BeePlugin(type = BeePluginType.STORE, name = "console")
public class ConsoleStore implements IStore {
    @Override
    public void init() {

    }

    @Override
    public void save(Object... streams) {
        if(streams == null){
            return;
        }
        for(Object item : streams){
            System.out.println("[console]==============>"+JSON.toJSONString(item));
        }
    }
}
