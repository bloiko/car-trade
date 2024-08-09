package org.transport.trade.transport.batch;

import org.jetbrains.annotations.NotNull;
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
        when(transportRestClient.getTransports(anyInt(), anyInt())).thenReturn(getTransportDtos());

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals("COMPLETED", jobExecution.getStatus().toString());

        verify(transportRestClient, times(2)).getTransports(anyInt(), anyInt());

        int retry = 0;
        while (retry < 5) {
            ExitStatus exitStatus = getExitStatus(jobExecution);
            if (exitStatus == ExitStatus.COMPLETED) {
                verifyIndexedTransports();
                break;
            } else {
                Thread.sleep(100);
                retry++;
            }
        }
    }

    private @NotNull ExitStatus getExitStatus(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        return jobRepository.getLastJobExecution(jobName, jobExecution.getJobParameters()).getExitStatus();
    }

    private void verifyIndexedTransports() {
        Filters filters = new Filters();
        filters.setFilters(List.of(new TextSearchFilter("brand", "Audi"), new TextSearchFilter("bodyType", "Sedan")));
        TransportsResponse transportsResponse = transportController.filterTransports(filters);

        assertEquals(4, transportsResponse.getTransports().size());
    }

    private static List<TransportDto> getTransportDtos() {
        TransportDto transport1 = new TransportDto("S7", "Audi", "Sedan");
        TransportDto transport2 = new TransportDto("e5", "Audi", "Sedan");

        return Arrays.asList(transport1, transport2);
    }
}
