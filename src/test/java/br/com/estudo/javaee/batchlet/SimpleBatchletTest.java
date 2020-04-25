package br.com.estudo.javaee.batchlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;

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

    @Test
    public void givenPartitionBatchlet_thenBatch_completeWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simplePartitionJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }

    @Test
    public void givenBatchLetStarted_whenStopped_thenBatchStopped() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simplePartitionJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobOperator.stop(executionId);
        jobExecution = BatchTestHelper.keepTestStopped(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.STOPPED);
    }

    @Test
    public void givenBatchLetStopped_whenRestarted_thenBatchCompletesSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simplePartitionJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobOperator.stop(executionId);
        jobExecution = BatchTestHelper.keepTestStopped(jobExecution);

        assertEquals(jobExecution.getBatchStatus(), BatchStatus.STOPPED);
        executionId = jobOperator.restart(jobExecution.getExecutionId(), new Properties());
        jobExecution = BatchTestHelper.keepTestAlive(jobOperator.getJobExecution(executionId));

        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}

