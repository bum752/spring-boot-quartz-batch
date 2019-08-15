package com.example.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;

@Slf4j
public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logJobExecution(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logJobExecution(jobExecution);
    }

    private void logJobExecution(JobExecution jobExecution) {
        JobInstance jobInstance = jobExecution.getJobInstance();
        long jobInstanceId = jobInstance.getId();
        String jobName = jobInstance.getJobName();
        BatchStatus batchStatus = jobExecution.getStatus();
        log.info("{}(#{}) {}", jobName, jobInstanceId, batchStatus);
    }

}
