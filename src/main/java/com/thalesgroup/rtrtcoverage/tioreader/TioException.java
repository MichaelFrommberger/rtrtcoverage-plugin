package com.thalesgroup.rtrtcoverage.tioreader;

/**
 * TioException: Exception specialized for .tio reader.
 */
public class TioException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 8921704487706940651L;

    /**
     * Constructor with string message.
     *
     * @param message
     *            error message
     */
    public TioException(final String message) {
        super("Error during reading .tio file: " + message);
    }

}
