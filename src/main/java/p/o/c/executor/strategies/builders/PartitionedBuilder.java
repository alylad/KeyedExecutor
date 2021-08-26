package p.o.c.executor.strategies.builders;

import com.lmax.disruptor.EventHandler;
import org.agrona.collections.Int2ObjectHashMap;
import p.o.c.Consumer;
import p.o.c.Slot;
import p.o.c.executor.strategies.Partitioned;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PartitionedBuilder<T> implements EventHandlerBuilder<T> {

    @Override
    public List<EventHandler<Slot<T>>> build(Consumer<T> proxied, int numExecutingThreads, int partitionCount) {
        Map<Integer, AtomicLong> allCounts = new Int2ObjectHashMap<>();
        for( int i = 0; i < partitionCount; i++ ) {
            allCounts.put( i, new AtomicLong() );
        }
        List<EventHandler<Slot<T>>> handlers = new ArrayList<>();
        for( int i = 0; i < numExecutingThreads; i++ ) {
            handlers.add( new Partitioned<T>( proxied, allCounts ) );
        }
        return handlers;
    }

}
