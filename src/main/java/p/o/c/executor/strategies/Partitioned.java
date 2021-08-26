package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;
import p.o.c.Slot;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Provides a per hash ordered execution of events. Hashes can traverse Threads so long as the order of execution
 * isn't lost, i.e. it re-distributes to guarantee best load distribution. Doesn't suite a load in which events
 * can concentrate on just a few hashes
 * @param <T>
 */
public class Partitioned<T> implements Dynamic, EventHandler<Slot<T>> {

    private static final int ACQUIRE = 1, RELEASE = 2;

    private final Consumer<T> proxied;

    private final long[] myCounts;
    private final int partitionCount;
    private final Map<Integer, AtomicLong> allCounts;

    public Partitioned(Consumer<T> proxied, Map<Integer, AtomicLong> allCounts) {
        this.proxied = proxied;
        this.partitionCount = allCounts.size();
        this.myCounts = new long[ partitionCount ];
        this.allCounts = allCounts;
    }

    @Override
    public void onEvent(Slot<T> tSlot, long l, boolean b) throws Exception {
        int partition = tSlot.hash % partitionCount;
        long myCount = myCounts[ partition ];
        AtomicLong allCount = allCounts.get( partition );
        if( tSlot.isAll ) {
            proxied.onEvent( tSlot.payload, tSlot.enqueueTimeNanos - System.nanoTime(), true );
        } else if( myCount == allCount.get() &&
                allCount.compareAndSet( myCount, myCount + ACQUIRE ) ) {
            try {
                proxied.onEvent( tSlot.payload, tSlot.enqueueTimeNanos - System.nanoTime(), false );
            } finally {
                allCount.lazySet( myCount + RELEASE );
            }
        }
        myCounts[ partition ] = myCount + RELEASE;
    }

}
