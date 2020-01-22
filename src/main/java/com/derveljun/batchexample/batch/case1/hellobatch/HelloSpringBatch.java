package com.derveljun.batchexample.batch.case1.hellobatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor // Lombok을 통한 생성자 주입
@Configuration
public class HelloSpringBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob(){
        return jobBuilderFactory.get("HelloSpringBatchJob")
                .start(batchStep1())
                .next(batchStep2())
                .build();
    }

    @Bean
    public Step batchStep1() {
        return stepBuilderFactory.get("HelloSpringBatchStep1")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("HelloSpringBatchStep1 Started");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step batchStep2() {
        return stepBuilderFactory.get("HelloSpringBatchStep2")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("HelloSpringBatchStep2 Started");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
