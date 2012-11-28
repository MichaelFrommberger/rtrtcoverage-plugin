package com.thalesgroup.rtrtcoverage.cioreader;

/**
 * Sort the report ratio according to the ordering of RTRT. Used for array
 * indexing.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public enum ReportTag {
    /**
     * functions and exits.
     */
    FUNCTION,
    /**
     * calls.
     */
    CALL,
    /**
     * statement blocks.
     */
    STATEMENTBLOCK,
    /**
     * implicit blocks.
     */
    IMPLICITBLOCK,
    /**
     * decisions.
     */
    DECISION,
    /**
     * loops.
     */
    LOOP,
    /**
     * basic conditions.
     */
    BASICCONDITION,
    /**
     * modified conditions.
     */
    MODIFIEDCONDITION,
    /**
     * multiple conditions.
     */
    MULTIPLECONDITION

}
