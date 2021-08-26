package p.o.c;

public interface KeyedExecutor {

    <T> Producer<T> create( EventFactory<T> eventFactory, Consumer<T> subscriber );

    <T> Producer<T> create( EventFactory<T> eventFactory, Consumer<T> subscriber, ExecutionMode executionMode );

    <T> Producer<T> create( EventFactory<T> eventFactory, Consumer<T> subscriber, int executionModeNum );

    <T> Producer<T> create( EventFactory<T> eventFactory, Consumer<T> subscriber, Config config );

    <T> Producer<T> create( EventFactory<T> eventFactory, Consumer<T> subscriber, Config config, ExecutionMode executionMode );

    <T> Producer<T> create( EventFactory<T> eventFactory, Consumer<T> subscriber, Config config, int executionModeNum );

}
