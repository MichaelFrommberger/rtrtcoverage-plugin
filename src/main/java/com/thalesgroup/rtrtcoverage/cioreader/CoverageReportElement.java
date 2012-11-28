package com.thalesgroup.rtrtcoverage.cioreader;

import java.util.ArrayList;

/**
 * Set of all the CoverageElement into the .cio file.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CoverageReportElement {

    /**
     * All the coverage report for the tests.
     */
    private final CoverageReportTestElement coverageTests;

    /**
     * Global coverage report by file.
     */
    private final ArrayList<CoverageElement> globalCoverages;

    /**
     * Default constructor.
     */
    public CoverageReportElement() {
        coverageTests = new CoverageReportTestElement();
        globalCoverages = new ArrayList<CoverageElement>();
    }

    /**
     * Add a coverage report by file for the current test.
     *
     * @param name
     *            the current test name
     * @param coverage
     *            the coverage report to add to this test
     */
    public final void addCoverageTest(final String name,
            final CoverageElement coverage) {
        coverageTests.addCoverage(name, coverage);
    }

    /**
     * Add a global coverage for a given file.
     *
     * @param coverage
     *            the global coverage
     */
    public final void addGlobalCoverage(final CoverageElement coverage) {
        globalCoverages.add(coverage);
    }

    /**
     * Get all the coverage reports of all the tests.
     *
     * @return coverage reports of all the tests
     */
    public final CoverageReportTestElement getCoverageReportForTests() {
        return coverageTests;
    }

    /**
     * Number of files having a global coverage.
     *
     * @return Number of files having a global coverage
     */
    public final int getNumberOfGlobalCoverageFiles() {
        return globalCoverages.size();
    }

    /**
     * Global Coverage for a given file.
     *
     * @param index
     *            the id of the file
     * @return global coverage of this file
     */
    public final CoverageElement getGlobalCoverageForFile(final int index) {
        return globalCoverages.get(index);
    }

    /**
     * Global Coverage of all the coverage reports.
     *
     * @return global coverage
     * @throws CioException
     *             if global coverage has no meaning
     */
    public final CoverageElement getGlobalCoverage() throws CioException {

        if (globalCoverages.size() == 0) {
            throw new CioException(
                    "No global coverage since no coverage report");
        }

        final CoverageElement global = new CoverageElement();

        for (int i = 0; i < globalCoverages.size(); ++i) {
            global.update(globalCoverages.get(i));
        }

        return global;
    }

}
