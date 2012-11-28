package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;
import hudson.model.HealthReport;
import hudson.model.HealthReportingAction;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.util.NullStream;
import hudson.util.StreamTaskListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.localizer.Localizable;
import org.kohsuke.stapler.StaplerProxy;

import com.thalesgroup.rtrtcoverage.cioreader.CioAttributes;
import com.thalesgroup.rtrtcoverage.cioreader.CioException;
import com.thalesgroup.rtrtcoverage.cioreader.CioReader;
import com.thalesgroup.rtrtcoverage.cioreader.CoverageElement;
import com.thalesgroup.rtrtcoverage.cioreader.Ratio;
import com.thalesgroup.rtrtcoverage.cioreader.ReportTag;

/**
 * Build view extension by RTRT plugin. As {@link CoverageObject}, it retains
 * the overall coverage report.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public final class RTRTBuildAction extends CoverageObject<RTRTBuildAction>
implements HealthReportingAction, StaplerProxy {

    /**
     * The owner.
     */
    private final AbstractBuild<?, ?> owner;

    /**
     * The report.
     */
    private transient WeakReference<CoverageReport> report;

    /**
     * Non-null if the coverage has pass/fail rules.
     */
    private final Rule rule;

    /**
     * The thresholds that applied when this build was built.
     */
    private final RTRTHealthReportThresholds thresholds;

    /**
     * Default Constructor.
     *
     * @param abOwner
     *            the owner
     * @param rRule
     *            rule for failing the coverage report (according to some
     *            ratios)
     * @param ratios
     *            the ratio of the coverage including: ratio for functions and
     *            exits coverage, ratio for calls coverage, ratio for statement
     *            blocks coverage, ratio for implicit blocks coverage, ratio for
     *            decisions coverage, ratio for basic conditions coverage, ratio
     *            for modified conditions coverage, ratio for multiple
     *            conditions coverage.
     * @param hrThresholds
     *            the thresholds for weather reports
     */
    public RTRTBuildAction(final AbstractBuild<?, ?> abOwner, final Rule rRule,
            final Ratio[] ratios, final RTRTHealthReportThresholds hrThresholds) {
        this.owner = abOwner;
        this.rule = rRule;
        this.thresholds = hrThresholds;

        final int sizeRatios = ReportTag.values().length;
        if (ratios == null || ratios.length != sizeRatios) {
            OWNLOGGER.log(Level.SEVERE, "Bad number of ratios");
            return;
        }

        try {
            for (int i = 0; i < sizeRatios; ++i) {
                setRatio(i, ratios[i]);
            }
        } catch (final CioException e) {
            OWNLOGGER.log(Level.SEVERE, "Bad number of ratios");
        }
    }

    /**
     * Get the owner.
     *
     * @return owner
     */
    public AbstractBuild<?, ?> getOwner() {
        return owner;
    }

    /**
     * Display the name of the action.
     *
     * @return name in messages.properties
     */
    public String getDisplayName() {
        return Messages.BuildAction_DisplayName();
    }

    /**
     * Display the icon.
     *
     * @return graph.gif
     */
    public String getIconFileName() {
        return "graph.gif";
    }

    /**
     * Get the url name.
     *
     * @return rtrtcoverage.
     */
    public String getUrlName() {
        return "rtrtcoverage";
    }

    /**
     * Get the coverage {@link hudson.model.HealthReport}.
     *
     * @return The health report or <code>null</code> if health reporting is
     *         disabled.
     */
    public HealthReport getBuildHealth() {
        if (thresholds == null) {
            // no thresholds => no report
            return null;
        }

        thresholds.ensureValid();
        final int nbRatio = ReportTag.values().length;
        final int maxScore = 100;
        int score = maxScore;
        int percent;
        final ArrayList<Localizable> reports = new ArrayList<Localizable>(
                nbRatio);
        if (getFunctionCoverage() != null
                && getFunctionCoverage().isInitialized()
                && thresholds.getMaxFunction() > 0) {
            percent = getFunctionCoverage().getPercentage();
            if (percent < thresholds.getMaxFunction()) {
                reports.add(Messages._BuildAction_Functions(
                        getFunctionCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinFunction(),
                    percent, thresholds.getMaxFunction());
        }
        if (getCallCoverage() != null && getCallCoverage().isInitialized()
                && thresholds.getMaxCall() > 0) {
            percent = getCallCoverage().getPercentage();
            if (percent < thresholds.getMaxCall()) {
                reports.add(Messages._BuildAction_Calls(getCallCoverage(),
                        percent));
            }
            score = updateHealthScore(score, thresholds.getMinCall(), percent,
                    thresholds.getMaxCall());
        }
        if (getStatementBlockCoverage() != null
                && getStatementBlockCoverage().isInitialized()
                && thresholds.getMaxStatBlock() > 0) {
            percent = getStatementBlockCoverage().getPercentage();
            if (percent < thresholds.getMaxStatBlock()) {
                reports.add(Messages._BuildAction_StatBlocks(
                        getStatementBlockCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinStatBlock(),
                    percent, thresholds.getMaxStatBlock());
        }
        if (getImplicitBlockCoverage() != null
                && getImplicitBlockCoverage().isInitialized()
                && thresholds.getMaxImplBlock() > 0) {
            percent = getImplicitBlockCoverage().getPercentage();
            if (percent < thresholds.getMaxImplBlock()) {
                reports.add(Messages._BuildAction_ImplBlocks(
                        getImplicitBlockCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinImplBlock(),
                    percent, thresholds.getMaxImplBlock());
        }
        if (getDecisionCoverage() != null
                && getDecisionCoverage().isInitialized()
                && thresholds.getMaxDecision() > 0) {
            percent = getDecisionCoverage().getPercentage();
            if (percent < thresholds.getMaxDecision()) {
                reports.add(Messages._BuildAction_Decisions(
                        getDecisionCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinDecision(),
                    percent, thresholds.getMaxDecision());
        }
        if (getLoopCoverage() != null && getLoopCoverage().isInitialized()
                && thresholds.getMaxLoop() > 0) {
            percent = getLoopCoverage().getPercentage();
            if (percent < thresholds.getMaxLoop()) {
                reports.add(Messages._BuildAction_Loops(getLoopCoverage(),
                        percent));
            }
            score = updateHealthScore(score, thresholds.getMinLoop(), percent,
                    thresholds.getMaxLoop());
        }
        if (getBasicConditionCoverage() != null
                && getBasicConditionCoverage().isInitialized()
                && thresholds.getMaxBasicCond() > 0) {
            percent = getBasicConditionCoverage().getPercentage();
            if (percent < thresholds.getMaxBasicCond()) {
                reports.add(Messages._BuildAction_BasicConds(
                        getBasicConditionCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinBasicCond(),
                    percent, thresholds.getMaxBasicCond());
        }
        if (getModifiedConditionCoverage() != null
                && getModifiedConditionCoverage().isInitialized()
                && thresholds.getMaxModifCond() > 0) {
            percent = getModifiedConditionCoverage().getPercentage();
            if (percent < thresholds.getMaxModifCond()) {
                reports.add(Messages._BuildAction_ModifConds(
                        getModifiedConditionCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinModifCond(),
                    percent, thresholds.getMaxModifCond());
        }
        if (getMultipleConditionCoverage() != null
                && getMultipleConditionCoverage().isInitialized()
                && thresholds.getMaxMultCond() > 0) {
            percent = getMultipleConditionCoverage().getPercentage();
            if (percent < thresholds.getMaxMultCond()) {
                reports.add(Messages._BuildAction_MultConds(
                        getMultipleConditionCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinMultCond(),
                    percent, thresholds.getMaxMultCond());
        }
        if (score == maxScore) {
            reports.add(Messages._BuildAction_Perfect());
        }
        // Collect params and replace nulls with empty string
        final Object[] args = reports.toArray(new Object[nbRatio + 1]);
        for (int i = nbRatio; i >= 0; i--) {
            if (args[i] == null) {
                args[i] = "";
            }
        }

        String compilation = "";
        for (int i = 0; i < nbRatio + 1; ++i) {
            compilation += args[i];
        }

        return new HealthReport(score,
                Messages._BuildAction_Description(compilation));
    }

    /**
     * Update the weather reports in hudson.
     *
     * @param score
     *            the value to display if success.
     * @param min
     *            min value
     * @param value
     *            current value
     * @param max
     *            max value
     * @return the value to display
     */
    private static int updateHealthScore(final int score, final int min,
            final int value, final int max) {
        final float perCent = 100.0f;
        if (value >= max) {
            return score;
        }
        if (value <= min) {
            return 0;
        }
        assert max != min;
        final int scaled = (int) (perCent * ((float) value - min) / (max - min));
        if (scaled < score) {
            return scaled;
        }
        return score;
    }

    /**
     * Get the target.
     *
     * @return the target
     */
    public Object getTarget() {
        return getResult();
    }

    /**
     * Get the build.
     *
     * @return the owner
     */
    @Override
    public AbstractBuild<?, ?> getBuild() {
        return owner;
    }

    /**
     * Obtain all the rtrt reports.
     *
     * @param file
     *            the path where to find the rtrt report
     * @return all the .cio file into the file path
     * @throws IOException
     *             if some error during reading path
     * @throws InterruptedException
     *             if some error during reading path
     */
    protected static FilePath[] getRTRTReports(final File file)
            throws IOException, InterruptedException {
        final FilePath path = new FilePath(file);

        if (path.isDirectory()) {
            return path.list("*CIO");
        }

        return null;
    }

    /**
     * Obtains the detailed {@link CoverageReport} instance.
     *
     * @return current coverage report
     */
    public synchronized CoverageReport getResult() {

        final File reportFolder = RTRTPublisher.getRTRTReport(owner);

        if (report != null) {
            final CoverageReport r = report.get();
            if (r != null) {
                return r;
            }
        }

        try {

            // Get the list of report files stored for this build
            final FilePath[] reports = getRTRTReports(reportFolder);

            final InputStream[] streams = new InputStream[2 * reports.length];
            final String[] nameReports = new String[reports.length];
            for (int i = 0; i < reports.length; i++) {
                streams[2 * i] = reports[i].read();
                streams[2 * i + 1] = reports[i].read();
                nameReports[i] = reports[i].getName();
            }

            // Generate the report
            // Change for master/slave configuration
            final CoverageReport r = new CoverageReport(this, RTRTPublisher
                    .getRTRTCodeSource(owner).getPath(), RTRTPublisher
                    .getRTRTReport(owner).getPath(), streams, nameReports);

            if (rule != null) {
                // we change the report so that the FAILED flag is set correctly
                OWNLOGGER.info("calculating failed packages based on " + rule);
                rule.enforce(r, new StreamTaskListener(new NullStream()));
            }

            report = new WeakReference<CoverageReport>(r);
            return r;
        } catch (final InterruptedException e) {
            OWNLOGGER.log(Level.WARNING, "Failed to load " + reportFolder, e);
            return null;
        } catch (final IOException e) {
            OWNLOGGER.log(Level.WARNING, "Failed to load " + reportFolder, e);
            return null;
        }
    }

    /**
     * Get the previous result.
     *
     * @return the previous result
     */
    @Override
    public RTRTBuildAction getPreviousResult() {
        return getPreviousResult(owner);
    }

    /**
     * Gets the previous {@link RTRTBuildAction} of the given build.
     *
     * @param start
     *            the top coverage build
     * @return previous BuildAction
     */
    /* package */static RTRTBuildAction getPreviousResult(
            final AbstractBuild<?, ?> start) {
        AbstractBuild<?, ?> b = start;
        while (true) {
            b = b.getPreviousBuild();
            if (b == null) {
                return null;
            }
            if (b.getResult() == Result.FAILURE) {
                continue;
            }
            final RTRTBuildAction r = b.getAction(RTRTBuildAction.class);
            if (r != null) {
                return r;
            }
        }
    }

    /**
     * Constructs the object from rtrtcoverage CIO report files.
     *
     * @param owner
     *            the build
     * @param rule
     *            rule for failing ratios
     * @param thresholds
     *            for weather displays in Hudson
     * @param files
     *            all the report files
     * @param logger
     *            log to the Hudson console for ui
     * @return the global coverage
     * @throws IOException
     *             if failed to parse the file.
     * @throws CioException
     *             if bad cio file
     */
    public static RTRTBuildAction load(final AbstractBuild<?, ?> owner,
            final PrintStream logger, final Rule rule,
            final RTRTHealthReportThresholds thresholds,
            final FilePath... files) throws IOException, CioException {
        Ratio[] ratios = null;
        for (final FilePath f : files) {
            if (logger != null) {
                logger.println("[RTRTCoverage] [info]: coverage for "
                        + f.getName());
            }
            final InputStream in1 = f.read();
            final InputStream in2 = f.read();
            try {
                ratios = loadRatios(in1, in2, ratios, f.getName());
            } finally {
                in1.close();
                in2.close();
            }
        }
        return new RTRTBuildAction(owner, rule, ratios, thresholds);
    }

    /**
     * Compute the ratio of the global coverage.
     *
     * @param in1
     *            the .cio input stream
     * @param in2
     *            the .cio input stream
     * @param r
     *            the array of ratios to determine
     * @param filename
     *            the name of the .tio file.
     * @return the array of ratios
     * @throws IOException
     *             if bad input
     * @throws CioException
     *             if bad .cio file
     */
    private static Ratio[] loadRatios(final InputStream in1,
            final InputStream in2, Ratio[] r, final String filename)
                    throws IOException, CioException {

        final CioAttributes attributes = new CioAttributes(in1);
        final CioReader reader = new CioReader(in2, filename.replace(".CIO",
                ".TIO"));
        final int maxRatios = ReportTag.values().length;

        // head for the global coverage
        if (r == null) {
            r = new Ratio[maxRatios];
            for (int i = 0; i < maxRatios; ++i) {
                r[i] = new Ratio();
            }
        }

        CoverageElement globalCoverage = new CoverageElement();
        globalCoverage = reader.populateGlobalCoverage(
                attributes.getAttributesFlags(),
                attributes.getAttributesInformationFlags());

        for (int i = 0; i < maxRatios; ++i) {
            if (globalCoverage.isInitialized(i)) {
                r[i].addValue(globalCoverage.getRatio(i));
            }
        }

        return r;

    }

    /**
     * Logger.
     */
    private static final Logger OWNLOGGER = Logger
            .getLogger(RTRTBuildAction.class.getName());

}
