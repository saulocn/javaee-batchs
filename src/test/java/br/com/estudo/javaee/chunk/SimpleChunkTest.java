package br.com.estudo.javaee.chunk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void givenTwoSteps_thenBatch_CompleteWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleTwoStepsJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(2 , jobOperator.getStepExecutions(executionId).size());
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }

    @Test
    public void givenFlow_thenBatch_CompleteWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleFlowJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(3, jobOperator.getStepExecutions(executionId).size());
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }

    @Test
    public void givenDecisionFlow_thenBatch_CompleteWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleDecisionJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }

    @Test
    public void givenSplit_thenBatch_CompletesWithSuccess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleConcurrentJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);

        assertEquals(3, stepExecutions.size());
        assertEquals("splitJobSequenceStep3", stepExecutions.get(2).getStepName());
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }


    @Test
    public void givenSplit_thenBatch_CompletesWithSuccessAssertNames() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleConcurrentJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        List<String> executedSteps = new ArrayList<>();
        for (StepExecution stepExecution : stepExecutions) {
            executedSteps.add(stepExecution.getStepName());
        }
        assertEquals(3, stepExecutions.size());
        assertTrue(executedSteps.contains("splitJobSequenceStep1"));
        assertTrue(executedSteps.contains("splitJobSequenceStep2"));
        assertTrue(executedSteps.contains("splitJobSequenceStep3"));
        assertTrue(executedSteps.get(0).equals("splitJobSequenceStep1") || executedSteps.get(0).equals("splitJobSequenceStep2"));
        assertTrue(executedSteps.get(1).equals("splitJobSequenceStep1") || executedSteps.get(1).equals("splitJobSequenceStep2"));
        assertTrue(executedSteps.get(2).equals("splitJobSequenceStep3"));
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }


    @Test
    public void givenChunk_whenJobStarts_thenStepsHaveMetrics() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("simpleChunk", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
        assertTrue(jobOperator.getJobNames().contains("simpleChunk"));
        assertTrue(jobOperator.getParameters(executionId).isEmpty());
        StepExecution stepExecution = jobOperator.getStepExecutions(executionId).get(0);
        Map<Metric.MetricType, Long> metricTest = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());
        assertEquals(10L, metricTest.get(Metric.MetricType.READ_COUNT).longValue());
        assertEquals(5L, metricTest.get(Metric.MetricType.FILTER_COUNT).longValue());
        assertEquals(4L, metricTest.get(Metric.MetricType.COMMIT_COUNT).longValue());
        assertEquals(5L, metricTest.get(Metric.MetricType.WRITE_COUNT).longValue());
    }

}
