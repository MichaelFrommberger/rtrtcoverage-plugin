package com.thalesgroup.rtrtcoverage;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Sebastien Barbier
 * @version 1.0
 */
public class RTRTProjectActionTest {

    @Test
    public void test() throws Exception {

        final RTRTProjectAction pa = new RTRTProjectAction(null);

        Assert.assertEquals("graph.gif", pa.getIconFileName());
        Assert.assertEquals("Coverage Trend", pa.getDisplayName());
        Assert.assertEquals("rtrtcoverage", pa.getUrlName());

    }
}
