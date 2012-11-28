package com.thalesgroup.rtrtcoverage.tioreader2;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure corresponding to an entire *.tio file.
 *
 * @author Bastien Reboulet
 */
public class TestSuiteTrace {

    /**
     * the name of the *.tio file (including the extension).
     * from where data is extracted
     */
    private String name;

    /**
     * the list of all the tests into this test suite.
     */
    private final List<TestTrace> testTraces;

    /**
     * Default constructor.
     */
    public TestSuiteTrace() {
        testTraces = new ArrayList<TestTrace>();
    }

    /**
     * @return all the tests from this test suite.
     */
    public final List<TestTrace> getTestTraces() {
        return testTraces;
    }

    /**
     * Adds a test trace to this test suite.
     *
     * @param testTrace a test trace
     */
    public final void addTestTrace(final TestTrace testTrace) {
        testTraces.add(testTrace);
    }

    /**
     * @return the name of the *.tio file from where data was extracted.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param newName
     *            the name of the *.tio file from where data is extracted.
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

}
