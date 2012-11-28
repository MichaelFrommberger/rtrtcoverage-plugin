package com.thalesgroup.rtrtcoverage;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sebastien Barbier
 * @version 1.0
 *
 */
public class RTRTHealthReportThresholdsTest {

    @Test
    public void test() throws Exception {

        final RTRTHealthReportThresholds hrThresholds = new RTRTHealthReportThresholds();

        hrThresholds.setMinFunction(0);
        hrThresholds.setMaxFunction(100);
        hrThresholds.setMinCall(0);
        hrThresholds.setMaxCall(120);
        hrThresholds.setMinStatBlock(0);
        hrThresholds.setMaxStatBlock(100);
        hrThresholds.setMinImplBlock(0);
        hrThresholds.setMaxImplBlock(100);
        hrThresholds.setMinDecision(0);
        hrThresholds.setMaxDecision(100);
        hrThresholds.setMinLoop(0);
        hrThresholds.setMaxLoop(100);
        hrThresholds.setMinBasicCond(-10);
        hrThresholds.setMaxBasicCond(100);
        hrThresholds.setMinModifCond(0);
        hrThresholds.setMaxModifCond(100);
        hrThresholds.setMinMultCond(0);
        hrThresholds.setMaxMultCond(100);

        hrThresholds.ensureValid();

        Assert.assertEquals(0, hrThresholds.getMinFunction());
        Assert.assertEquals(100, hrThresholds.getMaxFunction());
        Assert.assertEquals(0, hrThresholds.getMinCall());
        Assert.assertEquals(100, hrThresholds.getMaxCall());
        Assert.assertEquals(0, hrThresholds.getMinStatBlock());
        Assert.assertEquals(100, hrThresholds.getMaxStatBlock());
        Assert.assertEquals(0, hrThresholds.getMinImplBlock());
        Assert.assertEquals(100, hrThresholds.getMaxImplBlock());
        Assert.assertEquals(0, hrThresholds.getMinDecision());
        Assert.assertEquals(100, hrThresholds.getMaxDecision());
        Assert.assertEquals(0, hrThresholds.getMinLoop());
        Assert.assertEquals(100, hrThresholds.getMaxLoop());
        Assert.assertEquals(0, hrThresholds.getMinBasicCond());
        Assert.assertEquals(100, hrThresholds.getMaxBasicCond());
        Assert.assertEquals(0, hrThresholds.getMinModifCond());
        Assert.assertEquals(100, hrThresholds.getMaxModifCond());
        Assert.assertEquals(0, hrThresholds.getMinMultCond());
        Assert.assertEquals(100, hrThresholds.getMaxMultCond());
    }

}
