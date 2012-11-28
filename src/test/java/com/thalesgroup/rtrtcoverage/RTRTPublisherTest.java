package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;
import hudson.tasks.BuildStepMonitor;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Sebastien Barbier
 */
public class RTRTPublisherTest {

    @Test
    public void testLocateReports() throws Exception {

        // Create a temporary workspace in the system
        final File w = File.createTempFile("workspace", ".test");
        w.delete();
        w.mkdir();
        w.deleteOnExit();
        final FilePath workspace = new FilePath(w);

        // Create 4 files in the workspace
        final File f1 = File.createTempFile("mycoverage", ".cio", w);
        f1.deleteOnExit();
        final File f2 = File.createTempFile("othercoverage", ".cio", w);
        f2.deleteOnExit();
        final File f3 = File.createTempFile("mycoverage", ".cio", w);
        f3.deleteOnExit();
        final File f4 = File.createTempFile("othercoverage", ".cio", w);
        f4.deleteOnExit();

        // Create a folder and move there 2 files
        final File d1 = new File(workspace.child("subdir").getRemote());
        d1.mkdir();
        d1.deleteOnExit();

        final File f5 = new File(workspace.child(d1.getName())
                .child(f3.getName()).getRemote());
        final File f6 = new File(workspace.child(d1.getName())
                .child(f4.getName()).getRemote());
        f3.renameTo(f5);
        f4.renameTo(f6);
        f5.deleteOnExit();
        f6.deleteOnExit();

        // Look for files in the entire workspace recursively without providing
        // the includes parameter
        FilePath[] reports = RTRTPublisher.locateCoverageReports(workspace,
                "**/*coverage*.cio");
        Assert.assertEquals(4, reports.length);

        // Generate a includes string and look for files
        final String includes = f1.getName() + "; " + f2.getName() + "; "
                + d1.getName();
        reports = RTRTPublisher.locateCoverageReports(workspace, includes);
        Assert.assertEquals(4, reports.length);

        // Save files in local workspace
        final FilePath local = workspace.child("coverage_localfolder");
        RTRTPublisher.saveCoverageReports(local, reports);
        Assert.assertEquals(4, local.list().size());
        local.deleteRecursive();

    }

    @Test
    public void testFdcReports() throws Exception {

        // Create a temporary workspace in the system
        final File w = File.createTempFile("workspace", ".test");
        w.delete();
        w.mkdir();
        w.deleteOnExit();
        final FilePath workspace = new FilePath(w);

        // Create 4 files in the workspace
        final File f1 = File.createTempFile("mytest", ".fdc", w);
        f1.deleteOnExit();
        final File f2 = File.createTempFile("othertest", ".fdc", w);
        f2.deleteOnExit();
        final File f3 = File.createTempFile("mytest", ".fdc", w);
        f3.deleteOnExit();
        final File f4 = File.createTempFile("othertest", ".fdc", w);
        f4.deleteOnExit();

        // Create a folder and move there 2 files
        final File d1 = new File(workspace.child("subdir").getRemote());
        d1.mkdir();
        d1.deleteOnExit();

        final File f5 = new File(workspace.child(d1.getName())
                .child(f3.getName()).getRemote());
        final File f6 = new File(workspace.child(d1.getName())
                .child(f4.getName()).getRemote());
        f3.renameTo(f5);
        f4.renameTo(f6);
        f5.deleteOnExit();
        f6.deleteOnExit();

        // Look for files in the entire workspace recursively without providing
        // the includes parameter
        FilePath[] reports = workspace.list("**/*.fdc");
        Assert.assertEquals(4, reports.length);

        // Save files in local workspace
        final FilePath local = workspace.child("fdc_localfolder");
        RTRTPublisher.saveCodeSourceFiles(local, reports);
        Assert.assertEquals(4, local.list().size());
        local.deleteRecursive();

    }

    @Test
    public void testPublisher() throws Exception {

        // Create a publisher
        final RTRTPublisher publisher = new RTRTPublisher();

        Assert.assertEquals(RTRTPublisher.DESCRIPTOR, publisher.getDescriptor());
        Assert.assertEquals(BuildStepMonitor.BUILD,
                publisher.getRequiredMonitorService());
        Assert.assertEquals("Record RTRT coverage report",
                RTRTPublisher.DESCRIPTOR.getDisplayName());
        Assert.assertTrue(publisher.getDescriptor().isApplicable(null));

    }

}