package net.beeapm.server.store;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.server.core.store.IStore;
import net.beeapm.server.store.mysql.MysqlUtils;
/**
 * @author kaddddd
 * @date 2018/11/17
 */
@BeePlugin(type = BeePluginType.STORE,name = "mysql")
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
