package com.example;

import java.util.Random;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ExecutionListenerA implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Random r = new Random();
        long sleepTime = (long) (r.nextInt(100) * 1);
        Thread.sleep(sleepTime);
        System.out.println("[ExecutionListenerA] Sleep for " + sleepTime + " sec.");
        execution.setVariable("variableSetInExecutionListener", "firstValue");
        execution.setVariable("eventReceived", execution.getEventName());
        System.out.println(java.time.LocalDate.now() + " "
                + java.time.LocalTime.now() + " \033[0;1m\u001B[32m[ExecutionListenerA]\u001B[0m [Process="
                + execution.getProcessInstanceId() +
                "]\033[0;1m\u001B[32m[event=" + execution.getEventName() +
                "]\u001B[0m[ActivityId=" + execution.getCurrentActivityId() + "]" + "\u001B[0m");
    }

}
