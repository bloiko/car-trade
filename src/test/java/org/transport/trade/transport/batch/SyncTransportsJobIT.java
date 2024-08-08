package org.transport.trade.transport.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.transport.trade.elastic.ElasticSearchTransportClient;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.rest.TransportRestClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
class SyncTransportsJobIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importUserJob;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private TransportRestClient transportRestClient;

    @MockBean
    private ElasticSearchTransportClient elasticSearchTransportClient;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(importUserJob);
    }

    @Test
    void testSyncTransportsJob() throws Exception {
        when(transportRestClient.getTransports(anyInt(), anyInt())).thenReturn(getTransportDtos("Audi", "BMW"));

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals("COMPLETED", jobExecution.getStatus().toString());

        verify(transportRestClient, times(2)).getTransports(anyInt(), anyInt());
        verify(elasticSearchTransportClient, times(1)).bulkIndex(any(List.class));
    }

    private static List<TransportDto> getTransportDtos(String brand1, String brand2) {
        TransportDto transport1 = new TransportDto("S7", brand1, "Sedan");
        TransportDto transport2 = new TransportDto("e5", brand2, "Sedan");

        return Arrays.asList(transport1, transport2);
    }
}
