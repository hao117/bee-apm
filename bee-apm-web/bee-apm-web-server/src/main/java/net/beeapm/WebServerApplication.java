package net.beeapm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
public class WebServerApplication {

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return (container -> {
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errors/401.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errors/500.html");
			container.addErrorPages(error401Page, error404Page, error500Page);
		});
	}
	@Bean
	public FilterRegistrationBean testFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new RewriteFilter());//注册rewrite过滤器
		registration.addUrlPatterns("/*");
		registration.addInitParameter(RewriteFilter.REWRITE_TO,"/index.html");
		registration.addInitParameter(RewriteFilter.REWRITE_PATTERNS, "/ui/*");
		registration.setName("rewriteFilter");
		registration.setOrder(1);
		return registration;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebServerApplication.class, args);
	}
}
