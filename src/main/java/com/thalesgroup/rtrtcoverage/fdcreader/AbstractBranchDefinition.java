package com.thalesgroup.rtrtcoverage.fdcreader;


/**
 * Data structure for a branch extracted from a *.fdc file.
 * @author Bastien Reboulet
 */
abstract class AbstractBranchDefinition implements IBranchDefinition {

    /**
     * Type of the branch.
     */
    private BranchType type;

    /**
     * Mark of the branch ("TE", "TB", etc).
     */
    private String mark;

    /**
     * ID of the branch.
     */
    private String id;

    /**
     * Line number of the branch.
     */
    private int lineNumber = 0;

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#getMarkId()
     */
    public String getMarkId() {
        return mark + id;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#getType()
     */
    public final BranchType getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#setType(com.thalesgroup.rtrtcoverage.fdcreader.BranchType)
     */
    public final void setType(final BranchType newType) {
        this.type = newType;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#getMark()
     */
    public final String getMark() {
        return mark;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#setMark(java.lang.String)
     */
    public final void setMark(final String newMark) {
        this.mark = newMark;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#getId()
     */
    public final String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#setId(java.lang.String)
     */
    public final void setId(final String newId) {
        this.id = newId;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#getLineNumber()
     */
    public final int getLineNumber() {
        return lineNumber;
    }

    /* (non-Javadoc)
     * @see com.thalesgroup.rtrtcoverage.fdcreader.IBranchDefinition#setLineNumber(int)
     */
    public final void setLineNumber(final int newLineNumber) {
        this.lineNumber = newLineNumber;
    }
}
