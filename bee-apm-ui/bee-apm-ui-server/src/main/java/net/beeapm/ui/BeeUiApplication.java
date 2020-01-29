package net.beeapm.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author yuan
 * @date 2018-09-23
 */
@SpringBootApplication
public class BeeUiApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.initializers(new BeeInitializer());
		return application.sources(BeeUiApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication springApplication = new SpringApplication(BeeUiApplication.class);
		springApplication.addInitializers(new BeeInitializer());
		springApplication.run(args);
	}

}
