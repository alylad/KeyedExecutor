package p.o.c;

public interface Consumer<T> {

    void onEvent(T t, long delayTime);

    void onAllEvent(T t, long delayTime, boolean isThisThreadsKey);

}
