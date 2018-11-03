package net.beeapm.server.core.stream;

import net.beeapm.server.core.common.Stream;
import net.beeapm.server.core.handler.HandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStreamProvider implements IStreamProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractStreamProvider.class);
    public String write(Stream stream){
        try {
            HandlerFactory.getInstance().executeFirstHandler(stream);
            return "ok";
        }catch (Exception e){
            logger.error("",e);
        }
        return "fail";
    }
}
