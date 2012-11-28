package com.thalesgroup.rtrtcoverage;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import junit.framework.Assert;

import org.junit.Test;

import com.thalesgroup.rtrtcoverage.cioreader.Ratio;
import com.thalesgroup.rtrtcoverage.cioreader.ReportTag;

/**
 * @author Sebastien Barbier
 * @version 1.0
 *
 */
public class CoverageReportTest {

    @Test
    public void testBuildHierarchy() throws Exception {

        final File cioReport = new File(this.getClass()
                .getResource("cioreader/ATU.CIO").toURI());

        final float[] values = { 0, 0 };
        final Ratio[] ratios = new Ratio[ReportTag.values().length];
        for (int i = 0; i < ReportTag.values().length; i++) {
            ratios[i] = new Ratio(values);
        }
        final RTRTBuildAction bAction = new RTRTBuildAction(null, null, ratios,
                null);

        final CoverageReport report = new CoverageReport(bAction, "", "",
                cioReport);

        Assert.assertTrue(report.hasChildren());
        Assert.assertNotNull(report.getChildren());
        final SourceFileReport child = report.getDynamic(
                "STRINGUTILITIES_INITIALIZESTATICCLASS.C", null, null);
        Assert.assertNotNull(child);
        Assert.assertNull(child.getBuild());
        Assert.assertTrue(child.hasChildren());
        Assert.assertEquals("STRINGUTILITIES_INITIALIZESTATICCLASS.FDC",
                child.getSourcePath());
        Assert.assertTrue(child.isSourceCodeLevel());
        Assert.assertNull(child.getSourceFileContent());

        final TestReport test = child.getDynamic("T63", null, null);
        Assert.assertNotNull(test);
        Assert.assertTrue(report.hasChildrenFunctionCoverage());
        Assert.assertFalse(report.hasChildrenCallCoverage());
        Assert.assertTrue(report.hasChildrenStatementBlockCoverage());
        Assert.assertFalse(report.hasChildrenImplicitBlockCoverage());
        Assert.assertTrue(report.hasChildrenDecisionCoverage());
        Assert.assertTrue(report.hasChildrenBasicConditionCoverage());
        Assert.assertTrue(report.hasChildrenModifiedConditionCoverage());
        Assert.assertTrue(report.hasChildrenMultipleConditionCoverage());
        Assert.assertFalse(report.hasChildrenLoopCoverage());
        Assert.assertEquals("Couverture Globale", report.getName());
        Assert.assertEquals("Couverture Globale", report.getDisplayName());
        Assert.assertNull(report.getParent());

        final NumberFormat dataFormat = new DecimalFormat("000.00");

        Assert.assertEquals(
                "<td class='nowrap' data='"
                        + dataFormat.format(report.getFunctionCoverage()
                                .getPercentageFloat())
                                + "'>\n"
                                + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
                                + "<tr class='percentgraph'>"
                                + "<td width='64px' class='data'>"
                                + report.getFunctionCoverage().getPercentageFloat()
                                + "%</td>"
                                + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
                                + "<span class='text'>"
                                + report.getFunctionCoverage().toString()
                                + "</span></div></div></td></tr></table></td>\n"
                                + "<td align=\"center\">none</td>\n"
                                + "<td class='nowrap' data='"
                                + dataFormat.format(report.getStatementBlockCoverage()
                                        .getPercentageFloat())
                                        + "'>\n"
                                        + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
                                        + "<tr class='percentgraph'>"
                                        + "<td width='64px' class='data'>"
                                        + report.getStatementBlockCoverage()
                                        .getPercentageFloat()
                                        + "%</td>"
                                        + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
                                        + "<span class='text'>"
                                        + report.getStatementBlockCoverage().toString()
                                        + "</span></div></div></td></tr></table></td>\n"
                                        + "<td align=\"center\">none</td>\n"
                                        + "<td class='nowrap' data='"
                                        + dataFormat.format(report.getDecisionCoverage()
                                                .getPercentageFloat())
                                                + "'>\n"
                                                + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
                                                + "<tr class='percentgraph'>"
                                                + "<td width='64px' class='data'>"
                                                + report.getDecisionCoverage().getPercentageFloat()
                                                + "%</td>"
                                                + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
                                                + "<span class='text'>"
                                                + report.getDecisionCoverage().toString()
                                                + "</span></div></div></td></tr></table></td>\n"
                                                + "<td align=\"center\">none</td>\n"
                                                + "<td class='nowrap' data='"
                                                + dataFormat.format(report.getBasicConditionCoverage()
                                                        .getPercentageFloat())
                                                        + "'>\n"
                                                        + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
                                                        + "<tr class='percentgraph'>"
                                                        + "<td width='64px' class='data'>"
                                                        + report.getBasicConditionCoverage()
                                                        .getPercentageFloat()
                                                        + "%</td>"
                                                        + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
                                                        + "<span class='text'>"
                                                        + report.getBasicConditionCoverage().toString()
                                                        + "</span></div></div></td></tr></table></td>\n"
                                                        + "<td class='nowrap' data='"
                                                        + dataFormat.format(report
                                                                .getModifiedConditionCoverage()
                                                                .getPercentageFloat())
                                                                + "'>\n"
                                                                + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
                                                                + "<tr class='percentgraph'>"
                                                                + "<td width='64px' class='data'>"
                                                                + report.getModifiedConditionCoverage()
                                                                .getPercentageFloat()
                                                                + "%</td>"
                                                                + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
                                                                + "<span class='text'>"
                                                                + report.getModifiedConditionCoverage().toString()
                                                                + "</span></div></div></td></tr></table></td>\n"
                                                                + "<td class='nowrap' data='"
                                                                + dataFormat.format(report
                                                                        .getMultipleConditionCoverage()
                                                                        .getPercentageFloat())
                                                                        + "'>\n"
                                                                        + "<table class='percentgraph' cellpadding='0px' cellspacing='0px'>"
                                                                        + "<tr class='percentgraph'>"
                                                                        + "<td width='64px' class='data'>"
                                                                        + report.getMultipleConditionCoverage()
                                                                        .getPercentageFloat()
                                                                        + "%</td>"
                                                                        + "<td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width: 100.0px;'>"
                                                                        + "<span class='text'>"
                                                                        + report.getMultipleConditionCoverage().toString()
                                                                        + "</span></div></div></td></tr></table></td>\n",
                                                                        report.printNineCoverageColumns());

    }

}
