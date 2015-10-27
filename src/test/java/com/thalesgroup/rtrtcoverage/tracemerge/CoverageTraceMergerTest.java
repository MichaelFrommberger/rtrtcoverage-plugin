package com.thalesgroup.rtrtcoverage.tracemerge;

import hudson.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
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

        String testName = "T11 \"SAMC_CROSSTALK_1/1\"";
        Assert.assertEquals(4, fileCov.getNodesForTest(testName).
                get("samc_crosstalk_send_CAN_compatibility").getTestRate(testName, BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(6, fileCov.getNodesForTest(testName).
                get("samc_crosstalk_send_CAN_compatibility").getTestRate(testName, BranchType.TB_STATEMENTS).getTotal());
        Assert.assertEquals("66.7%", fileCov.getNodesForTest(testName).
                get("samc_crosstalk_send_CAN_compatibility").getTestRate(testName, BranchType.TB_STATEMENTS).getPercentRatio());
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

        String testName = "T74 \"SACO_RECEIVE_DATA/3\"";
        Assert.assertEquals(2, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_MODIFIEDS).getTotal());
        Assert.assertEquals("50.0%", fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_MODIFIEDS).getPercentRatio());

        // by test
        testName = "T13 \"SACO_RECEIVE_DATA/1\"";
        Assert.assertEquals(1, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(7, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("14.3%", fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(3, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(21, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(12, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(2, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(28, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_BASICS).getTotal());

                Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                      get("saco_receive_data").getTestRate(testName, BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(6, fileCov.getNodesForTest(testName).
                get("saco_receive_data").getTestRate(testName, BranchType.TE_MULTIPLES).getTotal());



        Assert.assertEquals(1, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TP_FUNCTIONS).getCoveredNumber()
                + fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TP_EXITS).getCoveredNumber());
        Assert.assertEquals(2, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TP_FUNCTIONS).getTotal()
                + fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TP_EXITS).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TA_CALLS).getCoveredNumber());
        Assert.assertEquals(4, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TA_CALLS).getTotal());
        Assert.assertEquals("25.0%", fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TA_CALLS).getPercentRatio());

        Assert.assertEquals(2, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TB_STATEMENTS).getCoveredNumber());
        Assert.assertEquals(11, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TB_STATEMENTS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TB_IMPLICIT).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TB_IMPLICIT).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TB_LOOPS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TB_LOOPS).getTotal());

        Assert.assertEquals(1, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TE_BASICS).getCoveredNumber());
        Assert.assertEquals(10, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TE_BASICS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TE_MODIFIEDS).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TE_MODIFIEDS).getTotal());

        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TE_MULTIPLES).getCoveredNumber());
        Assert.assertEquals(0, fileCov.getNodesForTest(testName).
                get("read_load_pn_message").getTestRate(testName, BranchType.TE_MULTIPLES).getTotal());


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
    }
    
    @Test
    public void traceMerger_Ada() throws Exception {
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("./FDC/ada.fdc")
                .getPath()));
        final File tioPath = new File(this
                .getClass()
                .getResource(
                        "./result/ada.tio")
                        .getPath());
        final CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FdcReader fdcReader = new FdcReader();
        final FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("4907780d");
        fileCovDef.setCrc("3c2b766a");
        fileCoverageDefs.add(fileCovDef);
        final List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        final FileInputStream ips = new FileInputStream(tioPath);
        final TioReader2 tioReader = new TioReader2(ips);
        traces.add(tioReader.readTio());
        final List<FileCoverage> result = coverageTraceMerger.merge(
                fileCoverageDefs, traces);

        Assert.assertNotNull(result);
        final FileCoverage fileCov = result.get(0);
        Assert.assertNotNull(fileCov);
        // nom du fichier couvert : CODECOVERAGE.ADB
        Assert.assertEquals("CODECOVERAGE.ADB", fileCov.getSourceFileName());
        // les noms des tests couvrant le fichier :
        Set<String> testNames = fileCov.getTestNames();
        // NT "SIMPLECONDITION/1" 4 2
        Assert.assertTrue(testNames.contains("T4 \"SIMPLECONDITION/1\""));
        // NT "SIMPLECONDITION/2" C 2
        Assert.assertTrue(testNames.contains("T12 \"SIMPLECONDITION/2\""));
        // NT "CALL/1" 61 41
        Assert.assertTrue(testNames.contains("T22 \"CALL/1\""));
        // NT "SIMPLECONDITIONWIT/1" D1 B1
        Assert.assertTrue(testNames.contains("T29 \"SIMPLECONDITIONWIT/1\""));
        // NT "SIMPLECONDITIONWIT/2" 52 B1
        Assert.assertTrue(testNames.contains("T37 \"SIMPLECONDITIONWIT/2\""));
        // NT "ANDCONDITION/1" F2 D2
        Assert.assertTrue(testNames.contains("T47 \"ANDCONDITION/1\""));
        // NT "ANDCONDITION/2" 73 D2
        Assert.assertTrue(testNames.contains("T55 \"ANDCONDITION/2\""));
        // NT "ANDCONDITION/3" F3 D2
        Assert.assertTrue(testNames.contains("T63 \"ANDCONDITION/3\""));
        // NT "ORCONDITION/1" 94 74
        Assert.assertTrue(testNames.contains("T73 \"ORCONDITION/1\""));
        // NT "ORCONDITION/2" 15 74
        Assert.assertTrue(testNames.contains("T81 \"ORCONDITION/2\""));
        // NT "ORCONDITION/3" 95 74
        Assert.assertTrue(testNames.contains("T89 \"ORCONDITION/3\""));
        // NT "SUM/1" 36 16
        Assert.assertTrue(testNames.contains("T99 \"SUM/1\""));
        // NT "SUM/2" B6 16
        Assert.assertTrue(testNames.contains("T107 \"SUM/2\""));
        // NT "SUM/3" 37 16
        Assert.assertTrue(testNames.contains("T115 \"SUM/3\""));
        // NT "CHECKCOVERAGESTATE/1" D7 B7
        Assert.assertTrue(testNames.contains("T125 \"CHECKCOVERAGESTATE/1\""));
        // NT "CHECKCOVERAGESTATE/2" A8 B7
        Assert.assertTrue(testNames.contains("T138 \"CHECKCOVERAGESTATE/2\""));
        // NT "CHECKCOVERAGESTATE/3" 99 B7
        Assert.assertTrue(testNames.contains("T153 \"CHECKCOVERAGESTATE/3\""));
        // NT "CHECKCOVERAGESTATE/4" 8A B7
        String testName = "T168 \"CHECKCOVERAGESTATE/4\"";
        Assert.assertTrue(testNames.contains(testName));
        Map<String,NodeCoverage> nodes = fileCov.getNodesForTest(testName);
        // ce test couvre la fonction CheckCoverageStatement
        String nodeName = (String)nodes.keySet().toArray()[0];
        Assert.assertEquals("CheckCoverageStatement(cond1, cond2, cond3, cond4, cond5: in boolean) return float",
        		nodeName);
        NodeCoverage node = nodes.get(nodeName);
        // ce test couvre les branches suivantes (dans CheckCoverageStatement) :
        //TP 01 1 => BRANCH MARK=TP ID=16 SUM=0@function
        //TP 11 1 => BRANCH MARK=TP ID=17 SUM=1@return (retValue);
		//TB 31 1 => BRANCH MARK=TB ID=19 SUM=20@begin
		//TB 51 1 => -POPUP BRANCH MARK=TB ID=21 SUM=21@implicit else
		//TB 91 1 => BRANCH MARK=TB ID=25 SUM=20@when others => Detect_switch_case:=set_switch(4);
		//TB B1 1 => -POPUP BRANCH MARK=TB ID=27 SUM=21@implicit else
		//TB E1 1 => BRANCH MARK=TB ID=30 SUM=22@2 loops or more
		//TB F1 2 => BRANCH MARK=TB ID=31 SUM=20@@POPUP@loop@-POPUP@Logical blocks:
		//TB 22 1 => BRANCH MARK=TB ID=34 SUM=22@2 loops or more
		//TB 32 23 => BRANCH MARK=TB ID=35 SUM=20@@POPUP@loop@-POPUP@Logical blocks:
        List<String> hitBranches = Arrays.asList(new String[] {
        		"TP16", "TP17", "TB19", "TB21", "TB25", "TB27", "TB30", "TB31", "TB34", "TB35"});
        for (String hitBranch: hitBranches) {
        	Assert.assertNotNull(node.getTestBranchCoverage(testName, hitBranch));
        	Assert.assertTrue(node.getTestBranchCoverage(testName, hitBranch).isCovered());
        }
        for (IBranchCoverage branchCov: node.getTestBranchCoverages(testName)) {
        	if (!hitBranches.contains(branchCov.getMarkId())) {
        		Assert.assertFalse(
        				"Branch " + branchCov.getMarkId() + " is not expected to be covered",
        				branchCov.isCovered());
        	}
        }
    }
}
