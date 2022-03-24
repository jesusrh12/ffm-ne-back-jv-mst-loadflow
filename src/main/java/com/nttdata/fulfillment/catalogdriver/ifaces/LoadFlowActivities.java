package com.nttdata.fulfillment.catalogdriver.ifaces;

import java.io.IOException;

import com.nttdata.exceptions.BusinessWorkflowException;
import com.nttdata.fulfillment.catalogdriver.Config;
import com.uber.cadence.activity.ActivityMethod;


public interface LoadFlowActivities {
	
	@ActivityMethod(scheduleToCloseTimeoutSeconds = 20000)
	public String queryServiceOrder(String id) ;
	
	@ActivityMethod(scheduleToCloseTimeoutSeconds = 20000)
	public String updateServiceOrder(String baseFlow, String id) throws BusinessWorkflowException ;
	
	@ActivityMethod(scheduleToCloseTimeoutSeconds = 20000)
	public String getOperationDMN(String category, String operation,  String cfs, String action, String baseFlow) ;
	
	@ActivityMethod(scheduleToCloseTimeoutSeconds = 20000)
	public String instanceWorkflow(String workflowName) throws IOException ;
	
	public void setConfig(Config config);

}
