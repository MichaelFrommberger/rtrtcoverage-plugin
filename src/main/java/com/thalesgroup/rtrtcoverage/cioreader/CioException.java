package com.thalesgroup.rtrtcoverage.cioreader;

/**
 * Dedicated exception for errors during reading .cio files.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CioException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -740402119622829362L;

    /**
     * Default Constructor.
     *
     * @param message
     *            the error message
     */
    public CioException(final String message) {
        super("CioException: " + message);
    }

}
