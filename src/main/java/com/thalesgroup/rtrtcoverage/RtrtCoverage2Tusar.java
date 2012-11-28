package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.GlobalCoverage;
import com.thalesgroup.rtrtcoverage.tusarexport.TusarExport;

/**
 * Class allowing to export a tusar (*.xml) file.
 * from global coverage object
 *
 * @author Bastien Reboulet
 *
 */
abstract class RtrtCoverage2Tusar {

    /**
     * Main function allowing to export the tusar file.
     * with all the inputs set in the default constructor
     *
     * @param globalCov the global caoverage.
     * @param outputFile the output file we want write data in.
     * @param buildDir the current build directory
     * @throws IOException an IOException
     * @throws InterruptedException an InterruptedException
     */
    public static final void export(final GlobalCoverage globalCov,
            final FilePath outputFile,
            final File buildDir)
                    throws IOException, InterruptedException {
        if (!(outputFile.getParent().exists())) {
            outputFile.getParent().mkdirs();
        }
        final TusarExport tusarExport = new TusarExport();
        tusarExport.export(tusarExport.convert(
                new ArrayList<FileCoverage>(globalCov.getSortedFileCoverages().values())),
                outputFile,
                buildDir);
    }

}
