package p.o.c;

import com.lmax.disruptor.EventTranslatorThreeArg;

public abstract class ProducerTwoArg<T,A,B> implements EventTranslatorThreeArg<Slot<T>,Header,A,B> {

    public void translateTo( Slot<T> t, long l, Header header, A arg1, B arg2 ) {
        t.enqueueTimeNanos = System.nanoTime();
        t.hash = header.hash.value;
        t.isAll = header.isAll.booleanValue();
        doTranslate( t.payload, arg1, arg2 );
    }

    abstract protected void doTranslate( T t, A arg1, B arg2 );

}
