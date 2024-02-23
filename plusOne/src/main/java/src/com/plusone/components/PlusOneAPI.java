package com.plusone.components;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import com.plusone.services.ConfigurationService;

@SpringBootApplication
@ComponentScan({"com.plusone.components"})
class PlusOneAPI extends Thread {
	private ConfigurationService configService = null;
	private String[] springArgs;

	public PlusOneAPI(String[] args) {
		this.configService = ConfigurationService.getInstance("");
		this.springArgs    = args;
	}

	public void run() {
		SpringApplication.run(PlusOneAPI.class, this.springArgs);
	}

	@Bean
	TomcatServletWebServerFactory serviceContainer() {
		try {
			TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
			return factory;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}

