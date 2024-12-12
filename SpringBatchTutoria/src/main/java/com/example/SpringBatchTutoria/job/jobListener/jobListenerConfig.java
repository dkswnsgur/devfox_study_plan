package com.example.SpringBatchTutoria.job.jobListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class jobListenerConfig {
	
	@Autowired
    private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    private StepBuilderFactory stepBuilderFactory;
	
	 @Bean
	    public Job jobListenerjob(Step jobListenerStep) {
	        return jobBuilderFactory.get("jobListenerjob")
	                .incrementer(new RunIdIncrementer())
	                .listener(new jobLoggerListener())
	                .start(jobListenerStep)
	                .build();
	    }
	 
	 @Bean
	 @JobScope
	    public Step jobListenerStep(Tasklet jobListenerTasklet) {
	        return stepBuilderFactory.get("jobListenerStep")
	                .tasklet(jobListenerTasklet)
	                .build();
	    }
	 
	 @Bean
	 @StepScope
	    public Tasklet jobListenerTasklet() {
	        return new Tasklet() {
	        	@Override
	        	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
	        		System.out.println("job Listener Tasklet");
	        		return RepeatStatus.FINISHED;
	        	}
	        };
	    }
}
