package com.thalesgroup.rtrtcoverage.tracemerge;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;

/**
 * Class managing hits coverage values.
 * @author Bastien Reboulet
 */
public class CoverageRate {

    /**
     * Number of branches of this type covered in one node.
     */
    private int coveredNumber;

    /**
     * Total number of this branch type in one node.
     */
    private int total;

    /**
     * Type of the branches considered.
     */
    private BranchType type;


    /**
     * @return the ratio coveredNumber/total in percent with one digit (ex: "12.3%").
     */
    public final String getPercentRatio() {
        if (total == 0) {
            return "--%";
        }
        final double thousand = 1000.0; // checkstyle need
        final double ten = 10.0;        // checkstyle need

        return Double.toString(Math.round(coveredNumber * thousand / total) / ten) + "%";
    }

    /**
     * @param newType a type of branch.
     */
    public CoverageRate(final BranchType newType) {
        this.type = newType;
    }

    /**
     * Increment the total value of one.
     */
    public final void incrementTotal() {
        this.total++;
    }

    /**
     * Increment the total value of the specified delta.
     * @param delta the value to increment the total.
     */
    public final void incrementTotal(final int delta) {
        this.total += delta;
    }

    /**
     * Increment the covered number value of one.
     */
    public final void incrementCovered() {
        this.coveredNumber++;
    }

    /**
     * Increment the covered number value of the specified delta.
     * @param delta the value to increment the covered number..
     */
    public final void incrementCovered(final int delta) {
        this.coveredNumber += delta;
    }

    /**
     * @return the covered number value.
     */
    public final int getCoveredNumber() {
        return this.coveredNumber;
    }

    /**
     * @return the total value.
     */
    public final int getTotal() {
        return total;
    }

    /**
     * @return the type of branches considered.
     */
    public final BranchType getType() {
        return type;
    }

    /**
     * @param newType of the branches considered.
     */
    public final void setType(final BranchType newType) {
        this.type = newType;
    }

}
