package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Provides a non-ordered execution of events, but guarantees 1 execution per event, similar to
 * Executor
 * @param <T>
 */
public class Any<T> extends AbstractStrategy<T> implements Dynamic {

    private final AtomicLong globalSequence;

    public Any(Consumer<T> proxied, AtomicLong globalSequence) {
        super(proxied);
        this.globalSequence = globalSequence;
    }

    boolean isGo(int hash, long sequence) {
        return globalSequence.compareAndSet( sequence, sequence + 1 );
    }

}
