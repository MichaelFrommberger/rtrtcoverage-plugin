package com.thalesgroup.rtrtcoverage;

import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.Api;
import hudson.util.ChartUtil;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;
import hudson.util.ColorPalette;
import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import hudson.util.ShiftedCategoryAxis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import com.thalesgroup.rtrtcoverage.cioreader.CoverageElement;
import com.thalesgroup.rtrtcoverage.cioreader.Ratio;

/**
 * Base class of all coverage objects. Map all the arguments of the RTRT
 * coverage report. Extends @see CoverageElement
 *
 * @author Sebastien Barbier
 * @version 1.0
 * @param <SELF>
 *            itself
 */
@ExportedBean
public abstract class CoverageObject<SELF extends CoverageObject<SELF>> extends
CoverageElement {

    /**
     * Flag if object failed rule.
     */
    private volatile boolean failed = false;

    /**
     * Object fails rule?
     *
     * @return true if fails rule
     */
    public final boolean isFailed() {
        return failed;
    }

    /**
     * Marks this coverage object as failed.
     *
     * @see Rule
     */
    public void setFailed() {
        failed = true;
    }

    /**
     * Gets the build object that owns the whole coverage report tree.
     *
     * @return Build
     */
    public abstract AbstractBuild<?, ?> getBuild();

    /**
     * Gets the corresponding coverage report object in the previous run that
     * has the record.
     *
     * @return null if no earlier record was found.
     */
    @Exported
    public abstract SELF getPreviousResult();

    /**
     * Used in the view to print out nine table columns with the coverage info.
     *
     * @return a String to display the array
     */
    public final String printNineCoverageColumns() {
        final StringBuilder buf = new StringBuilder();
        printRatioCell(isFailed(), getFunctionCoverage(), buf);
        printRatioCell(isFailed(), getCallCoverage(), buf);
        printRatioCell(isFailed(), getStatementBlockCoverage(), buf);
        printRatioCell(isFailed(), getImplicitBlockCoverage(), buf);
        printRatioCell(isFailed(), getDecisionCoverage(), buf);
        printRatioCell(isFailed(), getLoopCoverage(), buf);
        printRatioCell(isFailed(), getBasicConditionCoverage(), buf);
        printRatioCell(isFailed(), getModifiedConditionCoverage(), buf);
        printRatioCell(isFailed(), getMultipleConditionCoverage(), buf);

        return buf.toString();
    }

    /**
     * Data format.
     */
    private static NumberFormat dataFormat = new DecimalFormat("000.00");
    /**
     * Percent format.
     */
    private static NumberFormat percentFormat = new DecimalFormat("0.0");
    /**
     * Integer format.
     */
    private static NumberFormat intFormat = new DecimalFormat("0");

    /**
     * Display a cell containing the given ratio.
     *
     * @param failed
     *            the ratio is failed
     * @param ratio
     *            the ratio to display
     * @param buf
     *            the buffer to append
     */
    protected static void printRatioCell(final boolean failed,
            final Ratio ratio, final StringBuilder buf) {
        if (ratio != null && ratio.isInitialized()
                && ratio.getDenominator() != 0) {
            String color = "";
            if (failed) {
                color = " red";
            }
            final String className = "nowrap" + color;
            buf.append("<td class='").append(className).append("'");
            buf.append(" data='").append(
                    dataFormat.format(ratio.getPercentageFloat()));
            buf.append("'>\n");
            printRatioTable(ratio, buf);
            buf.append("</td>\n");
        } else {
            buf.append("<td align=\"center\">none</td>\n");
        }
    }

    /**
     * display the table of a given ratio.
     *
     * @param ratio
     *            the current ratio
     * @param buf
     *            the buffer to append
     */
    protected static void printRatioTable(final Ratio ratio,
            final StringBuilder buf) {
        final String percent = percentFormat.format(ratio.getPercentageFloat());
        final String numerator = intFormat.format(ratio.getNumerator());
        final String denominator = intFormat.format(ratio.getDenominator());
        buf.append(
                "<table class='percentgraph' cellpadding='0px' cellspacing='0px'><tr class='percentgraph'>")
                .append("<td width='64px' class='data'>")
                .append(percent)
                .append("%</td>")
                .append("<td class='percentgraph'>")
                .append("<div class='percentgraph'><div class='greenbar' style='width: ")
                .append(ratio.getPercentageFloat()).append("px;'>")
                .append("<span class='text'>").append(numerator).append("/")
                .append(denominator)
                .append("</span></div></div></td></tr></table>");
    }

    /**
     * Generates the graph that shows the coverage trend up to this report.
     *
     * @param req
     *            StaplerRequest
     * @param rsp
     *            StaplerResponse
     * @throws IOException
     *             if some reading issues
     */
    public final void doGraph(final StaplerRequest req,
            final StaplerResponse rsp) throws IOException {
        if (ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
            return;
        }

        final AbstractBuild<?, ?> build = getBuild();
        final Calendar t = build.getTimestamp();

        final String w = Util.fixEmptyAndTrim(req.getParameter("width"));
        final String h = Util.fixEmptyAndTrim(req.getParameter("height"));
        final int defaultWidth = 500;
        final int defaultHeight = 200;
        int width = defaultWidth;
        int height = defaultHeight;

        if (w != null) {
            width = Integer.valueOf(w);
        }
        if (h != null) {
            height = Integer.valueOf(h);
        }

        new GraphImpl(this, t, width, height) {

            @Override
            protected DataSetBuilder<String, NumberOnlyBuildLabel> createDataSet(
                    final CoverageObject<SELF> obj) {

                final DataSetBuilder<String, NumberOnlyBuildLabel> dsb = new DataSetBuilder<String, NumberOnlyBuildLabel>();

                for (CoverageObject<SELF> a = obj; a != null; a = a
                        .getPreviousResult()) {
                    final NumberOnlyBuildLabel label = new NumberOnlyBuildLabel(
                            a.getBuild());
                    if (a.getFunctionCoverage() != null
                            && a.getFunctionCoverage().isInitialized()) {
                        dsb.add(a.getFunctionCoverage().getPercentageFloat(),
                                Messages.CoverageObject_Legend_Function(),
                                label);
                    }
                    if (a.getCallCoverage() != null
                            && a.getCallCoverage().isInitialized()) {
                        dsb.add(a.getCallCoverage().getPercentageFloat(),
                                Messages.CoverageObject_Legend_Call(), label);
                    }
                    if (a.getStatementBlockCoverage() != null
                            && a.getStatementBlockCoverage().isInitialized()) {
                        dsb.add(a.getStatementBlockCoverage()
                                .getPercentageFloat(), Messages
                                .CoverageObject_Legend_StatBlock(), label);
                    }
                    if (a.getImplicitBlockCoverage() != null
                            && a.getImplicitBlockCoverage().isInitialized()) {
                        dsb.add(a.getImplicitBlockCoverage()
                                .getPercentageFloat(), Messages
                                .CoverageObject_Legend_ImplBlock(), label);
                    }
                    if (a.getDecisionCoverage() != null
                            && a.getDecisionCoverage().isInitialized()) {
                        dsb.add(a.getDecisionCoverage().getPercentageFloat(),
                                Messages.CoverageObject_Legend_Decision(),
                                label);
                    }
                    if (a.getLoopCoverage() != null
                            && a.getLoopCoverage().isInitialized()) {
                        dsb.add(a.getLoopCoverage().getPercentageFloat(),
                                Messages.CoverageObject_Legend_Loop(), label);
                    }
                    if (a.getBasicConditionCoverage() != null
                            && a.getBasicConditionCoverage().isInitialized()) {
                        dsb.add(a.getBasicConditionCoverage()
                                .getPercentageFloat(), Messages
                                .CoverageObject_Legend_BasicCond(), label);
                    }
                    if (a.getModifiedConditionCoverage() != null
                            && a.getModifiedConditionCoverage().isInitialized()) {
                        dsb.add(a.getModifiedConditionCoverage()
                                .getPercentageFloat(), Messages
                                .CoverageObject_Legend_ModifCond(), label);
                    }
                    if (a.getMultipleConditionCoverage() != null
                            && a.getMultipleConditionCoverage().isInitialized()) {
                        dsb.add(a.getMultipleConditionCoverage()
                                .getPercentageFloat(), Messages
                                .CoverageObject_Legend_MultCond(), label);
                    }
                }

                return dsb;
            }
        } .doPng(req, rsp);
    }

    /**
     * Get the API.
     *
     * @return the API
     */
    public final Api getApi() {
        return new Api(this);
    }

    /**
     * Implementation of the Graph representing all the ratios of a current
     * coverage report.
     *
     * @author Sebastien Barbier
     * @version 1.0
     */
    private abstract class GraphImpl extends Graph {

        /**
         * The coverage report.
         */
        private final CoverageObject<SELF> obj;

        /**
         * Default Constructor.
         *
         * @param cObj
         *            the coverage report
         * @param timestamp
         *            a timestamp
         * @param defaultW
         *            default width
         * @param defaultH
         *            default height
         */
        public GraphImpl(final CoverageObject<SELF> cObj,
                final Calendar timestamp, final int defaultW, final int defaultH) {
            super(timestamp, defaultW, defaultH);
            this.obj = cObj;
        }

        /**
         * Creation of the dataset. No implemented.
         *
         * @param cObj
         *            the current coverage
         * @return a data set builder
         */
        protected abstract DataSetBuilder<String, NumberOnlyBuildLabel> createDataSet(
                CoverageObject<SELF> cObj);

        /**
         * Creation of the graph.
         *
         * @return GUI
         */
        @Override
        protected JFreeChart createGraph() {
            final CategoryDataset dataset = createDataSet(obj).build();
            final JFreeChart chart = ChartFactory.createLineChart(null, // chart
                    // title
                    null, // unused
                    "%", // range axis label
                    dataset, // data
                    PlotOrientation.VERTICAL, // orientation
                    true, // include legend
                    true, // tooltips
                    false // urls
                    );

            // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

            final LegendTitle legend = chart.getLegend();
            legend.setPosition(RectangleEdge.RIGHT);

            chart.setBackgroundPaint(Color.white);

            final CategoryPlot plot = chart.getCategoryPlot();

            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlinePaint(null);
            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.black);

            final CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
            plot.setDomainAxis(domainAxis);
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            domainAxis.setLowerMargin(0.0);
            domainAxis.setUpperMargin(0.0);
            domainAxis.setCategoryMargin(0.0);

            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            final int upperBound = 100;
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setUpperBound(upperBound);
            rangeAxis.setLowerBound(0);

            final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
                    .getRenderer();
            final float baseStroke = 4.0f;
            renderer.setBaseStroke(new BasicStroke(baseStroke));
            ColorPalette.apply(renderer);

            // crop extra space around the graph
            final double rectangleDefaults = 5.0;
            plot.setInsets(new RectangleInsets(rectangleDefaults, 0, 0,
                    rectangleDefaults));

            return chart;
        }
    }
}
