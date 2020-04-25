package br.com.estudo.javaee.batchlet;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

@Named
public class SimpleBatchLet extends AbstractBatchlet {
    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Inject
    @BatchProperty(name = "name")
    private String nameString;
    @Override
    public String process() {
        if(StringUtils.isNotBlank(nameString)){
            System.out.println("JobName: "+ nameString);
        }
        System.out.println("Step Name: " + stepContext.getProperties().get("name"));
        System.out.println("Job Name: " + jobContext.getProperties().get("jobProp1"));
        System.out.println("Executando um Batchlet");
        return BatchStatus.COMPLETED.toString();
    }
}