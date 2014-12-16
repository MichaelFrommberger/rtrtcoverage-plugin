package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentitiesImport;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentity;
import com.thalesgroup.rtrtcoverage.filesmapping.FilesMapping;
import com.thalesgroup.rtrtcoverage.sourcecoloring.FdcSourceReader;
import com.thalesgroup.rtrtcoverage.sourcecoloring.SourceColourer;
import com.thalesgroup.rtrtcoverage.sourcecoloring.SourceFile;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;

/**
 * Global coverage by files. Could be an AggregatedReport if adding TestReport
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public final class SourceFileReport extends
AggregatedReport<CoverageReport, SourceFileReport, TestReport> {

    /**
     * Give the contents of the source file. Coverage is painted on the lines.
     *
     * @return the painted source code.
     */
    public String getPaintedSourceFileContent() {
        try {
            // Load file identity mapping
            final FilesMapping mapping =
                    (new FileIdentitiesImport()).importXml(new File(getBuild().getRootDir()
                            + System.getProperty("file.separator") + "file_identities.xml"));
            // FDC file corresponding to this source file
            FilePath fdcPath = new FilePath(new File(getSourcePath()));
            FileIdentity fdcId = mapping.get(getName());
            // Read associated TIO files
            List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
            for (FilePath tio : fdcId.getAssociedTios()) {
                traces.add(new TioReader2(tio.read()).readTio());
            }
            // merge FDC and TIO to build the detailed coverage status
            FileCoverageDefinition fileCovDef = new FdcReader().read(fdcPath);
            fileCovDef.setKey(fdcId.getKey());
            fileCovDef.setCrc(fdcId.getCrc());
            List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
            fileCoverageDefs.add(fileCovDef);
            FileCoverage fileCov = new CoverageTraceMerger().merge(fileCoverageDefs, traces).get(0);
            // Read structured source code
            SourceFile source = new FdcSourceReader().read(fdcPath);
            // Generate colored code
            return new SourceColourer().colorSource(source, fileCov);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}
