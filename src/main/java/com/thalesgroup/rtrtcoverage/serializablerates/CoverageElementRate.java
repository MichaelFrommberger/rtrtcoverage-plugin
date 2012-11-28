package com.thalesgroup.rtrtcoverage.serializablerates;

import java.io.Serializable;

import com.thalesgroup.rtrtcoverage.Ratio;
import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;

/**
 * Base class for all the serializable rates.
 * @author Bastien Reboulet
 */
public class CoverageElementRate implements Serializable {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -966535772034860531L;

    /**
     * Functions ratio.
     */
    private Ratio function;
    /**
     * Exits ratio.
     */
    private Ratio exit;
    /**
     * Calls ratio.
     */
    private Ratio call;
    /**
     * Statement blocks ratio.
     */
    private Ratio statBlock;
    /**
     * Implicit blocks ratio.
     */
    private Ratio implBlock;
    /**
     * Decisions ratio.
     */
    private Ratio decision;
    /**
     * Loops ratio.
     */
    private Ratio loop;
    /**
     * Basic Conditions ratio.
     */
    private Ratio basicCond;
    /**
     * Modified Conditions ratio.
     */
    private Ratio modifCond;
    /**
     * Multiple conditions ratio.
     */
    private Ratio multCond;

    /**
     * Default constructor.
     */
    public CoverageElementRate() {
        function = new Ratio();
        exit = new Ratio();
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
     * @param type of the ratio we want to get.
     * @return the ratio corresponding to the specified type.
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
     * @return a sum of function and exit ratios.
     */
    public final Ratio getFunctionAndExit() {
        Ratio functionAndExit = new Ratio();
        functionAndExit.addRatio(function);
        functionAndExit.addRatio(exit);
        return functionAndExit;
    }
    /**
     * @return the function ratio.
     */
    public final Ratio getFunction() {
        return function;
    }
    /**
     * @return the exit ratio.
     */
    public final Ratio getExit() {
        return exit;
    }
    /**
     * @return the call ratio.
     */
    public final Ratio getCall() {
        return call;
    }
    /**
     * @return the statement block ratio.
     */
    public final Ratio getStatBlock() {
        return statBlock;
    }
    /**
     * @return the implicit block ratio.
     */
    public final Ratio getImplBlock() {
        return implBlock;
    }
    /**
     * @return the sum of statement block and implicit block ratios.
     */
    public final Ratio getDecision() {
        decision.setRatio(statBlock.getNumerator(), statBlock.getDenominator());
        decision.addRatio(implBlock);
        return decision;
    }
    /**
     * @return the loop ratio.
     */
    public final Ratio getLoop() {
        return loop;
    }
    /**
     * @return the basic condition ratio.
     */
    public final Ratio getBasicCond() {
        return basicCond;
    }
    /**
     * @return the modified condition ratio.
     */
    public final Ratio getModifCond() {
        return modifCond;
    }
    /**
     * @return the multiple condition ratio
     */
    public final Ratio getMultCond() {
        return multCond;
    }

}
