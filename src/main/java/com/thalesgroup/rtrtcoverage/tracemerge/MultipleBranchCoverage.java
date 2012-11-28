package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition;

/**
 * Class allowing to handle Modified Conditions type.
 * Which necessitate a list of other SingleBranchDefinition
 * in order to process coverage rates.
 * @author Bastien Reboulet
 */
public class MultipleBranchCoverage extends AbstractBranchCoverage {

    /**
     * A list of single branches associated to this multiple branch.
     */
    private List<SingleBranchCoverage> subBranches;

    /**
     * Default constructor.
     * @param branchDef a branch definition.
     */
    public MultipleBranchCoverage(final IBranchDefinition branchDef) {
        super(branchDef);
        subBranches = new ArrayList<SingleBranchCoverage>();
    }

    /**
     * @return the list of all the single branches corresponding to this multiple branch.
     */
    public final List<SingleBranchCoverage> getSubBranches() {
        return subBranches;
    }

    /**
     * @param subBranch a single branch corresponding to this multiple branch.
     */
    public final void addSubBranch(final SingleBranchCoverage subBranch) {
        subBranches.add(subBranch);
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.tracemerge.AbstractBranchCoverage#isCovered()
     */
    @Override
    public final boolean isCovered() {
        boolean covered = true;
        for (SingleBranchCoverage branch : subBranches) {
            covered &= branch.isCovered();
        }
        return covered;
    }
}
