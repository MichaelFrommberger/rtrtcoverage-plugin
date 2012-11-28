package com.thalesgroup.rtrtcoverage.tracemerge;

import hudson.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchDefinitionType;
import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;

public class CoverageTraceMergerTest {

    /**
     * @throws Exception
     */
    @Test
    public void testMerger() throws Exception {
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./FDC/BUFFERMANAGER.FDC").getPath()));
        final File tioPath = new File(this.getClass()
                .getResource("./result/BUFFERMANAGER.TIO").getPath());
        final CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FdcReader fdcReader = new FdcReader();
        final FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("13324134");
        fileCovDef.setCrc("175D0F6C");
        fileCoverageDefs.add(fileCovDef);
        final List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        final FileInputStream ips = new FileInputStream(tioPath);
        final TioReader2 tioReader = new TioReader2(ips);
        traces.add(tioReader.readTio());
        final List<FileCoverage> result = coverageTraceMerger.merge(
                fileCoverageDefs, traces);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.get(0));

        final FileCoverage fileCov = result.get(0);

        Assert.assertEquals("BUFFERMANAGER.C", fileCov
                .getFileCoverageDefinition().getSourceName());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.PROC, "0").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.PROC, "1").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.PROC, "2").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.PROC, "3").getHits());
        Assert.assertEquals(2,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "0").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.LOOP, "1").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.LOOP, "2").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.LOOP, "3").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "4").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "5").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.LOOP, "6").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.LOOP, "7").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.LOOP, "8").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "9").getHits());

    }

    @Test
    public void testMerger2() throws Exception {
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./FDC/SAMC_CROSSTALK_SEND_CAN_COMPATIBILITY.FDC")
                .getPath()));
        final File tioPath = new File(this
                .getClass()
                .getResource(
                        "./result/SAMC_CROSSTALK_SEND_CAN_COMPATIBILITY.TIO")
                        .getPath());
        final CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FdcReader fdcReader = new FdcReader();
        final FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("374234");
        fileCovDef.setCrc("6493821");
        fileCoverageDefs.add(fileCovDef);
        final List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        final FileInputStream ips = new FileInputStream(tioPath);
        final TioReader2 tioReader = new TioReader2(ips);
        traces.add(tioReader.readTio());
        final List<FileCoverage> result = coverageTraceMerger.merge(
                fileCoverageDefs, traces);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.get(0));

        final FileCoverage fileCov = result.get(0);

        Assert.assertEquals("SAMC_CROSSTALK_SEND_CAN_COMPATIBILITY.C", fileCov
                .getFileCoverageDefinition().getSourceName());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.CALL, "0").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.CALL, "1").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "0").getHits());
        Assert.assertEquals(0,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "1").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "2").getHits());
        Assert.assertEquals(0,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "3").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "4").getHits());
        Assert.assertEquals(1,
                fileCov.getBranch(BranchDefinitionType.BLOCK, "8").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.LOOP, "5").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.LOOP, "6").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.LOOP, "7").getHits());
        Assert.assertEquals(0, fileCov
                .getBranch(BranchDefinitionType.PROC, "0").getHits());
        Assert.assertEquals(1, fileCov
                .getBranch(BranchDefinitionType.PROC, "1").getHits());

    }
}
