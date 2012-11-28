package com.thalesgroup.rtrtcoverage.tracemerge;

import com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition;

/**
 * @author Bastien Reboulet
 */
public class SingleBranchCoverage extends AbstractBranchCoverage {

    /**
     * Default constructor.
     * @param branchDef a branch definition
     */
    public SingleBranchCoverage(final IBranchDefinition branchDef) {
        super(branchDef);
    }

    @Override
    public final boolean isCovered() {
        if (this.getHits() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public final void markHit() {
        this.setHits(this.getHits() + 1);
    }

}
