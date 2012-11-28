package com.thalesgroup.rtrtcoverage.fdcreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class allowing to store data of a node from an FDC file.
 * @author Bastien Reboulet
 */
public class NodeDefinition {

    /**
     * A list of all the branch definitions corresponding to this node.
     */
    private List<IBranchDefinition> branchDefs;

    /**
     * The node name.
     */
    private String nodeName;

    /**
     * Default constructor.
     */
    public NodeDefinition() {
        branchDefs = new ArrayList<IBranchDefinition>();
    }

    /**
     * @param branch a branch defintion.
     */
    public final void addBranchDefinition(final IBranchDefinition branch) {
        branchDefs.add(branch);
    }

    /**
     * @return a list of all the branch definitions corresponding to this node.
     */
    public final List<IBranchDefinition> getBranchDefinitions() {
        return branchDefs;
    }

    /**
     * @return the name of this node.
     */
    public final String getNodeName() {
        return nodeName;
    }

    /**
     * @param newNodeName the name of this node.
     */
    public final void setNodeName(final String newNodeName) {
        this.nodeName = newNodeName;
    }
}
