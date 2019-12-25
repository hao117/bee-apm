package net.beeapm.server.store;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.server.core.store.IStore;
import net.beeapm.server.store.jest.JestUtils;

/**
 * @author yuan
 * @date 2018/08/23
 */
@BeePlugin(type = BeePluginType.STORE, name = "elasticsearch")
public class ElasticSearchStore implements IStore {
    @Override
    public void init() {
        System.out.println("ElasticSearchStore init ......................");
        JestUtils.inst();
    }

    @Override
    public void save(Object... datas) {
        JestUtils.inst().insert(datas);
    }
}
