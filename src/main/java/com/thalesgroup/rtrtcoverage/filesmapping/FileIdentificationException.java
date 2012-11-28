package com.thalesgroup.rtrtcoverage.filesmapping;

/**
 * Dedicated Exception for the identification of files.
 * @author Sebastien Barbier
 * @version 1.0
 */
public class FileIdentificationException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -3730536319612473763L;

    /**
     * Default Constructor.
     * @param message the error message
     */
    public FileIdentificationException(final String message) {
        super("FileIdentificationException: " + message);
    }

}
