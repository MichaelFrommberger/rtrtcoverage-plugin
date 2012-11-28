package com.thalesgroup.rtrtcoverage.tracemerge;

import hudson.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;

public class CoverageTraceMergerTest {

    /**
     * @throws Exception
     */
    @Test
    public void testMerger() throws Exception {
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./FDC/SAMC_CROSSTALK_SEND_CAN_COMPATIBILITY.FDC").getPath()));
        final File tioPath = new File(this.getClass()
                .getResource("./result/SAMC_CROSSTALK_SEND_CAN_COMPATIBILITY.TIO").getPath());
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

        Assert.assertEquals(4, fileCov.getNodesForTest("T11").
                get("samc_crosstalk_send_CAN_compatibility").getTestRate("T11", BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(6, fileCov.getNodesForTest("T11").
                get("samc_crosstalk_send_CAN_compatibility").getTestRate("T11", BranchType.TB_STATEMENTS).getTotal());
        Assert.assertEquals("66.7%", fileCov.getNodesForTest("T11").
                get("samc_crosstalk_send_CAN_compatibility").getTestRate("T11", BranchType.TB_STATEMENTS).getPercentRatio());
    }

    @Test
    public void testMerger2() throws Exception {
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./FDC/SACO_RECEIVE_DATA.FDC")
                .getPath()));
        final File tioPath = new File(this
                .getClass()
                .getResource(
                        "./result/SACO_RECEIVE_DATA.TIO")
                        .getPath());
        final CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FdcReader fdcReader = new FdcReader();
        final FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("4e721266");
        fileCovDef.setCrc("797d07b2");
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

        Assert.assertEquals("SACO_RECEIVE_DATA.C", fileCov.getSourceFileName());

        Assert.assertEquals(4, fileCov.getNodes().size());

        Assert.assertEquals(2, fileCov.getNodesForTest("T74").
                get("saco_receive_data").getTestRate("T74", BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodesForTest("T74").
                get("saco_receive_data").getTestRate("T74", BranchType.TE_MODIFIEDS).getTotal());
        Assert.assertEquals("50.0%", fileCov.getNodesForTest("T74").
                get("saco_receive_data").getTestRate("T74", BranchType.TE_MODIFIEDS).getPercentRatio());

        // by test
        Assert.assertEquals(1, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(7, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("14.3%", fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(3, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(21, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(12, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(2, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(28, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TE_BASICS).getTotal());

                Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                      get("saco_receive_data").getTestRate("T13", BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(6, fileCov.getNodesForTest("T13").
                get("saco_receive_data").getTestRate("T13", BranchType.TE_MULTIPLES).getTotal());



        Assert.assertEquals(1, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("25.0%", fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(2, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(11, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(10, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TE_BASICS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest("T13").
                get("read_load_pn_message").getTestRate("T13", BranchType.TE_MULTIPLES).getTotal());


        // global
        // saco_receive_data
        Assert.assertEquals(2, fileCov.getNodes().get(3).getGlobalRate(BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodes().get(3).getGlobalRate(BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodes().get(3).getGlobalRate(BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodes().get(3).getGlobalRate(BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(4, fileCov.getNodes().get(3).getGlobalRate(BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(7, fileCov.getNodes().get(3).getGlobalRate(BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("57.1%", fileCov.getNodes().get(3).getGlobalRate(BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(19, fileCov.getNodes().get(3).getGlobalRate(BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(21, fileCov.getNodes().get(3).getGlobalRate(BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(3).getGlobalRate(BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(3).getGlobalRate(BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(5, fileCov.getNodes().get(3).getGlobalRate(BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(12, fileCov.getNodes().get(3).getGlobalRate(BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(25, fileCov.getNodes().get(3).getGlobalRate(BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(28, fileCov.getNodes().get(3).getGlobalRate(BranchType.TE_BASICS).getTotal());

        Assert.assertEquals(3, fileCov.getNodes().get(3).getGlobalRate(BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodes().get(3).getGlobalRate(BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(5, fileCov.getNodes().get(3).getGlobalRate(BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(6, fileCov.getNodes().get(3).getGlobalRate(BranchType.TE_MULTIPLES).getTotal());

        // read_du_identifier
        Assert.assertEquals(2, fileCov.getNodes().get(0).getGlobalRate(BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodes().get(0).getGlobalRate(BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodes().get(0).getGlobalRate(BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodes().get(0).getGlobalRate(BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(4, fileCov.getNodes().get(0).getGlobalRate(BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodes().get(0).getGlobalRate(BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("100.0%", fileCov.getNodes().get(0).getGlobalRate(BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(9, fileCov.getNodes().get(0).getGlobalRate(BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(11, fileCov.getNodes().get(0).getGlobalRate(BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(8, fileCov.getNodes().get(0).getGlobalRate(BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(10, fileCov.getNodes().get(0).getGlobalRate(BranchType.TE_BASICS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(0).getGlobalRate(BranchType.TE_MULTIPLES).getTotal());

        // read_load_pn_message
        Assert.assertEquals(2, fileCov.getNodes().get(2).getGlobalRate(BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodes().get(2).getGlobalRate(BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodes().get(2).getGlobalRate(BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodes().get(2).getGlobalRate(BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(4, fileCov.getNodes().get(2).getGlobalRate(BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodes().get(2).getGlobalRate(BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("100.0%", fileCov.getNodes().get(2).getGlobalRate(BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(9, fileCov.getNodes().get(2).getGlobalRate(BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(11, fileCov.getNodes().get(2).getGlobalRate(BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(8, fileCov.getNodes().get(2).getGlobalRate(BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(10, fileCov.getNodes().get(2).getGlobalRate(BranchType.TE_BASICS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(2).getGlobalRate(BranchType.TE_MULTIPLES).getTotal());

        // compute_dcp_pn_sn
        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodes().get(1).getGlobalRate(BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodes().get(1).getGlobalRate(BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodes().get(1).getGlobalRate(BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(8, fileCov.getNodes().get(1).getGlobalRate(BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("0.0%", fileCov.getNodes().get(1).getGlobalRate(BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(33, fileCov.getNodes().get(1).getGlobalRate(BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(48, fileCov.getNodes().get(1).getGlobalRate(BranchType.TE_BASICS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(14, fileCov.getNodes().get(1).getGlobalRate(BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodes().get(1).getGlobalRate(BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(20, fileCov.getNodes().get(1).getGlobalRate(BranchType.TE_MULTIPLES).getTotal());

    }

    @Test
    public void testGlobalCoverage() throws Exception {
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./FDC/SACO_RECEIVE_DATA.FDC")
                .getPath()));
        final File tioPath = new File(this
                .getClass()
                .getResource(
                        "./result/SACO_RECEIVE_DATA.TIO")
                        .getPath());
        final CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FdcReader fdcReader = new FdcReader();
        final FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("4e721266");
        fileCovDef.setCrc("797d07b2");
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

        // Assert.assertEquals(4, globalCov.getNodeGlobalCoverage("saco_receive_data").getBranchGlobalCoverage(BranchType.TA).getGlobalHits());
        // Assert.assertEquals(7, globalCov.getNodeGlobalCoverage("saco_receive_data").getBranchGlobalCoverage(BranchType.TA).getNumber());

        // Assert.assertEquals(19, globalCov.getNodeGlobalCoverage("saco_receive_data").getBranchGlobalCoverage(BranchType.TBS).getGlobalHits());
        // Assert.assertEquals(21, globalCov.getNodeGlobalCoverage("saco_receive_data").getBranchGlobalCoverage(BranchType.TBS).getNumber());

        // Assert.assertEquals(25, globalCov.getNodeGlobalCoverage("saco_receive_data").getBranchGlobalCoverage(BranchType.TEB).getGlobalHits());
        // Assert.assertEquals(28, globalCov.getNodeGlobalCoverage("saco_receive_data").getBranchGlobalCoverage(BranchType.TEB).getNumber());
        int toto = 0;
    }
}
