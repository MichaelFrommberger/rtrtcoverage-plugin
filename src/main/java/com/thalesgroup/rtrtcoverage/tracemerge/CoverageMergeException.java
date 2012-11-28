package com.thalesgroup.rtrtcoverage.tracemerge;

/**
 * CoverageMergeException: Exception specialized for coverage trace merger.
 * @author Bastien Reboulet
 */
public class CoverageMergeException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -2608085282955022370L;

    /**
     * Constructor with string message.
     * @param message
     *            error message
     */
    public CoverageMergeException(final String message) {
        super(message);
    }
}
