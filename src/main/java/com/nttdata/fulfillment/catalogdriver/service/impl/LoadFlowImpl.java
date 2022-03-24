package com.nttdata.fulfillment.catalogdriver.service.impl;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.fulfillment.catalogdriver.BeanUtil;
import com.nttdata.fulfillment.catalogdriver.Config;
import com.nttdata.fulfillment.catalogdriver.ifaces.LoadFlow;
import com.nttdata.fulfillment.catalogdriver.ifaces.LoadFlowActivities;
import com.nttdata.model.tmf.ServiceOrder;
import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.workflow.Workflow;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configurable
public class LoadFlowImpl implements LoadFlow{

	private Config config;

	@Override
	public void loadFlow(String flow, Map<String, String> map) {

		log.debug("load {} flow ", flow);

		loadConfig();

		String serv = "";
		String category = "";
		String cfs = "";
		String action = "";
		String baseFlow = "";
		String id = "";
		try {

			ObjectMapper objectMapper = new ObjectMapper();

			ActivityOptions options = new ActivityOptions.Builder()
					.setScheduleToCloseTimeout(Duration.ofHours(config.getActivityTimeout()))
					.build();

			LoadFlowActivities activities = Workflow.newActivityStub(LoadFlowActivities.class,
					options);
			activities.setConfig(this.config);

			id = map.get("id");
			if (flow.equals("UpdateResource") || flow.equals("Postponement")
					|| flow.equals("Cancel")) {
				id = map.get("serviceOrder.id");
			}

			String services = activities.queryServiceOrder(id);
			ServiceOrder serviceOrder = objectMapper.readValue(services, ServiceOrder.class);

			if (serviceOrder.getServiceOrderItem() == null || serviceOrder.getServiceOrderItem().isEmpty()) {

				String soiSize = map.get("serviceOrderItem.size");

				for (int i = 0; i < Integer.parseInt(soiSize); i++) {
					String nameCFS = "serviceOrderItem[" + i
							+ "].service.serviceSpecification.name";
					String nameAction = "serviceOrderItem[" + i + "].action";

					if (action.isEmpty()) {
						action = map.get(nameAction);
					} else {
						action += ";" + map.get(nameAction);
					}

					if (cfs.isEmpty()) {
						cfs = map.get(nameCFS);
					} else {
						cfs += ";" + map.get(nameCFS);
						
					}

				}

				category = map.get("category");

			} else {

				category = serviceOrder.getCategory();

				if (flow.equals("ServiceOrder") || flow.equals("ISPChange")) {
					if (serviceOrder.getServiceOrderItem() != null
							&& !serviceOrder.getServiceOrderItem().isEmpty()) {
						for (com.nttdata.model.tmf.ServiceOrderItem soi : serviceOrder
								.getServiceOrderItem()) {
							if (action.equals("")) {
								action = soi.getAction().toString();
							} else {
								action += ";" + soi.getAction().toString();
							}

							if (cfs.equals("")) {
								cfs = soi.getService().getServiceSpecification().getName();
							} else {
								cfs += ";" + soi.getService().getServiceSpecification().getName();
							}

						}
					}
				}

				if (serviceOrder.getFlowExecutionRef().getBaseFlow() != null) {
					baseFlow = serviceOrder.getFlowExecutionRef().getBaseFlow();

				}

			}


			String responseDMN = activities.getOperationDMN(category, flow, cfs, action, baseFlow);
			JsonNode rootNode = objectMapper.readTree(responseDMN);
			String respuestaDMN = rootNode.get("dmnContext").get("Flow").asText();

			if (flow.equals("ServiceOrder")) {
				serv = activities.updateServiceOrder(respuestaDMN, id);
			}


			String res = activities.instanceWorkflow(flow);

		} catch (JsonProcessingException e) {
			log.error("No se puede procesar el objeto", e);
			Workflow.wrap(e);
		} catch (Exception e) {
			log.error("Error de negocio", e);
			Workflow.wrap(e);
		}

	}

    
	private void loadConfig() {
		config = BeanUtil.getConfig();
	}

}
