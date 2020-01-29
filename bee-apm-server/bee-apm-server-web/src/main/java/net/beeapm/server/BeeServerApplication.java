package net.beeapm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 * @author yuan
 * @date 2018-08-24
 */
@SpringBootApplication
public class BeeServerApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.initializers(new BeeInitializer());
        return application.sources(BeeServerApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(BeeServerApplication.class);
        springApplication.addInitializers(new BeeInitializer());
        springApplication.run(args);
    }
}
