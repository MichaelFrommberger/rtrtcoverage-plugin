package com.thalesgroup.rtrtcoverage;

/**
 * Coverage Report by test.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class TestReport extends AbstractReport<SourceFileReport, TestReport> {

    /**
     * Give the contents of the source file. Coverage is painted on the lines.
     *
     * @return the painted source code.
     */
    public final String getPaintedSourceFileContent() {
        // Input and output files.
        final StringBuilder buf = new StringBuilder();
        // TODO
        return buf.toString();
    }
}
