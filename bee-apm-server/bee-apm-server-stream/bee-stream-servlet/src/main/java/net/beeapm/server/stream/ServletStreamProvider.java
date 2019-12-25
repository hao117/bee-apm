package net.beeapm.server.stream;

import net.beeapm.server.core.stream.AbstractStreamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletStreamProvider extends AbstractStreamProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServletStreamProvider.class);
    @Override
    public void start() {
        logger.info("ServletStreamProvider start ............................................");
    }
}
