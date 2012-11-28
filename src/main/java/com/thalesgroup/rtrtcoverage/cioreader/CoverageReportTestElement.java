package com.thalesgroup.rtrtcoverage.cioreader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Set of all the coverage element by test.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CoverageReportTestElement {

    /**
     * Set of the coverage. Association between data and name of the file.
     * TreeMap is used to correctly sorted the tests.
     */
    private final TreeMap<TestString, ArrayList<CoverageElement>> coverage;

    /**
     * Intern iterator to search into the map.
     */
    private Iterator<TestString> itNameFile;
    /**
     * Current name of the file.
     */
    private TestString currentName;

    /**
     * Default constructor.
     */
    public CoverageReportTestElement() {
        coverage = new TreeMap<TestString, ArrayList<CoverageElement>>();
    }

    /**
     * Add a coverage element associated to the name file.
     *
     * @param name
     *            name of the file
     * @param coverageElement
     *            associated coverage
     */
    public final void addCoverage(final String name,
            final CoverageElement coverageElement) {
        final TestString nameTest = new TestString();
        nameTest.setName(name);
        ArrayList<CoverageElement> element = coverage.get(nameTest);
        if (element == null) {
            element = new ArrayList<CoverageElement>();
        }
        element.add(coverageElement);
        coverage.put(nameTest, element);
    }

    /**
     * Initialize search.
     */
    public final void begin() {
        itNameFile = coverage.keySet().iterator();
    }

    /**
     * One step more.
     *
     * @return if another element in the data
     */
    public final boolean hasNextTest() {
        return itNameFile.hasNext();
    }

    /**
     * name of the next test. hasNext must be done previously.
     *
     * @return name of the next test
     * @see hasNext
     */
    public final String getNextNameTest() {
        currentName = itNameFile.next();
        return currentName.getName();
    }

    /**
     * Number of coverage file in the next test.
     *
     * @return number of coverage file in the next test
     */
    public final int getNumberOfFilesInNextTest() {
        return coverage.get(currentName).size();
    }

    /**
     * Coverage report for the next test.
     *
     * @param index
     *            the current file into the next test
     * @return coverage report of the next test
     */
    public final CoverageElement getCoverageElementInNextTest(final int index) {
        return coverage.get(currentName).get(index);
    }
}
