package p.o.c.executor.strategies;

/**
 * A class implementing this marker interface can not dynamically adjust to load, rather the allocation
 * of a key/hash to an executing Thread is made once and once only for the lifetime of the JVM
 */
interface Fixed {
}
