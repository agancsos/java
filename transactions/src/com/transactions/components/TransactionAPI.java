package com.transactions.components;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import com.transactions.services.ConfigurationService;

@SpringBootApplication
@ComponentScan({"com.transactions.components"})
class TransactionAPI extends Thread {
	private ConfigurationService configService = null;
	private String[] springArgs;

	public TransactionAPI(String[] args) {
		this.configService = ConfigurationService.getInstance("");
		this.springArgs    = args;
	}

	public void run() {
		SpringApplication.run(TransactionAPI.class, this.springArgs);
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

