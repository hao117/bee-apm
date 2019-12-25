package net.beeapm.server.store;

import net.beeapm.server.core.store.IStore;
import net.beeapm.server.store.jest.JestUtils;

public class ElasticSearchStore implements IStore {
    @Override
    public void init() {
        System.out.println("ElasticSearchStore init ......................");
        JestUtils.inst();
    }

    @Override
    public void save(Object ... datas) {
        JestUtils.inst().insert(datas);
    }
}
