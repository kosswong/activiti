package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ExecutionListenerA implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("variableSetInExecutionListener", "firstValue");
        execution.setVariable("eventReceived", execution.getEventName());
        System.out.println(java.time.LocalDate.now() + " "
                + java.time.LocalTime.now() + " \033[0;1m\u001B[32m[ExecutionListenerA]\u001B[0m [Process="
                + execution.getProcessInstanceId() +
                "]\033[0;1m\u001B[32m[event=" + execution.getEventName() +
                "]\u001B[0m[ActivityId=" + execution.getCurrentActivityId() + "]" + "\u001B[0m");
    }

}
