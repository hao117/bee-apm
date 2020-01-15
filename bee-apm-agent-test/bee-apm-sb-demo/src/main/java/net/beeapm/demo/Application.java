package net.beeapm.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @date 2020-01-12
 * @author yuan
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.initializers(new DemoInitializer());
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.addInitializers(new DemoInitializer());
		springApplication.run(args);
	}

}
