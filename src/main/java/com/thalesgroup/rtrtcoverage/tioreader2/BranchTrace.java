package com.thalesgroup.rtrtcoverage.tioreader2;

/**
 * Data structure for a branch extracted from a *.tio file.
 *
 * @author Bastien Reboulet
 */
public class BranchTrace {

    /**
     * Type of the branch.
     */
    private BranchTraceType type;

    /**
     * ID number of the branch.
     */
    private String id;

    /**
     * @return the type of the branch.
     */
    public final BranchTraceType getType() {
        return type;
    }

    /**
     * @param newType
     *            of the branch
     */
    public final void setType(final BranchTraceType newType) {
        this.type = newType;
    }

    /**
     * @return the ID of the branch
     */
    public final String getId() {
        return id;
    }

    /**
     * @param newId
     *            of the branch
     */
    public final void setId(final String newId) {
        this.id = newId;
    }

}
