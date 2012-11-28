package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchDefinition;
import com.thalesgroup.rtrtcoverage.fdcreader.BranchDefinitionType;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;

/**
 * Data structure to store merged data from *.TIO and *.FDC.
 *
 * @author Bastien Reboulet
 */
public class FileCoverage {

    /**
     * Data extracted from *.FDC files.
     */
    private FileCoverageDefinition fileCoverageDefinition;

    /**
     * Mapping between (BranchDefintionType + ID) and branch coverages.
     */
    private final Map<String, BranchCoverage> branchCovs;

    /**
     * Default constructor : store all the data from a FileCoverageDefinition.
     *
     * @param newFileCoverageDefinition a fileCoverageDefinition
     */
    public FileCoverage(final FileCoverageDefinition newFileCoverageDefinition) {
        setFileCoverageDefinition(newFileCoverageDefinition);
        branchCovs = new HashMap<String, BranchCoverage>();
        for (final BranchDefinition branchDef : newFileCoverageDefinition
                .getBranches()) {
            final BranchCoverage branchCov = new BranchCoverage();
            branchCov.setBranchDefinition(branchDef);
            branchCovs.put(branchDef.getType() + branchDef.getId(), branchCov);
        }
    }

    /**
     * @return a collection of all the branch coverages
     */
    public final Collection<BranchCoverage> getBranches() {
        return branchCovs.values();
    }

    /**
     * @param type
     *            of the branch definition we have to get corresponding branch
     *            coverage
     * @param id
     *            of the branch definition we have to get corresponding branch
     *            coverage
     * @return the branch coverage corresponding to the type and id of branch
     *         definition corresponding
     */
    public final BranchCoverage getBranch(final BranchDefinitionType type,
            final String id) {
        return branchCovs.get(type + id);
    }

    /**
     * @return the FileCoverageDefinition
     */
    public final FileCoverageDefinition getFileCoverageDefinition() {
        return fileCoverageDefinition;
    }

    /**
     * @param newFileCoverageDefinition a FileCoverageDefinition
     */
    public final void setFileCoverageDefinition(
            final FileCoverageDefinition newFileCoverageDefinition) {
        this.fileCoverageDefinition = newFileCoverageDefinition;
    }

}
