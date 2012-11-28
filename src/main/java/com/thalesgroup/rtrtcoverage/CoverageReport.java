package com.thalesgroup.rtrtcoverage;

import hudson.model.AbstractBuild;

import com.thalesgroup.rtrtcoverage.serializablerates.FileRate;
import com.thalesgroup.rtrtcoverage.serializablerates.GlobalRate;

/**
 * Root object of the coverage report.
 *
 * @author Bastien Reboulet
 * @version 1.1
 */
public final class CoverageReport
extends
AggregatedReport<CoverageReport/* dummy */, CoverageReport, SourceFileReport> {

    /**
     * The action to perform.
     */
    private RTRTBuildAction action;

    /**
     * The global rate.
     */
    private GlobalRate globalRate;


    /**
     * @param bAction an action.
     * @param newGlobalRate a global rate.
     */
    public CoverageReport(final RTRTBuildAction bAction, final GlobalRate newGlobalRate) {
        this.action = bAction;
        this.globalRate = newGlobalRate;
        this.initRatios(globalRate);
        setName("RTRT");
        setParent(null);
        this.buildHierarchy();
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
     */
    private void buildHierarchy() {
        this.setName("Couverture Globale");
        this.setSourcePath(null);
        for (FileRate fileRate : globalRate.getFileRates()) {
            SourceFileReport sfr = new SourceFileReport();
            sfr.setParent(this);
            sfr.setName(fileRate.getSourceFileName());
            sfr.setSourcePath(fileRate.getFdcPath());
            sfr.initRatios(fileRate);
            super.add(sfr);
            // TODO : fix file coverage coloring per test before
            // resurrecting this code
//            for (TestRate testRate : fileRate.getTestRates()) {
//                TestReport testReport = new TestReport();
//                testReport.setSourcePath(fileRate.getFdcPath());
//                testReport.setName(testRate.getTestName());
//                testReport.initRatios(testRate);
//                sfr.add(testReport);
//            }
        }
    }
}
