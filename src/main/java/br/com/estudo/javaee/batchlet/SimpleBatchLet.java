package br.com.estudo.javaee.batchlet;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.inject.Named;

@Named
public class SimpleBatchLet extends AbstractBatchlet {

    @Override
    public String process() {
        System.out.println("Executando um Batchlet");
        return BatchStatus.COMPLETED.toString();
    }
}