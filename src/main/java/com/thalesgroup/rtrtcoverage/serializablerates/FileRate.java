package com.thalesgroup.rtrtcoverage.serializablerates;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing rates data for a file.
 * @author Babou
 */
public class FileRate extends SourceRate {

    /**
     * The serial ID.
     */
    private static final long serialVersionUID = 6513293654981058748L;

    /**
     * The test rates contained by this file.
     */
    private List<TestRate> testRates;

    /**
     * Default Constructor.
     */
    public FileRate() {
        testRates = new ArrayList<TestRate>();
    }

    /**
     * @return all the test rates included in this file.
     */
    public final List<TestRate> getTestRates() {
        return testRates;
    }
}
