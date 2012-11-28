package com.thalesgroup.rtrtcoverage.fdcparser;

/**
 * Dedicated exception for errors during reading .fdc files.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class FdcException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -1215513861348258623L;

    /**
     * Default Constructor.
     *
     * @param message
     *            description of the error
     */
    public FdcException(final String message) {
        super("Exception into a .fdc file:" + message);
    }

}
