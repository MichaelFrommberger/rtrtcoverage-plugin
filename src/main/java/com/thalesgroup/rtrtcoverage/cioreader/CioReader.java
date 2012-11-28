package com.thalesgroup.rtrtcoverage.cioreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Reader of a .cio file. Populate data for having coverage hierarchy.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CioReader {

    /**
     * The .cio file. Can be null;
     */
    private final File file;
    /**
     * The name of the cio file.
     */
    private final String name;
    /**
     * The input stream. Can be null.
     */
    private InputStream fis;

    /**
     * Default Constructor with file argument.
     *
     * @param cioFile
     *            the .cio file
     * @throws CioException
     *             if bad input
     */
    public CioReader(final File cioFile) throws CioException {
        file = cioFile;
        name = file.getName().toUpperCase();
        fis = null;
    }

    /**
     * Default constructor with input stream argument.
     *
     * @param is
     *            the .cio input stream
     * @param filename
     *            the name of the .cio file
     * @throws CioException
     *             if bad input
     */
    public CioReader(final InputStream is, final String filename)
            throws CioException {
        file = null;
        name = filename.toUpperCase();
        fis = is;
    }

    /**
     * Read the .cio file and build a hierarchy with all the data.
     *
     * @param isInFile
     *            indicates which attributes are written into the file
     * @param haveInformation
     *            indicates wiche attributes must be initialized if no hit
     * @return the data into the .cio file for coverage reports display.
     * @throws CioException
     *             if bad .cio file
     */
    public final CoverageReportElement populate(final boolean[] isInFile,
            final boolean[] haveInformation) throws CioException {

        if (fis == null) {
            try {
                fis = new FileInputStream(file);
            } catch (final FileNotFoundException e1) {
                throw new CioException(e1.getMessage());
            }
        }

        final InputStreamReader ipsr = new InputStreamReader(fis);
        final BufferedReader br = new BufferedReader(ipsr);

        final CoverageReportElement globalCoverage = new CoverageReportElement();
        String line;

        try {

            line = br.readLine();

            while (line != null) {
                line = line.trim();
                // Global coverage for test
                if (line.startsWith("T")) {
                    // name of the test
                    final String nameCurrentTest = line;
                    line = br.readLine();
                    if (line == null) {
                        throw new CioException("Bad format of the .cio file: "
                                + file.getAbsolutePath());
                    }
                    line = line.trim();
                    // skip comments
                    while (line.startsWith("(")) {
                        line = br.readLine();
                        if (line == null) {
                            throw new CioException(
                                    "Bad format of the .cio file: "
                                            + file.getAbsolutePath());
                        }
                        line = line.trim();
                    }

                    while (line.startsWith("File")) {
                        final CoverageElement currentCoverage = new CoverageElement();
                        currentCoverage.setNameOfAssociatedTioFile(name);
                        line = line.replaceFirst("File", "");
                        line = line.trim();
                        currentCoverage.init(br, line, isInFile, false);
                        globalCoverage.addCoverageTest(nameCurrentTest,
                                currentCoverage);
                        line = br.readLine();
                        if (line == null) {
                            throw new CioException(
                                    "Bad format of the .cio file: "
                                            + file.getAbsolutePath());
                        }
                        line = line.trim();
                    }

                } else if (line.equals("G")) {
                    // Global coverage
                    line = br.readLine();
                    if (line == null) {
                        throw new CioException("Bad format of the .cio file: "
                                + file.getAbsolutePath());
                    }
                    line = line.trim();

                    while (line.startsWith("File")) {

                        CoverageElement currentCoverage = new CoverageElement();
                        currentCoverage.setNameOfAssociatedTioFile(name);
                        line = line.replaceFirst("File", "");
                        line = line.trim();
                        boolean again = currentCoverage.init(br, line,
                                isInFile, true);
                        while (again) {
                            line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            line = line.trim();
                            if (line.startsWith("File")) {
                                // Initialiser les valeurs car aucun hit pour le
                                // pr�c�dent fichier
                                currentCoverage.init(haveInformation);
                                globalCoverage
                                .addGlobalCoverage(currentCoverage);
                                currentCoverage = new CoverageElement();
                                currentCoverage
                                .setNameOfAssociatedTioFile(name);
                                line = line.replaceFirst("File", "");
                                line = line.trim();
                                again = currentCoverage.init(br, line,
                                        isInFile, true);
                                continue;
                            }
                            again = currentCoverage.init(br, isInFile);
                        }
                        globalCoverage.addGlobalCoverage(currentCoverage);
                        line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        line = line.trim();
                    }

                } else {
                    line = br.readLine();
                }
            }

        } catch (final IOException e) {
            throw new CioException("Cannot read the file : " + e.toString());
        }

        try {
            br.close();
            ipsr.close();
            fis.close();
        } catch (final IOException e) {
            throw new CioException(e.getMessage());
        }

        return globalCoverage;

    }

    /**
     * Read the .cio file and extract only the global coverage report.
     *
     * @param isInFile
     *            indicates which attributes are written into the file
     * @param haveInformation
     *            indicates wiche attributes must be initialized if no hit
     * @return the global coverage into the .cio file.
     * @throws CioException
     *             if bad .cio file
     */
    public final CoverageElement populateGlobalCoverage(
            final boolean[] isInFile, final boolean[] haveInformation)
                    throws CioException {

        if (fis == null) {
            try {
                fis = new FileInputStream(file);
            } catch (final FileNotFoundException e1) {
                throw new CioException(e1.getMessage());
            }
        }

        final InputStreamReader ipsr = new InputStreamReader(fis);
        final BufferedReader br = new BufferedReader(ipsr);

        final CoverageReportElement globalCoverage = new CoverageReportElement();
        String line;

        try {

            line = br.readLine();

            while (line != null) {
                line = line.trim();
                // Global coverage
                if (line.equals("G")) {
                    line = br.readLine();
                    if (line == null) {
                        throw new CioException("Bad format of the .cio file: "
                                + file.getAbsolutePath());
                    }
                    line = line.trim();
                    line = line.replaceAll("\t", " .. "); // keep compatibility
                    // between v2002 & v7

                    while (line.startsWith("File")) {

                        CoverageElement currentCoverage = new CoverageElement();
                        currentCoverage.setNameOfAssociatedTioFile(name);
                        String[] s = new String[2];
                        s = line.split(" ");
                        currentCoverage.setNameFile(s[1]);
                        boolean again = currentCoverage.init(br, s[1],
                                isInFile, true);
                        while (again) {
                            line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            line = line.trim();
                            if (line.startsWith("File")) {
                                // Initialiser les valeurs car aucun hit pour le
                                // pr�c�dent fichier
                                currentCoverage.init(haveInformation);
                                globalCoverage
                                .addGlobalCoverage(currentCoverage);
                                currentCoverage = new CoverageElement();
                                currentCoverage
                                .setNameOfAssociatedTioFile(name);
                                continue;
                            }
                            again = currentCoverage.init(br, isInFile);
                        }
                        globalCoverage.addGlobalCoverage(currentCoverage);
                        line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        line = line.trim();
                    }

                } else {
                    line = br.readLine();
                }
            }

        } catch (final IOException e) {
            throw new CioException("Cannot read the file : " + e.toString());
        }

        return globalCoverage.getGlobalCoverage();

    }

}
