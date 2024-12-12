package com.example.SpringBatchTutoria.job.jobListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class jobLoggerListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(jobLoggerListener.class);

    private static String BEFORE_MESSAGE = "{} job is Running";
    private static String AFTER_MESSAGE = "{} job is Done. (Status: {})";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("Job has failed");
        }
    }
}
