package com.rest2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import java.util.*;

@SpringBootApplication
@ComponentScan({"com.rest2"})
public class Rest2Application {
	public static void main(String[] args) {
		SpringApplication.run(Rest2Application.class, args);
  	}

	@Bean
    public TomcatServletWebServerFactory servletContainer() {
		try {
        	TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        	return factory;
		}
		catch (Exception ex) {
			return null;
		}
    }
}
