package br.com.estudo.javaee.decision;

import javax.batch.api.Decider;
import javax.batch.runtime.StepExecution;
import javax.inject.Named;

@Named
public class DeciderJobSequence implements Decider {
    @Override
    public String decide(StepExecution[] ses) throws Exception {
        System.out.println("Tomando a decisão...");
        return "three";
    }

}