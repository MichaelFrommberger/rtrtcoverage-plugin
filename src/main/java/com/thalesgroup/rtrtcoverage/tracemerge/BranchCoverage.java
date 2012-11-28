package com.thalesgroup.rtrtcoverage.tracemerge;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchDefinition;

/**
 * Data structure for merged branch data (branch definitions + branch coverage).
 *
 * @author Bastien Reboulet
 */
public class BranchCoverage {

    /**
     * branch definition data.
     */
    private BranchDefinition branchDefinition;

    /**
     * number of times the branch is hit.
     */
    private int hits = 0;

    /**
     * @return the branch definition data.
     */
    public final BranchDefinition getBranchDefinition() {
        return branchDefinition;
    }

    /**
     * @param newBranchDefinition
     *            the branch definition data
     */
    public final void setBranchDefinition(final BranchDefinition newBranchDefinition) {
        this.branchDefinition = newBranchDefinition;
    }

    /**
     * @return the number of times the branch is hit.
     */
    public final int getHits() {
        return hits;
    }

    /**
     * Increment the number of hits each time this branch is hit.
     */
    public final void markHit() {
        this.hits++;
    }

    /**
     * @return true if this branch was hit at least 1 time else return false
     */
    public final boolean isCovered() {
        if (this.hits > 0) {
            return true;
        }
        return false;
    }

}
