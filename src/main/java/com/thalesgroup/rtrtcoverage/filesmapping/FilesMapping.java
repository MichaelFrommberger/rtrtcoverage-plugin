package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.thalesgroup.rtrtcoverage.tioreader2.FileTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TestTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioException;

/**
 * Class allowing mapping between tio and fdc files.
 * @author Bastien Reboulet
 */
public class FilesMapping {

    /**
     * The map. Keys are source file names (including the extension).
     */
    private HashMap<String, FileIdentity> mapping;

    /**
     * Default constructor.
     */
    public FilesMapping() {
        this.mapping = new HashMap<String, FileIdentity>();
    }

    /**
     * Build the mapping.
     * @param tioDir directory where all the tio files are located (generaly in the build dir)
     * @param workspace the workspace where we can find instrumented files
     * @param augPattern the ant-style pattern allowing to find instrumented files (relative to the workspace)
     * @param tsts the list of test suite traces
     * @throws IOException an IOException
     * @throws InterruptedException an InterruptedException
     * @throws FileIdentificationException a FileIdentificationException
     * @throws TioException a TioException
     */
    public final void build(final List<TestSuiteTrace> tsts,
            final FilePath workspace,
            final String augPattern,
            final FilePath tioDir)
                    throws IOException,
                    InterruptedException,
                    FileIdentificationException,
                    TioException {

        FilePath[] augFiles = workspace.list(augPattern);

        for (FilePath augFile : augFiles) {
            FileIdentity fi = InstrumentedFileParser.getFileIdentity(augFile, augPattern);
            if (fi != null) {
                for (TestSuiteTrace testSuiteTrace : tsts) {
                    if (isTioFileContains(testSuiteTrace, fi.getKey(), fi.getCrc())) {
                        fi.getAssociedTios().add(new FilePath(tioDir, testSuiteTrace.getName()));
                    }
                }
                mapping.put(fi.getSourceFileName(), fi);
            }
        }
    }

    /**
     * @param tst a test suite trace
     * @param key a key
     * @param crc a crc
     * @return true if the input tio file contains one or more occurences of key/crc pair, else return false
     * @throws IOException an IOException
     * @throws TioException a TioException
     */
    private boolean isTioFileContains(final TestSuiteTrace tst,
            final String key,
            final String crc)
                    throws IOException, TioException {
        for (TestTrace tt : tst.getTestTraces()) {
            List<FileTrace> fileTraces = tt.getFileTraces(key, crc);
            if ((fileTraces != null) && (fileTraces.size() != 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param sourceFileName a source file name
     * @return the FileIdentity corresponding to the source file name
     */
    public final FileIdentity get(final String sourceFileName) {
        return mapping.get(sourceFileName);
    }

    /**
     * @return a list of file identities
     */
    public final List<FileIdentity> getFileIdentities() {
        return new ArrayList<FileIdentity>(mapping.values());
    }

    /**
     * @param fileIdentities a list of FileIdentity
     */
    public final void setMapping(final List<FileIdentity> fileIdentities) {
        for (FileIdentity fi : fileIdentities) {
            mapping.put(fi.getSourceFileName(), fi);
        }
    }

    /**
     * @return the map
     */
    public final HashMap<String, FileIdentity> getMap() {
        return mapping;
    }

    /**
     * @param fm a FilesMapping
     * @return true if this FilesMapping contains the same mapping than fm, else return false
     */
    public final boolean equals(final FilesMapping fm) {
        for (String mapKey : fm.getMap().keySet()) {
            if (!this.mapping.get(mapKey).getKey().contentEquals(fm.get(mapKey).getKey())
                    || !this.mapping.get(mapKey).getCrc().contentEquals(fm.get(mapKey).getCrc())
                    || !this.mapping.get(mapKey).getAssociedTios().containsAll(fm.get(mapKey).getAssociedTios())) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
