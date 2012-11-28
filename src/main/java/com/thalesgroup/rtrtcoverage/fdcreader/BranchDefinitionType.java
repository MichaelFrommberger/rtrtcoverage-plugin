package com.thalesgroup.rtrtcoverage.fdcreader;

/**
 * Enumeration allowing to handle branch definition types.
 * @author Bastien Reboulet
 */
public enum BranchDefinitionType {
    /**
     * PR: Functions.
     */
    PROC("Functions"),
    /**
     * BL simple: statement block.
     */
    BLOCK("Statement Blocks"),
    /**
     * BL logical: loops.
     */
    LOOP("Loops"),
    /**
     * AP : calls.
     */
    CALL("Calls"),
    /**
     * Unknown type.
     */
    TODO_CE("CE"),
    /**
     * Unknown type.
     */
    TODO_CC("CC");

    /**
     * @param newType the type of the branch definition
     */
    private BranchDefinitionType(final String newType) {
        this.type = newType;
    }

    /**
     * type of the branch definition.
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
