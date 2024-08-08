package org.transport.trade.transport.batch;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.transport.trade.elastic.ElasticSearchTransportClient;
import org.transport.trade.transport.Transport;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;
import org.transport.trade.transport.rest.TransportRestClient;

@Configuration
public class SyncTransportsJobConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransportRestClient transportRestClient;

    @Autowired
    private ElasticSearchTransportClient elasticSearchTransportClient;

    @Value("${batch.sync-transports.maxPageNum:10}")
    private int maxPageNum;

    @Bean
    public Job syncTransportsJob() {
        return new JobBuilder("syncTransportsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(syncTransportsStep())
                .build();
    }

    @Bean
    public Step syncTransportsStep() {
        return new StepBuilder("syncTransportsStep", jobRepository)
                .<TransportDto, Transport>chunk(5, transactionManager)
                .reader(restTransportsReader())
                .processor(transportProcessor())
                .writer(transportWriter())
                //                .faultTolerant()
                //                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .build();
    }

    @Bean
    public ItemReader<TransportDto> restTransportsReader() {
        return new RestTransportReader(transportRestClient, 10, maxPageNum);
    }

    @Bean
    public ItemProcessor<TransportDto, Transport> transportProcessor() {
        return dto -> {
            Transport transport = new Transport();
            transport.setTransportType(TransportType.PASSENGER_CARS);
            transport.setBodyType(dto.getBodyType());
            transport.setManufacturerCountry(getRandom(Country.values()));
            transport.setManufacturerYear(getRandom(2010, 2015, 2017, 2020));
            transport.setBrand(dto.getBrand());
            transport.setModel(dto.getModel());
            transport.setPrice(getRandom(new BigInteger("1000"), new BigInteger("14230"), new BigInteger("432000")));
            transport.setRegion(getRandom("Volyn", "Kyiv"));
            return transport;
        };
    }

    @Bean
    public ItemWriter<Transport> transportWriter() {
        return items -> {
            elasticSearchTransportClient.bulkIndex(items.getItems());
        };
    }

    @SafeVarargs
    public static <T> T getRandom(T... values) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        int randomIndex = random.nextInt(values.length);
        return values[randomIndex];
    }
}
