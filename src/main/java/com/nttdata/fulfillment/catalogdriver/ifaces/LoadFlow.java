package com.nttdata.fulfillment.catalogdriver.ifaces;

import java.util.Map;

import com.uber.cadence.workflow.WorkflowMethod;

public interface LoadFlow {
    @WorkflowMethod(executionStartToCloseTimeoutSeconds = 1000,taskList = "${com.nttdata.fulfillment.cadence.tasklist}")
    void loadFlow(String flow, Map<String, String> map);
}
