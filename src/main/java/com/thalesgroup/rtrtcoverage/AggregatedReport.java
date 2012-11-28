package com.thalesgroup.rtrtcoverage;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Coverage reports having some children.
 *
 * @author Sebastien Barbier
 * @version 1.0
 * @param <PARENT>
 *            its parent
 * @param <SELF>
 *            itself
 * @param <CHILD>
 *            its children
 */
public abstract class AggregatedReport<PARENT extends AggregatedReport<?, PARENT, ?>, SELF extends AggregatedReport<PARENT, SELF, CHILD>, CHILD extends AbstractReport<SELF, CHILD>>
extends AbstractReport<PARENT, SELF> {

    /**
     * List of all the children.
     */
    private final Map<String, CHILD> children = new TreeMap<String, CHILD>();

    /**
     * Adding a children into the hierarchy.
     *
     * @param child
     *            the children to add
     */
    public final void add(final CHILD child) {
        children.put(child.getName(), child);
    }

    /**
     * All the children.
     *
     * @return a map with the different children
     */
    public final Map<String, CHILD> getChildren() {
        return children;
    }

    /**
     * Identification of the parent.
     *
     * @param p
     *            the parent of the current report
     */
    @Override
    protected void setParent(final PARENT p) {
        super.setParent(p);
        for (final CHILD c : children.values()) {
            c.setParent((SELF) this);
        }
    }

    /**
     * Get a precise children according to its name.
     *
     * @param token
     *            name of the children
     * @param req
     *            StaplerRequest
     * @param rsp
     *            StaplerResponse
     * @return the children of name token
     * @throws IOException
     *             if some errors during process
     */
    public final CHILD getDynamic(final String token, final StaplerRequest req,
            final StaplerResponse rsp) throws IOException {
        return getChildren().get(token);
    }

    /**
     * Set report failed according to rule. If failed, parent is also marked as
     * failed.
     */
    @Override
    public final void setFailed() {
        super.setFailed();

        if (getParent() != null) {
            getParent().setFailed();
        }
    }

    /**
     * Does current report have children?
     *
     * @return true if has children
     */
    public final boolean hasChildren() {
        return getChildren().size() > 0;
    }

    /**
     * Do children have function coverage?
     *
     * @return true if at least one have function coverage
     */
    public final boolean hasChildrenFunctionCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasFunctionCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have call coverage?
     *
     * @return true if at least one have call coverage
     */
    public final boolean hasChildrenCallCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasCallCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have statement block coverage?
     *
     * @return true if at least one have statement block coverage
     */
    public final boolean hasChildrenStatementBlockCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasStatementBlockCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have implicit block coverage?
     *
     * @return true if at least one have implicit block coverage
     */
    public final boolean hasChildrenImplicitBlockCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasImplicitBlockCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have decision coverage?
     *
     * @return true if at least one have decision coverage
     */
    public final boolean hasChildrenDecisionCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasDecisionCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have loop coverage?
     *
     * @return true if at least one have loop coverage
     */
    public final boolean hasChildrenLoopCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasLoopCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have basic condition coverage?
     *
     * @return true if at least one have basic condition coverage
     */
    public final boolean hasChildrenBasicConditionCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasBasicConditionCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have modified condition coverage?
     *
     * @return true if at least one have modified condition coverage
     */
    public final boolean hasChildrenModifiedConditionCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasModifiedConditionCoverage()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Do children have multiple condition coverage?
     *
     * @return true if at least one have multiple condition coverage
     */
    public final boolean hasChildrenMultipleConditionCoverage() {
        for (final CHILD child : getChildren().values()) {
            if (child.hasMultipleConditionCoverage()) {
                return true;
            }
        }
        return false;
    }
}
