package com.thalesgroup.rtrtcoverage.tioreader2;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for a test file extracted from a *.tio file NT->NT-1.
 *
 * @author Bastien Reboulet
 */
public class TestTrace {

    /**
     * Name of the test.
     */
    private String name;

    /**
     * List of all the file traces into this test.
     */
    private final List<FileTrace> fileTraces;

    /**
     * Default Constructor.
     */
    public TestTrace() {
        fileTraces = new ArrayList<FileTrace>();
    }

    /**
     * @return all the file traces
     */
    public final List<FileTrace> getAllFileTraces() {
        return fileTraces;
    }

    /**
     * Adds a file trace to this test.
     *
     * @param fileTrace
     *            a file trace
     */
    public final void addFileTrace(final FileTrace fileTrace) {
        fileTraces.add(fileTrace);
    }

    /**
     * @param key
     *            <=> FT
     * @param crc
     *            <=> DC
     * @return the list of all the file traces matching these key and crc
     */
    public final List<FileTrace> getFileTraces(final String key,
            final String crc) {
        final List<FileTrace> traces = new ArrayList<FileTrace>();
        for (final FileTrace ft : fileTraces) {
            if ((ft.getKey().equalsIgnoreCase(key))
                    && (ft.getCrc().equalsIgnoreCase(crc))) {
                traces.add(ft);
            }
        }
        return traces;
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
