package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.joda.time.format.DateTimeFormatter;

public class ExecutionListenerB implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("variableSetInExecutionListener", "firstValue");
        execution.setVariable("eventReceived", execution.getEventName());
        System.out.println("\u001B[34m" + java.time.LocalDate.now() + " "
                + java.time.LocalTime.now() + " [ExecutionListenerB] [Process="
                + execution.getProcessInstanceId() +
                "][event=" + execution.getEventName() +
                "][ExecutionListener=" + this +
                "][ActivityId=" + execution.getCurrentActivityId() + "]" + "\u001B[0m");
    }

}
