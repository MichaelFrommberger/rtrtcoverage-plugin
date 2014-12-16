package com.thalesgroup.rtrtcoverage;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

import com.thalesgroup.rtrtcoverage.fdcreader.BranchType;
import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentificationException;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentitiesExport;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentity;
import com.thalesgroup.rtrtcoverage.filesmapping.FilesMapping;
import com.thalesgroup.rtrtcoverage.serializablerates.FileRate;
import com.thalesgroup.rtrtcoverage.serializablerates.GlobalRate;
import com.thalesgroup.rtrtcoverage.serializablerates.NodeRate;
import com.thalesgroup.rtrtcoverage.serializablerates.TestRate;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioException;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageMergeException;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageRate;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.GlobalCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.NodeCoverage;

/**
 * {@link Publisher} that captures RTRT coverage reports.
 *
 * @author Sebastien Barbier
 *
 */
public class RTRTPublisher extends Recorder {

    /**
     * the ant-style pattern input in web ui where to find instrumented files
     * Must be public to be seen by config.jelly
     */
    public String augPattern;

    /**
     * true if TUSAR export checkbox checked in web ui. Must be public to be
     * seen by config.jelly
     */
    public boolean tusarExportInBuildDirRequired = false;

    /**
     * Relative (from build) path of TUSAR export folder. Must be public to be
     * seen by config.jelly
     */
    public String tusarExportPathFromBuildDir;

    /**
     * true if TUSAR export checkbox checked in web ui. Must be public to be
     * seen by config.jelly
     */
    public boolean tusarExportInWorkspaceDirRequired = false;

    /**
     * Relative (from build) path of TUSAR export folder. Must be public to be
     * seen by config.jelly
     */
    public String tusarExportPathFromWorkspaceDir;

    /**
     * Relative ant-style path to the RTRT fdc files inside the workspace. Must
     * be public to be seen by config.jelly
     */
    public String includesFdc;

    /**
     * Relative ant-style path to the RTRT tio files inside the workspace. Must
     * be public to be seen by config.jelly
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

        FilePath[] rtrtTestCoverageFiles = build.getWorkspace().list(
                includesTio);
        if (rtrtTestCoverageFiles.length == 0) {
            logger.println("[RTRTCoverage] [WARNING]: no rtrt test coverage file found.");
        }

        logger.println("[RTRTCoverage]: Storing *.tio files...");
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

        logger.println("[RTRTCoverage]: Storing *.fdc files...");
        final FilePath rtrtfdcfolder = new FilePath(getRTRTCodeSource(build));
        saveCodeSourceFiles(rtrtfdcfolder, fdcFiles);
        logger.println("[RTRTCoverage]: stored " + fdcFiles.length
                + " code sources files (.fdc) in the build folder: "
                + rtrtfdcfolder);

        // Extract tio data
        logger.println("[RTRTCoverage]: Extracting TIO data...");
        List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        for (FilePath tioFile : rtrtTestCoverageFolder.list("*.TIO")) {
            TioReader2 tioReader = new TioReader2(tioFile.read());
            try {
                TestSuiteTrace tst = tioReader.readTio();
                tst.setName(tioFile.getName());
                traces.add(tst);
            } catch (TioException e) {
                logger.println("[RTRTCoverage] [WARNING]: TIO file "
                        + tioFile.getName() + " is ignored.");
                logger.println("[RTRTCoverage] [WARNING]: Reason: "
                        + e.getMessage());
                build.setResult(hudson.model.Result.UNSTABLE);
            }
        }
        logger.println("[RTRTCoverage]: TIO data extraction done.");

        // Build files mapping
        logger.println("[RTRTCoverage]: Building files mapping...");
        FilesMapping mapping = new FilesMapping();
        try {
            mapping.build(traces, build.getWorkspace(), augPattern,
                    rtrtTestCoverageFolder);
        } catch (FileIdentificationException e1) {
            build.setResult(hudson.model.Result.FAILURE);
            logger.println(e1.getMessage());
            return false;
        } catch (TioException e1) {
            logger.println(e1.getMessage());
            build.setResult(hudson.model.Result.FAILURE);
            return false;
        }
        logger.println("[RTRTCoverage]: Files mapping done");

        // Save file IDs
        if (build.getWorkspace().list(augPattern).length != 0) {
            logger.println("[RTRTCoverage]: Saving files ID correspondances...");
            File fileIdOutput = new File(build.getRootDir()
                    + System.getProperty("file.separator")
                    + "file_identities.xml");
            (new FileIdentitiesExport()).export(mapping, fileIdOutput);
            logger.println("[RTRTCoverage]: Files ID correspondances saved at "
                    + fileIdOutput.getPath());
        } else {
            logger.println("[RTRTCoverage] [WARNING]: Cannot find instrumented files: "
                    + "unable to find and save files ID correspondances");
            build.setResult(hudson.model.Result.UNSTABLE);
        }

        // Extract fdc data
        logger.println("[RTRTCoverage]: Extracting FDC data...");
        List<FileCoverageDefinition> fileDefs = new ArrayList<FileCoverageDefinition>();
        FdcReader fdcReader = new FdcReader();
        for (FilePath fdcFile : rtrtfdcfolder.list("*.FDC")) {
            FileCoverageDefinition fileDef = fdcReader.read(fdcFile);
            FileIdentity id = mapping.get(fileDef.getSourceName());
            if (id == null) {
                logger.println("[RTRTCoverage] [ERROR]: Cannot get id for source: "
                        + fileDef.getSourceName());
                build.setResult(hudson.model.Result.FAILURE);
                return false;
            } else {
                fileDef.setKey(id.getKey());
                fileDef.setCrc(id.getCrc());
                fileDefs.add(fileDef);
            }
        }
        logger.println("[RTRTCoverage]: FDC data extraction done.");

        // Merge data
        logger.println("[RTRTCoverage]: Merging data...");
        List<FileCoverage> fileCovs = null;
        CoverageTraceMerger merger = new CoverageTraceMerger();
        try {
            fileCovs = merger.merge(fileDefs, traces);
        } catch (CoverageMergeException e1) {
            logger.println("[RTRTCoverage] [ERROR]: " + e1.getMessage());
        }
        GlobalCoverage globalCov = new GlobalCoverage(fileCovs);
        logger.println("[RTRTCoverage]: Data merge done.");

        // Build rates data
        logger.println("[RTRTCoverage]: Building ratios...");
        GlobalRate globalRate = buildRatesData(globalCov);
        logger.println("[RTRTCoverage]: Ratios build done.");

        // Data serialization
        logger.println("[RTRTCoverage]: Saving ratios data...");
        FileOutputStream fos = new FileOutputStream(build.getRootDir()
                + System.getProperty("file.separator") + "globalrate.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            oos.writeObject(globalRate);
            oos.flush();
        } finally {
            try {
                oos.close();
            } finally {
                fos.close();
            }
        }
        logger.println("[RTRTCoverage]: Ratios data saved.");

        RTRTBuildAction action = null;
        action = RTRTBuildAction.load(build, rule, logger, globalRate,
                healthReports);

        logger.println("[RTRTCoverage]: "
                + action.getBuildHealth().getDescription());

        build.getActions().add(action);

        if (action.getResult().isFailed()) {
            logger.println("[RTRTCoverage] [WARNING]: code coverage enforcement failed. Setting Build to unstable.");
            build.setResult(hudson.model.Result.UNSTABLE);
        }

        if (tusarExportInBuildDirRequired) {
            logger.println("[RTRTCoverage]: TUSAR export in build directory in progress...");
            if (build.getWorkspace().list(augPattern).length != 0) {
                FilePath outputFile = null;
                if (tusarExportPathFromBuildDir != null
                        && tusarExportPathFromBuildDir.equals("")) {
                    outputFile = new FilePath(new File(build.getRootDir()
                            + System.getProperty("file.separator")
                            + "generatedDTKITFiles"
                            + System.getProperty("file.separator") + "COVERAGE"
                            + System.getProperty("file.separator")
                            + "tusar_export.xml"));
                } else {
                    outputFile = new FilePath(new File(build.getRootDir()
                            + System.getProperty("file.separator")
                            + tusarExportPathFromBuildDir));
                }

                RtrtCoverage2Tusar.export(globalCov, outputFile,
                        build.getRootDir());

                logger.println("[RTRTCoverage]: stored TUSAR (.xml) file at : "
                        + outputFile.getRemote());
            } else {
                logger.println("[RTRTCoverage]: TUSAR export error: no instrumented file found!");
                build.setResult(hudson.model.Result.UNSTABLE);
            }
        }
        if (tusarExportInWorkspaceDirRequired) {
            logger.println("[RTRTCoverage]: TUSAR export in workspace directory in progress...");
            if (build.getWorkspace().list(augPattern).length != 0) {
                FilePath outputFile = null;
                if (tusarExportPathFromWorkspaceDir != null
                        && tusarExportPathFromWorkspaceDir.equals("")) {
                    // By default, export Tusar file to
                    // generatedDTKITFiles/COVERAGE
                    // directory, so that Tusar notifier plugin should find it.
                    outputFile = new FilePath(build.getWorkspace(),
                            "generatedDTKITFiles"
                                    + System.getProperty("file.separator")
                                    + "COVERAGE"
                                    + System.getProperty("file.separator")
                                    + "tusar_export.xml");
                } else {
                    outputFile = new FilePath(build.getWorkspace(),
                            System.getProperty("file.separator")
                                    + tusarExportPathFromWorkspaceDir);
                }

                RtrtCoverage2Tusar.export(globalCov, outputFile,
                        build.getRootDir());

                logger.println("[RTRTCoverage]: stored TUSAR (.xml) file at : "
                        + outputFile.getRemote());
            } else {
                logger.println("[RTRTCoverage]: TUSAR export error: no instrumented file found!");
                build.setResult(hudson.model.Result.UNSTABLE);
            }
        }
        return true;
    }

    /**
     * Build a serializable global rate object.
     *
     * @param globalCov
     *            a global coverage object.
     * @return a global rate object.
     */
    public static GlobalRate buildRatesData(final GlobalCoverage globalCov) {
        GlobalRate globalRate = new GlobalRate();
        for (FileCoverage fileCov : globalCov.getSortedFileCoverages().values()) {
            FileRate fileRate = new FileRate();
            fileRate.setSourceFileName(fileCov.getSourceFileName());
            fileRate.setFdcPath(fileCov.getFdcPath());
            globalRate.getFileRates().add(fileRate);
            for (NodeCoverage nodeCov : fileCov.getNodes()) {
                NodeRate nodeRate = new NodeRate();
                nodeRate.setNodeName(nodeCov.getNodeName());
                fileRate.getNodeRates().add(nodeRate);
                for (BranchType type : BranchType.values()) {
                    CoverageRate rate = nodeCov.getGlobalRate(type);
                    nodeRate.get(type).addRatio(rate.getCoveredNumber(),
                            rate.getTotal());
                }
            }
            // add the tests covering this file
            for (String testName : fileCov.getTestNames()) {
                TestRate testRate = new TestRate();
                fileRate.getTestRates().add(testRate);
                testRate.setTestName(testName);
                testRate.setSourceFileName(fileCov.getSourceFileName());
                testRate.setFdcPath(fileCov.getFdcPath());
                for (NodeCoverage node : fileCov.getNodesForTest(testName)
                        .values()) {
                    NodeRate nodeRate = new NodeRate();
                    nodeRate.setNodeName(node.getNodeName());
                    testRate.getNodeRates().add(nodeRate);
                    for (BranchType type : BranchType.values()) {
                        CoverageRate rate = node.getTestRate(testName, type);
                        nodeRate.get(type).addRatio(rate.getCoveredNumber(),
                                rate.getTotal());
                    }
                }
                for (NodeRate nodeRate : testRate.getNodeRates()) {
                    for (BranchType type : BranchType.values()) {
                        testRate.get(type).addRatio(nodeRate.get(type));
                    }
                }
            }
            for (NodeRate nodeRate : fileRate.getNodeRates()) {
                for (BranchType type : BranchType.values()) {
                    fileRate.get(type).addRatio(nodeRate.get(type));
                }
            }
        }
        for (FileRate fileRate : globalRate.getFileRates()) {
            for (BranchType type : BranchType.values()) {
                globalRate.get(type).addRatio(fileRate.get(type));
            }
        }
        return globalRate;
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
     * Gets the directory to store report files (.tio files).
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
