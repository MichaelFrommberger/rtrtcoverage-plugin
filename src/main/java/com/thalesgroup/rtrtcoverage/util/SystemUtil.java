package com.thalesgroup.rtrtcoverage.util;

/**
 * Transformation on string for os compatibility.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public final class SystemUtil {

    /**
     * Private Default Constructor.
     */
    private SystemUtil() {
    }

    /**
     * Create a good URI for any OS.
     *
     * @param filename
     *            the name of the file to transform
     * @return an absolute URI
     */
    public static String createGoodURI(final String filename) {

        String pathURI = filename;

        pathURI = pathURI.replace("\\", "/");

        // path is not an URI
        if (!filename.startsWith("file:/")) {
            if (filename.startsWith("/")) {
                // Unix
                pathURI = "file:" + pathURI;
            } else {
                // Windows
                pathURI = "file:/" + pathURI;
            }
        }

        return pathURI;

    }

}
