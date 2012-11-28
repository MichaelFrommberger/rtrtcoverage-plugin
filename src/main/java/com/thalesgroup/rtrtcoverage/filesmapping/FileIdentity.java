package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure containing a source file name, a key/crc pair, and a list of associated *.tio files.
 * @author Bastien Reboulet
 */
public class FileIdentity {

    /**
     * Key corresponding to one source file.
     */
    private String key;

    /**
     * Crc corresponding to one source file.
     */
    private String crc;

    /**
     * The source file name.
     */
    private String sourceFileName;

    /**
     * The list of all the tios associed with this source file.
     */
    private List<FilePath> associedTios;

    /**
     * Default constructor.
     * @param newKey the key
     * @param newCrc the crc
     * @param newSourceFileName the surce file name
     */
    public FileIdentity(final String newKey, final String newCrc, final String newSourceFileName) {
        associedTios = new ArrayList<FilePath>();
        setKey(newKey);
        setCrc(newCrc);
        setSourceFileName(newSourceFileName);
    }

    /**
     * @return the key
     */
    public final String getKey() {
        return key;
    }

    /**
     * @param newKey the key
     */
    public final void setKey(final String newKey) {
        this.key = newKey;
    }

    /**
     * @return the crc
     */
    public final String getCrc() {
        return crc;
    }

    /**
     * @param newCrc the crc
     */
    public final void setCrc(final String newCrc) {
        this.crc = newCrc;
    }

    /**
     * @return the source file name
     */
    public final String getSourceFileName() {
        return sourceFileName;
    }

    /**
     * @param newSourceFileName the source file name
     */
    public final void setSourceFileName(final String newSourceFileName) {
        this.sourceFileName = newSourceFileName;
    }

    /**
     * @return the list of all the tios associed with this source file
     */
    public final List<FilePath> getAssociedTios() {
        return associedTios;
    }

    /**
     * @param newAssociedTios the list of associed tios
     */
    public final void setAssociedTios(final List<FilePath> newAssociedTios) {
        this.associedTios = newAssociedTios;
    }
}
