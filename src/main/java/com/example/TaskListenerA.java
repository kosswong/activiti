package com.example;

import java.util.Random;
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
        Random r = new Random();
        long sleepTime = (long) (r.nextInt(100) * 1);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[TaskListenerA] Sleep for " + sleepTime + " sec.");
        DelegateExecution execution = delegateTask.getExecution();
        System.out.println(java.time.LocalDate.now() + " " + java.time.LocalTime.now()
                + " \033[0;1m\u001B[34m[TaskListenerA]\u001B[0m [Process="
                + execution.getProcessInstanceId() +
                "]\033[0;1m\u001B[34m[event=" + delegateTask.getEventName() +
                "\u001B[0m][ActivityId=" + execution.getCurrentActivityId() +
                "][TaskAssignee=" + delegateTask.getAssignee() +
                "][TaskForm=" + delegateTask.getFormKey() + "]" + "\u001B[0m");

    }

}
