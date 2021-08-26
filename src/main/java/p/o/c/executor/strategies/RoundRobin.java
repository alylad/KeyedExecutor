package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import org.agrona.collections.Int2IntHashMap;
import p.o.c.Consumer;

/**
 * Provides a per hash ordered execution of events using a round robin distribution across hashes
 * @param <T>
 */

public class RoundRobin<T> extends AbstractStrategy<T> implements Fixed {

    private static final int NOT_ALLOCATED_MARKER = -1;
    private final int myThreadNumber;
    private final int partitionCount;
    private final int totalThreadCount;
    private int totalStreams = 0;

    private final Int2IntHashMap threadOwner;

    public RoundRobin(Consumer<T> proxied, int totalThreadCount, int myThreadNumber, int partitionCount ) {
        super(proxied);
        this.myThreadNumber = myThreadNumber;
        this.partitionCount = partitionCount;
        this.threadOwner = new Int2IntHashMap( partitionCount, 0.55F, NOT_ALLOCATED_MARKER );
        this.totalThreadCount = totalThreadCount;
    }

    boolean isGo(int hash, long sequence) {
        hash = hash % partitionCount;
        int owner = threadOwner.get( hash );
        if( owner == NOT_ALLOCATED_MARKER ) {
            owner = totalStreams++ % totalThreadCount;
            threadOwner.put( hash, owner );
        }
        return owner == myThreadNumber;
    }

}
