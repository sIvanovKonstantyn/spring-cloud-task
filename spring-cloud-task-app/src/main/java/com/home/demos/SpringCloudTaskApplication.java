package com.home.demos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;

import java.util.Arrays;

@SpringBootApplication
@EnableTask
public class SpringCloudTaskApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(
                SpringCloudTaskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Task executed with params: " + Arrays.toString(args));
    }

    @BeforeTask
    public void onTaskStartup(TaskExecution taskExecution) {
        System.out.printf(
                "TaskName:%s ExecutionId:%s start %n",
                taskExecution.getTaskName(),
                taskExecution.getExecutionId()
        );
    }

    @AfterTask
    public void onTaskEnd(TaskExecution taskExecution) {
        System.out.printf(
                "TaskName:%s ExecutionId:%s end %n",
                taskExecution.getTaskName(),
                taskExecution.getExecutionId()
        );
    }

    @FailedTask
    public void onTaskFailed(TaskExecution taskExecution, Throwable throwable) {
        System.out.printf(
                "TaskName:%s ExecutionId:%s fail %n",
                taskExecution.getTaskName(),
                taskExecution.getExecutionId()
        );
    }
}
