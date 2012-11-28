package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;

/**
 * Class allowing to manage a list of file coverages.
 * @author Bastien Reboulet
 */
public class GlobalCoverage {

    /**
     * The list of all the file coverages for one job.
     */
    private List<FileCoverage> fileCovs;

    /**
     * @param newFileCovs the list of all the file coverages for one job
     */
    public GlobalCoverage(final List<FileCoverage> newFileCovs) {
        this.fileCovs = newFileCovs;
    }

    /**
     * @return a map of the global rates by branch type.
     */
    public final Map<BranchType, CoverageRate> getGlobalRates() {
        Map<BranchType, CoverageRate> globalRates = new HashMap<BranchType, CoverageRate>();
        for (FileCoverage fileCov : fileCovs) {
            for (NodeCoverage nodeCov : fileCov.getNodes()) {
                for (BranchType type : BranchType.values()) {
                    if (!globalRates.containsKey(type)) {
                        globalRates.put(type, nodeCov.getGlobalRate(type));
                    } else {
                        globalRates.get(type).incrementCovered(
                                nodeCov.getGlobalRate(type).getCoveredNumber());
                        globalRates.get(type).incrementTotal(
                                nodeCov.getGlobalRate(type).getTotal());
                    }
                }
            }
        }
        return globalRates;
    }

    /**
     * @return a treemap with file coverages sorted by source file name.
     */
    public final Map<String, FileCoverage> getSortedFileCoverages() {
        Map<String, FileCoverage> sortedFileCovs = new TreeMap<String, FileCoverage>();
        for (FileCoverage fileCov : fileCovs) {
            sortedFileCovs.put(fileCov.getSourceFileName(), fileCov);
        }
        return sortedFileCovs;
    }

}
