package net.beeapm.ui;


import net.beeapm.ui.common.ConfigHolder;
import net.beeapm.ui.es.EsQueryStringMap;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class BeeInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        ConfigHolder.buildConfig(configurableApplicationContext.getEnvironment());
        EsQueryStringMap.me();
    }

}
