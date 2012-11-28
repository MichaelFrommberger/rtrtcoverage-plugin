package com.thalesgroup.rtrtcoverage.tioreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Coverage for a test.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CoverageTest {

    /**
     * Name of the test.
     */
    private String name;

    /**
     * Coverage of the test by file. elements are {@see CoverageLine}
     * information.
     */
    private final List<CoverageLine> coverage;

    /**
     * Default Constructor.
     */
    public CoverageTest() {
        name = "";
        coverage = new ArrayList<CoverageLine>();
    }

    /**
     * The name of the test.
     *
     * @return the name of the test
     */
    public final String getName() {
        return name;
    }

    /**
     * The name of the current test.
     *
     * @param sName
     *            the name of the test
     */
    public final void setName(final String sName) {
        this.name = sName;
    }

    /**
     * Get the coverage associated to one file.
     *
     * @return coverage of the file for the current test : collection of {@see
     *         CoverageLine}
     */
    public final List<CoverageLine> getFileCoverage() {
        return coverage;
    }

    /**
     * Add the coverage associated to one file.
     *
     * @param lineCoverage
     *            coverage of the file for the current test
     */
    public final void add(final CoverageLine lineCoverage) {
        coverage.add(lineCoverage);
    }

}
