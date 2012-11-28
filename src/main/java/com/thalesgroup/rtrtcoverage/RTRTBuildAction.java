package com.thalesgroup.rtrtcoverage;

import hudson.model.HealthReport;
import hudson.model.HealthReportingAction;
import hudson.model.Result;
import hudson.model.AbstractBuild;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.jvnet.localizer.Localizable;
import org.kohsuke.stapler.StaplerProxy;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.serializablerates.GlobalRate;

/**
 * Build view extension by Emma plugin.
 *
 * As {@link CoverageObject}, it retains the overall coverage report.
 *
 * @author Kohsuke Kawaguchi
 */
public final class RTRTBuildAction extends CoverageObject<RTRTBuildAction>
implements HealthReportingAction, StaplerProxy {

    /**
     * The owner.
     */
    private final AbstractBuild<?, ?> owner;

    /**
     * Non-null if the coverage has pass/fail rules.
     */
    private final Rule rule;

    /**
     * The thresholds that applied when this build was built.
     * @TODO add ability to trend thresholds on the graph
     */
    private final RTRTHealthReportThresholds thresholds;

    /**
     * The report.
     */
    private transient WeakReference<CoverageReport> report;

    /**
     * @param newOwner the owner
     * @param newRule rule for failing the coverage report (according to some
     *            ratios)
     * @param globalRate a global rate
     * @param newThresholds the thresholds for weather reports
     */
    public RTRTBuildAction(final AbstractBuild<?, ?> newOwner,
            final Rule newRule,
            final GlobalRate globalRate,
            final RTRTHealthReportThresholds newThresholds) {
        this.owner = newOwner;
        this.rule = newRule;
        this.thresholds = newThresholds;
        this.initRatios(globalRate);
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
     * @return The health report or <code>null</code> if health reporting is disabled.
     * @since 1.7
     */
    public HealthReport getBuildHealth() {
        if (thresholds == null) {
            // no thresholds => no report
            return null;
        }
        thresholds.ensureValid();
        final int maxScore = 100;
        final int nbRatio = 9;
        int score = maxScore;
        int percent;
        ArrayList<Localizable> reports = new ArrayList<Localizable>(nbRatio);
        if (this.getFunctionCoverage() != null && this.getExitCoverage() != null && thresholds.getMaxFunction() > 0) {
            percent = this.getFunctionAndExitCoverage().getPercentage();
            if (percent < thresholds.getMaxFunction()) {
                reports.add(Messages._BuildAction_Functions(this.getFunctionAndExitCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinFunction(),
                    percent, thresholds.getMaxFunction());
        }
        if (this.getCallCoverage() != null && thresholds.getMaxCall() > 0) {
            percent = this.getCallCoverage().getPercentage();
            if (percent < thresholds.getMaxCall()) {
                reports.add(Messages._BuildAction_Calls(this.getCallCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinCall(),
                    percent, thresholds.getMaxCall());
        }
        if (this.getStatBlockCoverage() != null && thresholds.getMaxStatBlock() > 0) {
            percent = this.getStatBlockCoverage().getPercentage();
            if (percent < thresholds.getMaxStatBlock()) {
                reports.add(Messages._BuildAction_StatBlocks(this.getStatBlockCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinStatBlock(),
                    percent, thresholds.getMaxStatBlock());
        }
        if (this.getImplBlockCoverage() != null && thresholds.getMaxImplBlock() > 0) {
            percent = this.getImplBlockCoverage().getPercentage();
            if (percent < thresholds.getMaxImplBlock()) {
                reports.add(Messages._BuildAction_ImplBlocks(this.getImplBlockCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinImplBlock(),
                    percent, thresholds.getMaxImplBlock());
        }
        if (this.getDecisionCoverage() != null && thresholds.getMaxDecision() > 0) {
            percent = this.getDecisionCoverage().getPercentage();
            if (percent < thresholds.getMaxDecision()) {
                reports.add(Messages._BuildAction_Decisions(this.getDecisionCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinDecision(),
                    percent, thresholds.getMaxDecision());
        }
        if (this.getLoopCoverage() != null && thresholds.getMaxLoop() > 0) {
            percent = this.getLoopCoverage().getPercentage();
            if (percent < thresholds.getMaxLoop()) {
                reports.add(Messages._BuildAction_Loops(this.getLoopCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinLoop(),
                    percent, thresholds.getMaxLoop());
        }
        if (this.getBasicCondCoverage() != null && thresholds.getMaxBasicCond() > 0) {
            percent = this.getBasicCondCoverage().getPercentage();
            if (percent < thresholds.getMaxBasicCond()) {
                reports.add(Messages._BuildAction_BasicConds(this.getBasicCondCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinBasicCond(),
                    percent, thresholds.getMaxBasicCond());
        }
        if (this.getModifCondCoverage() != null && thresholds.getMaxModifCond() > 0) {
            percent = this.getModifCondCoverage().getPercentage();
            if (percent < thresholds.getMaxModifCond()) {
                reports.add(Messages._BuildAction_ModifConds(this.getModifCondCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinModifCond(),
                    percent, thresholds.getMaxModifCond());
        }
        if (this.getMultCondCoverage() != null && thresholds.getMaxMultCond() > 0) {
            percent = this.getMultCondCoverage().getPercentage();
            if (percent < thresholds.getMaxMultCond()) {
                reports.add(Messages._BuildAction_MultConds(this.getMultCondCoverage(), percent));
            }
            score = updateHealthScore(score, thresholds.getMinMultCond(),
                    percent, thresholds.getMaxMultCond());
        }

        if (score == maxScore) {
            reports.add(Messages._BuildAction_Perfect());
        }
        // Collect params and replace nulls with empty string
        final Object[] args = reports.toArray(new Object[BranchType.values().length + 1]);
        for (int i = BranchType.values().length; i >= 0; i--) {
            if (args[i] == null) {
                args[i] = "";
            }
        }
        String compilation = "";
        for (int i = 0; i < BranchType.values().length + 1; ++i) {
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
    private static int updateHealthScore(final int score, final int min, final int value, final int max) {
        final double hundred = 100.0;
        if (value >= max) {
            return score;
        }
        if (value <= min) {
            return 0;
        }
        assert max != min;
        final int scaled = (int) (hundred * ((float) value - min) / (max - min));
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
     * Obtains the detailed {@link CoverageReport} instance.
     *
     * @return current coverage report
     */
    public synchronized CoverageReport getResult() {
        if (report != null) {
            final CoverageReport r = report.get();
            if (r != null) {
                return r;
            }
        }

        // Import serialized rates
        GlobalRate globalRate = null;
        try {
            FileInputStream fis = new FileInputStream(owner.getRootDir()
                    + System.getProperty("file.separator")
                    + "globalrate.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                globalRate = (GlobalRate) ois.readObject();
            } finally {
                try {
                    ois.close();
                } finally {
                    fis.close();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }

        CoverageReport r = null;
        if (globalRate != null) {
            r = new CoverageReport(this, globalRate);
        }
        report = new WeakReference<CoverageReport>(r);
        return r;

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
     * Get the build.
     *
     * @return the owner
     */
    @Override
    public AbstractBuild<?, ?> getBuild() {
        return owner;
    }

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
    /*package*/ static RTRTBuildAction getPreviousResult(final AbstractBuild<?, ?> start) {
        AbstractBuild<?, ?> b = start;
        while (true) {
            b = b.getPreviousBuild();
            if (b == null) {
                return null;
            }
            if (b.getResult() == Result.FAILURE) {
                continue;
            }
            RTRTBuildAction r = b.getAction(RTRTBuildAction.class);
            if (r != null) {
                return r;
            }
        }
    }

    /**
     * Constructs the object from rtrtcoverage global rate object.
     *
     * @param owner
     *            the build
     * @param rule
     *            rule for failing ratios
     * @param thresholds
     *            for weather displays in Hudson
     * @param globalRate
     *            the global rate
     * @param logger
     *            log to the Hudson console for ui
     * @return the global coverage
     */
    public static RTRTBuildAction load(final AbstractBuild<?, ?> owner,
            final Rule rule,
            final PrintStream logger,
            final GlobalRate globalRate,
            final RTRTHealthReportThresholds thresholds) {
        return new RTRTBuildAction(owner, rule, globalRate, thresholds);
    }

    /**
     * Logger.
     */
    private static final Logger OWNLOGGER = Logger.getLogger(RTRTBuildAction.class.getName());
}
