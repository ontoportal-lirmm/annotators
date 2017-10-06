package org.sifrproject.util;

public final class ValueScale {

    private ValueScale() {
    }

    public static int scaleValue(final int val, final int min, final int max, final int nmin, final int nmax) {
        return (int) Math.round(scaleValue((double) val, (double) min, (double) max, (double) nmin, (double) nmax));
    }

    public static double scaleValue(final double val, final double min, final double max, final double nmin, final double nmax) {
        return (((val * nmax) - (val * nmin) - (min * nmax)) + (nmin * max)) / (max - min);
    }
}
