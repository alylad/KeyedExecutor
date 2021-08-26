package p.o.c;

import java.util.Optional;

public interface Config {

    default Optional<Integer> getQDepth() { return Optional.empty(); }

    default Optional<Integer> getNumExecutingThreads() { return Optional.empty(); }

    default Optional<Integer> getPartitionCount() { return Optional.empty(); }

}
