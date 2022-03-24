package com.nttdata.fulfillment.catalogdriver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Config {
	
	@Value("${com.nttdata.fulfillment.cadence.activity.timeout}")
	private int activityTimeout;
	
	@Value("${com.nttdata.fulfillment.query.service.order.url}")
	private String urlQueryServiceOrder;
	
	@Value("${com.nttdata.fulfillment.update.workflow.service.order.url}")
	private String urlUpdateWorkflowExecServiceOrder;

	@Value("${com.nttdata.fulfillment.query.rules.som.url}")
	private String urlSOMRuleManager;

	@Value("${com.nttdata.fulfillment.bpmn.url}")
	private String urlWorkflowURL;

	@Value("${com.nttdata.fulfillment.query.rule.som.name}")
	private String bussinesRuleName;

	@Value("${com.nttdata.fulfillment.http.connection.timeout}")
	private long connectionTimeout;
	
	@Value("${com.nttdata.fulfillment.http.read.timeout}")
	private long readTimeout;
	
	@Value("${com.nttdata.fulfillment.http.write.timeout}")
	private long writeTimeout;
	
	

}
