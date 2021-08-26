package p.o.c;

import com.lmax.disruptor.EventTranslatorTwoArg;

public abstract class ProducerOneArg<T,A> implements EventTranslatorTwoArg<Slot<T>,Header,A> {

    public void translateTo( Slot<T> t, long l, Header header, A arg1 ) {
        t.enqueueTimeNanos = System.nanoTime();
        t.hash = header.hash.value;
        t.isAll = header.isAll.booleanValue();
        doTranslate( t.payload, arg1 );
    }

    abstract protected void doTranslate( T t, A arg1 );

}
