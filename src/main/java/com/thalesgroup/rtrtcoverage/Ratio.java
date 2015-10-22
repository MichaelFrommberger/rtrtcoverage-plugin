package com.thalesgroup.rtrtcoverage;

import java.io.Serializable;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Represents <tt>x/y</tt> where x={@link #numerator} and y={@link #denominator}.
 *
 * @author Kohsuke Kawaguchi
 * @author Bastien Reboulet
 */
@ExportedBean
public final class Ratio implements Serializable {


    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1567509235864715551L;
    /**
     * The numerator.
     */
    private float numerator = 0;
    /**
     * The denominator. Must be > 0 if initialized and numerator > 0.
     */
    private float denominator = 0;
    /**
     * Ratio is initialized.
     */
    private boolean initialized = false;

    /**
     * Default Constructor.
     *
     * @param f an array: {numerator, denominator}.
     */
    public Ratio(final float...f) {
        if (f.length >= 2) {
            initialized = true;
            this.numerator = f[0];
            this.denominator = f[1];
        }
    }
    /**
     * Get numerator.
     *
     * @return the numerator
     */
    public float getNumerator() {
        return numerator;
    }
    /**
     * Get denominator.
     *
     * @return the denominator
     */
    public float getDenominator() {
        return denominator;
    }

    /**
     * Gets "x/y" representation.
     * @return "x/y"
     */
    @Override
    public String toString() {
        return print(numerator) + "/" + print(denominator);
    }
    /**
     * Print a float.
     *
     * @param f
     *            the float to print
     * @return an integer-based or float-based string
     */
    private String print(final float f) {
        int i = (int) f;
        if (i == f) {
            return String.valueOf(i);
        } else {
            return String.valueOf(f);
        }
    }

    @Exported
    public boolean isNA() {
    	return (denominator == 0);
    }
    /**
     * Gets the percentage in integer.
     * @return percentage as integer
     */
    @Exported
    public int getPercentage() {
    	
        return Math.round(getPercentageDouble());
    }

    /**
     * Gets the percentage in float.
     * @return percentage as float
     */
    @Exported
    public float getPercentageDouble() {
        final int perCent = 100;
        if (denominator <= 0) {
            return 0;
        } else {
            return perCent * numerator / denominator;
        }
    }
    /**
     * Equality.
     *
     * @param o
     *            an other ratio
     * @return true if equals
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ratio ratio = (Ratio) o;

        return Float.compare(ratio.denominator, denominator) == 0
                && Float.compare(ratio.numerator, numerator) == 0;

    }

    @Override
    public int hashCode() {
        final int keyMult = 31;
        int result = 0;
        if (numerator != +0.0f) {
            result = Float.floatToIntBits(numerator);
        }
        if (keyMult * result + denominator != +0.0f) {
            result = Float.floatToIntBits(denominator);
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * @param newNumerator the numerator.
     * @param newDenominator the denominator.
     */
    public void setRatio(final float newNumerator, final float newDenominator) {
        this.numerator = newNumerator;
        this.denominator = newDenominator;
        this.initialized = true;
    }

    /**
     * @param newRatio a ratio to add to this ratio.
     */
    public void addRatio(final Ratio newRatio) {
        if (newRatio.isInitialized()) {
            this.numerator += newRatio.getNumerator();
            this.denominator += newRatio.getDenominator();
            this.initialized = true;
        }
    }

    /**
     * @param newNumerator a numerator to add to this ratio.
     * @param newDenominator a denominator to add to this ratio.
     */
    public void addRatio(final float newNumerator, final float newDenominator) {
        this.numerator += newNumerator;
        this.denominator += newDenominator;
        this.initialized = true;
    }
    /**
     * Get if the ratio is initialized.
     *
     * @return true if initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

}
