package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thalesgroup.rtrtcoverage.fdcparser.FdcParser;
import com.thalesgroup.rtrtcoverage.filesmapping.FileIdentitiesImport;
import com.thalesgroup.rtrtcoverage.filesmapping.FilesMapping;
import com.thalesgroup.rtrtcoverage.tioreader.CoverageFile;
import com.thalesgroup.rtrtcoverage.tioreader.CoverageLine;
import com.thalesgroup.rtrtcoverage.tioreader.TioReader;

/**
 * Global coverage by files. Could be an AggregatedReport if adding TestReport
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public final class SourceFileReport extends
AggregatedReport<CoverageReport, SourceFileReport, TestReport> {

    /**
     * Where to find the fdc files.
     */
    private String includesFdc;

    /**
     * Where to find the tio files.
     */
    private String includesTio;

    /**
     * The tio file where to find the coverage.
     */
    private String tioName;

    /**
     * Determine where to find the .fdc files.
     *
     * @param sIncludes
     *            the path
     */
    public void setIncludesFdc(final String sIncludes) {
        this.includesFdc = sIncludes;
    }

    /**
     * Determine where to find the .tio files.
     *
     * @param sIncludes
     *            the path
     */
    public void setIncludesTio(final String sIncludes) {
        this.includesTio = sIncludes;
    }

    /**
     * Determine the name of the tio file.
     *
     * @param sTioName
     *            name of the tio file
     */
    public void setTioName(final String sTioName) {
        this.tioName = sTioName;
    }

    /**
     * Give the contents of the source file. Coverage is painted on the lines.
     *
     * @return the painted source code.
     */
    public String getPaintedSourceFileContent() {

        // Input and output files.
        final StringBuilder buf = new StringBuilder();

        try {
            // Determine where the .tio files are located.
            final List<FilePath> paths = new ArrayList<FilePath>();
            final FilesMapping mapping =
                    (new FileIdentitiesImport()).importXml(new File(getBuild().getRootDir() + "/file_identities.xml"));
            for (FilePath path : mapping.get(this.getName()).getAssociedTios()) {
                paths.add(path);
            }

            // Opening the source code stream for source code.
            final FdcParser fdcParser = new FdcParser(getSourcePath());

            // Perform a test on the file if several .tio files
            // the tio name contains the beginning of the name of the file.
            for (final FilePath f : paths) {

                if (f.exists()) {
                    final TioReader tioReader = new TioReader(f.read());
                    final CoverageFile coverageFile = tioReader
                            .readCoverageForFile();

                    int line = 0;
                    int hits = 0;
                    int methodMark = -1;
                    int mark = 0;
                    // to detect partial condition missed to failed conditions
                    String content = "";
                    String error = "";

                    while (fdcParser.hasNext()) {
                        fdcParser.next();
                        hits = 0;
                        mark = 0;
                        error = "";
                        CoverageLine[] coverage = fdcParser.getCoverageTypes();
                        for (int c = 0; c < fdcParser.getNumberOfCoverageType(); ++c) {
                            hits += coverageFile.getHit(coverage[c]);
                            if (coverageFile.getCoverage()
                                    .contains(coverage[c])) {
                                mark++;
                            } else {
                                if (coverage[c].isConditional()) {
                                    error += ",  missed condition: "
                                            + coverage[c].getValue();
                                }
                            }
                        }

                        // head
                        boolean isHeadOk = false;
                        coverage = fdcParser.getHeadCoverageTypes();
                        for (int c = 0; c < coverage.length; ++c) {
                            if (coverageFile.getCoverage()
                                    .contains(coverage[c])) {
                                isHeadOk |= true;
                            }
                        }

                        if (fdcParser.isFirstLineOfMethod()) {
                            methodMark = mark;
                        }

                        if (fdcParser.mustBePainted()) {
                            // New branch with same value as previous one.
                            // Coverage is not done!
                            if (methodMark == 0) {
                                buf.append("<tr class=\"coverNone\">");
                            } else if (mark == fdcParser
                                    .getNumberOfCoverageType()) {
                                buf.append("<tr class=\"coverFull\">");
                            } else if (isHeadOk) {
                                final float perCent = 100.f;
                                final int coveragePercent = (int) (mark
                                        * perCent / fdcParser
                                        .getNumberOfCoverageType());
                                buf.append("<tr class=\"coverPartial\" title=\"Line "
                                        + line
                                        + ": Conditional coverage "
                                        + coveragePercent
                                        + "% ("
                                        + mark
                                        + "/"
                                        + fdcParser.getNumberOfCoverageType()
                                        + ")" + error + "\">");
                            } else {
                                buf.append("<tr class=\"coverNone\">");
                            }
                            buf.append("<td class=\"line\">" + line + "</td>");
                            // buf.append("<td class=\"hits\">" + hits +
                            // "</td>"); //TODO : fix erratic hit values

                        } else {
                            buf.append("<tr class=\"noCover\">");
                            buf.append("<td class=\"line\">" + line + "</td>");
                            // buf.append("<td class=\"hits\"/>"); //TODO : fix
                            // erratic hit values
                        }

                        content = fdcParser.getSourceCode();
                        buf.append("<td>"
                                + content
                                .replaceAll("\\&", "&amp;")
                                .replaceAll("\\<", "&lt;")
                                .replaceAll("\\>", "&gt;")
                                .replaceAll("\\\\[nr]", "")
                                .replaceAll(" ", "&nbsp;")
                                .replaceAll("\t",
                                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
                                        .replaceAll("\n", "<br>") + "</td>");
                        buf.append("</tr>");
                        line++;
                    }
                } else {
                    System.out.println("[RTRTCoverage] Fichier *.tio manquant : " + f.getRemote());
                }
            }

        } catch (final Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return buf.toString();

    }

    /**
     * Determine if the file is a .tio file.
     *
     * @param f
     *            the file to test
     * @param name
     *            the name of the file to compare
     * @return <code>true</code> if it is a .tio file
     */
    private boolean isTioFile(final FilePath f, final String name) {
        final String nameOfTioFile = f.getName().toUpperCase();
        return nameOfTioFile.equals(name.toUpperCase());
    }
}
