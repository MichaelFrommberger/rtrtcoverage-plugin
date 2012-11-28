package com.thalesgroup.rtrtcoverage.tioreader2;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for a test file extracted from a *.tio file FT->NT-1.
 *
 * @author Bastien Reboulet
 */
public class FileTrace {

    /**
     * "key"(?) parameter of a test file (<=> FT).
     */
    private String key;

    /**
     * "crc"(?) parameter of test file (<=> DC).
     */
    private String crc;

    /**
     * List of all branch traces corresponding to this file.
     */
    private final List<BranchTrace> branchTraces;

    /**
     * Default constructor.
     */
    public FileTrace() {
        branchTraces = new ArrayList<BranchTrace>();
    }

    /**
     * Adds a branch trace to this file.
     *
     * @param trace a branch trace
     */
    public final void addTrace(final BranchTrace trace) {
        branchTraces.add(trace);
    }

    /**
     * @return the list of all the branch traces of this file.
     */
    public final List<BranchTrace> getTraces() {
        return branchTraces;
    }

    /**
     * @return the "key" of this file (FT).
     */
    public final String getKey() {
        return key;
    }

    /**
     * @param newKey
     *            the "key" (FT) of this file.
     */
    public final void setKey(final String newKey) {
        this.key = newKey;
    }

    /**
     * @return the "crc" of this file (DC).
     */
    public final String getCrc() {
        return crc;
    }

    /**
     * @param newCrc
     *            the "crc" of this file (DC).
     */
    public final void setCrc(final String newCrc) {
        this.crc = newCrc;
    }

}
