package p.o.c;

public class Slot<T> {

    public final T payload;
    public long enqueueTimeNanos;
    public int hash;
    public boolean isAll;
    public boolean isAllEvent;

    public Slot(T payload) {
        this.payload = payload;
    }

}
