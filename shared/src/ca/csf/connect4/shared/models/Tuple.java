package ca.csf.connect4.shared.models;

import java.io.Serializable;

/**
 * Created by dom on 16/12/15.
 */
public class Tuple<L, R> implements Serializable {
    private static final long serialVersionUID = -8918364761648003533L;
    private final L left;
    private final R right;

    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) return false;
        Tuple tuple = (Tuple) o;
        return this.left.equals(tuple.getLeft()) &&
                this.right.equals(tuple.getRight());
    }
}
