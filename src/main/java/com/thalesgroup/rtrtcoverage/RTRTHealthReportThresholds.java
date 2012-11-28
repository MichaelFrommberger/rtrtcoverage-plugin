package com.thalesgroup.rtrtcoverage;

import java.io.Serializable;

/**
 * Holds the configuration details for {@link hudson.model.HealthReport}
 * generation.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class RTRTHealthReportThresholds implements Serializable {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 5896706013370874498L;
    /**
     * Minimal value for functions and exits.
     */
    private int minFunction;
    /**
     * Maximal value for functions and exits.
     */
    private int maxFunction;
    /**
     * Minimal value for calls.
     */
    private int minCall;
    /**
     * Maximal value for calls.
     */
    private int maxCall;
    /**
     * Minimal value for statement blocks.
     */
    private int minStatBlock;
    /**
     * Maximal value for statement blocks.
     */
    private int maxStatBlock;
    /**
     * Minimal value for implicit blocks.
     */
    private int minImplBlock;
    /**
     * Maximal value for implicit blocks.
     */
    private int maxImplBlock;
    /**
     * Minimal value for decisions.
     */
    private int minDecision;

    /**
     * Maximal value for decisions.
     */
    private int maxDecision;
    /**
     * Minimal value for loops.
     */
    private int minLoop;
    /**
     * Maximal value for loops.
     */
    private int maxLoop;
    /**
     * Minimal value for basic conditions.
     */
    private int minBasicCond;
    /**
     * Maximal value for basic conditions.
     */
    private int maxBasicCond;
    /**
     * Minimal value for modified conditions.
     */
    private int minModifCond;
    /**
     * Maximal value for modified conditions.
     */
    private int maxModifCond;
    /**
     * Minimal value for multiplied conditions.
     */
    private int minMultCond;
    /**
     * Maximal value for multiplied conditions.
     */
    private int maxMultCond;

    /**
     * Default Constructor.
     */
    public RTRTHealthReportThresholds() {
        final int minValue = 0;
        final int maxValue = 100;

        this.minFunction = minValue;
        this.maxFunction = maxValue;
        this.minCall = minValue;
        this.maxCall = maxValue;
        this.minStatBlock = minValue;
        this.maxStatBlock = maxValue;
        this.minImplBlock = minValue;
        this.maxImplBlock = maxValue;
        this.minDecision = minValue;
        this.maxDecision = maxValue;
        this.minLoop = minValue;
        this.maxLoop = maxValue;
        this.minBasicCond = minValue;
        this.maxBasicCond = maxValue;
        this.minModifCond = minValue;
        this.maxModifCond = maxValue;
        this.minMultCond = minValue;
        this.maxMultCond = maxValue;
    }

    /**
     * Apply a range to a given value.
     *
     * @param min
     *            minimal value of the interval
     * @param value
     *            value to compare
     * @param max
     *            maximal value of the interval
     * @return a value in the given interval. value is unchanged if min <= value
     *         <= max.
     */
    private int applyRange(final int min, final int value, final int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Guarantee the validation of each interval.
     */
    public final void ensureValid() {
        final int minInterval = 0;
        final int maxInterval = 100;

        maxFunction = applyRange(minInterval, maxFunction, maxInterval);
        minFunction = applyRange(minInterval, minFunction, maxFunction);
        maxCall = applyRange(minInterval, maxCall, maxInterval);
        minCall = applyRange(minInterval, minCall, maxCall);
        maxStatBlock = applyRange(minInterval, maxStatBlock, maxInterval);
        minStatBlock = applyRange(minInterval, minStatBlock, maxStatBlock);
        maxImplBlock = applyRange(minInterval, maxImplBlock, maxInterval);
        minImplBlock = applyRange(minInterval, minImplBlock, maxImplBlock);
        maxDecision = applyRange(minInterval, maxDecision, maxInterval);
        minDecision = applyRange(minInterval, minDecision, maxDecision);
        maxLoop = applyRange(minInterval, maxLoop, maxInterval);
        minLoop = applyRange(minInterval, minLoop, maxLoop);
        maxBasicCond = applyRange(minInterval, maxBasicCond, maxInterval);
        minBasicCond = applyRange(minInterval, minBasicCond, maxBasicCond);
        maxModifCond = applyRange(minInterval, maxModifCond, maxInterval);
        minModifCond = applyRange(minInterval, minModifCond, maxModifCond);
        maxMultCond = applyRange(minInterval, maxMultCond, maxInterval);
        minMultCond = applyRange(minInterval, minMultCond, maxMultCond);
    }

    /**
     * Minimum for function and exits.
     *
     * @return corresponding minimum
     */
    public final int getMinFunction() {
        return minFunction;
    }

    /**
     * Set the Minimum for function and exits.
     *
     * @param iMinFunction
     *            minimal value
     */
    public final void setMinFunction(final int iMinFunction) {
        this.minFunction = iMinFunction;
    }

    /**
     * Maximum for function and exits.
     *
     * @return corresponding maximum
     */
    public final int getMaxFunction() {
        return maxFunction;
    }

    /**
     * Set the Maximum for function and exits.
     *
     * @param iMaxFunction
     *            maximal value
     */
    public final void setMaxFunction(final int iMaxFunction) {
        this.maxFunction = iMaxFunction;
    }

    /**
     * Minimum for calls.
     *
     * @return corresponding minimum
     */
    public final int getMinCall() {
        return minCall;
    }

    /**
     * Set the Minimum for calls.
     *
     * @param iMinCall
     *            minimal value
     */
    public final void setMinCall(final int iMinCall) {
        this.minCall = iMinCall;
    }

    /**
     * Maximum for calls.
     *
     * @return corresponding maximum
     */
    public final int getMaxCall() {
        return maxCall;
    }

    /**
     * Set the Maximum for calls.
     *
     * @param iMaxCall
     *            maximal value
     */
    public final void setMaxCall(final int iMaxCall) {
        this.maxCall = iMaxCall;
    }

    /**
     * Minimum for statement blocks.
     *
     * @return corresponding minimum
     */
    public final int getMinStatBlock() {
        return minStatBlock;
    }

    /**
     * Set the Minimum for statement blocks.
     *
     * @param iMinStatBlock
     *            minimal value
     */
    public final void setMinStatBlock(final int iMinStatBlock) {
        this.minStatBlock = iMinStatBlock;
    }

    /**
     * Maximum for statement blocks.
     *
     * @return corresponding maximum
     */
    public final int getMaxStatBlock() {
        return maxStatBlock;
    }

    /**
     * Set the Maximum for statement blocks.
     *
     * @param iMaxStatBlock
     *            maximal value
     */
    public final void setMaxStatBlock(final int iMaxStatBlock) {
        this.maxStatBlock = iMaxStatBlock;
    }

    /**
     * Minimum for implicit blocks.
     *
     * @return corresponding minimum
     */
    public final int getMinImplBlock() {
        return minImplBlock;
    }

    /**
     * Set the Minimum for implicit blocks.
     *
     * @param iMinImplBlock
     *            minimal value
     */
    public final void setMinImplBlock(final int iMinImplBlock) {
        this.minImplBlock = iMinImplBlock;
    }

    /**
     * Maximum for implicit blocks.
     *
     * @return corresponding maximum
     */
    public final int getMaxImplBlock() {
        return maxImplBlock;
    }

    /**
     * Set the Maximum for implicit blocks.
     *
     * @param iMaxImplBlock
     *            maximal value
     */
    public final void setMaxImplBlock(final int iMaxImplBlock) {
        this.maxImplBlock = iMaxImplBlock;
    }

    /**
     * Minimum for decisions.
     *
     * @return corresponding minimum
     */
    public final int getMinDecision() {
        return minDecision;
    }

    /**
     * Set the minimum for decisions.
     *
     * @param iMinDecision
     *            minimal value
     */
    public final void setMinDecision(final int iMinDecision) {
        this.minDecision = iMinDecision;
    }

    /**
     * Maximum for decisions.
     *
     * @return corresponding maximum
     */
    public final int getMaxDecision() {
        return maxDecision;
    }

    /**
     * Set the maximum for decisions.
     *
     * @param iMaxDecision
     *            maximal value
     */
    public final void setMaxDecision(final int iMaxDecision) {
        this.maxDecision = iMaxDecision;
    }

    /**
     * Minimum for loops.
     *
     * @return corresponding minimum
     */
    public final int getMinLoop() {
        return minLoop;
    }

    /**
     * Set the minimum value for loops.
     *
     * @param iMinLoop
     *            minimal value
     */
    public final void setMinLoop(final int iMinLoop) {
        this.minLoop = iMinLoop;
    }

    /**
     * Maximum for loops.
     *
     * @return corresponding maximum
     */
    public final int getMaxLoop() {
        return maxLoop;
    }

    /**
     * Set the maximum value for loops.
     *
     * @param iMaxLoop
     *            maximal value
     */
    public final void setMaxLoop(final int iMaxLoop) {
        this.maxLoop = iMaxLoop;
    }

    /**
     * Minimum for basic conditions.
     *
     * @return corresponding minimum
     */
    public final int getMinBasicCond() {
        return minBasicCond;
    }

    /**
     * Set the minimum value for basic conditions.
     *
     * @param iMinBasicCond
     *            minimal value
     */
    public final void setMinBasicCond(final int iMinBasicCond) {
        this.minBasicCond = iMinBasicCond;
    }

    /**
     * Maximum for basic conditions.
     *
     * @return corresponding maximum
     */
    public final int getMaxBasicCond() {
        return maxBasicCond;
    }

    /**
     * Set the maximum value for basic conditions.
     *
     * @param iMaxBasicCond
     *            maximal value
     */
    public final void setMaxBasicCond(final int iMaxBasicCond) {
        this.maxBasicCond = iMaxBasicCond;
    }

    /**
     * Minimum for modified conditions.
     *
     * @return corresponding minimum
     */
    public final int getMinModifCond() {
        return minModifCond;
    }

    /**
     * Set the minimum value for modified conditions.
     *
     * @param iMinModifCond
     *            minimal value
     */
    public final void setMinModifCond(final int iMinModifCond) {
        this.minModifCond = iMinModifCond;
    }

    /**
     * Maximum for modified conditions.
     *
     * @return corresponding maximum
     */
    public final int getMaxModifCond() {
        return maxModifCond;
    }

    /**
     * Set the maximum value for modified conditions.
     *
     * @param iMaxModifCond
     *            maximal value
     */
    public final void setMaxModifCond(final int iMaxModifCond) {
        this.maxModifCond = iMaxModifCond;
    }

    /**
     * Minimum for multiple conditions.
     *
     * @return corresponding minimum
     */
    public final int getMinMultCond() {
        return minMultCond;
    }

    /**
     * Set the minimum value for multiple conditions.
     *
     * @param iMinMultCond
     *            minimal value
     */
    public final void setMinMultCond(final int iMinMultCond) {
        this.minMultCond = iMinMultCond;
    }

    /**
     * Maximum for multiple conditions.
     *
     * @return corresponding maximum
     */
    public final int getMaxMultCond() {
        return maxMultCond;
    }

    /**
     * Set the maximum value for multiple conditions.
     *
     * @param iMaxMultCond
     *            maximal value
     */
    public final void setMaxMultCond(final int iMaxMultCond) {
        this.maxMultCond = iMaxMultCond;
    }
}
