package com.example;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MultithreadNew implements Runnable {
    private final ProcessEngine processEngine;

    public MultithreadNew(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public void run() {
        try {
            // Your existing logic to start the process instance and handle tasks
            // Initialize the services
            RuntimeService runtimeService = processEngine.getRuntimeService();
            TaskService taskService = processEngine.getTaskService();
            FormService formService = processEngine.getFormService();
            HistoryService historyService = processEngine.getHistoryService();

            // Start a new process instance
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("onboarding");
            System.out.println(java.time.LocalDate.now() + " " + java.time.LocalTime.now()
                    + " Onboarding process started with process instance id [" +
                    processInstance.getProcessInstanceId() + "]");

            // Your existing task processing logic...
            Scanner scanner = new Scanner(System.in); // If needed for user input

            while (processInstance != null && !processInstance.isEnded()) {

                List<Task> tasks = taskService.createTaskQuery()
                        .taskCandidateGroup("managers").list();
                System.out.println(java.time.LocalDate.now() + " " + java.time.LocalTime.now()
                        + " Active outstanding tasks: [" + tasks.size() + "]");
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);

                    // taskService.claim(task.getId(), "1");
                    // taskService.setAssignee(task.getId(), "1");

                    System.out.println("\u001B[33m" + java.time.LocalDate.now() + " " + java.time.LocalTime.now() +
                            " Processing Task [Process=" + processInstance.getProcessInstanceId() + "][Task Name="
                            + task.getName() + ", Task Id=" + task.getId() + "]"
                            + "\u001B[0m");
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("fullName", "Chan Tai Man");
                    variables.put("yearsOfExperience", Long.valueOf("7"));

                    taskService.complete(task.getId(), variables);

                    HistoricActivityInstance endActivity = null;
                    List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                            .processInstanceId(processInstance.getId()).finished()
                            .orderByHistoricActivityInstanceEndTime().asc()
                            .list();
                    for (HistoricActivityInstance activity : activities) {
                        if (activity.getActivityType().equals("startEvent")) {
                            System.out.println("START  [" + processInstance.getProcessDefinitionKey() +
                                    "] " + activity.getStartTime());
                        }
                        if (activity.getActivityType().equals("endEvent")) {
                            System.out.println("END  [" + processInstance.getProcessDefinitionKey() +
                                    "] " + activity.getStartTime());
                            // Handle edge case where end step happens so fast that the end step
                            // and previous step(s) are sorted the same. So, cache the end step
                            // and display it last to represent the logical sequence.
                            endActivity = activity;
                        } else {
                            System.out.println("-- " + activity.getActivityName() +
                                    " [" + activity.getActivityId() + "] " +
                                    activity.getDurationInMillis() + " ms");
                        }
                    }
                    if (endActivity != null) {
                        System.out.println("-- " + endActivity.getActivityName() +
                                " [" + endActivity.getActivityId() + "] " +
                                endActivity.getDurationInMillis() + " ms");
                        System.out.println("COMPLETE [" +
                                processInstance.getProcessDefinitionKey() + "] " +
                                endActivity.getEndTime());
                    }
                }
                // Re-query the process instance, making sure the latest state is available
                processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId()).singleResult();
            }
            scanner.close();
            System.out.println("\u001B[35m" + "Thread " + Thread.currentThread().getId() + " DONE" + "\u001B[0m");

        } catch (Exception e) {
            // Throwing an exception
            System.out.println("\u001B[31m" + "Exception is caught: " + e + "\u001B[0m");
            e.printStackTrace();
        }
    }
}
