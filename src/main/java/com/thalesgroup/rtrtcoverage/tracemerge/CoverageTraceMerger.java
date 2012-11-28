package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchDefinitionType;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.tioreader2.BranchTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.BranchTraceType;
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
        final List<FileCoverage> result = new ArrayList<FileCoverage>();
        for (final FileCoverageDefinition fileDef : fileCoverageDefs) {
            final FileCoverage coverage = new FileCoverage(fileDef);
            for (final TestSuiteTrace testSuite : traces) {
                for (final TestTrace testTrace : testSuite.getTestTraces()) {
                    for (final FileTrace fileTrace : testTrace.getFileTraces(
                            fileDef.getKey(), fileDef.getCrc())) {
                        for (final BranchTrace trace : fileTrace.getTraces()) {
                            if (trace.getType() == BranchTraceType.BLOCK_OR_LOOP
                                    || trace.getType() == BranchTraceType.CALL
                                    || trace.getType() == BranchTraceType.PROC) {
                                // special case: if trace type is BL (block or
                                // loop)
                                // first try to get a block type, then a loop
                                // type
                                BranchCoverage cov = null;
                                if (trace.getType().equals(
                                        BranchTraceType.BLOCK_OR_LOOP)) {
                                    cov = coverage.getBranch(
                                            BranchDefinitionType.BLOCK,
                                            trace.getId());
                                    if (cov == null) {
                                        cov = coverage.getBranch(
                                                BranchDefinitionType.LOOP,
                                                trace.getId());
                                    }
                                } else {
                                    cov = coverage.getBranch(
                                            convertBranchType(trace.getType()),
                                            trace.getId());
                                }
                                cov.markHit();
                            }
                        }
                    }
                }
            }
            result.add(coverage);
        }
        return result;
    }

    /**
     * Converts a BranchTraceType into a BranchDefinitionType.
     *
     * @param traceBranchType a BranchTraceType
     * @return BranchDefinitionType
     * @throws CoverageMergeException a CoverageMergeException
     */
    private BranchDefinitionType convertBranchType(
            final BranchTraceType traceBranchType)
                    throws CoverageMergeException {
        switch (traceBranchType) {
        case PROC:
            return BranchDefinitionType.PROC;
        case BLOCK_OR_LOOP:
            return BranchDefinitionType.BLOCK;
        case CALL:
            return BranchDefinitionType.CALL;
        default:
            throw new CoverageMergeException("Unexpected branch trace type: "
                    + traceBranchType);
        }
    }
}
