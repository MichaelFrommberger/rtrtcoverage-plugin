package com.thalesgroup.rtrtcoverage;

import hudson.model.AbstractBuild;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thalesgroup.rtrtcoverage.cioreader.CioAttributes;
import com.thalesgroup.rtrtcoverage.cioreader.CioException;
import com.thalesgroup.rtrtcoverage.cioreader.CioReader;
import com.thalesgroup.rtrtcoverage.cioreader.CoverageElement;
import com.thalesgroup.rtrtcoverage.cioreader.CoverageReportElement;
import com.thalesgroup.rtrtcoverage.cioreader.CoverageReportTestElement;

/**
 * Root object of the coverage report.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public final class CoverageReport
extends
AggregatedReport<CoverageReport/* dummy */, CoverageReport, SourceFileReport> {

    /**
     * The action to perform.
     */
    private final RTRTBuildAction action;

    /**
     * Default Constructor.
     *
     * @param bAction
     *            the action to perform
     */
    private CoverageReport(final RTRTBuildAction bAction) {
        this.action = bAction;
        setName("RTRT");
    }

    /**
     * Constructor having inputs.
     *
     * @param bAction
     *            the action to perform
     * @param includeFdc
     *            local path where .fdc reports are recorded
     * @param includeTio
     *            local path where .tio reports are recorded
     * @param cioReports
     *            the inputs to analyze and convert
     * @param filename
     *            name of the .cio file
     * @throws IOException
     *             if some errors during reading
     */
    public CoverageReport(final RTRTBuildAction bAction,
            final String includeFdc, final String includeTio,
            final InputStream[] cioReports, final String[] filename)
                    throws IOException {
        this(bAction);
        for (int i = 0, j = 0; i < cioReports.length; i += 2, ++j) {
            buildHierarchy(cioReports[i], cioReports[i + 1], includeFdc,
                    includeTio, filename[j].toUpperCase());
        }
        setParent(null);
    }

    /**
     * Constructor having one input.
     *
     * @param bAction
     *            the action to perform
     * @param includeFdc
     *            local path where .fdc reports are recorded
     * @param includeTio
     *            local path where .tio reports are recorded
     * @param cioReport
     *            the name of the .cio report
     * @throws IOException
     *             if some errors during reading
     */
    public CoverageReport(final RTRTBuildAction bAction,
            final String includeFdc, final String includeTio,
            final File cioReport) throws IOException {
        this(bAction);
        final InputStream is1 = new FileInputStream(cioReport);
        final InputStream is2 = new FileInputStream(cioReport);
        buildHierarchy(is1, is2, includeFdc, includeTio, cioReport.getName()
                .toUpperCase());
        setParent(null);
    }

    /**
     * Get the previous result if exists.
     *
     * @return the previous coverage report
     */
    @Override
    public CoverageReport getPreviousResult() {
        final RTRTBuildAction prev = action.getPreviousResult();
        if (prev != null) {
            return prev.getResult();
        } else {
            return null;
        }
    }

    /**
     * Get the build.
     *
     * @return the build linked to the action
     */
    @Override
    public AbstractBuild<?, ?> getBuild() {
        return action.getOwner();
    }

    /**
     * Build the hierarchy of the coverage It is done only for the global
     * coverage => only files are shown.
     *
     * @param in1
     *            an inputsteam on the .cio file
     * @param in2
     *            a second inputsteam on the .cio file
     * @param includeFdc
     *            local path where .fdc reports are recorded
     * @param includeTio
     *            local path where .tio reports are recorded
     * @param filename
     *            name of the .cio file.
     * @throws IOException
     *             if some errors during opening streams
     */
    private void buildHierarchy(final InputStream in1, final InputStream in2,
            final String includeFdc, final String includeTio,
            final String filename) throws IOException {

        CioAttributes attributes = null;
        CioReader reader = null;
        try {
            attributes = new CioAttributes(in1);
            reader = new CioReader(in2, filename.replace(".CIO", ".TIO"));
        } catch (final CioException e1) {
            throw new IOException(e1.getMessage());
        }
        CoverageReportElement coverageReport = new CoverageReportElement();
        try {
            coverageReport = reader.populate(attributes.getAttributesFlags(),
                    attributes.getAttributesInformationFlags());
        } catch (final CioException e1) {
            throw new IOException(e1.getMessage());
        }

        this.setName("Couverture Globale");
        try {
            this.updateCoverage(coverageReport.getGlobalCoverage());
        } catch (final CioException e) {
            throw new IOException(e.getMessage());
        }

        this.setSourcePath(null);

        for (int nbFile = 0; nbFile < coverageReport
                .getNumberOfGlobalCoverageFiles(); ++nbFile) {
            final SourceFileReport sfr = new SourceFileReport();
            sfr.setParent(this);
            final String nameFile = coverageReport.getGlobalCoverageForFile(
                    nbFile).getNameFile();
            sfr.setName(nameFile);
            sfr.setIncludesFdc(includeFdc);
            sfr.setIncludesTio(includeTio);
            final String tioName = coverageReport.getGlobalCoverageForFile(
                    nbFile).getNameOfAssociatedTioFile();
            sfr.setTioName(tioName);
            // Unix compatibility
            final String nameFdcFile = nameFile.substring(0,
                    nameFile.lastIndexOf('.'))
                    + ".FDC";
            if (getBuild() != null) {
                sfr.setSourcePath(includeFdc + "/" + nameFdcFile);
            } else {
                sfr.setSourcePath(nameFdcFile);
            }

            sfr.addCoverage(coverageReport.getGlobalCoverageForFile(nbFile));

            // Child
            final CoverageReportTestElement tests = coverageReport
                    .getCoverageReportForTests();
            tests.begin();
            while (tests.hasNextTest()) {
                final String nameTest = tests.getNextNameTest();
                for (int file = 0; file < tests.getNumberOfFilesInNextTest(); ++file) {
                    final CoverageElement fileCoverage = tests
                            .getCoverageElementInNextTest(file);
                    if (nameFile.equals(fileCoverage.getNameFile())) {
                        final TestReport tr = new TestReport();
                        tr.setParent(sfr);
                        tr.setName(nameTest);
                        tr.setIncludesFdc(includeFdc);
                        tr.setIncludesTio(includeTio);
                        tr.setClassification(nameTest);
                        tr.setTioName(tioName);
                        sfr.setTioName(tioName);
                        if (getBuild() != null) {
                            tr.setSourcePath(includeFdc + "/" + nameFdcFile);
                        } else {
                            tr.setSourcePath(nameFdcFile);
                        }
                        tr.addCoverage(fileCoverage);
                        sfr.add(tr);
                    }
                }
            }
            super.add(sfr);
        }

    }
}
