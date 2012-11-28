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
    private String mark;

    /**
     * ID number of the branch.
     */
    private String id;

    /**
     * @return a concatenation of mark+id.
     */
    public final String getMarkId() {
        return mark + id;
    }

    /**
     * @return the type of the branch.
     */
    public final String getMark() {
        return mark;
    }

    /**
     * @param newMark
     *            of the branch
     */
    public final void setMark(final String newMark) {
        this.mark = newMark;
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
