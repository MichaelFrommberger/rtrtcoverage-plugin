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

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.serializablerates.CoverageElementRate;

/**
 * Base class of all coverage objects.
 *
 * @author Bastien Reboulet
 *
 * @param <SELF> itself.
 */

@ExportedBean
public abstract class CoverageObject<SELF extends CoverageObject<SELF>> {

    /**
     * Coverage of the functions.
     */
    private Ratio function = new Ratio();
    /**
     * Coverage of exits.
     */
    private Ratio exit = new Ratio();
    /**
     * Coverage of the calls.
     */
    private Ratio call = new Ratio();
    /**
     * Coverage of the statement blocks.
     */
    private Ratio statBlock = new Ratio();
    /**
     * Coverage of the imlicit blocks.
     */
    private Ratio implBlock = new Ratio();
    /**
     * Coverage of the decisions.
     */
    private Ratio decision = new Ratio();
    /**
     * Coverage of the loops.
     */
    private Ratio loop = new Ratio();
    /**
     * Coverage of the basic conditions.
     */
    private Ratio basicCond = new Ratio();
    /**
     * Coverage of the modified conditions.
     */
    private Ratio modifCond = new Ratio();
    /**
     * Coverage of the multiple conditions.
     */
    private Ratio multCond = new Ratio();

    /**
     * Name of the report.
     */
    private String name;

    /**
     * Flag if object failed rule.
     */
    private volatile boolean failed = false;

    /**
     * Sets all the ratios of this coverage object.
     * @param covRate a general coverage element rate
     */
    public final void initRatios(final CoverageElementRate covRate) {
        for (BranchType type : BranchType.values()) {
            this.get(type).addRatio(covRate.get(type));
        }
        decision.addRatio(statBlock);
        decision.addRatio(implBlock);
    }

    /**
     * @param type the type of the ratio we want.
     * @return the ratio of the specified type.
     */
    public final Ratio get(final BranchType type) {
        switch (type) {
        case TP_FUNCTIONS:
            return function;
        case TP_EXITS:
            return exit;
        case TA_CALLS:
            return call;
        case TB_STATEMENTS:
            return statBlock;
        case TB_IMPLICIT:
            return implBlock;
        case TB_DECISIONS:
            return decision;
        case TB_LOOPS:
            return loop;
        case TE_BASICS:
            return basicCond;
        case TE_MODIFIEDS:
            return modifCond;
        case TE_MULTIPLES:
            return multCond;
        default:
            return null;
        }
    }

    /**
     * @return true if the flag rule is failed.
     */
    public final boolean isFailed() {
        return failed;
    }

    /**
     * Marks this coverage object as failed.
     * @see Rule
     */
    public void setFailed() {
        failed = true;
    }

    /**
     * @return functions and exits coverage ratio (sum of function and exit).
     */
    @Exported(inline = true)
    public final Ratio getFunctionAndExitCoverage() {
        Ratio functionAndExit = new Ratio();
        functionAndExit.addRatio(function);
        functionAndExit.addRatio(exit);
        return functionAndExit;
    }

    /**
     * @return functions coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getFunctionCoverage() {
        return function;
    }

    /**
     * @return exits coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getExitCoverage() {
        return exit;
    }

    /**
     * @return calls coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getCallCoverage() {
        return call;
    }

    /**
     * @return statement blocks coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getStatBlockCoverage() {
        return statBlock;
    }

    /**
     * @return implicit blocks coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getImplBlockCoverage() {
        return implBlock;
    }

    /**
     * @return decisions (statement blocks + implicit blocks) coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getDecisionCoverage() {
        return decision;
    }

    /**
     * @return loops coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getLoopCoverage() {
        return loop;
    }


    /**
     * @return basic conditions coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getBasicCondCoverage() {
        return basicCond;
    }

    /**
     * @return modified conditions coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getModifCondCoverage() {
        return modifCond;
    }

    /**
     * @return multiple conditions coverage ratio.
     */
    @Exported(inline = true)
    public final Ratio getMultCondCoverage() {
        return multCond;
    }

    /**
     * @return the build object that owns the whole coverage report tree.
     */
    public abstract AbstractBuild<?, ?> getBuild();

    /**
     * Gets the corresponding coverage report object in the previous
     * run that has the record.
     *
     * @return
     *      null if no earlier record was found.
     */
    @Exported
    public abstract SELF getPreviousResult();

    /**
     * Used in the view to print out four table columns with the coverage info.
     * @return a html string.
     */
    public final String printNineCoverageColumns() {
        StringBuilder buf = new StringBuilder();
        printRatioCell(isFailed(), getFunctionAndExitCoverage(), buf);
        printRatioCell(isFailed(), getCallCoverage(), buf);
        printRatioCell(isFailed(), getStatBlockCoverage(), buf);
        printRatioCell(isFailed(), getImplBlockCoverage(), buf);
        printRatioCell(isFailed(), getDecisionCoverage(), buf);
        printRatioCell(isFailed(), getLoopCoverage(), buf);
        printRatioCell(isFailed(), getBasicCondCoverage(), buf);
        printRatioCell(isFailed(), getModifCondCoverage(), buf);
        printRatioCell(isFailed(), getMultCondCoverage(), buf);
        return buf.toString();
    }

    /**
     * Determine if functions and exits coverage is defined.
     * @return true if defined
     */
    public final boolean hasFunctionAndExitCoverage() {
        return function.isInitialized() && exit.isInitialized();
    }

    /**
     * Determine if exit coverage is defined.
     * @return true if defined
     */
    public final boolean hasExitCoverage() {
        return exit.isInitialized();
    }

    /**
     * Determine if call coverage is defined.
     * @return true if defined
     */
    public final boolean hasCallCoverage() {
        return call.isInitialized();
    }

    /**
     * Determine if statement block coverage is defined.
     * @return true if defined
     */
    public final boolean hasStatBlockCoverage() {
        return statBlock.isInitialized();
    }

    /**
     * Determine if implicit block coverage is defined.
     * @return true if defined
     */
    public final boolean hasImplBlockCoverage() {
        return implBlock.isInitialized();
    }

    /**
     * Determine if decision coverage is defined.
     * @return true if defined
     */
    public final boolean hasDecisionCoverage() {
        return decision.isInitialized();
    }

    /**
     * Determine if loop coverage is defined.
     * @return true if defined
     */
    public final boolean hasLoopCoverage() {
        return loop.isInitialized();
    }

    /**
     * Determine if basic condition coverage is defined.
     * @return true if defined
     */
    public final boolean hasBasicCondCoverage() {
        return basicCond.isInitialized();
    }

    /**
     * Determine if modified condition coverage is defined.
     * @return true if defined
     */
    public final boolean hasModifCondCoverage() {
        return modifCond.isInitialized();
    }

    /**
     * Determine if multiple condition coverage is defined.
     * @return true if defined
     */
    public final boolean hasMultCondCoverage() {
        return multCond.isInitialized();
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
    protected static void printRatioCell(final boolean failed, final Ratio ratio, final StringBuilder buf) {
        if (ratio != null && ratio.isInitialized() && ratio.getDenominator() != 0) {
            String className = "nowrap";
            if (failed) {
                className += " red";
            }
            buf.append("<td class='").append(className).append("'");
            buf.append(" data='").append(dataFormat.format(ratio.getPercentageFloat()));
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
    protected static void printRatioTable(final Ratio ratio, final StringBuilder buf) {
        String data = dataFormat.format(ratio.getPercentageFloat());
        String percent = percentFormat.format(ratio.getPercentageFloat());
        String numerator = intFormat.format(ratio.getNumerator());
        String denominator = intFormat.format(ratio.getDenominator());
        buf.append("<table class='percentgraph' cellpadding='0px' cellspacing='0px'><tr class='percentgraph'>")
        .append("<td width='64px' class='data'>").append(percent).append("%</td>")
        .append("<td class='percentgraph'>")
        .append("<div class='percentgraph'><div class='greenbar' style='width: ").append(ratio.getPercentageFloat()).append("px;'>")
        .append("<span class='text'>").append(numerator).append("/").append(denominator)
        .append("</span></div></div></td></tr></table>");
    }

    /**
     * Generates the graph that shows the coverage trend up to this report.
     * @param req
     *            StaplerRequest
     * @param rsp
     *            StaplerResponse
     * @throws IOException
     *             if some reading issues
     */
    public final void doGraph(final StaplerRequest req, final StaplerResponse rsp) throws IOException {
        if (ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
            return;
        }

        AbstractBuild<?, ?> build = getBuild();
        Calendar t = build.getTimestamp();

        String w = Util.fixEmptyAndTrim(req.getParameter("width"));
        String h = Util.fixEmptyAndTrim(req.getParameter("height"));
        final int defaultWidth = 500;
        final int defaultHeight = 200;

        int width = defaultWidth;
        if (w != null) {
            width = Integer.valueOf(w);
        }
        int height = defaultHeight;
        if (h != null) {
            height = Integer.valueOf(h);
        }

        new GraphImpl(this, t, width, height) {

            @Override
            protected DataSetBuilder<String, NumberOnlyBuildLabel> createDataSet(final CoverageObject<SELF> obj) {
                DataSetBuilder<String, NumberOnlyBuildLabel> dsb = new DataSetBuilder<String, NumberOnlyBuildLabel>();

                for (CoverageObject<SELF> a = obj; a != null; a = a.getPreviousResult()) {
                    NumberOnlyBuildLabel label = new NumberOnlyBuildLabel(a.getBuild());
                    dsb.add(a.getFunctionAndExitCoverage().getPercentageFloat(), Messages.CoverageObject_Legend_Function(), label);
                    dsb.add(a.call.getPercentageFloat(), Messages.CoverageObject_Legend_Call(), label);
                    dsb.add(a.statBlock.getPercentageFloat(), Messages.CoverageObject_Legend_StatBlock(), label);
                    dsb.add(a.implBlock.getPercentageFloat(), Messages.CoverageObject_Legend_ImplBlock(), label);
                    dsb.add(a.statBlock.getPercentageFloat() + a.implBlock.getPercentageFloat(), Messages.CoverageObject_Legend_Decision(), label);
                    dsb.add(a.loop.getPercentageFloat(), Messages.CoverageObject_Legend_Loop(), label);
                    dsb.add(a.basicCond.getPercentageFloat(), Messages.CoverageObject_Legend_BasicCond(), label);
                    dsb.add(a.modifCond.getPercentageFloat(), Messages.CoverageObject_Legend_ModifCond(), label);
                    dsb.add(a.multCond.getPercentageFloat(), Messages.CoverageObject_Legend_MultCond(), label);

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
     * @return the name of this coverage object.
     */
    @Exported(inline = true)
    public final String getName() {
        return name;
    }

    /**
     * @param newName the name of this coverage object.
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Implementation of the Graph representing all the ratios of a current
     * coverage report.
     *
     * @author Bastien Reboulet
     */
    private abstract class GraphImpl extends Graph {

        /**
         * The coverage report.
         */
        private CoverageObject<SELF> obj;

        /**
         * Default Constructor.
         *
         * @param newObj
         *            the coverage report
         * @param timestamp
         *            a timestamp
         * @param defaultW
         *            default width
         * @param defaultH
         *            default height
         */
        public GraphImpl(final CoverageObject<SELF> newObj, final Calendar timestamp, final int defaultW, final int defaultH) {
            super(timestamp, defaultW, defaultH);
            this.obj = newObj;
        }

        /**
         * Creation of the dataset. No implemented.
         *
         * @param newObj
         *            the current coverage
         * @return a data set builder
         */
        protected abstract DataSetBuilder<String, NumberOnlyBuildLabel> createDataSet(CoverageObject<SELF> newObj);

        @Override
        protected JFreeChart createGraph() {
            final CategoryDataset dataset = createDataSet(obj).build();
            final JFreeChart chart = ChartFactory.createLineChart(
                    null, // chart title
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

            // plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlinePaint(null);
            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.black);

            CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
            plot.setDomainAxis(domainAxis);
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            domainAxis.setLowerMargin(0.0);
            domainAxis.setUpperMargin(0.0);
            domainAxis.setCategoryMargin(0.0);

            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            final int upperbound = 100;
            rangeAxis.setUpperBound(upperbound);
            rangeAxis.setLowerBound(0);

            final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            final float basestroke = 4.0f;
            renderer.setBaseStroke(new BasicStroke(basestroke));
            ColorPalette.apply(renderer);

            // crop extra space around the graph
            final double rectangleDefaults = 5.0;
            plot.setInsets(new RectangleInsets(rectangleDefaults, 0, 0, rectangleDefaults));

            return chart;
        }
    }


}
