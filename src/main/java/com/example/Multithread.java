package com.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.logging.Logger;

// Main Class
public class Multithread {
        public static final Logger logger = Logger.getLogger(Multithread.class.getName());

        public static void main(String[] args) {

                ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                                .setJdbcUrl("jdbc:mysql://localhost:3306/mufg_activiti")
                                .setJdbcUsername("root")
                                .setJdbcPassword("password")
                                .setJdbcDriver("com.mysql.jdbc.Driver")
                                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
                ProcessEngine processEngine = cfg.buildProcessEngine();

                RepositoryService repositoryService = processEngine.getRepositoryService();
                Deployment deployment = repositoryService.createDeployment()
                                .addClasspathResource("onboarding.bpmn20.xml").deploy();

                ExecutorService executorService = Executors.newFixedThreadPool(10);

                // Single thread x 3
                // for (int i = 0; i < 1; i++) {
                // MultithreadNew object = new MultithreadNew(processEngine, deployment);
                // object.run();
                // }

                // Submit three onboarding tasks to be executed in parallel
                for (int i = 0; i < 1000; i++) {
                        executorService.submit(new MultithreadNew(processEngine, deployment));
                }
                executorService.shutdown();
        }
}