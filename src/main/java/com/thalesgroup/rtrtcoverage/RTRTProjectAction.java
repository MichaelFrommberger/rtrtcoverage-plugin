package com.thalesgroup.rtrtcoverage;

import hudson.model.Action;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Project view extension by RTRT plugin.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public final class RTRTProjectAction implements Action {

    /**
     * The project. Must be public to find the Messages pattern!
     */
    public final AbstractProject<?, ?> project;

    /**
     * Default Constructor.
     *
     * @param aProject
     *            the project
     */
    public RTRTProjectAction(final AbstractProject<?, ?> aProject) {
        this.project = aProject;
    }

    /**
     * Get the icon file name.
     *
     * @return graph.gif
     */
    public String getIconFileName() {
        return "graph.gif";
    }

    /**
     * Display the name of the action.
     *
     * @return name in Messages.properties
     */
    public String getDisplayName() {
        return Messages.ProjectAction_DisplayName();
    }

    /**
     * Get the url name.
     *
     * @return rtrtcoverage.
     */
    public String getUrlName() {
        return "rtrtcoverage";
    }

    /**
     * Gets the most recent {@link RTRTBuildAction} object.
     *
     * @return the most recent RTRTBuildAction
     */
    public RTRTBuildAction getLastResult() {
        for (AbstractBuild<?, ?> b = project.getLastBuild(); b != null; b = b
                .getPreviousBuild()) {
            if (b.getResult() == Result.FAILURE) {
                continue;
            }
            final RTRTBuildAction r = b.getAction(RTRTBuildAction.class);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    /**
     * Display the graph.
     *
     * @param req
     *            StaplerRequest
     * @param rsp
     *            StaplerResponse
     * @throws IOException
     *             if some reading errors
     */
    public void doGraph(final StaplerRequest req, final StaplerResponse rsp)
            throws IOException {
        if (getLastResult() != null) {
            getLastResult().doGraph(req, rsp);
        }
    }
}
