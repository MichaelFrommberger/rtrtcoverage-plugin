package com.thalesgroup.rtrtcoverage;

import hudson.model.ModelObject;
import hudson.model.AbstractBuild;
import hudson.util.TextFile;

import java.io.File;
import java.io.IOException;


/**
 * Base class of the coverage report tree, which maintains the details of the
 * coverage report.
 *
 * @author Sebastien Barbier
 * @version 1.0
 * @param <PARENT>
 *            its parent
 * @param <SELF>
 *            itself
 */
public abstract class AbstractReport<PARENT extends AggregatedReport<?, PARENT, ?>, SELF extends CoverageObject<SELF>>
extends CoverageObject<SELF> implements ModelObject {

    /**
     * Parent in the tree of the report.
     */
    private PARENT parent;

    /**
     * source path of an associate source file (if exists).
     */
    private String sourcePath;


    /**
     * The name to display in the gui.
     *
     * @return the name to display
     */
    public final String getDisplayName() {
        return getName();
    }

    /**
     * Called at the last stage of the tree construction, to set the back
     * pointer.
     *
     * @param p
     *            the parent to the current coverage report
     */
    protected void setParent(final PARENT p) {
        this.parent = p;
    }

    /**
     * Gets the back pointer to the parent coverage object.
     *
     * @return the parent of the current report
     */
    public final PARENT getParent() {
        return parent;
    }

    /**
     * Return the previous coverage report if exists.
     *
     * @return previous coverage report if exists, null otherwise
     */
    @Override
    public SELF getPreviousResult() {
        PARENT p = parent;
        while (true) {
            p = p.getPreviousResult();
            if (p == null) {
                return null;
            }
            final SELF prev = (SELF) p.getChildren().get(getName());
            if (prev != null) {
                return prev;
            }
        }
    }

    /**
     * Get the build associated with the report.
     *
     * @return the build of the parent
     */
    @Override
    public AbstractBuild<?, ?> getBuild() {
        return parent.getBuild();
    }

    /**
     * Fix the path to an existing source file.
     *
     * @param path
     *            the path where to find the source file. Null if not exist.
     */
    public final void setSourcePath(final String path) {
        this.sourcePath = path;
    }

    /**
     * Get the path to an existing source file.
     *
     * @return the path. Null if not exist.
     */
    public final String getSourcePath() {
        return sourcePath;
    }

    /**
     * Indicate if current node have an associated source file.
     *
     * @return true if an associated source file exists.
     */
    public final boolean isSourceCodeLevel() {
        return (sourcePath != null);
    }

    /**
     * gets the file corresponding to the source file.
     *
     * @return The file where the source file is if exists
     */
    private File getSourceFile() {
        return new File(sourcePath);
    }

    /**
     * Indicates if the source file is available. Modified to allow source code
     * coverage when failed!
     *
     * @return true if the file is available and that last build is stable
     */
    public final boolean isSourceFileAvailable() {
        // return parent.getBuild() == parent.getBuild().getProject()
        // .getLastStableBuild()
        // && getSourceFile().exists();
        return getSourceFile().exists();
    }

    /**
     * Give the contents of the source file.
     *
     * @return the content
     */
    public final String getSourceFileContent() {
        try {
            return new TextFile(getSourceFile()).read();
        } catch (final IOException e) {
            return null;
        }
    }

}
