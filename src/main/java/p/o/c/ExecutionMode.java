package p.o.c;

public enum ExecutionMode {

    ANY( false, 1, false ),
    HASH( true, 2, true ),
    LOAD_BALANCED( true, 3, true ),
    PARTITIONED( true, 4, false ),
    RANDOM( true, 5, true ),
    ROUND_ROBIN( true, 6, true ),
    SEQUENCE( false, 7, false );

    private final boolean ordered;
    private final int number;
    private final boolean threadSticky;

    ExecutionMode(boolean ordered, int number, boolean threadSticky) {
        this.ordered = ordered;
        this.number = number;
        this.threadSticky = threadSticky;
    }

    /**
     * @return true if execution is ordered per Key
     */
    public boolean isOrdered() {
        return ordered;
    }

    /**
     * @return the unique number across modes, for this mode
     */
    public int number() { return number; }

    /**
     * @return true if the key sticks to it's Thread of first execution
     */
    public boolean isThreadSticky() {
        return threadSticky;
    }

}
