package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;
import hudson.model.HealthReport;
import hudson.model.TaskListener;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.thalesgroup.rtrtcoverage.cioreader.Ratio;
import com.thalesgroup.rtrtcoverage.cioreader.ReportTag;

/**
 * @author Sebastien Barbier
 * @version 1.0
 *
 */
public class RTRTBuildActionTest {

    @Test
    public void testBuildAction() throws Exception {

        final Rule rRule = new Rule() {
            @Override
            public void enforce(final CoverageReport report,
                    final TaskListener listener) {
                if (!report.hasCallCoverage()) {
                    report.setFailed();
                }
            }
        };

        final float[] values = { 0, 0 };
        final Ratio[] ratios = new Ratio[ReportTag.values().length];
        for (int i = 0; i < ReportTag.values().length; i++) {
            ratios[i] = new Ratio(values);
        }

        final RTRTHealthReportThresholds hrThresholds = new RTRTHealthReportThresholds();

        final RTRTBuildAction bAction = new RTRTBuildAction(null, rRule,
                ratios, hrThresholds);

        Assert.assertNotNull(bAction);
        Assert.assertNull(bAction.getOwner());
        Assert.assertEquals("Coverage Report", bAction.getDisplayName());
        Assert.assertEquals("graph.gif", bAction.getIconFileName());
        Assert.assertEquals("rtrtcoverage", bAction.getUrlName());
        Assert.assertNull(bAction.getBuild());

        final HealthReport hr = bAction.getBuildHealth();
        Assert.assertNotNull(hr);
        Assert.assertEquals(0, hr.getScore());
        Assert.assertEquals(
                "Coverage: Functions and Exits 0/0 (0%).Calls 0/0 (0%).Statement Blocks 0/0 (0%).Implicit Blocks 0/0 (0%).Decisions 0/0 (0%).Loops 0/0 (0%).Basic Conditions 0/0 (0%).Modified Conditions 0/0 (0%).Multiple Conditions 0/0 (0%). ",
                hr.getDescription());

    }

    @Test
    public void testBuildActionLoader() throws Exception {

        final RTRTHealthReportThresholds hrThresholds = new RTRTHealthReportThresholds();

        final FilePath file = new FilePath(new File(this.getClass()
                .getResource("cioreader/ATU.CIO").toURI()));
        final RTRTBuildAction bAction = RTRTBuildAction.load(null, null, null,
                hrThresholds, file);
        Assert.assertNotNull(bAction);
        Assert.assertEquals(100, bAction.getFunctionCoverage().getPercentage());
        Assert.assertEquals(100, bAction.getStatementBlockCoverage()
                .getPercentage());
        Assert.assertEquals(100, bAction.getBasicConditionCoverage()
                .getPercentage());
        Assert.assertEquals(100, bAction.getModifiedConditionCoverage()
                .getPercentage());
        Assert.assertEquals(100, bAction.getMultipleConditionCoverage()
                .getPercentage());
        Assert.assertFalse(bAction.getCallCoverage().isInitialized());
        Assert.assertFalse(bAction.getLoopCoverage().isInitialized());
        Assert.assertFalse(bAction.getImplicitBlockCoverage().isInitialized());

        final HealthReport hr = bAction.getBuildHealth();
        Assert.assertNotNull(hr);
        Assert.assertEquals("Coverage: All coverage targets have been met. ",
                hr.getDescription());
        Assert.assertEquals(100, hr.getScore());

    }
}
