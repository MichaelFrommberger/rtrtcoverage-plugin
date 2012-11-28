package com.thalesgroup.rtrtcoverage.tioreader2;

/**
 * Enumeration allowing to handle branch trace types.
 * @author Bastien Reboulet
 */
public enum BranchTraceType {
    /**
     * TP: Functions.
     */
    PROC("TP"),
    /**
     * TB simple: statement block.
     */
    BLOCK_OR_LOOP("TB"),
    /**
     * TA : calls.
     */
    CALL("TA"),
    /**
     * Unknown type.
     */
    TODO_TE("TE");

    /**
     * @param newType the type of the branch trace
     */
    private BranchTraceType(final String newType) {
        this.type = newType;
    }

    /**
     * the type of the branch trace.
     */
    private final String type;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return type;
    }
}
