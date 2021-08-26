package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;

/**
 * Provides a non-ordered execution of events, but guarantees 1 execution per event, similar to
 * Executor
 * @param <T>
 */
public class Sequence<T> extends AbstractStrategy<T> implements Dynamic {

    private final int totalThreadCount;
    private final int myThreadNumber;

    public Sequence(Consumer<T> proxied, int totalThreadCount, int myThreadNumber ) {
        super(proxied);
        this.totalThreadCount = totalThreadCount;
        this.myThreadNumber = myThreadNumber;
    }

    boolean isGo(int hash, long sequence) {
        return (sequence % totalThreadCount) == myThreadNumber;
    }

}
