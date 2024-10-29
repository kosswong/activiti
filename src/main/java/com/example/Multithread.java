package com.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.logging.Logger;

//1. Pending： Add delay, ramdonly complie
//2. two thread, where bug if colliect, order

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

                // Multi thread
                Random r = new Random();
                for (int i = 0; i < 2; i++) {
                        // Add ramdom delay
                        // try {
                        // long sleepTime = (long) (r.nextInt(100) * 1);
                        // Thread.sleep(sleepTime);
                        // System.out.println("[Multithread] Sleep for " + sleepTime + " sec.");
                        // } catch (InterruptedException e) {
                        // e.printStackTrace();
                        // }
                        executorService.submit(new MultithreadNew(processEngine, deployment));
                }
                executorService.shutdown();
        }
}