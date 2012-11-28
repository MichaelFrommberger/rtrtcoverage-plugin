package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentificationException;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentity;
import com.thalesgroup.rtrtcoverage.filesmapping.FilesMapping;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioException;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageMergeException;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tusarexport.TusarExport;

/**
 * Class allowing to export a tusar (*.xml) file.
 * from *.fdc, *.tio and instrumented files
 *
 * @author Bastien Reboulet
 *
 */
public class RtrtCoverage2Tusar {

    /**
     * the directory where to find the *.FDC files.
     */
    private final File fdcDir;

    /**
     * the directory where to find the *.TIO files.
     */
    private final File tioDir;

    /**
     * the workspace directory (can be a remote dir).
     */
    private final FilePath workSpace;

    /**
     * the ant-style pattern allowing to find instrumented files.
     * root dir is the workspace
     */
    private final String augPattern;

    /**
     * the *.xml file that will be written with tusar data.
     */
    private final File outputFile;

    /**
     * Default constructor, sets all the necessary inputs for the export.
     *
     * @param fdcdir
     *            the directory where to find the *.FDC files
     * @param tiodir
     *            the directory where to find the *.TIO files
     * @param workspace
     *            the workspace directory (can be a remote dir)
     * @param augpattern
     *            the ant-style pattern allowing to find instrumented files
     * @param outputfile
     *            the *.xml file that will be written with tusar data
     */
    public RtrtCoverage2Tusar(final File fdcdir, final File tiodir,
            final FilePath workspace, final String augpattern,
            final File outputfile) {
        this.fdcDir = fdcdir;
        this.tioDir = tiodir;
        this.workSpace = workspace;
        this.augPattern = augpattern;
        this.outputFile = outputfile;
    }

    /**
     * Main function allowing to export the tusar file.
     * with all the inputs set in the default constructor
     *
     * @param mapping a files mapping
     * @throws FileIdentificationException a FileIdentificationException
     * @throws IOException an IOException
     * @throws InterruptedException an InterruptedException
     * @throws TioException a TioException
     * @throws CoverageMergeException a CoverageMergeException
     */
    public final void export(final FilesMapping mapping)
            throws FileIdentificationException, IOException,
    InterruptedException, TioException, CoverageMergeException {
        final List<FileCoverageDefinition> fileCovDefs = extractFdcData(mapping);
        final List<TestSuiteTrace> traces = extractTioData();
        final List<FileCoverage> fileCovs = mergeData(fileCovDefs, traces);
        writeXml(fileCovs, outputFile);
    }

    /**
     * @param mapping
     *            the mapping between key+crc and source file names
     * @return a list of FileCoverageDefinition filled with *.FDC data + key and
     *         crc data
     * @throws FileIdentificationException a FileIdentificationException
     * @throws IOException an IOException
     * @throws InterruptedException an InterruptedException
     */
    private List<FileCoverageDefinition> extractFdcData(final FilesMapping mapping)
                    throws FileIdentificationException, IOException, InterruptedException {
        final FdcReader fdcReader = new FdcReader();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FilePath[] fdcPaths = (new FilePath(fdcDir)).list("*.FDC");
        for (final FilePath fdcFile : fdcPaths) {
            final FileCoverageDefinition fileDef = fdcReader.read(fdcFile);
            // add key+crc data to file coverage definitions
            // This is required because we don't know how to convert
            // the source pathname and crc into the key and crc found in TIO.
            final FileIdentity id = mapping.get(fileDef.getSourceName());
            if (id == null) {
                throw new FileIdentificationException(
                    "Cannot get id for source: " + fileDef.getSourceName());
            }
            fileDef.setKey(id.getKey());
            fileDef.setCrc(id.getCrc());
            fileCoverageDefs.add(fileDef);
        }
        return fileCoverageDefs;
    }

    /**
     * @return a list of TestSuiteTrace filled with *.TIO data.
     * @throws TioException a TioException
     * @throws IOException an IOException
     * @throws InterruptedException an InterruptedException
     */
    private List<TestSuiteTrace> extractTioData() throws TioException,
    IOException, InterruptedException {
        final FilePath[] tioPaths = (new FilePath(tioDir)).list("*.TIO");
        final List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        for (final FilePath tioFile : tioPaths) {
            final TioReader2 tioReader = new TioReader2(new FileInputStream(
                    tioFile.getRemote()));
            final TestSuiteTrace tst = tioReader.readTio();
            tst.setName(tioFile.getName());
            traces.add(tst);
        }
        return traces;
    }

    /**
     * @param fileCoverageDefs a list of FileCoverageDefinition
     * @param traces a list of TestSuiteTrace
     * @return a list of FileCoverage which represent all the merged data
     *         between *.FDC, *.TIO and key/crc-source file names
     * @throws CoverageMergeException a CoverageMergeException
     */
    private List<FileCoverage> mergeData(
            final List<FileCoverageDefinition> fileCoverageDefs,
            final List<TestSuiteTrace> traces) throws CoverageMergeException {
        final CoverageTraceMerger merger = new CoverageTraceMerger();
        return merger.merge(fileCoverageDefs, traces);
    }

    /**
     * Using jaxb, write an xml file from Tusar data input.
     *
     * @param fileCovs a list of FileCoverage
     * @param newOutputFile the file where to write
     */
    private void writeXml(final List<FileCoverage> fileCovs,
            final File newOutputFile) {
        if (!(new File(newOutputFile.getParent())).exists()) {
            (new File(newOutputFile.getParent())).mkdirs();
        }
        final TusarExport tusarExport = new TusarExport();
        tusarExport.export(tusarExport.convert(fileCovs), newOutputFile);
    }

}
