package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import org.agrona.collections.Int2IntHashMap;
import p.o.c.ArrayNumberUtils;
import p.o.c.Consumer;

/**
 * Provides a per hash ordered execution of events using a per Thread count load distribution
 * @param <T>
 */
public class LoadBalanced<T> extends AbstractStrategy<T> implements Fixed {

    private static final int NOT_ALLOCATED_MARKER = -1;
    private final int myThreadNumber;
    private final int partitionCount;

    private final Int2IntHashMap threadOwner;
    private final long[] perThreadCounts;

    public LoadBalanced(Consumer<T> proxied, int totalThreadCount, int myThreadNumber, int partitionCount ) {
        super(proxied);
        this.myThreadNumber = myThreadNumber;
        this.partitionCount = partitionCount;
        this.threadOwner = new Int2IntHashMap( partitionCount, 0.55F, NOT_ALLOCATED_MARKER );
        this.perThreadCounts = new long[ totalThreadCount ];
    }

    boolean isGo(int hash, long sequence) {
        hash = hash % partitionCount;
        int owner = threadOwner.get( hash );
        if( owner == NOT_ALLOCATED_MARKER ) {
            owner =  ArrayNumberUtils.findLowestSubscript( perThreadCounts );
            threadOwner.put( hash, owner );
        }
        perThreadCounts[ owner ]++;
        return owner == myThreadNumber;
    }

}
