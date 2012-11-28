package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.tioreader2.BranchTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.FileTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TestTrace;

/**
 * Class providing merge function between branch definitions and coverage traces.
 *
 * @author Bastien Reboulet
 */
public class CoverageTraceMerger {

    /**
     * Merge data between branch definitions and coverage traces.
     * @param fileCoverageDefs
     *            a list of file coverage definition
     * @param traces
     *            a list of coverage traces
     * @return a list of merged branch coverage information for source files
     * @throws CoverageMergeException a CoverageMergeException
     */
    public final List<FileCoverage> merge(
            final List<FileCoverageDefinition> fileCoverageDefs,
            final List<TestSuiteTrace> traces) throws CoverageMergeException {
        List<FileCoverage> mergedResult = new ArrayList<FileCoverage>();

        for (FileCoverageDefinition fileDef : fileCoverageDefs) {
            FileCoverage fileCov = new FileCoverage(fileDef);
            // we want to get all the traces relative to this file
            // in order to mark covered branches
            for (TestSuiteTrace testSuite : traces) {
                for (TestTrace testTrace : testSuite.getTestTraces()) {
                    for (FileTrace fileTrace : testTrace.getFileTraces(fileDef.getKey(), fileDef.getCrc())) {
                        // here we have all the traces relative to this file : fileTrace
                        for (BranchTrace branchTrace : fileTrace.getTraces()) {
                            // NodeCoverage for which trace apply has to be determined
                            NodeCoverage nodeCov = fileCov.getNodeByMarkId(branchTrace.getMarkId());
                            // if the test is new for this node
                            // initialize all its branchCov (by copying globalBranches)
                            if (nodeCov != null) {
                                nodeCov.initTestIfNew(testTrace.getName());
                                nodeCov.getGlobalBranchCoverage(branchTrace.getMarkId()).markHit();
                                nodeCov.getTestBranchCoverage(
                                        testTrace.getName(),
                                        branchTrace.getMarkId()).markHit();
                            }
                        }
                    }
                }
            }
            mergedResult.add(fileCov);
        }


        return mergedResult;
    }
}
