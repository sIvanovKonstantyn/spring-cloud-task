package com.home.demos;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
public class SpringCloudTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                SpringCloudTaskApplication.class, args);
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

    @Bean
    public Job job2(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("job1")
                .flow(
                        stepBuilderFactory.get("job2step1")
                                .allowStartIfComplete(true)
                                .tasklet(new Tasklet() {
                                    @Override
                                    public RepeatStatus execute(
                                            StepContribution contribution,
                                            ChunkContext chunkContext) throws Exception {
                                        System.out.println("This is first job step  from Batch job");
                                        return RepeatStatus.FINISHED;
                                    }
                                })
                                .build()
                )
                .next(
                        stepBuilderFactory.get("job2step2")
                                .allowStartIfComplete(true)
                                .tasklet(new Tasklet() {
                                    @Override
                                    public RepeatStatus execute(
                                            StepContribution contribution,
                                            ChunkContext chunkContext) throws Exception {
                                        System.out.println("This is second job step  from Batch job");
                                        return RepeatStatus.FINISHED;
                                    }
                                })
                                .build()
                )
                .end()
                .build();
    }
}
