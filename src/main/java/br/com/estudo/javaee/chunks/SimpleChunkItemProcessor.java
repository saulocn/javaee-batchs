package br.com.estudo.javaee.chunks;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

/**
 * Um chunk é um job que executa determinados passos.
 * Ele utiliza o {@link SimpleChunkItemReader} para realizar a leitura de dados
 * (Pode ser de uma base ou de um arquivo, por exemplo), o
 * {@link SimpleChunkItemProcessor} para processá-los e o {@link SimpleChunkWriter}
 * para escrever o resultado em algum lugar.
 *
 * Neste caso estamos passando para a próxima etapa do job apenas os números pares.
 * Quando se retorna null, o item é retirado do lote.
 */
@Named
public class SimpleChunkItemProcessor implements ItemProcessor {
    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Inject
    @BatchProperty(name = "name")
    private String nameString;

    @Override
    public Integer processItem(Object t) {
        Integer item = (Integer) t;
        System.out.println("Chunk: Executando o processamento do item "+ item);
        return item % 2 == 0 ? item : null;
    }
}