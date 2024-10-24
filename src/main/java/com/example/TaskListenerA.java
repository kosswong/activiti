package com.example;

import java.util.concurrent.TimeUnit;

//1. time, one more task
//2. async
//3. user a/b string/object? how to tell im B

//two listener
//two users trigger same task -> 2 thread

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerA implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        DelegateExecution execution = delegateTask.getExecution();
        System.out.println("\u001B[32m" + java.time.LocalDate.now() + " " + java.time.LocalTime.now()
                + " [TaskListenerA] [Process="
                + execution.getProcessInstanceId() +
                "][event=" + delegateTask.getEventName() +
                "][TaskListener=" + this +
                "][ActivityId=" + execution.getCurrentActivityId() +
                "][TaskAssignee=" + delegateTask.getAssignee() +
                "][TaskForm=" + delegateTask.getFormKey() + "]" + "\u001B[0m");

    }

}
