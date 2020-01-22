package com.derveljun.batchexample;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@RequiredArgsConstructor // Lombook 을 통한 생성자 주입
@Configuration
public class FileBatch {

/*    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job FileJob(){
        return this.jobBuilderFactory.get("fileJobExample")
                .start(Step1())
                .next(Step2())
                .build();
    }

    @Bean
    public Step Step1(){
        return this.stepBuilderFactory.get("fileJobExample_step1")
                .chunk(5)
                .reader(new ItemReader<File>() {
                })
    }

    @Bean Step Step2(){

    }*/

}
