package net.beeapm.server.core.handler;

import net.beeapm.server.core.common.ConfigHolder;
import net.beeapm.server.core.common.ServiceProviderLoader;
import net.beeapm.server.core.common.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HandlerFactory {
    private static final Logger logger = LoggerFactory.getLogger(HandlerFactory.class);
    static HandlerFactory instance;
    static List<IStreamHandler> handlerList;
    public static synchronized HandlerFactory getInstance(){
        if(instance == null){
            synchronized (HandlerFactory.class){
                if(instance == null){
                    instance = new HandlerFactory();
                }
            }
        }
        return instance;
    }
    public HandlerFactory(){
        init();
    }
    public void init(){
        try {
            handlerList = new ArrayList<>();
            String[] handlerNames = ConfigHolder.getProperty("bee.handlers.flow", "").split(">");
            ServiceProviderLoader loader = new ServiceProviderLoader("bee-handler.def");
            IStreamHandler prevHandler = null;
            for (String name : handlerNames) {
                logger.error("load handler : " + name);
                IStreamHandler handler = loader.load(name);
                handler.init();
                if(prevHandler != null){
                    prevHandler.setNextStreamHandler(handler);
                }
                handlerList.add(handler);
                prevHandler = handler;
            }
        }catch (Exception e){
            logger.error("handler初始化失败，应用退出",e);
            System.exit(0);
        }
    }

    public List<IStreamHandler> getHandlerList(){
        return handlerList;
    }

    public void executeFirstHandler(Stream stream) throws Exception {
        executeHandler(stream,getHandlerList().get(0));
    }

    public void executeHandler(Stream stream,IStreamHandler handler) throws Exception {
        if(stream != null && stream.getSource() != null){
            handler.handle(stream);
            IStreamHandler nextHandler = handler.getNextStreamHandler();
            if(nextHandler != null){
                executeHandler(stream,nextHandler);
            }
        }
    }
}
