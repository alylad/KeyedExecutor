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
            boolean isGo = isGo( slot.hash, sequence );
            if( slot.isAll ) {
                proxied.onAllEvent( slot.payload, slot.enqueueTimeNanos - System.nanoTime(), isGo );
            } else if( isGo ) {
                proxied.onEvent( slot.payload, slot.enqueueTimeNanos - System.nanoTime() );
            }
        } catch( Exception ex ){
            throw new RuntimeException( ex );
        }
    }

    abstract boolean isGo( int hash, long sequence );

}
