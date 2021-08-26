package p.o.c.executor.strategies.builders;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;
import p.o.c.Slot;
import p.o.c.executor.strategies.Any;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class AnyBuilder<T> implements EventHandlerBuilder<T> {

    @Override
    public List<EventHandler<Slot<T>>> build(Consumer<T> proxied, int numExecutingThreads, int partitionCount) {
        List<EventHandler<Slot<T>>> handlers = new ArrayList<>();
        AtomicLong sequence = new AtomicLong();
        for( int i = 0; i < numExecutingThreads; i++ ) {
            handlers.add( new Any<T>( proxied, sequence ) );
        }
        return handlers;
    }

}
