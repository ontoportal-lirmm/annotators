package org.sifrproject.annotatorclient.util;


public class PairImpl<U, V> implements Pair<U, V> {
    private U first;
    private V second;

    public PairImpl(final U first, final V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PairImpl<?, ?> pair = (PairImpl<?, ?>) o;

        return first != null ? first.equals(pair.first) : pair.first == null &&
                !(second != null ? !second.equals(pair.second) : pair.second != null);

    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    @SuppressWarnings("PublicMethodNotExposedInInterface")
    public U first() {
        return first;
    }

    @Override
    @SuppressWarnings("PublicMethodNotExposedInInterface")
    public V second() {
        return second;
    }
}
