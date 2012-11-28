package com.thalesgroup.rtrtcoverage.tracemerge;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition;

/**
 * Interface allowing to handle branch coverages.
 * @author Bastien Reboulet
 */
public interface IBranchCoverage {

    /**
     * @return a concatenation of mark+id.
     */
    String getMarkId();

    /**
     * @return the mark of the branch.
     */
    String getMark();

    /**
     * @return the id of the branch.
     */
    String getId();

    /**
     * @return the type of the branch.
     */
    BranchType getType();

    /**
     * @return the line number of this branch.
     */
    int getLineNumber();

    /**
     * @return the branch definition of this branch.
     */
    IBranchDefinition getBranchDefinition();

    /**
     * @param newBranchDefinition a branch definition.
     */
    void setBranchDefinition(IBranchDefinition newBranchDefinition);

    /**
     * @return the hits number of this branch.
     */
    int getHits();

    /**
     * @param newHits the number of time the branch has been hit.
     */
    void setHits(final int newHits);

    /**
     * increment hits counter of one.
     */
    void markHit();

    /**
     * @return true if the branch has been covered at least one time, else return false.
     */
    boolean isCovered();
}
