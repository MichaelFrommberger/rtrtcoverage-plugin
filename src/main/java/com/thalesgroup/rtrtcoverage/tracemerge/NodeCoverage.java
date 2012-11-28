package com.thalesgroup.rtrtcoverage.tracemerge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.fdcreader.MultipleBranchDefinition;

/**
 * Class allowing to store data of a node from merged data between FDC and TIO file.
 * @author Bastien Reboulet
 */
public class NodeCoverage {

    /**
     * The name of this node.
     */
    private String nodeName;

    /**
     * A mapping between mark+id of a branch and the corresponding branch.
     */
    private Map<String, IBranchCoverage> globalBranchCovs;

    /**
     * A mapping between test name and a corresponding mapping of branch coverages.
     */
    private Map<String, Map<String, IBranchCoverage>> testBranchCovs;

    /**
     * Default constructor.
     */
    public NodeCoverage() {
        globalBranchCovs = new HashMap<String, IBranchCoverage>();
        testBranchCovs = new HashMap<String, Map<String, IBranchCoverage>>();
    }

    /**
     * During the merging, if the test is new for this node.
     * initialize all its branchCov (by copying globalBranches)
     * @param testName a test name.
     */
    public final void initTestIfNew(final String testName) {
        if (!testBranchCovs.containsKey(testName)) {
            Map<String, IBranchCoverage> testBranches =
                    new HashMap<String, IBranchCoverage>();
            testBranchCovs.put(testName, testBranches);
            for (IBranchCoverage branch : globalBranchCovs.values()) {
                if (branch instanceof SingleBranchCoverage) {
                    testBranches.put(
                            branch.getMarkId(),
                            new SingleBranchCoverage(branch.getBranchDefinition()));
                } else if (branch instanceof MultipleBranchCoverage) {
                    testBranches.put(
                            branch.getMarkId(),
                            new MultipleBranchCoverage(branch.getBranchDefinition()));
                }
            }
            // Process the matching between multiple and single branches (for Modified Conditions type)
            for (IBranchCoverage branch : testBranches.values()) {
                if (branch instanceof MultipleBranchCoverage) {
                    MultipleBranchDefinition branchDef = (MultipleBranchDefinition) branch.getBranchDefinition();
                    for (String markId : branchDef.getSubBranchMarkIds()) {
                        for (IBranchCoverage branch2: testBranches.values()) {
                            if (branch2.getMarkId().equals(markId)) {
                                ((MultipleBranchCoverage) branch).addSubBranch(
                                        (SingleBranchCoverage) branch2);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the names of all the tests covering this node.
     */
    public final Set<String> getTestNames() {
        return testBranchCovs.keySet();
    }

    /**
     * @param branches a collection of the branches we want coverage rates from.
     * @param type the type of branches we want coverage rates from.
     * @return a coverage rate for the branches of the specified type.
     */
    private CoverageRate getRate(
            final Collection<IBranchCoverage> branches,
            final BranchType type) {
        CoverageRate rate = new CoverageRate(type);
        for (IBranchCoverage branch : branches) {
            if (branch.getType() == type) {
                rate.incrementTotal();
                if (branch.isCovered()) {
                    rate.incrementCovered();
                }
            }
        }
        return rate;
    }

    /**
     * @param type of the branches we want coverage from.
     * @return a global coverage rate (for all the tests) for the specified type.
     */
    public final CoverageRate getGlobalRate(final BranchType type) {
        return getRate(globalBranchCovs.values(), type);
    }

    /**
     * @param testName the name of the test we want coverage rate from.
     * @param type the type of branches we want coverage rate from.
     * @return the coverage rate for the specified test name and type
     */
    public final CoverageRate getTestRate(final String testName, final BranchType type) {
        return getRate(testBranchCovs.get(testName).values(), type);
    }

    /**
     * @return the name of this node.
     */
    public final String getNodeName() {
        return nodeName;
    }

    /**
     * @param newNodeName a node name.
     */
    public final void setNodeName(final String newNodeName) {
        this.nodeName = newNodeName;
    }

    /**
     * @param branch a branch coverage.
     */
    public final void addBranchToGlobalCoverage(final IBranchCoverage branch) {
        globalBranchCovs.put(branch.getBranchDefinition().getMarkId(), branch);
    }

    /**
     * @param testName a test name.
     * @param branch a branch coverage corresponding to the specified test.
     */
    public final void addBranchToTestCoverage(final String testName, final IBranchCoverage branch) {
        Map<String, IBranchCoverage> map = testBranchCovs.get(testName);
        if (map == null) {
            map = new HashMap<String, IBranchCoverage>();
            map.put(branch.getBranchDefinition().getMarkId(), branch);
            testBranchCovs.put(testName, map);
        }
        testBranchCovs.get(testName).put(branch.getBranchDefinition().getMarkId(), branch);
    }

    /**
     * @param markId a branch mark+id.
     * @return a branch coverage corresponding to the specified mark+id.
     */
    public final IBranchCoverage getGlobalBranchCoverage(final String markId) {
        return globalBranchCovs.get(markId);
    }

    /**
     * @return a collection of all the global branch coverages contained in this node.
     */
    public final Collection<IBranchCoverage> getAllGlobalCoverages() {
        return globalBranchCovs.values();
    }

    /**
     * @param testName a test name.
     * @param markId a concatenation of mark+id.
     * @return the branch coverage corresponding to the specified test name and mark+id.
     */
    public final IBranchCoverage getTestBranchCoverage(final String testName, final String markId) {
        if (testBranchCovs.get(testName) == null) {
            return null;
        }
        return testBranchCovs.get(testName).get(markId);
    }

    /**
     * @param testName a test name.
     * @return a collection of all the branch coverages corresponding to the specified test.
     */
    public final Collection<IBranchCoverage> getTestBranchCoverages(final String testName) {
        if (testBranchCovs.get(testName) == null) {
            return null;
        }
        return testBranchCovs.get(testName).values();
    }

    /**
     * @param testName a test name.
     * @return true if this node if covered by the specified test, else return false.
     */
    public final boolean isCoveredByTest(final String testName) {
        if (testBranchCovs.containsKey(testName)) {
            return true;
        }
        return false;
    }

}
