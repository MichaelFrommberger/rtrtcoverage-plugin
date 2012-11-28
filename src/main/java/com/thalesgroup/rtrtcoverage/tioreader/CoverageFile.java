package com.thalesgroup.rtrtcoverage.tioreader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Coverage for a file.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CoverageFile {

    /**
     * Name of the file.
     */
    private String name;

    /**
     * Coverage of the file. Set is used to avoid redundancy (unitary tests with
     * same coverage). elements are {@see CoverageLine} information.
     */
    private final Set<CoverageLine> coverage;

    /**
     * Number of times the line is hit.
     */
    private final Map<CoverageLine, Integer> hit;

    /**
     * Default Constructor.
     */
    public CoverageFile() {
        name = "";
        coverage = new HashSet<CoverageLine>();
        hit = new HashMap<CoverageLine, Integer>();
    }

    /**
     * The name of the file.
     *
     * @return the name of the file
     */
    public final String getName() {
        return name;
    }

    /**
     * The name of the current file.
     *
     * @param sName
     *            the name of the file
     */
    public final void setName(final String sName) {
        this.name = sName;
    }

    /**
     * Get the coverage associated to one file.
     *
     * @return coverage of the file : {@see Set} of {@see CoverageLine}
     */
    public final Set<CoverageLine> getCoverage() {
        return coverage;
    }

    /**
     * Return the number of hits for a coverage line.
     *
     * @param line
     *            the coverage line
     * @return 0 if not hit, at least 1 otherwise
     */
    public final int getHit(final CoverageLine line) {
        final Integer hits = hit.get(line);
        if (hits == null) {
            return 0;
        } else {
            return hits.intValue();
        }
    }

    /**
     * Add the coverage associated to one file and update hits of the line.
     *
     * @param lineCoverage
     *            coverage of a line of the file
     */
    public final void add(final CoverageLine lineCoverage) {
        final boolean notAlreadyIn = coverage.add(lineCoverage);
        if (notAlreadyIn) {
            hit.put(lineCoverage, 1);
        } else {
            hit.put(lineCoverage, hit.get(lineCoverage) + 1);
        }
    }
}
