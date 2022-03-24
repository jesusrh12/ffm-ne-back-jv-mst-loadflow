package com.nttdata.fulfillment.catalogdriver.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;
@Component
public class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter{

		
	@Value("#{${swagger.server.listOfServer}}")
	private List<String> listOfServer;

	
	@Override
	public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
		OpenAPI swagger = context.getSpecification();
	    List<Server> listServer = new ArrayList<>();
	    
	    for (int i = 0; i < listOfServer.size(); i++) {
	    	 Server server = new Server();
	    	 String[] parts = listOfServer.get(i).split("---");
	 	     server.setUrl(parts[0]);
	 	     server.setDescription(parts[1]);
	 	     listServer.add(server);
		} 
	   
	    
	    swagger.setServers(listServer);
	    return swagger;
	}

	@Override
	public boolean supports(DocumentationType docType) {
		return docType.equals(DocumentationType.OAS_30);
	}

}
