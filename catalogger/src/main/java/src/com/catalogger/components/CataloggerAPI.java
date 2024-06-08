package com.catalogger.components;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.catalogger.components"})
class CataloggerAPI extends SpringBootServletInitializer {
    private String[] springArgs;

	public CataloggerAPI() {
	}

    public CataloggerAPI(String[] args) {
        this.springArgs        = args;
    }

    public void run() {
        SpringApplication.run(CataloggerAPI.class, this.springArgs);
    }

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CataloggerAPI.class);
    }
} 
