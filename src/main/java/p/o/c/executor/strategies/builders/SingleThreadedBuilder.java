package p.o.c.executor.strategies.builders;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;
import p.o.c.Slot;

import java.util.ArrayList;
import java.util.List;

public class SingleThreadedBuilder<T> implements EventHandlerBuilder<T> {

    @Override
    public List<EventHandler<Slot<T>>> build(final Consumer<T> proxied, int numExecutingThreads, int partitionCount) {
        List<EventHandler<Slot<T>>> handler = new ArrayList<>();
        handler.add( new EventHandler<Slot<T>>() {
            @Override
            public void onEvent(Slot<T> tSlot, long l, boolean b) throws Exception {
                proxied.onEvent( tSlot.payload, tSlot.enqueueTimeNanos - System.nanoTime(), tSlot.isAll );
            }
        });
        return handler;
    }

}
