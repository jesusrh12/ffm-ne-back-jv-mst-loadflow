package com.nttdata.fulfillment.catalogdriver.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
	
	
	@Value("${swagger.tags.controller}")
    private String controller;
	
	@Value("${swagger.info.title}")
    private String title;
	
	@Value("${swagger.info.description}")
    private String description;
    
    @Value("${swagger.info.version}")
    private String version;
    
    @Value("${swagger.info.controller.description}")
    private String contDescrip;
    
    @Value("${swagger.info.package}")
    private String packageName;
	
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
        		.apiInfo(new ApiInfoBuilder()
        	            .title(title)
        	            .description(description)
        	            .version(version)
        	            .build())
        		
        		.tags(new Tag(controller, contDescrip))
                .select()
                .apis(RequestHandlerSelectors.basePackage(packageName))
                .paths(PathSelectors.any())
                .build();
    }
   
}
