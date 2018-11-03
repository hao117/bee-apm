package net.beeapm.server;

import net.beeapm.server.core.common.ConfigHolder;
import net.beeapm.server.core.common.ServiceProviderLoader;
import net.beeapm.server.core.handler.HandlerFactory;
import net.beeapm.server.core.stream.IStreamProvider;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class BeeInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        ConfigHolder.buildConfig(configurableApplicationContext.getEnvironment());
        try {
            initHandler();
            startStreamProvider();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initHandler(){
        HandlerFactory.getInstance();
    }

    private void startStreamProvider() throws Exception{
        ServiceProviderLoader streamLoader = new ServiceProviderLoader("bee-stream.def");
        String provider = ConfigHolder.getProperty("bee.provider.name");
        IStreamProvider servletProvider = streamLoader.load(provider);
        servletProvider.start();
    }


}
