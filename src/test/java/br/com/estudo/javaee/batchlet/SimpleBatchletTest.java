package br.com.estudo.javaee.batchlet;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

import br.com.estudo.javaee.BatchTestHelper;
import org.junit.Test;

public class SimpleBatchletTest {
    @Test
    public void givenBatchlet_thenBatch_completeWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleBatchlet", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}
