package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides a per hash ordered execution of events using a per random distribution
 * @param <T>
 */
public class Random<T> extends AbstractStrategy<T> implements Fixed {

    private final int totalThreadCount;
    private final int myThreadNumber;
    private final int partitionCount;
    private final int notAllocatedMarker;
    private final Map<Integer, AtomicInteger> threadOwner;

    public Random(Consumer<T> proxied, int totalThreadCount, int myThreadNumber, int notAllocatedMarker,
                  Map<Integer, AtomicInteger> threadOwner ) {
        super(proxied);
        this.totalThreadCount = totalThreadCount;
        this.myThreadNumber = myThreadNumber;
        this.partitionCount = threadOwner.size();
        this.notAllocatedMarker = notAllocatedMarker;
        this.threadOwner = threadOwner;
    }

    boolean isGo(int hash, long sequence) {
        hash = hash % partitionCount;
        AtomicInteger ownerAI = threadOwner.get( hash );
        int owner = ownerAI.get();
        if( ownerAI.get() == notAllocatedMarker ) {
            owner = ThreadLocalRandom.current().nextInt( totalThreadCount );
            if( ! ownerAI.compareAndSet( notAllocatedMarker, owner ) ) {
                owner = ownerAI.get();
            }
        }
        return owner == myThreadNumber;
    }

}
