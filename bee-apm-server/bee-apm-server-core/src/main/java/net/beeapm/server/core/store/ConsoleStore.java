package net.beeapm.server.core.store;

import com.alibaba.fastjson.JSON;

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
