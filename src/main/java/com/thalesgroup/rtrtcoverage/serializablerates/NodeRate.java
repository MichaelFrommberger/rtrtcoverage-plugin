package com.thalesgroup.rtrtcoverage.serializablerates;

/**
 * Class containing rates for one node.
 * @author Bastien Reboulet
 */
public class NodeRate extends CoverageElementRate {

    /**
     * The Serial ID.
     */
    private static final long serialVersionUID = -3886278987834744305L;

    /**
     * The name of this node.
     */
    private String nodeName;

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
