package com.example;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MultithreadNew implements Runnable {
        private final ProcessEngine processEngine;
        private final Deployment deployment;

        public MultithreadNew(ProcessEngine processEngine, Deployment deployment) {
                this.processEngine = processEngine;
                this.deployment = deployment;
        }

        @Override
        public void run() {
                Random r = new Random();
                RepositoryService repositoryService = processEngine.getRepositoryService();
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                                .deploymentId(deployment.getId()).singleResult();
                System.out.println(
                                "Found process definition ["
                                                + processDefinition.getName() + "] with id ["
                                                + processDefinition.getId() + "]");

                // PROBLEM in taskService/processInstance

                System.out.println("\u001B[35m" + "Thread " + Thread.currentThread().getId() + " START" + "\u001B[0m");
                // Your existing logic to start the process instance and handle tasks
                // Initialize the services
                RuntimeService runtimeService = processEngine.getRuntimeService();
                TaskService taskService = processEngine.getTaskService();
                HistoryService historyService = processEngine.getHistoryService();

                // Start a new process instance
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("onboarding");
                System.out.println(java.time.LocalDate.now() + " " + java.time.LocalTime.now()
                                + " \033[0;1m\u001B[33m[MultithreadNew]\u001B[0m Onboarding process started with process instance id ["
                                +
                                processInstance.getProcessInstanceId() + "]");

                // Your existing task processing logic...
                Scanner scanner = new Scanner(System.in); // If needed for user input

                while (processInstance != null && !processInstance.isEnded()) {

                        // List<Task> tasks =
                        // taskService.createTaskQuery().taskCandidateGroup("managers").list();

                        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                                        .list();
                        System.out.println(java.time.LocalDate.now() + " " +
                                        java.time.LocalTime.now()
                                        + " \033[0;1m\u001B[33m[MultithreadNew]\u001B[0m Active outstanding tasks: ["
                                        + tasks.size()
                                        + "]");
                        for (int i = 0; i < tasks.size(); i++) {
                                Task task = tasks.get(i);

                                taskService.setAssignee(task.getId(), "1");

                                System.out.println("" + java.time.LocalDate.now() + " " +
                                                java.time.LocalTime.now() +
                                                " \033[0;1m\u001B[33m[MultithreadNew]\u001B[0m [Process="
                                                + processInstance.getProcessInstanceId()
                                                + "][Task Name="
                                                + task.getName() + ", Task Id=" + task.getId() + "]");
                                Map<String, Object> variables = new HashMap<>();
                                variables.put("fullName", "Chan Tai Man");
                                variables.put("yearsOfExperience", Long.valueOf("7"));

                                String lt = java.time.LocalTime.now().toString();
                                System.out.println(task.getId() + "-" + lt);

                                try {
                                        // Randomly complete the task
                                        if (Integer.valueOf(processInstance.getProcessInstanceId()) % 2 == 1
                                                        || 1 == 1) {
                                                taskService.complete(task.getId(), variables);
                                        } else {
                                                System.out.println("" + java.time.LocalDate.now() + " " +
                                                                java.time.LocalTime.now() +
                                                                "\u001B[31m" + "Not to complete task" + "\u001B[0m"
                                                                + " \033[0;1m\u001B[33m[MultithreadNew]\u001B[0m [Process="
                                                                + processInstance.getProcessInstanceId()
                                                                + "][Task Name="
                                                                + task.getName() + ", Task Id=" + task.getId() + "]");

                                        }
                                } catch (Exception e) {
                                        // Throwing an exception
                                        System.out.println("\u001B[31m" + "Exception is caught: " + e + "\u001B[0m");
                                        e.printStackTrace();
                                }

                                HistoricActivityInstance endActivity = null;
                                List<HistoricActivityInstance> activities = historyService
                                                .createHistoricActivityInstanceQuery()
                                                .processInstanceId(processInstance.getId()).finished()
                                                .orderByHistoricActivityInstanceEndTime().asc()
                                                .list();
                                for (HistoricActivityInstance activity : activities) {
                                        if (activity.getActivityType().equals("startEvent")) {
                                                System.out.println(
                                                                "START [" + processInstance.getProcessDefinitionKey() +
                                                                                "] " + activity.getStartTime());
                                        }
                                        if (activity.getActivityType().equals("endEvent")) {
                                                System.out.println(
                                                                "END  [" + processInstance.getProcessDefinitionKey() +
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
                                        System.out.println("-- " + endActivity.getActivityName()
                                                        + " [" + endActivity.getActivityId() + "] "
                                                        + endActivity.getDurationInMillis() + " ms");
                                        System.out.println("COMPLETE " + processDefinition.getName() + " ["
                                                        + processInstance.getProcessDefinitionKey() + "] "
                                                        + endActivity.getEndTime());
                                }
                        }
                        // Re-query the process instance, making sure the latest state is available
                        processInstance = runtimeService.createProcessInstanceQuery()
                                        .processInstanceId(processInstance.getId()).singleResult();
                }
                scanner.close();
                System.out.println("\u001B[35m" + "Thread " + Thread.currentThread().getId() + " DONE" + "\u001B[0m");

        }
}
