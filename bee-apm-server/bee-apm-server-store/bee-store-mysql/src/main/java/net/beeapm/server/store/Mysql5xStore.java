package net.beeapm.server.store;

import net.beeapm.server.core.store.IStore;
import net.beeapm.server.store.mysql.MysqlUtils;

public class Mysql5xStore implements IStore {
    @Override
    public void init() {
        System.out.println("Mysql5xStore init ......................");
        MysqlUtils.inst();
    }

    @Override
    public void save(Object ... datas) {
        MysqlUtils.insert(datas);
    }
}
