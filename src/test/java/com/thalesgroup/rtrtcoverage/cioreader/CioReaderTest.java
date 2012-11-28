package com.thalesgroup.rtrtcoverage.cioreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

public class CioReaderTest {

    @Test
    public void testGeneralAttributes() throws Exception {
        final File file = new File(this.getClass().getResource("ATU.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);
        final boolean[] flags = attributes.getAttributesFlags();
        for (int i = 0; i < flags.length; ++i) {
            if (i == ReportTag.LOOP.ordinal()) {
                Assert.assertFalse(flags[i]);
            } else {
                Assert.assertTrue(flags[i]);
            }
        }
    }

    @Test
    public void testLimitedAttributes() throws Exception {

        final File file = new File(this.getClass().getResource("LIMITED.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);
        Assert.assertFalse(attributes.hasFunction());
        Assert.assertTrue(attributes.hasCall());
        Assert.assertTrue(attributes.hasStatBlock());
        Assert.assertFalse(attributes.hasImplBlock());
        Assert.assertFalse(attributes.hasDecision());
        Assert.assertFalse(attributes.hasLoop());
        Assert.assertTrue(attributes.hasBasicCond());
        Assert.assertFalse(attributes.hasModifCond());
        Assert.assertFalse(attributes.hasMultCond());

    }

    @Test
    public void testLoopAttributes() throws Exception {
        final File file = new File(this.getClass().getResource("LOOP.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);
        Assert.assertTrue(attributes.hasFunction());
        Assert.assertTrue(attributes.hasCall());
        Assert.assertTrue(attributes.hasStatBlock());
        Assert.assertTrue(attributes.hasImplBlock());
        Assert.assertTrue(attributes.hasDecision());
        Assert.assertTrue(attributes.hasLoop());
        Assert.assertFalse(attributes.hasBasicCond());
        Assert.assertFalse(attributes.hasModifCond());
        Assert.assertFalse(attributes.hasMultCond());
    }

    @Test
    public void testOEndingAttributes() throws Exception {
        final File file = new File(this.getClass().getResource("WITHO.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);
        Assert.assertTrue(attributes.hasFunction());
        Assert.assertTrue(attributes.hasCall());
        Assert.assertTrue(attributes.hasStatBlock());
        Assert.assertTrue(attributes.hasImplBlock());
        Assert.assertTrue(attributes.hasDecision());
        Assert.assertTrue(attributes.hasLoop());
        Assert.assertFalse(attributes.hasBasicCond());
        Assert.assertFalse(attributes.hasModifCond());
        Assert.assertFalse(attributes.hasMultCond());
    }

    @Test
    public void testTwoTestsAttributes() throws Exception {
        final File file = new File(this.getClass().getResource("TWOTESTS.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);
        Assert.assertTrue(attributes.hasFunction());
        Assert.assertTrue(attributes.hasCall());
        Assert.assertTrue(attributes.hasStatBlock());
        Assert.assertTrue(attributes.hasImplBlock());
        Assert.assertTrue(attributes.hasDecision());
        Assert.assertTrue(attributes.hasLoop());
        Assert.assertFalse(attributes.hasBasicCond());
        Assert.assertFalse(attributes.hasModifCond());
        Assert.assertFalse(attributes.hasMultCond());
    }

    @Test
    public void testOK() throws Exception {

        final File file = new File(this.getClass().getResource("ATU.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);

        final CioReader cioReader = new CioReader(file);

        Exception exception = null;
        CoverageReportElement coverage = null;

        try {
            coverage = cioReader.populate(attributes.getAttributesFlags(),
                    attributes.getAttributesInformationFlags());
        } catch (final CioException e) {
            exception = e;
        }

        Assert.assertNull(exception);
        Assert.assertNotNull(coverage);

        // Test global coverage
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getFunctionCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getMultipleConditionCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getDecisionCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getStatementBlockCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getBasicConditionCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getModifiedConditionCoverage().getPercentage());
        Assert.assertFalse(coverage.getGlobalCoverage().hasCallCoverage());
        Assert.assertFalse(coverage.getGlobalCoverage()
                .hasImplicitBlockCoverage());

        Assert.assertEquals(8, coverage.getNumberOfGlobalCoverageFiles());

        // Test global coverage by files
        int index = 0;
        CoverageElement report = coverage.getGlobalCoverageForFile(index);
        Assert.assertEquals("STRINGUTILITIES_INITIALIZESTATICCLASS.C",
                report.getNameFile());
        Assert.assertEquals("2/2", report.getFunctionCoverage().toString());
        Assert.assertFalse(report.hasCallCoverage());
        Assert.assertEquals("1/1", report.getStatementBlockCoverage()
                .toString());
        Assert.assertFalse(report.hasImplicitBlockCoverage());
        Assert.assertEquals("1/1", report.getDecisionCoverage().toString());
        Assert.assertFalse(report.hasBasicConditionCoverage());
        Assert.assertFalse(report.hasModifiedConditionCoverage());
        Assert.assertFalse(report.hasMultipleConditionCoverage());
        index = 3;
        report = coverage.getGlobalCoverageForFile(index);
        Assert.assertEquals("STRINGUTILITIES_STATIC_EQUALS.C",
                report.getNameFile());
        Assert.assertEquals("2/2", report.getFunctionCoverage().toString());
        Assert.assertFalse(report.hasCallCoverage());
        Assert.assertEquals("6/6", report.getStatementBlockCoverage()
                .toString());
        Assert.assertFalse(report.hasImplicitBlockCoverage());
        Assert.assertEquals("6/6", report.getDecisionCoverage().toString());
        Assert.assertEquals("12/12", report.getBasicConditionCoverage()
                .toString());
        Assert.assertEquals("5/5", report.getModifiedConditionCoverage()
                .toString());
        Assert.assertEquals("7/7", report.getMultipleConditionCoverage()
                .toString());
        index = 7;
        report = coverage.getGlobalCoverageForFile(index);
        Assert.assertEquals("STRINGUTILITIES_STATIC_NEQUALS.C",
                report.getNameFile());
        Assert.assertEquals(report.getFunctionCoverage().getNumerator(), report
                .getFunctionCoverage().getDenominator());
        Assert.assertFalse(report.hasCallCoverage());
        Assert.assertEquals(report.getStatementBlockCoverage().getNumerator(),
                report.getStatementBlockCoverage().getDenominator());
        Assert.assertFalse(report.hasImplicitBlockCoverage());
        Assert.assertEquals(report.getDecisionCoverage().getNumerator(), report
                .getDecisionCoverage().getDenominator());
        Assert.assertEquals(report.getBasicConditionCoverage().getNumerator(),
                report.getBasicConditionCoverage().getDenominator());
        Assert.assertEquals(report.getModifiedConditionCoverage()
                .getNumerator(), report.getModifiedConditionCoverage()
                .getDenominator());
        Assert.assertEquals(report.getMultipleConditionCoverage()
                .getNumerator(), report.getMultipleConditionCoverage()
                .getDenominator());

        // Test coverage by test
        final CoverageReportTestElement testReport = coverage
                .getCoverageReportForTests();
        Assert.assertNotNull(testReport);
        index = 0;
        testReport.begin();

        // Data for checking
        final HashMap<String, Integer> numberOfFiles = new HashMap<String, Integer>();
        numberOfFiles.put("T11", 1);
        numberOfFiles.put("T20", 2);
        numberOfFiles.put("T63", 2);
        numberOfFiles.put("T107", 2);
        numberOfFiles.put("T152", 2);
        numberOfFiles.put("T197", 3);
        numberOfFiles.put("T241", 3);
        numberOfFiles.put("T286", 3);
        numberOfFiles.put("T332", 3);
        numberOfFiles.put("T378", 4);
        numberOfFiles.put("T403", 4);
        numberOfFiles.put("T419", 4);
        numberOfFiles.put("T444", 4);
        numberOfFiles.put("T466", 4);
        numberOfFiles.put("T489", 4);
        numberOfFiles.put("T512", 4);
        numberOfFiles.put("T536", 4);
        numberOfFiles.put("T561", 4);
        numberOfFiles.put("T586", 4);
        numberOfFiles.put("T611", 4);
        numberOfFiles.put("T636", 5);
        numberOfFiles.put("T658", 5);
        numberOfFiles.put("T681", 5);
        numberOfFiles.put("T704", 5);
        numberOfFiles.put("T728", 5);
        numberOfFiles.put("T753", 5);
        numberOfFiles.put("T777", 5);
        numberOfFiles.put("T802", 5);
        numberOfFiles.put("T827", 5);
        numberOfFiles.put("T852", 6);
        numberOfFiles.put("T853", 4);

        while (testReport.hasNextTest()) {

            final String nameTest = testReport.getNextNameTest();
            Assert.assertNotNull(nameTest);
            Assert.assertNotNull(numberOfFiles.get(nameTest));
            Assert.assertEquals(numberOfFiles.get(nameTest),
                    testReport.getNumberOfFilesInNextTest());
            for (int nbFiles = 0; nbFiles < testReport
                    .getNumberOfFilesInNextTest(); ++nbFiles) {
                Assert.assertNotNull(testReport
                        .getCoverageElementInNextTest(nbFiles));
            }
            ++index;
        }

        Assert.assertEquals(31, index);

    }

    @Test
    public void testOK2() throws Exception {

        final File file = new File(this.getClass()
                .getResource("A350_RTRT7.CIO").toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);

        final CioReader cioReader = new CioReader(file);

        Exception exception = null;
        CoverageReportElement coverage = null;

        try {
            coverage = cioReader.populate(attributes.getAttributesFlags(),
                    attributes.getAttributesInformationFlags());
        } catch (final CioException e) {
            exception = e;
        }

        Assert.assertNull(exception);
        Assert.assertNotNull(coverage);

        // Test global coverage
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getFunctionCoverage().getPercentage());
        Assert.assertEquals(0, coverage.getGlobalCoverage()
                .getMultipleConditionCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getDecisionCoverage().getPercentage());
        Assert.assertEquals(100, coverage.getGlobalCoverage()
                .getStatementBlockCoverage().getPercentage());
        Assert.assertEquals(0, coverage.getGlobalCoverage()
                .getBasicConditionCoverage().getPercentage());
        Assert.assertEquals(0, coverage.getGlobalCoverage()
                .getModifiedConditionCoverage().getPercentage());
        Assert.assertFalse(coverage.getGlobalCoverage().hasCallCoverage());
        Assert.assertFalse(coverage.getGlobalCoverage()
                .hasImplicitBlockCoverage());

        Assert.assertEquals(9, coverage.getNumberOfGlobalCoverageFiles());

        // Test global coverage by files
        int index = 0;
        CoverageElement report = coverage.getGlobalCoverageForFile(index);
        Assert.assertEquals("MAPAIRPORTDATA__CHECKAIRCRAFTLOCALIZATION.C",
                report.getNameFile());
        Assert.assertEquals("2/2", report.getFunctionCoverage().toString());
        Assert.assertFalse(report.hasCallCoverage());
        Assert.assertEquals("3/3", report.getStatementBlockCoverage()
                .toString());
        Assert.assertFalse(report.hasImplicitBlockCoverage());
        Assert.assertEquals("3/3", report.getDecisionCoverage().toString());
        Assert.assertFalse(report.hasBasicConditionCoverage());
        Assert.assertFalse(report.hasModifiedConditionCoverage());
        Assert.assertFalse(report.hasMultipleConditionCoverage());
        index = 3;
        report = coverage.getGlobalCoverageForFile(index);
        Assert.assertEquals("MAPAIRPORTDATA__FORMATACTIVEELEMENTDATA.C",
                report.getNameFile());
        Assert.assertEquals("2/2", report.getFunctionCoverage().toString());
        Assert.assertFalse(report.hasCallCoverage());
        Assert.assertEquals("5/5", report.getStatementBlockCoverage()
                .toString());
        Assert.assertFalse(report.hasImplicitBlockCoverage());
        Assert.assertEquals("5/5", report.getDecisionCoverage().toString());
        Assert.assertEquals("0/0", report.getBasicConditionCoverage()
                .toString());
        Assert.assertEquals("0/0", report.getModifiedConditionCoverage()
                .toString());
        Assert.assertEquals("0/0", report.getMultipleConditionCoverage()
                .toString());
        index = 7;
        report = coverage.getGlobalCoverageForFile(index);
        Assert.assertEquals("MAPAIRPORTDATA_SETAIRPORTTODISPLAY.C",
                report.getNameFile());
        Assert.assertEquals(report.getFunctionCoverage().getNumerator(), report
                .getFunctionCoverage().getDenominator());
        Assert.assertFalse(report.hasCallCoverage());
        Assert.assertEquals(report.getStatementBlockCoverage().getNumerator(),
                report.getStatementBlockCoverage().getDenominator());
        Assert.assertFalse(report.hasImplicitBlockCoverage());
        Assert.assertEquals(report.getDecisionCoverage().getNumerator(), report
                .getDecisionCoverage().getDenominator());
        Assert.assertEquals(report.getBasicConditionCoverage().getNumerator(),
                report.getBasicConditionCoverage().getDenominator());
        Assert.assertEquals(report.getModifiedConditionCoverage()
                .getNumerator(), report.getModifiedConditionCoverage()
                .getDenominator());
        Assert.assertEquals(report.getMultipleConditionCoverage()
                .getNumerator(), report.getMultipleConditionCoverage()
                .getDenominator());

        // Test coverage by test
        final CoverageReportTestElement testReport = coverage
                .getCoverageReportForTests();
        Assert.assertNotNull(testReport);
        index = 0;
        testReport.begin();

        // Data for checking
        final HashMap<String, Integer> numberOfFiles = new HashMap<String, Integer>();
        numberOfFiles.put("T35", 8);
        numberOfFiles.put("T309", 8);
        numberOfFiles.put("T583", 8);
        numberOfFiles.put("T857", 8);
        numberOfFiles.put("T1131", 8);
        numberOfFiles.put("T1405", 8);
        numberOfFiles.put("T1679", 8);
        numberOfFiles.put("T1953", 8);
        numberOfFiles.put("T2227", 8);
        numberOfFiles.put("T2501", 8);
        numberOfFiles.put("T2775", 8);
        numberOfFiles.put("T3049", 8);
        numberOfFiles.put("T3323", 8);
        numberOfFiles.put("T3597", 8);
        numberOfFiles.put("T3871", 8);
        numberOfFiles.put("T4147", 8);
        numberOfFiles.put("T4416", 8);
        numberOfFiles.put("T4685", 8);
        numberOfFiles.put("T4954", 8);
        numberOfFiles.put("T5223", 8);
        numberOfFiles.put("T5492", 8);
        numberOfFiles.put("T5761", 8);
        numberOfFiles.put("T6035", 8);
        numberOfFiles.put("T6309", 8);
        numberOfFiles.put("T6580", 8);
        numberOfFiles.put("T6851", 8);
        numberOfFiles.put("T7122", 9);
        numberOfFiles.put("T7452", 9);
        numberOfFiles.put("T7782", 9);
        numberOfFiles.put("T8115", 9);
        numberOfFiles.put("T8449", 9);
        numberOfFiles.put("T8783", 9);
        numberOfFiles.put("T9117", 9);
        numberOfFiles.put("T9450", 9);
        numberOfFiles.put("T9783", 9);
        numberOfFiles.put("T10116", 9);
        numberOfFiles.put("T10450", 8);
        numberOfFiles.put("T10784", 9);
        numberOfFiles.put("T11117", 8);
        numberOfFiles.put("T11451", 9);
        numberOfFiles.put("T11784", 7);
        numberOfFiles.put("T11785", 9);

        while (testReport.hasNextTest()) {

            final String nameTest = testReport.getNextNameTest();
            Assert.assertNotNull(nameTest);
            Assert.assertNotNull(numberOfFiles.get(nameTest));
            Assert.assertEquals(numberOfFiles.get(nameTest),
                    testReport.getNumberOfFilesInNextTest());
            for (int nbFiles = 0; nbFiles < testReport
                    .getNumberOfFilesInNextTest(); ++nbFiles) {
                Assert.assertNotNull(testReport
                        .getCoverageElementInNextTest(nbFiles));
            }
            ++index;
        }

        Assert.assertEquals(41, index);

    }

    @Test
    public void testGlobalCoverage() throws Exception {

        final File file = new File(this.getClass().getResource("ATU.CIO")
                .toURI());
        final InputStream is = new FileInputStream(file);
        final CioAttributes attributes = new CioAttributes(is);

        final InputStream is2 = new FileInputStream(file);
        final CioReader cioReader = new CioReader(is2, file.getName());

        final CoverageElement global = cioReader.populateGlobalCoverage(
                attributes.getAttributesFlags(),
                attributes.getAttributesInformationFlags());

        final CoverageElement result = new CoverageElement();
        result.setNameOfAssociatedTioFile(file.getName()
                .replace(".CIO", ".TIO"));
        result.setNameFile(global.getNameFile());
        final float[] value1 = { 16, 16 };
        final float[] value2 = { 28, 28 };
        final float[] value3 = { 50, 50 };
        final float[] value4 = { 16, 16 };
        final String value5 = "100% (22/22)";
        final Ratio function = new Ratio(value1);
        result.setFunctionCoverage(function);
        final Ratio statBlock = new Ratio(value2);
        result.setStatementBlockCoverage(statBlock);
        final Ratio decision = new Ratio(value2);
        result.setDecisionCoverage(decision);
        final Ratio basicCond = new Ratio(value3);
        result.setBasicConditionCoverage(basicCond);
        final Ratio modifCond = new Ratio(value4);
        result.setModifiedConditionCoverage(modifCond);
        final Ratio multCond = Ratio.parseValue(value5);
        result.setMultipleConditionCoverage(multCond);

        Assert.assertTrue(result.getFunctionCoverage().equals(
                result.getFunctionCoverage()));

        Assert.assertEquals(result.getNameFile(), global.getNameFile());
        Assert.assertEquals(result.getFunctionCoverage(),
                global.getFunctionCoverage());
        Assert.assertEquals(result.getCallCoverage(), global.getCallCoverage());
        Assert.assertEquals(result.getImplicitBlockCoverage(),
                global.getImplicitBlockCoverage());
        Assert.assertEquals(result.getStatementBlockCoverage(),
                global.getStatementBlockCoverage());
        Assert.assertEquals(result.getDecisionCoverage(),
                global.getDecisionCoverage());
        Assert.assertEquals(result.getBasicConditionCoverage(),
                global.getBasicConditionCoverage());
        Assert.assertEquals(result.getModifiedConditionCoverage(),
                global.getModifiedConditionCoverage());
        Assert.assertEquals(result.getMultipleConditionCoverage(),
                global.getMultipleConditionCoverage());

    }

    @Test
    public void testBadinput() throws Exception {
        // test token without data
        executeBadTest("BAD1.CIO");
        // tests without Global Coverage
        executeBadTest("BAD2.CIO");
        // bad token Functions
        executeBadTest("BAD3.CIO");
        // bad token Calls
        executeBadTest("BAD4.CIO");
        // bad token Statement
        executeBadTest("BAD5.CIO");
        // bad token Implicit
        executeBadTest("BAD6.CIO");
        // bad token Decisions
        executeBadTest("BAD7.CIO");
        // bad token Basic
        executeBadTest("BAD8.CIO");
        // bad token Modified
        executeBadTest("BAD9.CIO");
        // bad token Multiple
        executeBadTest("BAD10.CIO");

    }

    private void executeBadTest(final String input) throws Exception {
        final File file = new File(this.getClass().getResource(input).toURI());
        final InputStream is = new FileInputStream(file);
        Exception exception = null;

        CioAttributes attributes = null;

        try {
            attributes = new CioAttributes(is);
        } catch (final CioException e) {
            exception = e;
            Assert.assertNotNull(exception);
        }

        if (attributes != null) {

            final CioReader cioReader = new CioReader(file);

            try {
                cioReader.populate(attributes.getAttributesFlags(),
                        attributes.getAttributesInformationFlags());
            } catch (final CioException e) {
                exception = e;

            }
            Assert.assertNotNull(exception);
        }

    }

}
