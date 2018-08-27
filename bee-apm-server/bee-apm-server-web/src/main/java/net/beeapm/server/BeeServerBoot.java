package net.beeapm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BeeServerBoot extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.initializers(new BeeInitializer());
        return application.sources(BeeServerBoot.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(BeeServerBoot.class);
        springApplication.addInitializers(new BeeInitializer());
        springApplication.run(args);
    }
}
