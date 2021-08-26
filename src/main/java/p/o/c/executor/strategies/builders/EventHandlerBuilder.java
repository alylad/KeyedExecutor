package p.o.c.executor.strategies.builders;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;
import p.o.c.Slot;

import java.util.List;

public interface EventHandlerBuilder<T> {

    List<EventHandler<Slot<T>>> build(Consumer<T> proxied, int numExecutingThreads, int partitionCount);

}
