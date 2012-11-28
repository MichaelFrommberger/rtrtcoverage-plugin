package com.thalesgroup.rtrtcoverage.fdcparser;

/**
 * Nodes of the .fdc file encoding the hierarchy of the coverage.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public enum FdcTag {
    /**
     * FIRST TOKEN.
     */
    RIK,
    /**
     * COMMENT TOKEN: no coverage for this line.
     */
    COMMENT,
    /**
     * PRINCIPAL BLOCK.
     */
    NODE,
    /**
     * CONDITIONAL BLOCK.
     */
    BRANCH,
    /**
     * JUMP.
     */
    JUMP,
    /**
     * DECISION.
     */
    DECISION,
    /**
     * LABEL.
     */
    POPUP,
    /**
     * CONSTANT.
     */
    ALT,
    /**
     * RECALL OF LABEL.
     */
    LINK,
    /**
     * RECALL OF BLOCK.
     */
    SHORTCUT
}
