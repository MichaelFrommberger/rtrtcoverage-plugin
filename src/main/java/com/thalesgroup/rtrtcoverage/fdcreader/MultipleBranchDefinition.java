package com.thalesgroup.rtrtcoverage.fdcreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class allowing to handle Modified Conditions type.
 * Which necessitate a list of other SingleBranchDefinition
 * in order to process coverage rates.
 * @author Bastien Reboulet
 */
public class MultipleBranchDefinition extends AbstractBranchDefinition {

    /**
     * A list of mark+id of single branches associated to this multiple branch.
     */
    private List<String> subBranchMarkIds;

    /**
     * A list of single branches associated to this multiple branch.
     */
    private List<IBranchDefinition> subBranches;

    /**
     * Default constructor.
     */
    public MultipleBranchDefinition() {
        subBranches = new ArrayList<IBranchDefinition>();
        subBranchMarkIds = new ArrayList<String>();
    }

    /**
     * @return the list of all the mark+id of the single branches corresponding to this multiple branch.
     */
    public final List<String> getSubBranchMarkIds() {
        return subBranchMarkIds;
    }

    /**
     * @return the list of all the single branches corresponding to this multiple branch.
     */
    public final List<IBranchDefinition> getSubBranches() {
        return subBranches;
    }

    /**
     * Adds a branch only if it's not already in the list.
     * @param branch a single branch corresponding to this multiple branch.
     */
    public final void addSubBranch(final IBranchDefinition branch) {
        if (!subBranches.contains(branch)) {
            subBranches.add(branch);
        }
    }
}
