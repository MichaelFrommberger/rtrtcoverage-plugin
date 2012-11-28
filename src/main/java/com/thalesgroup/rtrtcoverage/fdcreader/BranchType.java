package com.thalesgroup.rtrtcoverage.fdcreader;

/**
 * Enumeration allowing to handle branch definition types.
 * @author Bastien Reboulet
 */
public enum BranchType {
    /**
     * Functions.
     * SUM=0
     */
    TP_FUNCTIONS("Functions"),
    /**
     * Exits.
     * SUM=1
     */
    TP_EXITS("Exits"),
    /**
     * Calls.
     * SUM=10
     */
    TA_CALLS("Calls"),
    /**
     * Statement Blocks.
     * SUM=20
     */
    TB_STATEMENTS("Statement Blocks"),
    /**
     * Implicit Blocks.
     * SUM=21
     */
    TB_IMPLICIT("Implicit Blocks"),
    /**
     * Decisions.
     * ( = Statement Blocks + Implicit BLocks)
     */
    TB_DECISIONS("Decisions"),
    /**
     * Loops.
     * SUM=22
     */
    TB_LOOPS("Loops"),
    /**
     * Basic Conditions.
     * SUM=30
     */
    TE_BASICS("Basic Conditions"),
    /**
     * Modified Conditions.
     * SUM=31
     */
    TE_MODIFIEDS("Modified Conditions"),
    /**
     * Multiple Conditions.
     * SUM=32
     */
    TE_MULTIPLES("Multiple Conditions");

    /**
     * @param newType the type of the branch definition
     */
    private BranchType(final String newType) {
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
