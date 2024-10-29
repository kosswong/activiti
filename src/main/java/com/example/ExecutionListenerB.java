package com.example;

import java.util.Random;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ExecutionListenerB implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Random r = new Random();
        long sleepTime = (long) (r.nextInt(100) * 1);
        Thread.sleep(sleepTime);
        System.out.println("[ExecutionListenerB] Sleep for " + sleepTime + " sec.");
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
