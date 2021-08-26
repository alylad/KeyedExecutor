package p.o.c.executor.strategies.builders;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;
import p.o.c.Slot;
import p.o.c.executor.strategies.Sequence;

import java.util.ArrayList;
import java.util.List;

public class SequenceBuilder<T> implements EventHandlerBuilder<T> {

    @Override
    public List<EventHandler<Slot<T>>> build(Consumer<T> proxied, int numExecutingThreads, int partitionCount) {
        List<EventHandler<Slot<T>>> handlers = new ArrayList<>();
        for( int i = 0; i < numExecutingThreads; i++ ) {
            handlers.add( new Sequence<T>( proxied, numExecutingThreads, i ) );
        }
        return handlers;
    }

}
