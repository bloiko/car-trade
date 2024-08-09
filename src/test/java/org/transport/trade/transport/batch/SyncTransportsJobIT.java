package org.transport.trade.transport.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.transport.trade.filter.Filters;
import org.transport.trade.filter.TextSearchFilter;
import org.transport.trade.transport.TransportController;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.dto.TransportsResponse;
import org.transport.trade.transport.rest.TransportRestClient;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
class SyncTransportsJobIT {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importUserJob;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private TransportController transportController;

    @MockBean
    private TransportRestClient transportRestClient;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(importUserJob);
    }

    @Test
    void testSyncTransportsJob() throws Exception {
        mockTransportRestClient();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals("COMPLETED", jobExecution.getStatus().toString());

        verify(transportRestClient, times(2)).getTransports(anyInt(), anyInt());

        assertJobCompletedWithExpectedResults(jobExecution);
    }

    private void mockTransportRestClient() {
        when(transportRestClient.getTransports(anyInt(), anyInt())).thenReturn(getTransportDtos());
    }

    private void assertJobCompletedWithExpectedResults(JobExecution jobExecution) throws InterruptedException {
        int retry = 0;
        while (retry++ < 5) {
            if (isCompleted(jobExecution)) {
                verifyIndexedTransports();
                return;
            } else {
                Thread.sleep(100);
            }
        }
    }

    private boolean isCompleted(JobExecution jobExecution) {
        return jobRepository
                        .getLastJobExecution(
                                jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters())
                        .getExitStatus()
                == ExitStatus.COMPLETED;
    }

    private void verifyIndexedTransports() {
        Filters filters = createFilters();
        TransportsResponse response = transportController.filterTransports(filters);
        assertEquals(4, response.getTransports().size());
    }

    private Filters createFilters() {
        Filters filters = new Filters();
        filters.setFilters(List.of(new TextSearchFilter("brand", "Audi"), new TextSearchFilter("bodyType", "Sedan")));
        return filters;
    }

    private static List<TransportDto> getTransportDtos() {
        return Arrays.asList(new TransportDto("S7", "Audi", "Sedan"), new TransportDto("e5", "Audi", "Sedan"));
    }
}
