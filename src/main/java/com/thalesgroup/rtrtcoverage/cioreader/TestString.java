package com.thalesgroup.rtrtcoverage.cioreader;

/**
 * Class for sorting the tests into the good order (accordingly to their number
 * and not lexicographically).
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class TestString implements Comparable<TestString> {

    /**
     * Name of the test.
     */
    private String name;

    /**
     * Return the name of the test.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the name of the test.
     *
     * @param sName
     *            the name to set
     */
    public final void setName(final String sName) {
        this.name = sName;
    }

    /**
     * Equality between same TestString.
     *
     * @param o
     *            the test string which comparison is done
     * @return true if equals.
     */
    @Override
    public final boolean equals(final Object o) {
        final TestString ts = (TestString) o;
        final String value1 = name.replace("T", "");
        final int order1 = new Integer(Integer.valueOf(value1));
        final String value2 = ts.name.replace("T", "");
        final int order2 = new Integer(Integer.valueOf(value2));
        return order1 == order2;
    }

    /**
     * hashCode for using into HashMap.
     *
     * @return a key
     */
    @Override
    public final int hashCode() {
        final String value = name.replace("T", "");
        final Integer order = new Integer(Integer.valueOf(value));
        return order.hashCode();
    }

    /**
     * Must be implemented for MapTree. Compare to TestString such as:
     *
     * @param o
     *            the string to compare
     * @return 0 if equals, -1 if before, 1 if after.
     */
    public final int compareTo(final TestString o) {
        final int cBEFORE = -1;
        final int cEQUAL = 0;
        final int cAFTER = 1;

        final String value1 = name.replace("T", "");
        final int order1 = new Integer(Integer.valueOf(value1));
        final String value2 = o.name.replace("T", "");
        final int order2 = new Integer(Integer.valueOf(value2));

        if (order1 < order2) {
            return cBEFORE;
        } else if (order1 == order2) {
            return cEQUAL;
        } else {
            return cAFTER;
        }

    }

}
