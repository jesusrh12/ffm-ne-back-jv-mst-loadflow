package com.nttdata.fulfillment.catalogdriver.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nttdata.exceptions.BusinessWorkflowException;
import com.nttdata.fulfillment.catalogdriver.BeanUtil;
import com.nttdata.fulfillment.catalogdriver.Config;
import com.nttdata.fulfillment.catalogdriver.ifaces.LoadFlowActivities;
import com.nttdata.interceptor.AuthRequestInterceptor;
import com.uber.cadence.workflow.Workflow;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Configurable
public class LoadFlowActivitiesImpl implements LoadFlowActivities {
	
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final String RELATEDPARTY = "relatedParty";
	private static final String SERVICESPECIFICATION = "serviceSpecification";
	private static final String PLACE = "place";

	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private static ApplicationContext appCxt;
	
	@Autowired
	private Config config;
	private AuthRequestInterceptor auth;
	
	

	OkHttpClient client;
	
	

	@Override
	public String queryServiceOrder(String id) {

		String json = "";
		String serviceOrder = "";
		try {
			
			client = new OkHttpClient.Builder()
					.connectTimeout(160, TimeUnit.SECONDS)
					.readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
					.addInterceptor(auth)
					.build();
				
			List<String> services = new ArrayList<>();

			String urlQuery = config.getUrlQueryServiceOrder().concat(id);
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(urlQuery).build();
			Response response = client.newCall(request).execute();
			serviceOrder = response.body().string();

		} catch (IOException e1) {
			log.error("Query SO Failed {} ", id);
		}

		return serviceOrder;

	}

	@Override
	public String updateServiceOrder(String baseFlow, String id) throws BusinessWorkflowException {
		String serviceOrder = "";
		JSONObject rootNode = new JSONObject();
		rootNode.put("baseFlow", baseFlow);

		Response response = null;
		try {
			final String urlService = config.getUrlUpdateWorkflowExecServiceOrder().concat(id);
			RequestBody body = RequestBody.create(JSON, rootNode.toJSONString());

			Request request = new Request.Builder().url(urlService).post(body).build();
			response = client.newCall(request).execute();

			if (response.code() != 200) {
				String errorResponse = response.body().string();
				response.close();
//				throw new BusinessWorkflowException(errorResponse);
			}

			serviceOrder = response.body().string();

		} catch (IOException e) {
			log.error("Update workflow Execution information failed {} ", e);
		}

		return serviceOrder;
	}

	@Override
	public String getOperationDMN(String category, String operation, String cfs, String action,
			String baseFlow) {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.createObjectNode();
		String respuesta = "";
		ObjectNode obj = (ObjectNode) node;

		obj.put("Category", category);
		obj.put("Operation", operation);
		obj.put("CFS", cfs);
		obj.put("Action", action);
		obj.put("BaseFlow", baseFlow);
		Response response = null;
		String json;
		try {
			json = mapper.writeValueAsString(obj);

			RequestBody body = RequestBody.create(JSON, json);

			Request request = new Request.Builder().url(config.getUrlSOMRuleManager().concat(config.getBussinesRuleName()))
					.post(body).addHeader("Content-Type", "application/json").build();

			response = client.newCall(request).execute();
			respuesta = response.body().string();

		} catch (IOException e) {
			log.error("Bussines Rules ");
		}

		return respuesta;

	}

	/**
	 * @author lbonillc
	 * create workflow instance on BPMN camunda engine
	 */
	@Override
	public String instanceWorkflow(String workflowName) throws IOException {

		
		String wfResponse = null;
		try {
			
			RequestBody body = RequestBody.create(JSON, "");
			Request request1 = new Request.Builder().url(config.getUrlWorkflowURL().concat(workflowName)).post(body)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request1).execute();
			wfResponse = response.body().string();
			
		}catch(Exception e) {
			log.error("Instance workflow error {} exception detail is {}", workflowName, e);
			Workflow.wrap(e);
		}

		

		return wfResponse;
	}
	
	
	
	@Override
	public void setConfig(Config config){
		this.config= config;
		this.auth = BeanUtil.getAuth();
	}

}
