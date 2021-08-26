package p.o.c;

/**
 * Provides execution of events keyed by a hash across a number of Threads using a distribution strategy
 * @param <T>
 */
public interface Producer<T> {

    /**
     * Publish to the Thread that is processing this hash
     * @param translator
     * @param hash
     * @param arg1
     * @param <A>
     */
    <A> void publish( ProducerOneArg<T,A> translator, int hash, A arg1 );

    /**
     * Publish to the Thread that is processing this hash
     * @param translator
     * @param hash
     * @param arg1
     * @param arg2
     * @param <A>
     * @param <B>
     */
    <A,B> void publish( ProducerTwoArg<T,A,B> translator, int hash, A arg1, B arg2 );

    /**
     * Publish to all Threads
     * @param translator
     * @param arg1
     * @param <A>
     */
    <A> void publishAll( ProducerOneArg<T,A> translator, A arg1 );

    /**
     * Publish to all Threads
     * @param translator
     * @param arg1
     * @param arg2
     * @param <A>
     * @param <B>
     */
    <A,B> void publishAll( ProducerTwoArg<T,A,B> translator, A arg1, B arg2 );

}
