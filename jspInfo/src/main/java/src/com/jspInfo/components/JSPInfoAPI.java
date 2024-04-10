package com.jspInfo.components;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.jspInfo.components"})
class JSPInfoAPI extends SpringBootServletInitializer {
    private String[] springArgs;

	public JSPInfoAPI() {
	}

    public JSPInfoAPI(String[] args) {
        this.springArgs        = args;
    }

    public void run() {
        SpringApplication.run(JSPInfoAPI.class, this.springArgs);
    }

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JSPInfoAPI.class);
    }
} 
