package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;

/**
 * Provides a per hash ordered execution of events using a hash/mod distribution
 * @param <T>
 */

public class Hash<T> extends AbstractStrategy<T> implements Fixed {

    private final int totalThreadCount;
    private final int myThreadNumber;

    public Hash(Consumer<T> proxied, int totalThreadCount, int myThreadNumber) {
        super(proxied);
        this.totalThreadCount = totalThreadCount;
        this.myThreadNumber = myThreadNumber;
    }

    boolean isGo(int hash, long sequence) {
        return ( hash % totalThreadCount ) == myThreadNumber;
    }

}
