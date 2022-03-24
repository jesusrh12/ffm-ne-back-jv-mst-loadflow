package com.nttdata.fulfillment.catalogdriver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

import com.nttdata.config.AuthProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScans({ @ComponentScan(basePackages = { "com.nttdata.exceptions", "com.nttdata.interceptor", "com.nttdata.config", "com.nttdata.fulfillment.catalogdriver" }) })

@Configuration
public class Application {
	
	
	
	@Value("${com.nttdata.fulfillment.cadence.domain}")
	private String cadenceDomain;
	@Value("${com.nttdata.fulfillment.cadence.host}")
	private String cadenceHost;
	@Value("${com.nttdata.fulfillment.cadence.tasklist}")
	private String cadenceTaskList;
	@Value("${com.nttdata.fulfillment.cadence.port}")
	private int cadencePort;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	

}
