package net.beeapm.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebServerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.initializers(new BeeInitializer());
		return application.sources(WebServerApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication springApplication = new SpringApplication(WebServerApplication.class);
		springApplication.addInitializers(new BeeInitializer());
		springApplication.run(args);
	}

}
