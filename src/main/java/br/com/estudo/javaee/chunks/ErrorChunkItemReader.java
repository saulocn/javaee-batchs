package br.com.estudo.javaee.chunks;

import java.io.Serializable;
import java.util.StringTokenizer;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ErrorChunkItemReader extends AbstractItemReader {
    private StringTokenizer tokens;
    private Integer count=0;

    @Inject
    JobContext jobContext;

    @Override
    public Integer readItem() throws Exception {
        if (tokens.hasMoreTokens()) {
            count++;
            jobContext.setTransientUserData(count);
            int token = Integer.valueOf(tokens.nextToken());
            if (token == 3) {
                throw new RuntimeException("Something happened");
            }
            return Integer.valueOf(token);
        }
        return null;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        tokens = new StringTokenizer("1,2,3,4,5,6,7,8,9,10", ",");
    }
}