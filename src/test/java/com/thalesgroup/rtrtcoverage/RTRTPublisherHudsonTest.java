package com.thalesgroup.rtrtcoverage;

import hudson.model.Action;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import junit.framework.Assert;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * @author Sebastien Barbier
 * @version 1.0
 */
public class RTRTPublisherHudsonTest extends HudsonTestCase {

    @Test
    public void testInit() throws Exception {

        final FreeStyleProject hudson = createFreeStyleProject();
        final FreeStyleBuild build = hudson.createExecutable();

        final RTRTPublisher publisher = new RTRTPublisher();

        final Action action = publisher
                .getProjectAction((hudson.model.AbstractProject<?, ?>) hudson);
        Assert.assertNotNull(action);
        Assert.assertEquals("rtrtcoverage", RTRTPublisher.getRTRTReport(build)
                .getName());
        Assert.assertEquals("rtrtcodesource",
                RTRTPublisher.getRTRTCodeSource(build).getName());
    }

}
