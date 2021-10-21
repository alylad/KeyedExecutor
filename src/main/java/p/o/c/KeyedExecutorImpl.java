package p.o.c;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import org.agrona.collections.Int2ObjectHashMap;
import p.o.c.executor.strategies.builders.AnyBuilder;
import p.o.c.executor.strategies.builders.EventHandlerBuilder;

import java.util.*;
import java.util.concurrent.ThreadFactory;

public class KeyedExecutorImpl implements KeyedExecutor {

    private final ThreadFactory threadFactory;
    private final int defaulRingBufferSize;
    private final int defaultNumExecutingThreads;
    private final int defaultPartitionCount;
    private final int defaultExecutionModeNum;

    private final Map<Integer, EventHandlerBuilder> builders = new Int2ObjectHashMap<>();
    {
        builders.put( ExecutionMode.ANY.number(), new AnyBuilder() );
    }

    public KeyedExecutorImpl(ThreadFactory threadFactory, int defaultRingBufferSize, int defaultNumExecutingThreads,
                             int defaultPartitionCount, ExecutionMode defaultExecutionMode) {
        this( threadFactory, defaultRingBufferSize, defaultNumExecutingThreads, defaultPartitionCount, defaultExecutionMode.number() );
    }

    public KeyedExecutorImpl(ThreadFactory threadFactory, int defaultRingBufferSize, int defaultNumExecutingThreads,
                             int defaultPartitionCount, int defaultExecutionModeNum) {
        this.threadFactory = threadFactory;
        this.defaulRingBufferSize = defaultRingBufferSize;
        this.defaultNumExecutingThreads = defaultNumExecutingThreads;
        this.defaultPartitionCount = defaultPartitionCount;
        this.defaultExecutionModeNum = defaultExecutionModeNum;
    }

    public <T> Producer<T> create(final EventFactory<T> eventFactory, Consumer<T> subscriber) {
        return create(eventFactory, subscriber, DEFAULT_CONFIG, defaultExecutionModeNum );
    }

    @Override
    public <T> Producer<T> create(EventFactory<T> eventFactory, Consumer<T> subscriber, ExecutionMode executionMode) {
        return create(eventFactory, subscriber, DEFAULT_CONFIG, executionMode );
    }

    @Override
    public <T> Producer<T> create(EventFactory<T> eventFactory, Consumer<T> subscriber, int executionModeNum) {
        return create(eventFactory, subscriber, DEFAULT_CONFIG, executionModeNum );
    }

    @Override
    public <T> Producer<T> create(EventFactory<T> eventFactory, Consumer<T> subscriber, Config config) {
        return create(eventFactory, subscriber, config, defaultExecutionModeNum );
    }

    @Override
    public <T> Producer<T> create(EventFactory<T> eventFactory, Consumer<T> subscriber, Config config, ExecutionMode executionMode) {
        return create( eventFactory, subscriber, config, executionMode.number() );
    }

    @Override
    public <T> Producer<T> create(EventFactory<T> eventFactory, Consumer<T> subscriber, Config config, int executionModeNum) {
        return doCreate(eventFactory, subscriber, new Config() {
            @Override
            public Optional<Integer> getQDepth() {
                return config.getQDepth().isPresent() ?
                        config.getQDepth() : DEFAULT_CONFIG.getQDepth();
            }

            @Override
            public Optional<Integer> getNumExecutingThreads() {
                return config.getNumExecutingThreads().isPresent() ?
                        config.getNumExecutingThreads() : DEFAULT_CONFIG.getNumExecutingThreads();
            }

            @Override
            public Optional<Integer> getPartitionCount() {
                return config.getPartitionCount().isPresent() ?
                        config.getPartitionCount() : DEFAULT_CONFIG.getPartitionCount();
            }

        }, executionModeNum );
    }

    private <T> Producer<T> doCreate(EventFactory<T> eventFactory, Consumer<T> subscriber, Config config, int executionModeNum ) {
        final Disruptor<Slot<T>> disruptor = new Disruptor<Slot<T>>(
                () -> new Slot<T>( eventFactory.create() ),
                config.getQDepth().get(), threadFactory
        );
        EventHandlerBuilder<T> eventHandlerBuilder = builders.get( executionModeNum );
        List<EventHandler<Slot<T>>> eventHandlers =
                eventHandlerBuilder.build( subscriber, config.getNumExecutingThreads().get(), config.getPartitionCount().get() );

        disruptor.handleEventsWith( eventHandlers.toArray( new EventHandler[ eventHandlers.size() ] ) );
        return new Producer<T>() {

            final ThreadLocal<Header> headerTLTrue = ThreadLocal.withInitial( () -> new Header( new Key(), Boolean.TRUE ) );
            final ThreadLocal<Header> headerTLFalse = ThreadLocal.withInitial( () -> new Header( new Key(), Boolean.FALSE ) );

            public <A> void publish(ProducerOneArg<T, A> translator, int hash, A arg1) {
                Header header = headerTLFalse.get();
                header.hash.value = hash;
                disruptor.publishEvent( translator, header, arg1 );
            }

            public <A, B> void publish(ProducerTwoArg<T, A, B> translator, int hash, A arg1, B arg2) {
                Header header = headerTLFalse.get();
                header.hash.value = hash;
                disruptor.publishEvent( translator, header, arg1, arg2 );
            }

            @Override
            public <A> void publishAll(ProducerOneArg<T, A> translator, int hash, A arg1) {
                Header header = headerTLTrue.get();
                header.hash.value = hash;
                disruptor.publishEvent( translator, header, arg1 );
            }

            @Override
            public <A, B> void publishAll(ProducerTwoArg<T, A, B> translator, int hash, A arg1, B arg2) {
                Header header = headerTLTrue.get();
                header.hash.value = hash;
                disruptor.publishEvent( translator, header, arg1, arg2 );
            }
        };
    }

    private final Config DEFAULT_CONFIG = new Config() {
        @Override
        public Optional<Integer> getQDepth() {
            return Optional.of( defaulRingBufferSize );
        }

        @Override
        public Optional<Integer> getNumExecutingThreads() {
            return Optional.of( defaultNumExecutingThreads );
        }

        @Override
        public Optional<Integer> getPartitionCount() {
            return Optional.of( defaultPartitionCount );
        }
    };

    public void addExecutionModes( Map<Integer, EventHandlerBuilder> newExecutionModes ) {
        builders.putAll( newExecutionModes );
    }
}
