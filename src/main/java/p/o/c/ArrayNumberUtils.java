package p.o.c;

public class ArrayNumberUtils {

    public static int findLowestSubscript( long[] ary ) {
        long currentMin = Long.MAX_VALUE;
        int subscript = 0;
        for( int i = 0; i < ary.length; i++ ) {
            if( ary[ i ] <= currentMin ) {
                currentMin = ary[ i ];
                subscript = i;
            }
        }
        return subscript;
    }

}
