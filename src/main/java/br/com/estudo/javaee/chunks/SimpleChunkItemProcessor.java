package br.com.estudo.javaee.chunks;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

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
    @Override
    public Integer processItem(Object t) {
        Integer item = (Integer) t;
        System.out.println("Chunk: Executando o processamento do item "+ item);
        return item % 2 == 0 ? item : null;
    }
}