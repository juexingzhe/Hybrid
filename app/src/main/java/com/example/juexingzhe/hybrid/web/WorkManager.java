package com.example.juexingzhe.hybrid.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkManager {

    private static WorkManager manager;
    private ExecutorService executorService;

    private WorkManager() {
        if (this.executorService == null) {
            executorService = Executors.newFixedThreadPool(3);

        }
    }

    public static WorkManager getInstance() {
        if (manager == null) {
            synchronized (WorkManager.class) {
                if (manager == null) {
                    manager = new WorkManager();
                }
            }
        }

        return manager;
    }

    public void postTask(Runnable task) {
        executorService.execute(task);
    }

}
