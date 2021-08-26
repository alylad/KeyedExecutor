package p.o.c.executor.strategies.builders;

import com.lmax.disruptor.EventHandler;
import org.agrona.collections.Int2ObjectHashMap;
import p.o.c.Consumer;
import p.o.c.Slot;
import p.o.c.executor.strategies.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomBuilder<T> implements EventHandlerBuilder<T> {

    @Override
    public List<EventHandler<Slot<T>>> build(Consumer<T> proxied, int numExecutingThreads, int partitionCount) {
        int NOT_ALLOCATED_MARKER = -1;
        Map<Integer, AtomicInteger> partitionThreadOwner = new Int2ObjectHashMap<>();
        for( int i = 0; i < partitionCount; i++ ) {
            partitionThreadOwner.put( i, new AtomicInteger( NOT_ALLOCATED_MARKER ) );
        }
        List<EventHandler<Slot<T>>> handlers = new ArrayList<>();
        for( int i = 0; i < numExecutingThreads; i++ ) {
            handlers.add( new Random<T>( proxied, numExecutingThreads, i, NOT_ALLOCATED_MARKER, partitionThreadOwner ) );
        }
        return handlers;
    }

}
