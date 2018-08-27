package net.beeapm.server.core.handler;

import net.beeapm.server.core.common.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyStreamHandler extends AbstractStreamHandler{
    private static final Logger logger = LoggerFactory.getLogger(EmptyStreamHandler.class);
    @Override
    public void doInit() throws Exception {
        logger.debug("init .......");
    }

    @Override
    public void handle(Stream stream) throws Exception {
        if(stream!= null && stream.getSource() != null){
            logger.debug("stream : " + stream.getSource().toString());
        }else{
            logger.debug("stream : NULL");
        }
    }
}
