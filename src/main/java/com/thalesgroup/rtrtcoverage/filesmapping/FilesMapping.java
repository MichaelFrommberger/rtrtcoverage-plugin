package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thalesgroup.rtrtcoverage.tioreader2.FileTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TestTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioException;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;

/**
 * Class allowing mapping between tio and fdc files.
 *
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
     *
     * @param tioDir
     *            directory where all the tio files are located (generally in
     *            the build dir)
     * @param workspace
     *            the workspace where we can find instrumented files
     * @param augPattern
     *            the ant-style pattern allowing to find instrumented files
     *            (relative to the workspace)
     * @throws IOException
     *             an IOException
     * @throws InterruptedException
     *             an InterruptedException
     * @throws FileIdentificationException
     *             a FileIdentificationException
     * @throws TioException
     *             a TioException
     */
    public final void build(final FilePath tioDir, final FilePath workspace,
            final String augPattern) throws IOException, InterruptedException,
            FileIdentificationException, TioException {
        FilePath[] augFiles = workspace.list(augPattern);
        FilePath[] tioFiles = tioDir.list("**/*.TIO");
        Map<String, FileIdentity> fileIds = new HashMap<String, FileIdentity>();
        // build source filename <--> key/crc association
        for (FilePath augFile : augFiles) {
            FileIdentity fi = InstrumentedFileParser.getFileIdentity(augFile,
                    augPattern);
            if (fi != null) {
                mapping.put(fi.getSourceFileName(), fi);
                fileIds.put(fi.getKey() + fi.getCrc(), fi);
            }
        }
        // collect tio files covering each source file
        for (FilePath tioFile : tioFiles) {
            for (String id : coveredFileIds(tioFile)) {
                if (fileIds.containsKey(id)) {
                    FileIdentity fileId = fileIds.get(id);
                    if (!fileId.getAssociedTios().contains(tioFile)) {
                        fileIds.get(id).getAssociedTios().add(tioFile);
                    }
                }
            }
        }
    }

    /**
     * @param tioFile
     *            a tio file
     * @return a list of all the "key+crc" strings contained in this tio file
     * @throws IOException
     *             an IOException
     * @throws TioException
     *             a TioException
     */
    private List<String> coveredFileIds(final FilePath tioFile)
            throws IOException, TioException {
        List<String> coveredFileIds = new ArrayList<String>();
        TioReader2 tioReader = new TioReader2(tioFile.read());
        TestSuiteTrace tst = tioReader.readTio();
        for (TestTrace testTrace : tst.getTestTraces()) {
            for (FileTrace fileTrace : testTrace.getAllFileTraces()) {
                coveredFileIds.add(fileTrace.getKey() + fileTrace.getCrc());
            }
        }
        return coveredFileIds;
    }

    /**
     * @param sourceFileName
     *            a source file name
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
     * @param fileIdentities
     *            a list of FileIdentity
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
     * @param fm
     *            a FilesMapping
     * @return true if this FilesMapping contains the same mapping than fm, else
     *         return false
     */
    public final boolean equals(final FilesMapping fm) {
        for (String mapKey : fm.getMap().keySet()) {
            if (!this.mapping.get(mapKey).getKey()
                    .contentEquals(fm.get(mapKey).getKey())
                    || !this.mapping.get(mapKey).getCrc()
                            .contentEquals(fm.get(mapKey).getCrc())
                    || !this.mapping.get(mapKey).getAssociedTios()
                            .containsAll(fm.get(mapKey).getAssociedTios())) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
