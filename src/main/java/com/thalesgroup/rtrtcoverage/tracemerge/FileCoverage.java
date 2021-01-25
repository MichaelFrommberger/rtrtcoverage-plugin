package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition;
import com.thalesgroup.rtrtcoverage.fdcreader.MultipleBranchDefinition;
import com.thalesgroup.rtrtcoverage.fdcreader.NodeDefinition;
import com.thalesgroup.rtrtcoverage.fdcreader.SingleBranchDefinition;

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
    private List<NodeCoverage> nodes;

    /**
     * A mapping between the mark+id of a branch and the containing node.
     */
    private Map<String, NodeCoverage> nodeByMarkId;

    /**
     * Default constructor : store all the data from a FileCoverageDefinition.
     *
     * @param newFileCoverageDefinition a fileCoverageDefinition
     */
    public FileCoverage(final FileCoverageDefinition newFileCoverageDefinition) {
        nodeByMarkId = new HashMap<String, NodeCoverage>();
        setFileCoverageDefinition(newFileCoverageDefinition);
        nodes = new ArrayList<NodeCoverage>();
        for (NodeDefinition nodeDef : newFileCoverageDefinition.getNodes()) {
            NodeCoverage nodeCov = new NodeCoverage();
            nodeCov.setNodeName(nodeDef.getNodeName());
            for (IBranchDefinition branchDef : nodeDef.getBranchDefinitions()) {
                IBranchCoverage branchCov = null;
                if (branchDef instanceof SingleBranchDefinition) {
                    branchCov = new SingleBranchCoverage(branchDef);
                } else if (branchDef instanceof MultipleBranchDefinition) {
                    branchCov = new MultipleBranchCoverage(branchDef);
                }
                if (branchCov != null) {
                    nodeCov.addBranchToGlobalCoverage(branchCov);
                }
                nodeByMarkId.put(branchDef.getMarkId(), nodeCov);
            }
            // link multipleCoverage with singles
            for (IBranchCoverage branch : nodeCov.getAllGlobalCoverages()) {
                if (branch instanceof MultipleBranchCoverage) {
                    MultipleBranchDefinition branchDef =
                            (MultipleBranchDefinition) branch.getBranchDefinition();
                    for (String markId : branchDef.getSubBranchMarkIds()) {
                        for (IBranchCoverage branch2: nodeCov.getAllGlobalCoverages()) {
                            if (branch2.getMarkId().equals(markId)) {
                                ((MultipleBranchCoverage) branch).addSubBranch(
                                        (SingleBranchCoverage) branch2);
                            }
                        }
                    }
                }
            }
            nodes.add(nodeCov);
        }
    }

    /**
     * @param testName a test name.
     * @return all the nodes corresponding to the specified test.
     */
    public final Map<String, NodeCoverage> getNodesForTest(final String testName) {
        Map<String, NodeCoverage> nodesForTest = new HashMap<String, NodeCoverage>();
        for (NodeCoverage node : nodes) {
            if (node.isCoveredByTest(testName)) {
                nodesForTest.put(node.getNodeName(), node);
            }
        }
        return nodesForTest;
    }

    /**
     * @return the source file name corresponding to this file coverage.
     */
    public final String getSourceFileName() {
        return fileCoverageDefinition.getSourceName();
    }

    /**
     * @return the source directory of the source file.
     */
    public final String getSourceDir() {
        return fileCoverageDefinition.getSourceDir();
    }

    /**
     * @return the key corresponding to this file.
     */
    public final String getKey() {
        return fileCoverageDefinition.getKey();
    }

    /**
     * @return the crc corresponding to this file.
     */
    public final String getCrc() {
        return fileCoverageDefinition.getCrc();
    }

    /**
     * @return all the nodes contained in this file.
     */
    public final List<NodeCoverage> getNodes() {
        return nodes;
    }

    /**
     * @param markId a concatenation of mark+id of a branch.
     * @return the node corresponding to this mark+id.
     */
    public final NodeCoverage getNodeByMarkId(final String markId) {
        return nodeByMarkId.get(markId);
    }

    /**
     * @return the FileCoverageDefinition of this file coverage.
     */
    public final FileCoverageDefinition getFileCoverageDefinition() {
        return fileCoverageDefinition;
    }


    /** Get the test covering this file.
     * @return names of the test covering this file
     */
    public final Set<String> getTestNames() {
        Set<String> result = new HashSet<String>();
        for (NodeCoverage node: this.getNodes()) {
            result.addAll(node.getTestNames());
        }
        return result;
    }

    /**
     * @param newFileCoverageDefinition a FileCoverageDefinition
     */
    public final void setFileCoverageDefinition(
            final FileCoverageDefinition newFileCoverageDefinition) {
        this.fileCoverageDefinition = newFileCoverageDefinition;
    }

    /**
     * @return the fdc path corresponding to the source file (local to the build dir).
     */
    public final String getFdcPath() {
        return fileCoverageDefinition.getFdcPath();
    }

}
