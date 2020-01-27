package com.derveljun.batchexample.batch.case2.file;

import com.derveljun.batchexample.batch.case2.file.StockData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class SimpleFileBatch {

    @Value("classpath:files/000120.csv")
    private Resource csvFile;

    private final String randomString = UUID.randomUUID().toString();
    private final String jobName = "fileToDbJob_" + randomString;
    private final String stepName = "fileToDbStep_" + randomString;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory batchEntityManagerFactory;

    @Bean
    public Job fileToDbJob () {
        return jobBuilderFactory.get(jobName)
                .start(fileToDbStep())
                .build();
    }

    @Bean
    public Step fileToDbStep(){
        return stepBuilderFactory.get(stepName)
                .<CsvStockData, StockData>chunk(5)
                .reader(simpleCsvFileReader())
                .processor(simpleProcessor()) // 간단한 변경이라면 function 으로 처리해도 좋다.
                // .processor(csvStockData -> { ... });
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    FlatFileItemReader<CsvStockData> simpleCsvFileReader() {
        return new FlatFileItemReaderBuilder<CsvStockData>()
                .name("simpleReader")
                .resource(csvFile)
                .targetType(CsvStockData.class)
                .delimited()
                .names(CsvStockData.CsvStockDataFields)
                .build();
    }

    @Bean
    public ItemProcessor<CsvStockData, StockData> simpleProcessor() {
        return new SimpleCustomProcessor();
    }



    @Bean
    JpaItemWriter<StockData> jpaItemWriter() {
        return new JpaItemWriterBuilder<StockData>()
                .entityManagerFactory(batchEntityManagerFactory)
                .build();
    }

//    @Bean
//    public FlatFileItemReader<StockData> csvFileReader() {
//        FlatFileItemReader<StockData> itemReader = new FlatFileItemReader<>();
//        itemReader.setLineMapper(lineMapper());
//        itemReader.setResource(csvFile);
//        return itemReader;
//    }
//
//    @Bean
//    public DefaultLineMapper lineMapper() {
//        DefaultLineMapper<StockData> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setLineTokenizer(tokenizer());
//        lineMapper.setFieldSetMapper(fieldSetMapper());
//
//        return lineMapper;
//    }
//
//    @Bean
//    public BeanWrapperFieldSetMapper<StockData> fieldSetMapper() {
//        BeanWrapperFieldSetMapper<StockData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(StockData.class);
//        return fieldSetMapper;
//    }
//
//    @Bean
//    public DelimitedLineTokenizer tokenizer() {
//        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        tokenizer.setDelimiter(",");
//        tokenizer.setNames(new String[] {"stockCode", "close", "volume", "unknown", "date", "start", "high", "low"});
//
//        return tokenizer;
//    }
}

/**
 * Reader를 통해 읽어온 POJO 데이터를
 * Writer로 쓰기 전에 데이터 처리를 하는 부분.
 */
class SimpleCustomProcessor implements ItemProcessor<CsvStockData, StockData> {

    private String stockCode = "";

    private boolean isValidData(CsvStockData data) {

        // 유효성 검사

        return true;
    }

    @Override
    public StockData process(CsvStockData csvStockData) throws Exception {

        if (!isValidData(csvStockData))
            return null; // null 반환 시, 배치 작업은 중단된다.

        // CSV에서 첫 행에만 stockCode가 있다.
        if (csvStockData.getStockCode() != null && stockCode.isEmpty())
            this.stockCode = csvStockData.getStockCode();

        return StockData.builder()
                .stockCode(stockCode)
                .date(LocalDate.parse(csvStockData.getDate(), DateTimeFormatter.BASIC_ISO_DATE))
                .start(csvStockData.getStart())
                .high(csvStockData.getHigh())
                .low(csvStockData.getLow())
                .close(csvStockData.getHigh())
                .volume(csvStockData.getVolume())
                .build();
    }
}