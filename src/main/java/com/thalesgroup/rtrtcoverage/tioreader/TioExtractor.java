package com.thalesgroup.rtrtcoverage.tioreader;

import hudson.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Find all the .tio files into the directory.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class TioExtractor {

    /**
     * Build the mapping from all the files *.tio.
     *
     * @param workspace
     *            where to find the sources *.c
     * @return list of all the .tio filePaths
     *             if some errors during reading
     * @throws InterruptedException
     *             if some errors during reading
     * @throws IOException
     *             if some errors during reading
     */
    public final FilePath[] findFilePath(final FilePath workspace)
            throws IOException, InterruptedException {

        FilePath[] paths = locateAugmentedSource(workspace, "**/*.tio");
        // Unix compatibility
        if (paths == null || paths.length == 0) {
            paths = locateAugmentedSource(workspace, "**/*.TIO");
        }

        return paths;
    }

    /**
     * Look for .tio files based in the configured parameter includes.
     * 'includes' is - an Ant-style pattern - a list of files and folders
     * separated by the characters ;:,
     *
     * @param workspace
     *            the global path
     * @param includes
     *            where to search
     * @return the path of all the .tio files
     * @throws IOException
     *             if error during reading
     * @throws InterruptedException
     *             if error during reading
     */
    protected static FilePath[] locateAugmentedSource(final FilePath workspace,
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
                    files.addAll(Arrays.asList(src.list("**/*.TIO")));
                    // Unix compatibility
                    files.addAll(Arrays.asList(src.list("**/*.tio")));
                } else {
                    files.add(src);
                }
            }
        }
        return files.toArray(new FilePath[files.size()]);
    }

}
