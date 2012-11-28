package com.thalesgroup.rtrtcoverage.tracemerge;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition;

/**
 * Data structure for merged branch data (branch definitions + branch coverage).
 *
 * @author Bastien Reboulet
 */
abstract class AbstractBranchCoverage implements IBranchCoverage {

    /**
     * branch definition data.
     */
    private IBranchDefinition branchDefinition;

    /**
     * number of times the branch is hit.
     */
    private int hits = 0;

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getMarkId()
     */
    public String getMarkId() {
        return branchDefinition.getMarkId();
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getMark()
     */
    public String getMark() {
        return branchDefinition.getMark();
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getId()
     */
    public String getId() {
        return branchDefinition.getId();
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getType()
     */
    public BranchType getType() {
        return branchDefinition.getType();
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getLineNumber()
     */
    public int getLineNumber() {
        return branchDefinition.getLineNumber();
    }

    /**
     * @param branchDef the branch definition corresponding to this branch coverage.
     */
    public AbstractBranchCoverage(final IBranchDefinition branchDef) {
        setBranchDefinition(branchDef);
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getBranchDefinition()
     */
    public IBranchDefinition getBranchDefinition() {
        return branchDefinition;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#setBranchDefinition(com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition)
     */
    public void setBranchDefinition(final IBranchDefinition newBranchDefinition) {
        this.branchDefinition = newBranchDefinition;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#getHits()
     */
    public int getHits() {
        return hits;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#setHits(int)
     */
    public void setHits(final int newHits) {
        this.hits = newHits;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#markHit()
     */
    public void markHit() {
        // nop
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage#isCovered()
     */
    public abstract boolean isCovered();

}
