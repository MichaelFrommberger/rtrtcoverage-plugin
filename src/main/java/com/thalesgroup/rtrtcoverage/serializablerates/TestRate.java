package com.thalesgroup.rtrtcoverage.serializablerates;

/**
 * Class containing rates for one test.
 * @author Bastien Reboulet
 */
public class TestRate extends SourceRate {

    /**
     * The serial ID.
     */
    private static final long serialVersionUID = 2306824714822730622L;

    /**
     * the name of this test (ex: "T74").
     */
    private String testName;

    /**
     * @return the name of this test.
     */
    public final String getTestName() {
        return testName;
    }

    /**
     * @param newTestName the name of this test.
     */
    public final void setTestName(final String newTestName) {
        this.testName = newTestName;
    }

}
