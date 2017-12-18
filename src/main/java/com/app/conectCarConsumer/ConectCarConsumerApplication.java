package com.app.conectCarConsumer;

import com.app.helper.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;


@Configuration
@ComponentScan
@SpringBootApplication
public class ConectCarConsumerApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
	private static final Log log = LogFactory.getFactory().getInstance(SendMessageFactory.class);


	public static void main(String[] args) {
		//ConfigurableApplicationContext context = SpringApplication.run(ConectCarConsumerApplication.class, args);
		SpringApplication.run(ConectCarConsumerApplication.class, args);

		//context.getBean(ConsumeRun.class).run();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ConectCarConsumerApplication.class);
	}
}