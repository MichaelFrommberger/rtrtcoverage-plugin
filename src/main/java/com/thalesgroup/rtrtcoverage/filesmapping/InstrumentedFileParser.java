package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class allowing to get key/crc/source file name association by reading instrumented file.
 * @author Bastien Reboulet
 */
public final class InstrumentedFileParser {

    /**
     * Default constructor.
     */
    private InstrumentedFileParser() {
    }

    /**
     * @param augFile an instrumented file
     * @param augPattern the ant-style pattern of the instrumented file (allowing to replace the extension).
     * @return a FileIdentity filled with instrumented file info
     * @throws FileIdentificationException a FileIdentificationException
     * @throws IOException a IOException
     */
    public static FileIdentity getFileIdentity(final FilePath augFile, final String augPattern)
            throws FileIdentificationException, IOException {

        InputStream augStream = augFile.read();
        InputStreamReader augReader = null;
        BufferedReader augBuffer = null;

        augReader = new InputStreamReader(augStream);
        augBuffer = new BufferedReader(augReader);

        String line;
        String key = null;
        String crc = null;
        boolean isAdaFile = false;

        try {
            while ((line = augBuffer.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("_ATC_DECLARE(")) {
                	// C file:
                	// _ATC_DECLARE(1,0x13324134UL,0x175D0F6CUL,...
                    final int maxData = 4;
                    String[] data = new String[maxData];
                    data = line.split(",", maxData);
                    key = giveCleanAddress(data[1]);
                    crc = giveCleanAddress(data[2]);
                    break;
                } else if (line.startsWith("ATW.COVERAGE.LINK_DESC")) {
                	// Ada file:
                	// ATW.COVERAGE.LINK_DESC( TRACES'ACCESS, "  d0877094", "  a667b2c3" );
                	isAdaFile = true;
                    final int maxData = 4;
                    String[] data = new String[maxData];
                    data = line.split(",", maxData);
                    key = new StringBuffer(giveCleanAddress(data[1])).reverse().toString();
                    crc = new StringBuffer(giveCleanAddress(data[2])).reverse().toString();
                    break;
                }
            }
        } catch (final IOException e) {
            throw new FileIdentificationException(
                    "Error during reading the file.");
        } finally {
            try {
                augBuffer.close();
                augReader.close();
            } catch (final IOException e) {
                throw new FileIdentificationException(
                        "Error during closing the file.");
            }
        }

        if ((key == null && crc != null)
                || (key != null && crc == null)) {
            throw new FileIdentificationException(
                    "The file does not contain identification : " + key
                    + ", " + crc + ".");
        }

        if (key != null && crc != null) {
            final FileIdentity fi = new FileIdentity(key, crc, getCleanSourceName(augFile, augPattern, isAdaFile));
            return fi;
        }
        return null;
    }

    /**
     * @param augFile an instrumented file
     * @param augPattern an ant-style pattern for an instrumented file
     * @return the source name (including the extension)
     */
    private static String getCleanSourceName(final FilePath augFile, final String augPattern, final boolean isAdaFile) {
    	String sourceFileName = augFile.getName().toUpperCase();
    	// strip the instrumented prefix (for Ada files) and suffix (for both C and Ada files)
    	String prefixAda = "ATC_";
    	if (isAdaFile && sourceFileName.startsWith(prefixAda)) {
    		sourceFileName = sourceFileName.substring(prefixAda.length());
    	}
    	String suffix = augPattern.substring(augPattern.lastIndexOf("*") + 1, augPattern.length()).toUpperCase();
    	sourceFileName = sourceFileName.replace(suffix, "");
    	// Add the file type, depending on the language
    	if (isAdaFile) {
    		sourceFileName = sourceFileName +  ".ADB";
    	} else {
    		sourceFileName = sourceFileName +  ".C";
    	}
        return sourceFileName;
    }

    /**
     * Clean an address to ease comparisons with tokens.
     *
     * @param bigAddress
     *            the initial address with pattern 0x([0-9,A-F][0-9,A-F]^4)UL
     * @return the address to compare with token [0-9,A-F][0-9,A-F]^4 and
     *         bit-wise is reverse
     */
    private static String giveCleanAddress(final String bigAddress) {
        String output = bigAddress.replace("0x", "");
        output = output.replace("UL", "");
        output = output.replace(");", "");
        output = output.replace("\"", "");
        output = output.trim();
        return output.toLowerCase();
    }

}
