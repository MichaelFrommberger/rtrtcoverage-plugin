package com.thalesgroup.rtrtcoverage.tioreader;

/**
 * Coverage for a line into a file.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CoverageLine {

    /**
     * Type of the line coverage. No enum field is used, as check verification
     * is done by the parser.
     */
    private String type;

    /**
     * Number of the line coverage.
     */
    private int number;

    /**
     * The value of the mark if exists. Used only for conditional marks.
     */
    private String value;

    /**
     * The type of the line coverage.
     *
     * @return the type
     */
    public final String getType() {
        return type;
    }

    /**
     * Specify the type of the line coverage.
     *
     * @param sType
     *            the type to set
     */
    public final void setType(final String sType) {
        this.type = sType;
    }

    /**
     * Get the number of the type of the line coverage.
     *
     * @return the number
     */
    public final int getNumber() {
        return number;
    }

    /**
     * Specify which line into the type of the line coverage. WARNING: into .tio
     * file, type is hexadecimal
     *
     * @param hexaNumber
     *            a number in hexadecimal basis
     */
    public final void setNumber(final String hexaNumber) {
        final int hexaBasis = 16;
        final String output = new StringBuffer(hexaNumber).reverse().toString();
        this.number = Integer.parseInt(output, hexaBasis);
    }

    /**
     * Specify which line into the type of the line coverage. WARNING: into .fdc
     * file, type is decimal
     *
     * @param sNumber
     *            a number in decimal basis
     */
    public final void setIntNumber(final String sNumber) {
        this.number = Integer.parseInt(sNumber);
    }

    /**
     * Equality between same CoverageLine.
     *
     * @param o
     *            the coverage line which comparison is done
     * @return true if equals.
     */
    @Override
    public final boolean equals(final Object o) {
        final CoverageLine cl = (CoverageLine) o;
        return (this.type.equals(cl.type) && (this.number == cl.number));
    }

    /**
     * hashCode for using into HashMap.
     *
     * @return a key
     */
    @Override
    public final int hashCode() {
        return type.toString().hashCode() ^ Integer.toString(number).hashCode();
    }

    /**
     * The current type of the line coverage is conditional.
     *
     * @return true if the coverage type is a condition
     */
    public final boolean isConditional() {
        return (value != null);
    }

    /**
     * Return the value of the condition. Precondition : not empty iff
     * isConditional is true
     *
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * Set the value associated to a condition.
     *
     * @param sValue
     *            the current value of the condition tested
     */
    public final void setValue(final String sValue) {
        this.value = sValue;
    }
}
