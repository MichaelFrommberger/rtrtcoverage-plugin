package com.thalesgroup.rtrtcoverage;

import hudson.model.HealthReport;
import hudson.model.TaskListener;

import org.junit.Assert;
import org.junit.Test;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.serializablerates.GlobalRate;

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
        final Ratio[] ratios = new Ratio[BranchType.values().length];
        for (int i = 0; i < BranchType.values().length; i++) {
            ratios[i] = new Ratio(values);
        }

        final RTRTHealthReportThresholds hrThresholds = new RTRTHealthReportThresholds();

        final RTRTBuildAction bAction = new RTRTBuildAction(null, rRule,
                new GlobalRate(), hrThresholds);

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

        GlobalRate rate = new GlobalRate();
        rate.getFunction().setRatio(1, 1);
        rate.getExit().setRatio(1, 1);
        rate.getDecision().setRatio(1, 1);
        rate.getStatBlock().setRatio(2, 2);
        rate.getBasicCond().setRatio(2, 2);
        rate.getModifCond().setRatio(2, 2);
        rate.getMultCond().setRatio(2, 2);

        final RTRTBuildAction bAction = RTRTBuildAction.load(null, null, null, rate,
                hrThresholds);
        Assert.assertNotNull(bAction);
        Assert.assertEquals(100, bAction.getFunctionAndExitCoverage().getPercentage());
        Assert.assertEquals(100, bAction.getStatBlockCoverage().getPercentage());
        Assert.assertEquals(100, bAction.getBasicCondCoverage().getPercentage());
        Assert.assertEquals(100, bAction.getModifCondCoverage().getPercentage());
        Assert.assertEquals(100, bAction.getMultCondCoverage().getPercentage());
        Assert.assertFalse(bAction.getCallCoverage().isInitialized());
        Assert.assertFalse(bAction.getLoopCoverage().isInitialized());
        Assert.assertFalse(bAction.getImplBlockCoverage().isInitialized());

        final HealthReport hr = bAction.getBuildHealth();
        Assert.assertNotNull(hr);
        Assert.assertEquals("Coverage: Calls 0/0 (0%).Implicit Blocks 0/0 (0%).Loops 0/0 (0%). ",
                hr.getDescription());
        Assert.assertEquals(0, hr.getScore());

    }
}
