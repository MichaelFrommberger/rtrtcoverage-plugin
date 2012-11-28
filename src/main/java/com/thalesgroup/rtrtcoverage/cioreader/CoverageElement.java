package com.thalesgroup.rtrtcoverage.cioreader;

import java.io.BufferedReader;
import java.io.IOException;

import org.kohsuke.stapler.export.Exported;

import com.thalesgroup.rtrtcoverage.AbstractReport;

/**
 * Set of all the ratio coverage for a test into a .cio file.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CoverageElement {

    /**
     * name of the file associated with the coverage.
     */
    private String name;
    /**
     * name of the tio file.
     */
    private String nameOfAssociatedTioFile;
    /**
     * Coverage of the functions and exits.
     */
    private Ratio function;
    /**
     * Coverage of the calls. Must be undefined.
     */
    private Ratio call;
    /**
     * Coverage of the statement blocks.
     */
    private Ratio statBlock;
    /**
     * Coverage of the implicit blocks. Must be undefined.
     */
    private Ratio implBlock;
    /**
     * Coverage of the decision.
     */
    private Ratio decision;
    /**
     * Coverage of the loops. Must be undefined.
     */
    private Ratio loop;
    /**
     * Coverage of the basic conditions. Must be undefined.
     */
    private Ratio basicCond;
    /**
     * Coverage of the modified conditions. Must be undefined.
     */
    private Ratio modifCond;
    /**
     * Coverage of the multiple conditions. Must be undefined.
     */
    private Ratio multCond;

    /**
     * Default constructor.
     */
    public CoverageElement() {
        name = new String();
        function = new Ratio();
        call = new Ratio();
        statBlock = new Ratio();
        implBlock = new Ratio();
        decision = new Ratio();
        loop = new Ratio();
        basicCond = new Ratio();
        modifCond = new Ratio();
        multCond = new Ratio();

    }

    /**
     * Get the name of the file.
     * @return name of the file
     */
    @Exported(inline = true)
    public final String getNameFile() {
        return name;
    }

    /**
     * Get the coverage of the functions and exits.
     * @return coverage of the functions and exits
     */
    @Exported(inline = true)
    public final Ratio getFunctionCoverage() {
        return function;
    }

    /**
     * Get the coverage of the calls.
     * @return coverage of the calls. Can be undefined.
     */
    @Exported(inline = true)
    public final Ratio getCallCoverage() {
        return call;
    }

    /**
     * Get the coverage of the statement blocks.
     * @return coverage of the statement blocks.
     */
    @Exported(inline = true)
    public final Ratio getStatementBlockCoverage() {
        return statBlock;
    }

    /**
     * Get the coverage of the implicit blocks.
     * @return coverage of the implicit blocks. Can be undefined.
     */
    @Exported(inline = true)
    public final Ratio getImplicitBlockCoverage() {
        return implBlock;
    }

    /**
     * Get the coverage of the decision coverage.
     * @return coverage of the decision coverage.
     */
    @Exported(inline = true)
    public final Ratio getDecisionCoverage() {
        return decision;
    }

    /**
     * Get the coverage of the loops.
     * @return coverage of the loops. Can be undefined.
     */
    @Exported(inline = true)
    public final Ratio getLoopCoverage() {
        return loop;
    }

    /**
     * Get the coverage of the basic conditions.
     * @return coverage of the basic conditions. Can be undefined.
     */
    @Exported(inline = true)
    public final Ratio getBasicConditionCoverage() {
        return basicCond;
    }

    /**
     * Get the coverage of the modified conditions.
     * @return coverage of the modified conditions. Can be undefined.
     */
    @Exported(inline = true)
    public final Ratio getModifiedConditionCoverage() {
        return modifCond;
    }

    /**
     * Get the coverage of the multiple conditions.
     * @return coverage of the multiple conditions. Can be undefined.
     */
    @Exported(inline = true)
    public final Ratio getMultipleConditionCoverage() {
        return multCond;
    }

    /**
     * Set the name of the file.
     * @param fileName
     *            name of the file
     */
    @Exported(inline = true)
    public final void setNameFile(final String fileName) {
        this.name = fileName;
    }

    /**
     * Set the coverage of the functions and exits.
     * @param rFunction
     *            coverage of the functions and exits
     */
    @Exported(inline = true)
    public final void setFunctionCoverage(final Ratio rFunction) {
        this.function = rFunction;
    }

    /**
     * Set the coverage of the calls.
     * @param rCall
     *            coverage of the calls. Can be undefined.
     */
    @Exported(inline = true)
    public final void setCallCoverage(final Ratio rCall) {
        this.call = rCall;
    }

    /**
     * Set the coverage of the statement blocks.
     * @param rStatBlock
     *            coverage of the statement blocks.
     */
    @Exported(inline = true)
    public final void setStatementBlockCoverage(final Ratio rStatBlock) {
        this.statBlock = rStatBlock;
    }

    /**
     * Set the coverage of the implicit blocks.
     * @param rImplBlock
     *            coverage of the implicit blocks. Can be undefined.
     */
    @Exported(inline = true)
    public final void setImplicitBlockCoverage(final Ratio rImplBlock) {
        this.implBlock = rImplBlock;
    }

    /**
     * Set the coverage of the decision coverage.
     * @param rDecision
     *            coverage of the decision coverage.
     */
    @Exported(inline = true)
    public final void setDecisionCoverage(final Ratio rDecision) {
        this.decision = rDecision;
    }

    /**
     * Set the coverage of the loop coverage.
     * @param rLoop
     *            coverage of the loop coverage.
     */
    @Exported(inline = true)
    public final void setLoopCoverage(final Ratio rLoop) {
        this.loop = rLoop;
    }

    /**
     * Set the coverage of the basic conditions.
     * @param rBasicCond
     *            coverage of the basic conditions. Can be undefined.
     */
    @Exported(inline = true)
    public final void setBasicConditionCoverage(final Ratio rBasicCond) {
        this.basicCond = rBasicCond;
    }

    /**
     * Set the coverage of the modified conditions.
     * @param rModifCond
     *            coverage of the modified conditions. Can be undefined.
     */
    @Exported(inline = true)
    public final void setModifiedConditionCoverage(final Ratio rModifCond) {
        this.modifCond = rModifCond;
    }

    /**
     * Set the coverage of the multiple conditions.
     * @param rMultCond
     *            coverage of the multiple conditions. Can be undefined.
     */
    @Exported(inline = true)
    public final void setMultipleConditionCoverage(final Ratio rMultCond) {
        this.multCond = rMultCond;
    }

    /**
     * Determine if function coverage is defined.
     * @return true if defined
     */
    public final boolean hasFunctionCoverage() {
        return function.isInitialized();
    }

    /**
     * Determine if call coverage is defined.
     * @return true if defined
     */
    public final boolean hasCallCoverage() {
        return call.isInitialized();
    }

    /**
     * Determine if statement blocks coverage is defined.
     * @return true if defined
     */
    public final boolean hasStatementBlockCoverage() {
        return statBlock.isInitialized();
    }

    /**
     * Determine if implicit blocks coverage is defined.
     * @return true if defined
     */
    public final boolean hasImplicitBlockCoverage() {
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
    public final boolean hasBasicConditionCoverage() {
        return basicCond.isInitialized();
    }

    /**
     * Determine if modified condition coverage is defined.
     * @return true if defined
     */
    public final boolean hasModifiedConditionCoverage() {
        return modifCond.isInitialized();
    }

    /**
     * Determine if multiple condition coverage is defined.
     * @return true if defined
     */
    public final boolean hasMultipleConditionCoverage() {
        return multCond.isInitialized();
    }

    /**
     * Match between a coverageElement and an array of ratio. Used by daughter
     * class.
     * @param index
     *            the index of the array
     * @return the corresponding ratio
     * @throws CioException
     *             if bad index
     */
    public final Ratio getRatio(final int index) throws CioException {
        final ReportTag tag = ReportTag.values()[index];
        switch (tag) {
        case FUNCTION:
            return function;
        case CALL:
            return call;
        case STATEMENTBLOCK:
            return statBlock;
        case IMPLICITBLOCK:
            return implBlock;
        case DECISION:
            return decision;
        case LOOP:
            return loop;
        case BASICCONDITION:
            return basicCond;
        case MODIFIEDCONDITION:
            return modifCond;
        case MULTIPLECONDITION:
            return multCond;
        default:
            throw new CioException(
                    "Bad index. The report contains only nine ratios.");
        }
    }

    /**
     * Match between a coverageElement and an array of ratio. Used by daughter
     * class.
     * @param index
     *            the index of the array
     * @return if the corresponding ratio has been initialized
     * @throws CioException
     *             if bad index
     */
    public final boolean isInitialized(final int index) throws CioException {
        final ReportTag tag = ReportTag.values()[index];
        switch (tag) {
        case FUNCTION:
            return function.isInitialized();
        case CALL:
            return call.isInitialized();
        case STATEMENTBLOCK:
            return statBlock.isInitialized();
        case IMPLICITBLOCK:
            return implBlock.isInitialized();
        case DECISION:
            return decision.isInitialized();
        case LOOP:
            return loop.isInitialized();
        case BASICCONDITION:
            return basicCond.isInitialized();
        case MODIFIEDCONDITION:
            return modifCond.isInitialized();
        case MULTIPLECONDITION:
            return multCond.isInitialized();
        default:
            throw new CioException(
                    "Bad index. The report contains only nine ratios.");
        }
    }

    /**
     * Match between a coverageElement and an array of ratio. Used by daughter
     * class.
     * @param index
     *            the index of the array
     * @param value
     *            the value of the array
     * @throws CioException
     *             if bad index
     */
    public final void setRatio(final int index, final Ratio value)
            throws CioException {
        final ReportTag tag = ReportTag.values()[index];
        switch (tag) {
        case FUNCTION:
            function = value;
            break;
        case CALL:
            call = value;
            break;
        case STATEMENTBLOCK:
            statBlock = value;
            break;
        case IMPLICITBLOCK:
            implBlock = value;
            break;
        case DECISION:
            decision = value;
            break;
        case LOOP:
            loop = value;
            break;
        case BASICCONDITION:
            basicCond = value;
            break;
        case MODIFIEDCONDITION:
            modifCond = value;
            break;
        case MULTIPLECONDITION:
            multCond = value;
            break;
        default:
            throw new CioException(
                    "Bad index. The report contains only nine ratios.");
        }
    }

    /**
     * Read a part of the .cio file only. Following such schema : read only 10
     * lines Class -> useless Hit -> useless Functions Calls Statements Implicit
     * Decision Basic Modified Mult.
     * @param br
     *            current line with info
     * @param sName
     *            name of the current file
     * @param isSetFlag
     *            flag indicating if the ratio is present into file
     * @param global
     *            true if global coverage
     * @return <code>true</code> if code must be executed.
     * @throws IOException
     *             if some bad reading
     * @throws CioException
     *             if bad format into .cio file
     */
    public final boolean init(final BufferedReader br, final String sName,
            final boolean[] isSetFlag, final boolean global)
                    throws IOException, CioException {
        String line;
        boolean again = false;
        boolean getNext = true;

        name = sName;
        line = br.readLine(); // Name of the function
        line = line.replaceAll("\t", " .. "); // keep compatibility between
        // v2002 & v7

        if (global) {
            line = nextCleanLine(br); // Hit
            final int hitSize = 3;
            String[] v = new String[hitSize];
            v = line.split(" ");
            if (v[0].equals("Hit")) {
                if (v[2].equals("no")) {
                    // The coverage is considered as wrong.
                    again = true;
                }
            } else {
                getNext = false;
            }
        }

        final int sixParams = 6;
        final int fiveParams = 5;
        final int fourParams = 4;

        // Functions
        String value = null;
        if (isSetFlag[ReportTag.FUNCTION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, sixParams, "Functions");
            if (!again && value != null) {
                function.addValue(value);
            }
        }

        // Calls
        if (isSetFlag[ReportTag.CALL.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fourParams, "Calls");
            if (!again && value != null) {
                call.addValue(value);
            }
        }

        // Statement
        if (isSetFlag[ReportTag.STATEMENTBLOCK.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Statement");
            if (!again && value != null) {
                statBlock.addValue(value);
            }
        }

        // Implicit
        if (isSetFlag[ReportTag.IMPLICITBLOCK.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Implicit");
            if (!again && value != null) {
                implBlock.addValue(value);
            }
        }

        // Decision
        if (isSetFlag[ReportTag.DECISION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fourParams, "Decisions");
            if (!again && value != null) {
                decision.addValue(value);
            }
        }

        // Loop
        if (isSetFlag[ReportTag.LOOP.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fourParams, "Loops");
            if (!again && value != null) {
                loop.addValue(value);
            }
        }

        // Basic
        if (isSetFlag[ReportTag.BASICCONDITION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Basic");
            if (!again && value != null) {
                basicCond.addValue(value);
            }
        }

        // Modified
        if (isSetFlag[ReportTag.MODIFIEDCONDITION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Modified");
            if (!again && value != null) {
                modifCond.addValue(value);
            }
        }

        // Mult
        if (isSetFlag[ReportTag.MULTIPLECONDITION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Multiple");
            if (!again && value != null) {
                multCond.addValue(value);
            }
        }

        return again;
    }

    /**
     * Read a part of the .cio file only. Following such schema : read only 10
     * lines Class -> useless Hit -> useless Functions Calls Statements Implicit
     * Decision Basic Modified Mult.
     * @param br
     *            current line with info
     * @param isSetFlag
     *            flag indicating if the ratio is present into file
     * @return <code>true</code> if code must be executed.
     * @throws IOException
     *             if some bad reading
     * @throws CioException
     *             if bad format into .cio file
     */
    public final boolean init(final BufferedReader br, final boolean[] isSetFlag)
            throws IOException, CioException {
        String line;
        boolean again = false;
        boolean getNext = true;

        line = nextCleanLine(br); // Hit
        final int hitSize = 3;
        String[] v = new String[hitSize];
        v = line.split(" ");
        if (v[0].equals("Hit")) {
            if (v[2].equals("no")) {
                // The coverage is considered as wrong.
                again = true;
            }
        } else {
            getNext = false;
        }

        final int sixParams = 6;
        final int fiveParams = 5;
        final int fourParams = 4;

        // Functions
        String value = null;
        if (isSetFlag[ReportTag.FUNCTION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, sixParams, "Functions");
            if (!again && value != null) {
                function.addValue(value);
            }
        }

        // Calls
        if (isSetFlag[ReportTag.CALL.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fourParams, "Calls");
            if (!again && value != null) {
                call.addValue(value);
            }
        }

        // Statement
        if (isSetFlag[ReportTag.STATEMENTBLOCK.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Statement");
            if (!again && value != null) {
                statBlock.addValue(value);
            }
        }

        // Implicit
        if (isSetFlag[ReportTag.IMPLICITBLOCK.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Implicit");
            if (!again && value != null) {
                implBlock.addValue(value);
            }
        }

        // Decision
        if (isSetFlag[ReportTag.DECISION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fourParams, "Decisions");
            if (!again && value != null) {
                decision.addValue(value);
            }
        }

        // Loop
        if (isSetFlag[ReportTag.LOOP.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fourParams, "Loops");
            if (!again && value != null) {
                loop.addValue(value);
            }
        }

        // Basic
        if (isSetFlag[ReportTag.BASICCONDITION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Basic");
            if (!again && value != null) {
                basicCond.addValue(value);
            }
        }

        // Modified
        if (isSetFlag[ReportTag.MODIFIEDCONDITION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Modified");
            if (!again && value != null) {
                modifCond.addValue(value);
            }
        }

        // Mult
        if (isSetFlag[ReportTag.MULTIPLECONDITION.ordinal()]) {
            if (getNext) {
                line = nextCleanLine(br);
            }
            getNext = true;
            value = computeRatio(line, fiveParams, "Multiple");
            if (!again && value != null) {
                multCond.addValue(value);
            }
        }

        return again;
    }

    /**
     * Update the coverage adding values of ce.
     * @param ce
     *            the ratio to add
     */
    public final void update(final CoverageElement ce) {

        if (ce.hasFunctionCoverage()) {
            this.function.addValue(ce.function);
        }
        if (ce.hasCallCoverage()) {
            this.call.addValue(ce.call);
        }
        if (ce.hasStatementBlockCoverage()) {
            this.statBlock.addValue(ce.statBlock);
        }
        if (ce.hasImplicitBlockCoverage()) {
            this.implBlock.addValue(ce.implBlock);
        }
        if (ce.hasDecisionCoverage()) {
            this.decision.addValue(ce.decision);
        }
        if (ce.hasLoopCoverage()) {
            this.loop.addValue(ce.loop);
        }
        if (ce.hasBasicConditionCoverage()) {
            this.basicCond.addValue(ce.basicCond);
        }
        if (ce.hasModifiedConditionCoverage()) {
            this.modifCond.addValue(ce.modifCond);
        }
        if (ce.hasMultipleConditionCoverage()) {
            this.multCond.addValue(ce.multCond);
        }
    }

    /**
     * Update the coverage adding values to a precise report.
     * @param report
     *            which correspond to the coverage Element
     */
    public final void updateTo(final AbstractReport<?, ?> report) {

        if (this.hasFunctionCoverage()) {
            report.getFunctionCoverage().addValue(this.function);
        }
        if (this.hasCallCoverage()) {
            report.getCallCoverage().addValue(this.call);
        }
        if (this.hasStatementBlockCoverage()) {
            report.getStatementBlockCoverage().addValue(this.statBlock);
        }
        if (this.hasImplicitBlockCoverage()) {
            report.getImplicitBlockCoverage().addValue(this.implBlock);
        }
        if (this.hasDecisionCoverage()) {
            report.getDecisionCoverage().addValue(this.decision);
        }
        if (this.hasLoopCoverage()) {
            report.getLoopCoverage().addValue(this.loop);
        }
        if (this.hasBasicConditionCoverage()) {
            report.getBasicConditionCoverage().addValue(this.basicCond);
        }
        if (this.hasModifiedConditionCoverage()) {
            report.getModifiedConditionCoverage().addValue(this.modifCond);
        }
        if (this.hasMultipleConditionCoverage()) {
            report.getMultipleConditionCoverage().addValue(this.multCond);
        }
    }

    /**
     * Add the current coverageElement to a precise report.
     * @param report
     *            which correspond to the coverage Element
     */
    public final void addTo(final AbstractReport<?, ?> report) {
        report.setFunctionCoverage(this.function);
        report.setCallCoverage(this.call);
        report.setStatementBlockCoverage(this.statBlock);
        report.setImplicitBlockCoverage(this.implBlock);
        report.setLoopCoverage(this.loop);
        report.setDecisionCoverage(this.decision);
        report.setBasicConditionCoverage(this.basicCond);
        report.setModifiedConditionCoverage(this.modifCond);
        report.setMultipleConditionCoverage(this.multCond);
    }

    /**
     * Extract the ratio from the .cio file.
     * @param line
     *            the current line
     * @param size
     *            the number of token on line
     * @param pattern
     *            check the syntax of the file
     * @return ratio the ratio
     * @throws IOException
     *             if bad reading issues
     * @throws CioException
     *             if bad format in .cio file
     */
    private String computeRatio(final String line, final int size,
            final String pattern) throws IOException, CioException {
        String[] v = new String[size];
        final String cleanLine = line.replace(",", "");
        v = cleanLine.split(" ");

        if (!(v[0].equals(pattern))) {
            throw new CioException("bad pattern in .cio file: " + v[0]
                    + " instead of " + pattern);
        }

        if (!v[size - 2].equals("none") && !v[size - 2].equals("n/a")) {
            // some ratio are not computed for each file
            return (v[size - 2] + " " + v[size - 1]);
        }

        return null;

    }

    /**
     * Next clean line in the .cio file.
     * @param br
     *            input stream buffer
     * @return next clean line
     * @throws IOException
     *             if read error
     */
    private String nextCleanLine(final BufferedReader br) throws IOException {
        String line = br.readLine();
        line = line.trim();
        line = line.replaceAll("\t", " .. ");
        line = line.replaceAll(" {2,}", " ");
        return line;
    }

    /**
     * Initialize a coverage report if no hit.
     * @param haveInformation
     *            the field to initialized
     */
    public final void init(final boolean[] haveInformation) {
        // Functions
        if (haveInformation[ReportTag.FUNCTION.ordinal()]) {
            function.setValue(0, 1);
        }

        // Calls
        if (haveInformation[ReportTag.CALL.ordinal()]) {
            call.setValue(0, 1);
        }

        // Statement
        if (haveInformation[ReportTag.STATEMENTBLOCK.ordinal()]) {
            statBlock.setValue(0, 1);
        }

        // Implicit
        if (haveInformation[ReportTag.IMPLICITBLOCK.ordinal()]) {
            implBlock.setValue(0, 1);
        }

        // Decision
        if (haveInformation[ReportTag.DECISION.ordinal()]) {
            decision.setValue(0, 1);
        }

        // Loop
        if (haveInformation[ReportTag.LOOP.ordinal()]) {
            loop.setValue(0, 1);
        }

        // Basic
        if (haveInformation[ReportTag.BASICCONDITION.ordinal()]) {
            basicCond.setValue(0, 1);
        }

        // Modified
        if (haveInformation[ReportTag.MODIFIEDCONDITION.ordinal()]) {
            modifCond.setValue(0, 1);
        }

        // Mult
        if (haveInformation[ReportTag.MULTIPLECONDITION.ordinal()]) {
            multCond.setValue(0, 1);
        }

    }

    /**
     * Give the name of the associated tio file.
     * @return the name of the associated tio file
     */
    public final String getNameOfAssociatedTioFile() {
        return nameOfAssociatedTioFile;
    }

    /**
     * Modify the name of the associated tio file.
     * @param sNameOfAssociatedTioFile
     *            the name of the associated tio file
     */
    public final void setNameOfAssociatedTioFile(
            final String sNameOfAssociatedTioFile) {
        this.nameOfAssociatedTioFile = sNameOfAssociatedTioFile;
    }
}
