package com.thalesgroup.rtrtcoverage;

import org.junit.Test;

import com.thalesgroup.rtrtcoverage.serializablerates.FileRate;
import com.thalesgroup.rtrtcoverage.serializablerates.GlobalRate;

/**
 * @author Sebastien Barbier
 * @version 1.0
 *
 */
public class CoverageReportTest {

    @Test
    public void testBuildHierarchy() throws Exception {


        GlobalRate globalRate = new GlobalRate();

        FileRate fileRate = new FileRate();

        fileRate.setSourceFileName("STRINGUTILITIES_INITIALIZESTATICCLASS.C");
        fileRate.setFdcPath("STRINGUTILITIES_INITIALIZESTATICCLASS.FDC");

//        final float[] values = { 0, 0 };
//        final Ratio[] ratios = new Ratio[BranchType.values().length];
//        for (int i = 0; i < BranchType.values().length; i++) {
//            ratios[i] = new Ratio(values);
//        }
//        final RTRTBuildAction bAction = new RTRTBuildAction(null, null, globalRate,
//                null);
//
//        final CoverageReport report = new CoverageReport(bAction, globalRate);
//
//        Assert.assertTrue(report.hasChildren());
//        Assert.assertNotNull(report.getChildren());
//        final SourceFileReport child = report.getDynamic(
//                "STRINGUTILITIES_INITIALIZESTATICCLASS.C", null, null);
//        Assert.assertNotNull(child);
//        Assert.assertNull(child.getBuild());
//        Assert.assertTrue(child.hasChildren());
//        Assert.assertEquals("STRINGUTILITIES_INITIALIZESTATICCLASS.C",
//                child.getSourcePath());
//        Assert.assertTrue(child.isSourceCodeLevel());
//        Assert.assertNull(child.getSourceFileContent());
//
//        final TestReport test = child.getDynamic("T63", null, null);
//        Assert.assertNotNull(test);
//        Assert.assertTrue(report.hasChildrenFunctionCoverage());
//        Assert.assertFalse(report.hasChildrenCallCoverage());
//        Assert.assertTrue(report.hasChildrenStatementBlockCoverage());
//        Assert.assertFalse(report.hasChildrenImplicitBlockCoverage());
//        Assert.assertTrue(report.hasChildrenDecisionCoverage());
//        Assert.assertTrue(report.hasChildrenBasicConditionCoverage());
//        Assert.assertTrue(report.hasChildrenModifiedConditionCoverage());
//        Assert.assertTrue(report.hasChildrenMultipleConditionCoverage());
//        Assert.assertFalse(report.hasChildrenLoopCoverage());
//        Assert.assertEquals("Couverture Globale", report.getName());
//        Assert.assertEquals("Couverture Globale", report.getDisplayName());
//        Assert.assertNull(report.getParent());
//
//        final NumberFormat dataFormat = new DecimalFormat("000.00");
//
//        Assert.assertEquals(
//                "<td class='nowrap' data='"
//                        + dataFormat.format(report.getFunctionCoverage()
//                                .getPercentageFloat())
//                                + "'>\n"
//                                + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
//                                + "<tr class='percentgraph'>"
//                                + "<td width='64px' class='data'>"
//                                + report.getFunctionCoverage().getPercentageFloat()
//                                + "%</td>"
//                                + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
//                                + "<span class='text'>"
//                                + report.getFunctionCoverage().toString()
//                                + "</span></div></div></td></tr></table></td>\n"
//                                + "<td align=\"center\">none</td>\n"
//                                + "<td class='nowrap' data='"
//                                + dataFormat.format(report.getStatBlockCoverage()
//                                        .getPercentageFloat())
//                                        + "'>\n"
//                                        + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
//                                        + "<tr class='percentgraph'>"
//                                        + "<td width='64px' class='data'>"
//                                        + report.getStatBlockCoverage()
//                                        .getPercentageFloat()
//                                        + "%</td>"
//                                        + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
//                                        + "<span class='text'>"
//                                        + report.getStatBlockCoverage().toString()
//                                        + "</span></div></div></td></tr></table></td>\n"
//                                        + "<td align=\"center\">none</td>\n"
//                                        + "<td class='nowrap' data='"
//                                        + dataFormat.format(report.getDecisionCoverage()
//                                                .getPercentageFloat())
//                                                + "'>\n"
//                                                + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
//                                                + "<tr class='percentgraph'>"
//                                                + "<td width='64px' class='data'>"
//                                                + report.getDecisionCoverage().getPercentageFloat()
//                                                + "%</td>"
//                                                + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
//                                                + "<span class='text'>"
//                                                + report.getDecisionCoverage().toString()
//                                                + "</span></div></div></td></tr></table></td>\n"
//                                                + "<td align=\"center\">none</td>\n"
//                                                + "<td class='nowrap' data='"
//                                                + dataFormat.format(report.getBasicCondCoverage()
//                                                        .getPercentageFloat())
//                                                        + "'>\n"
//                                                        + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
//                                                        + "<tr class='percentgraph'>"
//                                                        + "<td width='64px' class='data'>"
//                                                        + report.getBasicCondCoverage()
//                                                        .getPercentageFloat()
//                                                        + "%</td>"
//                                                        + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
//                                                        + "<span class='text'>"
//                                                        + report.getBasicCondCoverage().toString()
//                                                        + "</span></div></div></td></tr></table></td>\n"
//                                                        + "<td class='nowrap' data='"
//                                                        + dataFormat.format(report
//                                                                .getModifCondCoverage()
//                                                                .getPercentageFloat())
//                                                                + "'>\n"
//                                                                + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
//                                                                + "<tr class='percentgraph'>"
//                                                                + "<td width='64px' class='data'>"
//                                                                + report.getModifCondCoverage()
//                                                                .getPercentageFloat()
//                                                                + "%</td>"
//                                                                + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
//                                                                + "<span class='text'>"
//                                                                + report.getModifCondCoverage().toString()
//                                                                + "</span></div></div></td></tr></table></td>\n"
//                                                                + "<td class='nowrap' data='"
//                                                                + dataFormat.format(report
//                                                                        .getMultCondCoverage()
//                                                                        .getPercentageFloat())
//                                                                        + "'>\n"
//                                                                        + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
//                                                                        + "<tr class='percentgraph'>"
//                                                                        + "<td width='64px' class='data'>"
//                                                                        + report.getMultCondCoverage()
//                                                                        .getPercentageFloat()
//                                                                        + "%</td>"
//                                                                        + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
//                                                                        + "<span class='text'>"
//                                                                        + report.getMultCondCoverage().toString()
//                                                                        + "</span></div></div></td></tr></table></td>\n",
//                                                                        report.printNineCoverageColumns());
//
    }

}
