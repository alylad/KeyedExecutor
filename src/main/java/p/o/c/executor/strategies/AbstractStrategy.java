package p.o.c.executor.strategies;

import com.lmax.disruptor.EventHandler;
import p.o.c.Consumer;
import p.o.c.Slot;

abstract class AbstractStrategy<T> implements EventHandler<Slot<T>> {

    private final Consumer<T> proxied;

    public AbstractStrategy(Consumer<T> proxied) {
        this.proxied = proxied;
    }

    public void onEvent(Slot<T> slot, long sequence, boolean b) {
        try {
            if( slot.isAll ) {
                proxied.onEvent( slot.payload, slot.enqueueTimeNanos - System.nanoTime(), true );
            } else if( isGo( slot.hash, sequence ) ) {
                proxied.onEvent( slot.payload, slot.enqueueTimeNanos - System.nanoTime(), false );
            }
        } catch( Exception ex ){
            throw new RuntimeException( ex );
        }
    }

    abstract boolean isGo( int hash, long sequence );

}
