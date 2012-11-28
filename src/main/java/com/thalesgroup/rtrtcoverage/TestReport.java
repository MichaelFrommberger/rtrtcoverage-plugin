package com.thalesgroup.rtrtcoverage;

import hudson.FilePath;

import java.io.File;
import java.util.Stack;

import com.thalesgroup.rtrtcoverage.fdcparser.FdcParser;
import com.thalesgroup.rtrtcoverage.tioreader.CoverageLine;
import com.thalesgroup.rtrtcoverage.tioreader.CoverageTest;
import com.thalesgroup.rtrtcoverage.tioreader.TioException;
import com.thalesgroup.rtrtcoverage.tioreader.TioExtractor;
import com.thalesgroup.rtrtcoverage.tioreader.TioReader;

/**
 * Coverage Report by test.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class TestReport extends AbstractReport<SourceFileReport, TestReport> {

    /**
     * The lexical order of the test. This number is used to identify the test
     * into .tio file
     */
    private String classification;

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
    public final void setIncludesFdc(final String sIncludes) {
        this.includesFdc = sIncludes;
    }

    /**
     * Determine where to find the .tio files.
     *
     * @param sIncludes
     *            the path
     */
    public final void setIncludesTio(final String sIncludes) {
        this.includesTio = sIncludes;
    }

    /**
     * Determine the name of the tio file.
     *
     * @param sTioName
     *            name of the tio file
     */
    public final void setTioName(final String sTioName) {
        this.tioName = sTioName;
    }

    /**
     * Set the classification of the test.
     *
     * @param sClassification
     *            the order of the test
     */
    public final void setClassification(final String sClassification) {
        this.classification = sClassification;
    }

    /**
     * Give the contents of the source file. Coverage is painted on the lines.
     *
     * @return the painted source code.
     */
    public final String getPaintedSourceFileContent() {

        // Input and output files.
        final StringBuilder buf = new StringBuilder();

        try {

            // Determine where the .tio files are located.
            final TioExtractor tioExtractor = new TioExtractor();
            final FilePath[] paths = tioExtractor.findFilePath(new FilePath(
                    new File(includesTio)));

            // Opening the source code stream for source code.
            final FdcParser fdcParser = new FdcParser(getSourcePath());

            // Wrong code if several *.tio files.
            boolean noTioFile = true;
            for (final FilePath f : paths) {
                if (isTioFile(f, tioName)) {
                    noTioFile = false;
                    final TioReader tioReader = new TioReader(f.read());
                    final CoverageTest coverageTest = tioReader
                            .readCoverageForFileIntoTest();
                    int line = 0;
                    int hits = 0;
                    int methodHit = -1;
                    // to detect partial condition missed to failed conditions
                    int previousHits = 0;
                    // for decisional block: initialization
                    // The current max value of the decisional block. If current
                    // mark is superior, conditional coverage can be activated
                    final Stack<Integer> decisionalMark = new Stack<Integer>();
                    // To mark when complete coverage is detected: conditional
                    // coverage cannot be activated after.
                    final Stack<Boolean> localMaxMark = new Stack<Boolean>();
                    decisionalMark.push(1);
                    localMaxMark.push(false); // if false, no complete coverage
                    // already occurred into
                    // decisional block
                    String content = "";
                    String error = "";
                    while (fdcParser.hasNext()) {
                        fdcParser.next();

                        // end of decisional block ?
                        int removeDecision = fdcParser.hasRemoveDecision();
                        while (removeDecision != 0) {
                            decisionalMark.pop();
                            localMaxMark.pop();
                            removeDecision--;
                        }

                        previousHits = hits;
                        hits = 0;
                        CoverageLine[] coverage = fdcParser.getCoverageTypes();
                        error = "";
                        for (int c = 0; c < fdcParser.getNumberOfCoverageType(); ++c) {

                            if (coverageTest.getFileCoverage().contains(
                                    coverage[c])) {

                                hits++;
                            } else {
                                if (coverage[c].isConditional()) {
                                    error += ", missed condition: "
                                            + coverage[c].getValue();
                                }
                            }
                        }

                        // head
                        boolean isHeadOk = false;
                        coverage = fdcParser.getHeadCoverageTypes();
                        for (int c = 0; c < coverage.length; ++c) {
                            if (coverageTest.getFileCoverage().contains(
                                    coverage[c])) {
                                isHeadOk |= true;
                            }
                        }

                        if (fdcParser.isFirstLineOfMethod()) {
                            methodHit = hits;
                        }

                        if (fdcParser.mustBePainted()) {

                            // New branch with same value as previous one.
                            // Coverage is not done!
                            if (previousHits == hits
                                    && fdcParser.hasNewBranch()
                                    && decisionalMark.size() > 1) {
                                decisionalMark.pop();
                                decisionalMark.push(hits);
                            }
                            if (methodHit == 0) {
                                buf.append("<tr class=\"coverNone\">");
                            } else if (hits == fdcParser
                                    .getNumberOfCoverageType()) {
                                buf.append("<tr class=\"coverFull\">");
                                if (fdcParser.hasDecision()) {
                                    // as full coverage, the mark is not
                                    // permissive.
                                    localMaxMark.pop();
                                    localMaxMark.push(hits > decisionalMark
                                            .peek());
                                }
                            } else if (isHeadOk) {
                                final float perCent = 100.f;
                                final int coveragePercent = (int) (hits
                                        * perCent / fdcParser
                                        .getNumberOfCoverageType());
                                buf.append("<tr class=\"coverPartial\" title=\"Line "
                                        + line
                                        + ": Conditional coverage "
                                        + coveragePercent
                                        + "% ("
                                        + hits
                                        + "/"
                                        + fdcParser.getNumberOfCoverageType()
                                        + ")" + error + "\">");
                            } else {
                                buf.append("<tr class=\"coverNone\">");
                            }
                            buf.append("<td class=\"line\">" + line + "</td>");
                            buf.append("<td class=\"hits\">" + hits + "</td>");

                        } else {
                            buf.append("<tr class=\"noCover\">");
                            buf.append("<td class=\"line\">" + line + "</td>");
                            buf.append("<td class=\"hits\"/>");
                        }

                        // Update decisional block.
                        if (fdcParser.hasNewDecision()) {
                            decisionalMark.push(hits);
                            localMaxMark.push(false);
                        }
                        content = fdcParser.getSourceCode();
                        buf.append("<td class=\"code\">"
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
                } // end good .tio
            } // end for each tio
            if (noTioFile) {
                throw new TioException(tioName + " is not found.");
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
