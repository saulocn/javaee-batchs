package br.com.estudo.javaee.chunk;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.StepExecution;

import br.com.estudo.javaee.BatchTestHelper;
import org.junit.Test;

public class SimpleChunkTest {
    @Test
    public void givenChunk_thenBatch_completesWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleChunk", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }

    @Test
    public void givenChunk_whenCustomCheckPoint_thenCommitCountIsThree() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleChunkWithCustomCheckpoint", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        jobOperator.getStepExecutions(executionId)
                .stream()
                .map(BatchTestHelper::getCommitCount)
                .forEach(count -> assertEquals(3L, count.longValue()));
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
    @Test
    public void whenChunkError_thenBatch_CompletesWithFailed() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleChunkError", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.FAILED);
    }

    @Test
    public void givenChunkError_thenErrorSkipped_CompletesWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleChunkErrorSkipped", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName()
                    .equals("errorStep")) {
                jobOperator.getStepExecutions(executionId)
                        .stream()
                        .map(BatchTestHelper::getProcessSkipCount)
                        .forEach(skipCount -> assertEquals(1L, skipCount.longValue()));
            }
        }
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}
