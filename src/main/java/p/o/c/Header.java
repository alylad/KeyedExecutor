package p.o.c;

public class Header {
    public final Key hash;
    public final Boolean isAll;

    public Header(Key hash, Boolean isAll) {
        this.hash = hash;
        this.isAll = isAll;
    }
}
