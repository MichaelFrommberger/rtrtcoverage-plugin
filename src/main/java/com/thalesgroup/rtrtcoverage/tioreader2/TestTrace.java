package com.thalesgroup.rtrtcoverage.tioreader2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure for a test file extracted from a *.tio file NT->NT-1.
 *
 * @author Bastien Reboulet
 */
public class TestTrace {

    /**
     *
     */
    private Map<String, List<FileTrace>> fileTraces;

    /**
     * Name of the test.
     */
    private String name;

    /**
     * Default Constructor.
     */
    public TestTrace() {
        fileTraces = new HashMap<String, List<FileTrace>>();
    }

    /**
     * Adds a file trace to this test.
     *
     * @param fileTrace a file trace
     */
    public final void addFileTrace(final FileTrace fileTrace) {
        if (fileTraces.get(fileTrace.getKey() + fileTrace.getCrc()) == null) {
            List<FileTrace> traces = new ArrayList<FileTrace>();
            traces.add(fileTrace);
            fileTraces.put(fileTrace.getKey() + fileTrace.getCrc(), traces);
        } else {
            fileTraces.get(fileTrace.getKey() + fileTrace.getCrc()).add(fileTrace);
        }
    }

    /**
     * @param key
     *            <=> FT
     * @param crc
     *            <=> DC
     * @return the list of all the file traces matching these key and crc
     */
    public final List<FileTrace> getFileTraces(final String key, final String crc) {
        List<FileTrace> result = fileTraces.get(key + crc);
        if (result == null) {
            return new ArrayList<FileTrace>();
        }
        return result;
    }

    /**
     * @return the name of the test
     */
    public final String getName() {
        return name;
    }

    /**
     * @param newName
     *            the name of the test
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

}
