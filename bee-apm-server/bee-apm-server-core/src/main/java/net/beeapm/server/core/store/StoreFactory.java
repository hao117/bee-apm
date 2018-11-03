package net.beeapm.server.core.store;

import net.beeapm.server.core.common.ConfigHolder;
import net.beeapm.server.core.common.ServiceProviderLoader;
import net.beeapm.server.core.handler.IStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.SocketHandler;

public class StoreFactory {
    private static Logger logger = LoggerFactory.getLogger(StoreFactory.class);
    static StoreFactory instance;
    static IStore store;
    public StoreFactory(){
        init();
    }
    public static StoreFactory getInstance(){
        if(instance == null){
            synchronized (StoreFactory.class){
                if(instance == null){
                    instance = new StoreFactory();
                }
            }
        }
        return instance;
    }

    public void init(){
        try {
            String storeName = ConfigHolder.getProperty("bee.store.name");
            ServiceProviderLoader loader = new ServiceProviderLoader("bee-store.def");
            store = loader.load(storeName);
            if(store != null){
                store.init();
            }
        }catch (Exception e){
            logger.error("",e);
        }

    }

    public IStore getStore(){
        return store;
    }
}
