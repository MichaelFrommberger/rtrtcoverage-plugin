package com.thalesgroup.rtrtcoverage;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.thalesgroup.rtrtcoverage.cioreader.CioException;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentificationException;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentitiesExport;
import com.thalesgroup.rtrtcoverage.filesmapping.FilesMapping;
import com.thalesgroup.rtrtcoverage.tioreader2.TioException;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageMergeException;

/**
 * {@link Publisher} that captures RTRT coverage reports.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class RTRTPublisher extends Recorder {
    private static final Result Result = null;

    /**
     * the ant-style pattern input in web ui where to find instrumented files
     * Must be public to be seen by config.jelly
     */
    public String augPattern;

    /**
     * true if TUSAR export checkbox checked in web ui. Must be public to be
     * seen by config.jelly
     */
    public boolean mustTusarExport = false;

    /**
     * Relative (from build) path of TUSAR export folder. Must be public to be
     * seen by config.jelly
     */
    public String tusarExportPath;

    /**
     * Relative ant-style path to the RTRT cio files inside the workspace. Must be public
     * to be seen by config.jelly
     */
    public String includesCio;

    /**
     * Relative ant-style path to the RTRT fdc files inside the workspace. Must be public
     * to be seen by config.jelly
     */
    public String includesFdc;

    /**
     * Relative ant-style path to the RTRT tio files inside the workspace. Must be public
     * to be seen by config.jelly
     */
    public String includesTio;

    /**
     * Rule to be enforced. Can be null.
     */
    private Rule rule;

    /**
     * {@link hudson.model.HealthReport} thresholds to apply. Must be public to
     * be seen by config.jelly
     */
    public final RTRTHealthReportThresholds healthReports = new RTRTHealthReportThresholds();

    /**
     * Look for rtrtcoverage reports (*.cio) based in the configured parameter
     * includes. 'includes' is - an Ant-style pattern - a list of files and
     * folders separated by the characters ;:,
     *
     * @param workspace
     *            the global path
     * @param includes
     *            where to search
     * @return the path of all the rtrtcoverage reports
     * @throws IOException
     *             if error during reading
     * @throws InterruptedException
     *             if error during reading
     */
    protected static FilePath[] locateCoverageReports(final FilePath workspace,
            final String includes) throws IOException, InterruptedException {

        // First use ant-style pattern
        try {
            final FilePath[] ret = workspace.list(includes);
            if (ret.length > 0) {
                return ret;
            }
        } catch (final Exception e) {
            throw new InterruptedException(e.getMessage());
        }

        // If it fails, do a legacy search
        final ArrayList<FilePath> files = new ArrayList<FilePath>();
        final String[] parts = includes.split("\\s*[;:,]+\\s*");
        for (final String path : parts) {
            final FilePath src = workspace.child(path);
            if (src.exists()) {
                if (src.isDirectory()) {
                    files.addAll(Arrays.asList(src.list("**/*.cio")));
                } else {
                    files.add(src);
                }
            }
        }
        return files.toArray(new FilePath[files.size()]);
    }

    /**
     * Look for rtrtcoverage reports (*.tio) based in the configured parameter
     * includes. 'includes' is - an Ant-style pattern - a list of files and
     * folders separated by the characters ;:,
     *
     * @param workspace
     *            the global path
     * @param includes
     *            where to search
     * @return the path of all the rtrtcoverage by test reports
     * @throws IOException
     *             if error during reading
     * @throws InterruptedException
     *             if error during reading
     */
    protected static FilePath[] locateTestCoverageReports(
            final FilePath workspace, final String includes)
                    throws IOException, InterruptedException {

        // First use ant-style pattern
        try {
            final FilePath[] ret = workspace.list(includes);
            if (ret.length > 0) {
                return ret;
            }
        } catch (final Exception e) {
            throw new InterruptedException(e.getMessage());
        }

        // If it fails, do a legacy search
        final ArrayList<FilePath> files = new ArrayList<FilePath>();
        final String[] parts = includes.split("\\s*[;:,]+\\s*");
        for (final String path : parts) {
            final FilePath src = workspace.child(path);
            if (src.exists()) {
                if (src.isDirectory()) {
                    files.addAll(Arrays.asList(src.list("**/*.tio")));
                } else {
                    files.add(src);
                }
            }
        }
        return files.toArray(new FilePath[files.size()]);
    }

    /**
     * Save rtrtcoverage reports (*.cio files).
     * From the workspace to build folder.
     *
     * @param folder
     *            directory where to save files
     * @param tioFiles
     *            the associed tio pairs
     * @throws Exception an Exception
     */
    protected static int saveCoverageReports(final FilePath folder, final FilePath[] tioFiles)
            throws Exception {
        int savedFileNum = 0;
        folder.mkdirs();
        for (FilePath tio : tioFiles) {
            FilePath src = new FilePath(tio.getParent(), tio.getBaseName() + ".cio");
            if (src.exists()) {
                src.copyTo(folder.child(src.getName().toUpperCase()));
                savedFileNum++;
            } else {
                FilePath[] cioFile = tio.getParent().list("*.cio");
                if (tio.getParent().list("*.tio").length == 1) {
                    src = cioFile[0];
                    src.copyTo(folder.child(tio.getBaseName().toUpperCase() + ".CIO"));
                    savedFileNum++;
                } else {
                    throw new Exception("[RTRTCoverage]: ERROR: *.cio<->*.tio files match impossible. Multiple tio files for unique cio not yet supported.");
                }
            }
        }
        return savedFileNum;
    }

    /**
     * Save rtrtcoverage by test reports (*.tio files) from the workspace to
     * build folder.
     *
     * @param folder
     *            directory where to save files
     * @param files
     *            the files to save
     * @throws IOException
     *             if error during reading
     * @throws InterruptedException
     *             if error during reading
     */
    protected static void saveTestCoverageReports(final FilePath folder,
            final FilePath[] files) throws IOException, InterruptedException {
        folder.mkdirs();
        for (int i = 0; i < files.length; i++) {
            final String name = files[i].getName();
            final FilePath src = files[i];
            final FilePath dst = folder.child(name.toUpperCase());
            src.copyTo(dst);
        }
    }

    /**
     * Save rtrtcoverage source code files from the workspace to build folder.
     *
     * @param folder
     *            directory where to save files
     * @param files
     *            the files to save
     * @throws IOException
     *             if error during reading
     * @throws InterruptedException
     *             if error during reading
     */
    protected static void saveCodeSourceFiles(final FilePath folder,
            final FilePath[] files) throws IOException, InterruptedException {
        folder.mkdirs();
        for (int i = 0; i < files.length; i++) {
            final String name = files[i].getName().toUpperCase();
            final FilePath src = files[i];
            final FilePath dst = folder.child(name);
            src.copyTo(dst);
        }
    }

    /**
     * Perform the search of the files.
     *
     * @param build
     *            the builder
     * @param launcher
     *            a launcher
     * @param listener
     *            a listener
     * @return true if some results
     * @throws InterruptedException
     *             if error during reading
     * @throws IOException
     *             if error during reading
     */
    @Override
    public final boolean perform(final AbstractBuild<?, ?> build,
            final Launcher launcher, final BuildListener listener)
                    throws InterruptedException, IOException {

        final PrintStream logger = listener.getLogger();

        FilePath[] reports = build.getWorkspace().list(includesTio);

        if (reports.length == 0) {
            if (build.getResult().isWorseThan(hudson.model.Result.UNSTABLE)) {
                return true;
            }

            logger.println("[RTRTCoverage] [WARNING]: no coverage files found in workspace. Was any report generated?");
            build.setResult(hudson.model.Result.FAILURE);
            return true;
        }

        final FilePath rtrtcoveragefolder = new FilePath(getRTRTReport(build));
        int numCioFilesSaved = 0;
        try {
            numCioFilesSaved = saveCoverageReports(rtrtcoveragefolder, reports);
        } catch (Exception e2) {
            build.setResult(hudson.model.Result.UNSTABLE);
            logger.println(e2.getMessage());
        }
        logger.println("[RTRTCoverage]: stored " + numCioFilesSaved
                + " report files (*.cio) in the build folder: "
                + rtrtcoveragefolder);

        FilePath[] rtrtTestCoverageFiles = build.getWorkspace().list(includesTio);
        if (rtrtTestCoverageFiles.length == 0) {
            logger.println("[RTRTCoverage] [WARNING]: no rtrt test coverage file found.");
        }

        final FilePath rtrtTestCoverageFolder = new FilePath(
                getRTRTReport(build));
        saveCodeSourceFiles(rtrtTestCoverageFolder, rtrtTestCoverageFiles);
        logger.println("[RTRTCoverage]: stored " + rtrtTestCoverageFiles.length
                + " test coverage files (.tio) in the build folder: "
                + rtrtTestCoverageFolder);

        FilePath[] fdcFiles = build.getWorkspace().list(includesFdc);
        if (fdcFiles.length == 0) {
            logger.println("[RTRTCoverage] [WARNING]: no source file found.");
        }

        final FilePath rtrtfdcfolder = new FilePath(getRTRTCodeSource(build));
        saveCodeSourceFiles(rtrtfdcfolder, fdcFiles);
        logger.println("[RTRTCoverage]: stored " + fdcFiles.length
                + " code sources files (.fdc) in the build folder: "
                + rtrtfdcfolder);

        RTRTBuildAction action = null;
        try {
            action = RTRTBuildAction.load(
                    build,
                    logger,
                    rule,
                    healthReports,
                    locateCoverageReports(rtrtcoveragefolder,
                            includesCio.toUpperCase()));
        } catch (final CioException e) {
            logger.println("[RTRTCoverage] [ERROR]: " + e.getMessage());
            return false;
        }

        logger.println("[RTRTCoverage]: "
                + action.getBuildHealth().getDescription());

        build.getActions().add(action);

        if (action.getResult().isFailed()) {
            logger.println("[RTRTCoverage] [WARNING]: code coverage enforcement failed. Setting Build to unstable.");
            build.setResult(hudson.model.Result.UNSTABLE);
        }

        // Build files mapping
        logger.println("[RTRTCoverage]: Building files mapping...");
        FilesMapping mapping = new FilesMapping();
        try {
            mapping.build(rtrtTestCoverageFolder, build.getWorkspace(), augPattern);
        } catch (FileIdentificationException e1) {
            build.setResult(hudson.model.Result.UNSTABLE);
            logger.println(e1.getMessage());
        } catch (TioException e1) {
            logger.println(e1.getMessage());
            build.setResult(hudson.model.Result.UNSTABLE);
        }
        logger.println("[RTRTCoverage]: Files mapping done");

        // Save file IDs
        if (build.getWorkspace().list(augPattern).length != 0) {
            logger.println("[RTRTCoverage]: Saving files ID correspondances...");
            File fileIdOutput = new File(build.getRootDir() + "/file_identities.xml");
            (new FileIdentitiesExport()).export(mapping, fileIdOutput);
            logger.println("[RTRTCoverage]: Files ID correspondances saved at " + fileIdOutput.getPath());
        } else {
            logger.println("[RTRTCoverage] [WARNING]: Cannot find instrumented files: "
                    + "unable to find and save files ID correspondances");
            build.setResult(hudson.model.Result.UNSTABLE);
        }

        if (mustTusarExport) {
            logger.println("[RTRTCoverage]: TUSAR export in progress...");
            if (build.getWorkspace().list(augPattern).length != 0) {
                File outputFile = null;
                if (tusarExportPath != null
                        && tusarExportPath.contentEquals("")) {
                    outputFile = new File(build.getRootDir()
                            + "/TUSAR/tusar_export.xml");
                } else {
                    outputFile = new File(build.getRootDir() + "/"
                            + tusarExportPath);
                }

                final RtrtCoverage2Tusar tusarExporter = new RtrtCoverage2Tusar(
                        getRTRTCodeSource(build), getRTRTReport(build),
                        build.getWorkspace(), augPattern, outputFile);
                try {
                    tusarExporter.export(mapping);
                } catch (final FileIdentificationException e) {
                    logger.println(e.getMessage());
                    build.setResult(hudson.model.Result.UNSTABLE);
                } catch (final TioException e) {
                    logger.println(e.getMessage());
                    build.setResult(hudson.model.Result.UNSTABLE);
                } catch (final CoverageMergeException e) {
                    logger.println(e.getMessage());
                    build.setResult(hudson.model.Result.UNSTABLE);
                }
                logger.println("[RTRTCoverage]: stored TUSAR (.xml) file at : "
                        + outputFile.getPath());
            } else {
                logger.println("[RTRTCoverage]: TUSAR export error: no instrumented file found!");
                build.setResult(hudson.model.Result.UNSTABLE);
            }
        }
        return true;
    }

    /**
     * Get the project action.
     *
     * @param project
     *            the projet
     * @return the action
     */

    @Override
    public final Action getProjectAction(final AbstractProject<?, ?> project) {
        return new RTRTProjectAction(project);
    }

    /**
     * Get the required monitor service.
     *
     * @return BuildStepMonitor.BUILD
     */

    public final BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    /**
     * Gets the directory to store report files (.tio and .cio files).
     *
     * @param build
     *            the build
     * @return the directory to store report files
     */
    static final File getRTRTReport(final AbstractBuild<?, ?> build) {
        return new File(build.getRootDir(), "rtrtcoverage");
    }

    /**
     * Gets the directory to store .fdc files.
     *
     * @param build
     *            the build
     * @return the directory where to store source files
     */
    static final File getRTRTCodeSource(final AbstractBuild<?, ?> build) {
        return new File(build.getRootDir(), "rtrtcodesource");
    }

    /**
     * Get the descriptor.
     *
     * @return DESCRIPTOR
     */
    @Override
    public final BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    /**
     * The descriptor.
     */
    @Extension
    public static final BuildStepDescriptor<Publisher> DESCRIPTOR = new DescriptorImpl();

    /**
     * Implementation du descriptor.
     *
     * @author Sebastien Barbier
     * @version 1.0
     */
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * Default Constructor.
         */
        public DescriptorImpl() {
            super(RTRTPublisher.class);
        }

        /**
         * Give the display name.
         *
         * @return the name in Messages.properties
         */
        @Override
        public final String getDisplayName() {
            return Messages.RTRTPublisher_DisplayName();
        }

        /**
         * Validation of the pattern of includesCio.
         *
         * @param value
         *            the cio path.
         * @return if written string is ok.
         */
        public final FormValidation doCheckIncludesCio(
                @QueryParameter final String value) {
            return FormValidation.error("There's a problem here");
        }

        /**
         * Does the class is applicable?
         *
         * @param aClass
         *            the class
         * @return true
         */
        @Override
        public final boolean isApplicable(
                final Class<? extends AbstractProject> aClass) {
            return true;
        }

        /**
         * Create a new Instance of the publisher.
         *
         * @param req
         *            StaplerRequest
         * @param json
         *            JSONObject
         * @return a publisher
         */
        @Override
        public final Publisher newInstance(final StaplerRequest req,
                final JSONObject json) {
            final RTRTPublisher pub = new RTRTPublisher();
            req.bindParameters(pub, "rtrtcoverage.");
            req.bindParameters(pub.healthReports, "rtrtcoverageHealthReports.");
            // start ugly hack
            // @TODO remove ugly hack
            // the default converter for integer values used by
            // req.bindParameters
            // defaults an empty value to 0. This happens even if the type is
            // Integer
            // and not int. We want to change the default values, so we use this
            // hack.
            //
            // If you know a better way, please fix.
            final int maxValue = 100;

            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxFunction"))) {
                pub.healthReports.setMaxFunction(maxValue);
            }
            if ("".equals(req.getParameter("rtrtcoverageHealthReports.maxCall"))) {
                pub.healthReports.setMaxCall(maxValue);
            }
            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxStatBlock"))) {
                pub.healthReports.setMaxStatBlock(maxValue);
            }
            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxImplBlock"))) {
                pub.healthReports.setMaxImplBlock(maxValue);
            }
            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxDecision"))) {
                pub.healthReports.setMaxDecision(maxValue);
            }
            if ("".equals(req.getParameter("rtrtcoverageHealthReports.maxLoop"))) {
                pub.healthReports.setMaxLoop(maxValue);
            }
            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxBasicCond"))) {
                pub.healthReports.setMaxBasicCond(maxValue);
            }
            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxModifCond"))) {
                pub.healthReports.setMaxModifCond(maxValue);
            }
            if ("".equals(req
                    .getParameter("rtrtcoverageHealthReports.maxMultCond"))) {
                pub.healthReports.setMaxMultCond(maxValue);
            }
            // end ugly hack
            return pub;
        }
    }
}
