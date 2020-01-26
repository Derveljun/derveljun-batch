package com.derveljun.batchexample.batch.case2.file;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
public class SimpleFileBatch {

    @Value("classpath:files/000120.csv")
    private Resource csvFile;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory batchEntityManagerFactory;

    @Bean
    public Job fileToDbJob () {
        return jobBuilderFactory.get("fileToDbJob")
                .start(fileToDbStep())
                .build();
    }

    @Bean
    public Step fileToDbStep(){
        return stepBuilderFactory.get("fileToDbStep")
                .<StockData, StockData>chunk(5)
                .reader(simpleCsvFileReader())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<StockData> csvFileReader() {
        FlatFileItemReader<StockData> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setResource(csvFile);
        return itemReader;
    }

    @Bean
    FlatFileItemReader<StockData> simpleCsvFileReader() {
        return new FlatFileItemReaderBuilder<StockData>()
                .name("simpleReader")
                .resource(csvFile)
                .targetType(StockData.class)
                .delimited()
                .names(new String[] {"stockCode", "close", "volume", "unknown", "date", "start", "high", "low", "unknown1", "unknown2"})
                .build();
    }

    @Bean
    JpaItemWriter<StockData> jpaItemWriter() {
        return new JpaItemWriterBuilder<StockData>()
                .entityManagerFactory(batchEntityManagerFactory)
                .build();
    }

    @Bean
    public DefaultLineMapper lineMapper() {
        DefaultLineMapper<StockData> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer());
        lineMapper.setFieldSetMapper(fieldSetMapper());

        return lineMapper;
    }

    @Bean
    public BeanWrapperFieldSetMapper<StockData> fieldSetMapper() {
        BeanWrapperFieldSetMapper<StockData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StockData.class);
        return fieldSetMapper;
    }

    @Bean
    public DelimitedLineTokenizer tokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames(new String[] {"stockCode", "close", "volume", "unknown", "date", "start", "high", "low"});

        return tokenizer;
    }


}
