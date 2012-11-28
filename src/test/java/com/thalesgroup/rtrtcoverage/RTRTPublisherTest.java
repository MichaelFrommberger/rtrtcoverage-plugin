package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;
import hudson.tasks.BuildStepMonitor;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.serializablerates.FileRate;
import com.thalesgroup.rtrtcoverage.serializablerates.GlobalRate;
import com.thalesgroup.rtrtcoverage.serializablerates.NodeRate;
import com.thalesgroup.rtrtcoverage.serializablerates.TestRate;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.GlobalCoverage;

/**
 * @author Sebastien Barbier
 */
public class RTRTPublisherTest {

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

    @Test
    public void testBuildRatesData() throws Exception {
        FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./tracemerge/FDC/SACO_RECEIVE_DATA.FDC")
                .getPath()));
        File tioPath = new File(this
                .getClass()
                .getResource(
                        "./tracemerge/result/SACO_RECEIVE_DATA.TIO")
                        .getPath());
        CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        FdcReader fdcReader = new FdcReader();
        FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("4e721266");
        fileCovDef.setCrc("797d07b2");
        fileCoverageDefs.add(fileCovDef);
        List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        FileInputStream ips = new FileInputStream(tioPath);
        TioReader2 tioReader = new TioReader2(ips);
        traces.add(tioReader.readTio());
        List<FileCoverage> result = coverageTraceMerger.merge(
                fileCoverageDefs, traces);
        GlobalCoverage globalCov = new GlobalCoverage(result);

        GlobalRate globalRate = RTRTPublisher.buildRatesData(globalCov);

        Assert.assertNotNull(globalRate);

        final FileRate fileRate = globalRate.getFileRates().get(0);

        Assert.assertEquals("SACO_RECEIVE_DATA.C", fileRate.getSourceFileName());

        Assert.assertEquals(4, fileRate.getNodeRates().size());
        TestRate testRate = null;
        for (TestRate test : fileRate.getTestRates()) {
            if (test.getTestName().equals("T74")) {
                testRate = test;
                break;
            }
        }
        Assert.assertNotNull(testRate);
        NodeRate nodeRate = null;
        for (NodeRate node : testRate.getNodeRates()) {
            if (node.getNodeName().equals("saco_receive_data")) {
                nodeRate = node;
                break;
            }
        }
        Assert.assertNotNull(nodeRate);
        Assert.assertEquals(2, Math.round(nodeRate.get(BranchType.TE_MODIFIEDS).getNumerator()));
        Assert.assertEquals(4, Math.round(nodeRate.get(BranchType.TE_MODIFIEDS).getDenominator()));


        // by test

        for (TestRate test : fileRate.getTestRates()) {
            if (test.getTestName().equals("T13")) {
                testRate = test;
                break;
            }
        }
        Assert.assertNotNull(testRate);
        for (NodeRate node : testRate.getNodeRates()) {
            if (node.getNodeName().equals("saco_receive_data")) {
                nodeRate = node;
                break;
            }
        }
        Assert.assertNotNull(nodeRate);

        Assert.assertEquals(1, (int) nodeRate.getFunctionAndExit().getNumerator());
        Assert.assertEquals(2, (int) nodeRate.getFunctionAndExit().getDenominator());

        Assert.assertEquals(1, (int) nodeRate.getCall().getNumerator());
        Assert.assertEquals(7, (int) nodeRate.getCall().getDenominator());

        Assert.assertEquals(3, (int) nodeRate.getStatBlock().getNumerator());
        Assert.assertEquals(21, (int) nodeRate.getStatBlock().getDenominator());

        Assert.assertEquals(0, (int) nodeRate.getImplBlock().getNumerator());
        Assert.assertEquals(0, (int) nodeRate.getImplBlock().getDenominator());

        Assert.assertEquals(3, (int) nodeRate.getDecision().getNumerator());
        Assert.assertEquals(21, (int) nodeRate.getDecision().getDenominator());

        Assert.assertEquals(1, (int) nodeRate.getLoop().getNumerator());
        Assert.assertEquals(12, (int) nodeRate.getLoop().getDenominator());

        Assert.assertEquals(2, (int) nodeRate.getBasicCond().getNumerator());
        Assert.assertEquals(28, (int) nodeRate.getBasicCond().getDenominator());

        Assert.assertEquals(0, (int) nodeRate.getModifCond().getNumerator());
        Assert.assertEquals(4, (int) nodeRate.getModifCond().getDenominator());

        Assert.assertEquals(0, (int) nodeRate.getMultCond().getNumerator());
        Assert.assertEquals(6, (int) nodeRate.getMultCond().getDenominator());

        // global
        // saco_receive_data
        for (NodeRate node : fileRate.getNodeRates()) {
            if (node.getNodeName().equals("saco_receive_data")) {
                nodeRate = node;
                break;
            }
        }
        Assert.assertNotNull(nodeRate);

        Assert.assertEquals(2, (int) nodeRate.getFunctionAndExit().getNumerator());
        Assert.assertEquals(2, (int) nodeRate.getFunctionAndExit().getDenominator());

        Assert.assertEquals(4, (int) nodeRate.getCall().getNumerator());
        Assert.assertEquals(7, (int) nodeRate.getCall().getDenominator());

        Assert.assertEquals(19, (int) nodeRate.getStatBlock().getNumerator());
        Assert.assertEquals(21, (int) nodeRate.getStatBlock().getDenominator());

        Assert.assertEquals(0, (int) nodeRate.getImplBlock().getNumerator());
        Assert.assertEquals(0, (int) nodeRate.getImplBlock().getDenominator());

        Assert.assertEquals(19, (int) nodeRate.getDecision().getNumerator());
        Assert.assertEquals(21, (int) nodeRate.getDecision().getDenominator());

        Assert.assertEquals(5, (int) nodeRate.getLoop().getNumerator());
        Assert.assertEquals(12, (int) nodeRate.getLoop().getDenominator());

        Assert.assertEquals(25, (int) nodeRate.getBasicCond().getNumerator());
        Assert.assertEquals(28, (int) nodeRate.getBasicCond().getDenominator());

        Assert.assertEquals(3, (int) nodeRate.getModifCond().getNumerator());
        Assert.assertEquals(4, (int) nodeRate.getModifCond().getDenominator());

        Assert.assertEquals(5, (int) nodeRate.getMultCond().getNumerator());
        Assert.assertEquals(6, (int) nodeRate.getMultCond().getDenominator());

        // read_du_identifier
        for (NodeRate node : fileRate.getNodeRates()) {
            if (node.getNodeName().equals("saco_receive_data")) {
                nodeRate = node;
                break;
            }
        }
        Assert.assertNotNull(nodeRate);
        Assert.assertEquals(2, (int) nodeRate.getFunctionAndExit().getNumerator());
        Assert.assertEquals(2, (int) nodeRate.getFunctionAndExit().getDenominator());
    }

}