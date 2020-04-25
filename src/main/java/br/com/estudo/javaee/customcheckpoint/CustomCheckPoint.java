package br.com.estudo.javaee.customcheckpoint;

import javax.batch.api.chunk.AbstractCheckpointAlgorithm;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class CustomCheckPoint extends AbstractCheckpointAlgorithm {

    @Inject
    JobContext jobContext;

    @Override
    public boolean isReadyToCheckpoint() throws Exception {
        int counterRead = (Integer) jobContext.getTransientUserData();
        System.out.println("Checkpoint em "+counterRead + (counterRead % 5 == 0));
        return counterRead % 5 == 0;
    }
}