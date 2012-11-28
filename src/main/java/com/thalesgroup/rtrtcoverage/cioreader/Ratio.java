package com.thalesgroup.rtrtcoverage.cioreader;

import java.io.IOException;
import java.io.Serializable;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Represents <tt>x/y</tt> where x={@link #numerator} and y={@link #denominator}.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
@ExportedBean
public class Ratio implements Serializable {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -6669262405419252043L;
    /**
     * The numerator.
     */
    private float numerator;
    /**
     * The denominator. Must be > 0 if initialized and numerator > 0.
     */
    private float denominator;
    /**
     * Ratio is initialized.
     */
    private boolean initialized;

    /**
     * Default Constructor. Ratio is uninitialized.
     */
    public Ratio() {
        this.numerator = 0;
        this.denominator = 0;
        this.initialized = false;
    }

    /**
     * Initialization of the ratio.
     *
     * @param f
     *            the array with the ratio
     * @throws CioException
     *             if bad size of the array or bad denominator
     */
    public Ratio(final float... f) throws CioException {
        if (f.length >= 2) {
            initialized = true;
            this.numerator = f[0];
            if (f[1] == 0.0f && f[0] != 0.0f) {
                throw new CioException("Bad denominator. Cannot be zero.");
            }
            this.denominator = f[1];
        } else {
            throw new CioException(
                    "Bad size of the float array. Must be at least 2 instead of "
                            + f.length);
        }

    }

    /**
     * Get numerator.
     *
     * @return the numerator
     */
    public final float getNumerator() {
        return numerator;
    }

    /**
     * Get denominator.
     *
     * @return the denominator
     */
    public final float getDenominator() {
        return denominator;
    }

    /**
     * Gets "x/y" representation.
     *
     * @return "x/y"
     */
    @Override
    public final String toString() {
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
        final int i = (int) f;
        if (i == f) {
            return String.valueOf(i);
        } else {
            return String.valueOf(f);
        }
    }

    /**
     * Gets the percentage in integer.
     *
     * @return percentage as integer
     */
    @Exported
    public final int getPercentage() {
        return Math.round(getPercentageFloat());
    }

    /**
     * Gets the percentage in float.
     *
     * @return percentage as float
     */
    @Exported
    public final float getPercentageFloat() {
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
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Ratio ratio = (Ratio) o;

        return Float.compare(ratio.denominator, denominator) == 0
                && Float.compare(ratio.numerator, numerator) == 0;

    }

    /**
     * The hashcode.
     *
     * @return the hashcode
     */
    @Override
    public final int hashCode() {
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
     * Add a value to the current ratio.
     *
     * @param v
     *            the string value to add
     */
    public final void addValue(final String v) {
        final float[] f = parse(v);
        numerator += f[0];
        denominator += f[1];
        initialized = true;
    }

    /**
     * Set a value to the current ratio. Only use if no information found into
     * the files. Otherwise use {@see addValue}.
     *
     * @param fnumerator
     *            the numerator
     * @param fdenominator
     *            the denominator
     */
    public final void setValue(final float fnumerator, final float fdenominator) {
        numerator = fnumerator;
        denominator = fdenominator;
        initialized = true;
    }

    /**
     * Add a value to the current ratio.
     *
     * @param r
     *            the ratio to add
     */
    public final void addValue(final Ratio r) {
        this.numerator += r.numerator;
        this.denominator += r.denominator;
        initialized = true;
    }

    /**
     * Get if the ratio is initialized.
     *
     * @return true if initialized
     */
    public final boolean isInitialized() {
        return initialized;
    }

    /**
     * Parses the value attribute format of CIO "52% (52/100)".
     *
     * @param v
     *            input string
     * @return a float array such as {52,100}
     */
    public static float[] parse(final String v) {
        // if only I could use java.util.Scanner...

        // only leave "a/b" in "N% (a/b)"
        int idx = v.indexOf('(');
        final String cleanV = v.substring(idx + 1, v.length() - 1);
        idx = cleanV.indexOf('/');

        return new float[] {parseFloat(cleanV.substring(0, idx)),
                parseFloat(cleanV.substring(idx + 1))};
    }

    /**
     * Parse a value.
     *
     * @param v
     *            the string to parse
     * @return the ratio in the string v
     * @throws IOException
     *             if parsing error
     * @throws CioException
     *             if bad ratio
     */
    public static Ratio parseValue(final String v) throws IOException,
    CioException {
        return new Ratio(parse(v));
    }

    /**
     * Parses the float value stored in a string. Uses simple heuristics to
     * handle comma or dot as a decimal point.
     *
     * @param v
     *            the string to parse
     * @return a float
     */
    private static float parseFloat(final String v) {
        final int idx = v.indexOf(',');
        String cleanV = v;
        if (idx >= 0) {
            cleanV = v.substring(0, idx) + "." + v.substring(idx + 1);
        }
        return Float.parseFloat(cleanV);
    }

}
