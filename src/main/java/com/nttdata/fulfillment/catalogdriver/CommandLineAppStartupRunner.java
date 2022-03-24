package com.nttdata.fulfillment.catalogdriver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nttdata.fulfillment.catalogdriver.service.impl.LoadFlowActivitiesImpl;
import com.nttdata.fulfillment.catalogdriver.service.impl.LoadFlowImpl;
import com.uber.cadence.worker.Worker;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lbonillc
 *
 */
@Slf4j
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

	
	@Value("${com.nttdata.fulfillment.cadence.domain}")
	private String cadenceDomain;
	@Value("${com.nttdata.fulfillment.cadence.host}")
	private String cadenceHost;
	@Value("${com.nttdata.fulfillment.cadence.tasklist}")
	private String cadenceTaskList;
	@Value("${com.nttdata.fulfillment.cadence.port}")
	private int cadencePort;

	
	@Bean
	public BeanUtil beanUtil() {
		return new BeanUtil();
	}
	
	/**
	 * 
	 */
	@Override
	public void run(String... args) throws Exception {
		log.info("Runing Worker Load flow..... ");

		Worker.Factory factory = new Worker.Factory(cadenceHost, cadencePort,cadenceDomain);
		Worker worker = factory.newWorker(cadenceTaskList);
		worker.registerWorkflowImplementationTypes(LoadFlowImpl.class);
		worker.registerActivitiesImplementations(new LoadFlowActivitiesImpl());
		factory.start();
		
		log.info("Worker started success");
		
	}
	
	
//	@Bean
//	OkHttpClient client = new OkHttpClient.Builder()
//	.connectTimeout(160, TimeUnit.SECONDS)
//	.readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
//	.authenticator(new AuthRequestInterceptor())
//	.build();
	
	

}
