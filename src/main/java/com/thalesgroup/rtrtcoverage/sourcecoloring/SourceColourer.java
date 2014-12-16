package com.thalesgroup.rtrtcoverage.sourcecoloring;

import org.apache.commons.lang.StringEscapeUtils;

import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.MultipleBranchCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.NodeCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.SingleBranchCoverage;

/**
 * Generate HTML code showing colored source code.
 *
 */
public class SourceColourer {
    /**
     * Generate HTML code showing colored source code.
     *
     * @param source
     *            the structured source file (read from FDC file)
     * @param fileCov
     *            the computed coverage data corresponding to the file
     * @return the HTML code
     */
    public final String colorSource(final SourceFile source,
            final FileCoverage fileCov) {
        StringBuilder buf = new StringBuilder();
        buf.append("<style media=\"screen\" type=\"text/css\">\n")
                .append(".rtrtcoverage-complete {\n")
                .append("  background-color: #ccffcc;")
                .append("}\n")
                .append(".rtrtcoverage-partial {\n")
                .append("  background-color: #ffffcc;")
                .append("}\n")
                .append(".rtrtcoverage-none {\n")
                .append("  background-color: #ffcccc;")
                .append("}\n")
                .append(".rtrtcoverage-hint {\n")
                .append("  border: 1px dashed grey;")
                .append("}\n")
                .append("</style>\n")
                .append("<tr><td><pre>")
                .append(1)
                .append("</pre></td><td><pre>");
        colorBlock(source.getCode(), fileCov, buf, 1);
        buf.append("</pre></td>").append(System.getProperty("line.separator"))
                .append("</tr>");
        return buf.toString();
    }

    /**
     * Generate the HTML code for the given source block coloration.
     *
     * @param block
     *            the source block to color
     * @param fileCov
     *            computed coverage data
     * @param buf
     *            buffer to which HTML code shall be added
     * @param currentLine
     *            the current line number
     * @return the new line number
     */
    private int colorBlock(final SourceBlock block, final FileCoverage fileCov,
            final StringBuilder buf, final int currentLine) {
        int line = currentLine;
        for (SourcePiece piece : block.getContent()) {
            if (piece instanceof SourceLineEnd) {
                line++;
                buf.append("</pre></td></tr>")
                        .append(System.getProperty("line.separator"))
                        .append("<tr><td><pre>").append(line)
                        .append("</pre></td><td><pre>");
            } else if (piece instanceof SourceLineChunk) {
                CoverageResult covResult = computePieceCoverage(piece, fileCov);
                buf.append("<span");
                if (!covResult.getCoverageStatus().getCssClass().isEmpty()
                        || !covResult.getDetails().isEmpty()) {
                    buf.append(" class=\"");
                    if (covResult.getCoverageStatus().getCssClass().isEmpty()) {
                        buf.append("rtrtcoverage-hint");
                    } else if (covResult.getDetails().isEmpty()) {
                        buf.append(covResult.getCoverageStatus().getCssClass());
                    } else {
                        buf.append(covResult.getCoverageStatus().getCssClass())
                            .append(" rtrtcoverage-hint");
                    }
                    buf.append("\"");
                }
                if (!covResult.getDetails().isEmpty()) {
                    buf.append(" title=\"")
                    .append(covResult.getDetails())
                    .append("\"");
                }
                buf.append(">")
                    .append(StringEscapeUtils.escapeHtml(((SourceLineChunk) piece).getChunk()))
                    .append("</span>");
            } else if (piece instanceof SourceBlock) {
                SourceBlock childBlock = (SourceBlock) piece;
                line = colorBlock(childBlock, fileCov, buf, line);
            }
        }
        return line;
    }

    /**
     * Coverage status enumeration: complete, partial, none or unknown.
     */
    enum CoverageStatus {
        /**
         * Complete coverage.
         */
        COMPLETE("rtrtcoverage-complete", "covered"),
        /**
         * Partial coverage.
         */
        PARTIAL("rtrtcoverage-partial", "partialy-covered"),
        /**
         * No coverage at all.
         */
        NONE("rtrtcoverage-none", "non-covered"),
        /**
         * Unkown coverage status.
         */
        UNKNOWN("", "");
        /**
         * CSS class corresponding to the coverage class.
         */
        private final String cssClass;
        /**
         * Hint to be printed.
         */
        private final String hint;

        /**
         * Private constructor.
         *
         * @param cssClass
         *            CSS class
         * @param hint
         *            Hint
         */
        private CoverageStatus(final String cssClass, final String hint) {
            this.cssClass = cssClass;
            this.hint = hint;
        }

        /**
         * Return the CSS class.
         *
         * @return the CSS class
         */
        public String getCssClass() {
            return cssClass;
        }

        /**
         * Return the hint.
         *
         * @return the hint
         */
        public String getHint() {
            return hint;
        }
    }

    /**
     * A coverage class: coverage status and hint.
     */
    class CoverageResult {
        /**
         * Coverage status.
         */
        private final CoverageStatus covStatus;
        /**
         * Coverage status details.
         */
        private final String details;

        /**
         * Constructor.
         *
         * @param covStatus
         *            coverage status
         * @param details
         *            coverage details message
         */
        public CoverageResult(final CoverageStatus covStatus,
                final String details) {
            this.covStatus = covStatus;
            this.details = details;
        }

        /**
         * Return coverage details.
         *
         * @return the coverage details
         */
        public String getDetails() {
            return details;
        }

        /**
         * Return the coverage status.
         *
         * @return the coverage status.
         */
        public CoverageStatus getCoverageStatus() {
            return covStatus;
        }
    }

    /**
     * Compute the coverage status and details of the given source code piece.
     *
     * @param piece
     *            the source code piece
     * @param fileCov
     *            the source file coverage results
     * @return the coverage status and details
     */
    private CoverageResult computePieceCoverage(final SourcePiece piece,
            final FileCoverage fileCov) {
        if (piece instanceof SourceLineChunk) {
            return computePieceCoverage(piece.getParent(), fileCov);
        } else {
            // piece is a SourceBlock
            SourceBlock block = (SourceBlock) piece;
            if (block.getType() != null) {
                if (block.getType().equals("BRANCH")) {
                    if (block.getAttributes().get("MARK") == null
                            || block.getAttributes().get("ID") == null) {
                        // a NO block: get the parent coverage
                        if (block.getParent() != null) {
                            return computePieceCoverage(block.getParent(), fileCov);
                        } else {
                            return new CoverageResult(CoverageStatus.UNKNOWN, "");
                        }
                    } else {
                        String markId =
                                block.getAttributes().get("MARK")
                                + block.getAttributes().get("ID");
                        NodeCoverage nodeCov = fileCov.getNodeByMarkId(markId);
                        if (nodeCov == null) {
                            // this should not happen: there is probably a problem
                            // in the FDC parser.
                            return new CoverageResult(CoverageStatus.UNKNOWN, "");
                        } else {
                            CoverageStatus covClass =
                                    getBranchCoverage(nodeCov.getGlobalBranchCoverage(markId));
                            return new CoverageResult(covClass, "");
                        }
                    }
                } else if (block.getType().equals("JUMP")
                        || block.getType().equals("LINK")
                        || block.getType().equals("DECISION")
                        || block.getType().equals("SHORTCUT")) {
                    // get the parent coverage
                    return computePieceCoverage(block.getParent(), fileCov);
                } else if (block.getType().equals("POPUP")
                        || block.getType().equals("ALT")) {
                    // compute the popup/alt second part coverage: generate a hint
                    // describing the coverage of the various branches composing
                    // this part.
                    CoverageResult result = computeComplexCoverageResult(block, fileCov);
                    if (CoverageStatus.UNKNOWN.equals(result.getCoverageStatus())) {
                        CoverageResult result2 = computePieceCoverage(block.getParent(), fileCov);
                        return new CoverageResult(result2.getCoverageStatus(), result.getDetails());
                    } else {
                        return result;
                    }
                }
            }
            return new CoverageResult(CoverageStatus.UNKNOWN, "");
        }
    }

    /**
     * Compute the coverage status and hint of the second, composite, part of a
     * source block (either a ALT or POPUP block).
     *
     * @param block
     *            the source block
     * @param fileCov
     *            the file coverage results
     * @return the coverage status and hint
     */
    private CoverageResult computeComplexCoverageResult(
            final SourceBlock block, final FileCoverage fileCov) {
        if (block.getSecondContent() != null) {
            int none = 0;
            int partial = 0;
            int complete = 0;
            StringBuilder hint = new StringBuilder();
            for (SourcePiece piece2 : block.getSecondContent()) {
                if (piece2 instanceof SourceLineChunk) {
                    hint.append(StringEscapeUtils.escapeHtml(((SourceLineChunk) piece2).getChunk()));
                } else if (piece2 instanceof SourceBlock) {
                    SourceBlock block2 = (SourceBlock) piece2;
                    hint.append(StringEscapeUtils.escapeHtml(block2.toRawCode()));
                    CoverageStatus covStatus = CoverageStatus.UNKNOWN;
                    if (block2.getType().equals("BRANCH")) {
                        CoverageResult covResult = computePieceCoverage(block2, fileCov);
                        covStatus = covResult.getCoverageStatus();
                        hint.append("[")
                            .append(covResult.getCoverageStatus().getHint())
                            .append("]");
                    } else if (block2.getType().equals("POPUP")) {
                        CoverageResult covResult = computeComplexCoverageResult(block2, fileCov);
                        covStatus = covResult.getCoverageStatus();
                        hint.append("[")
                            .append(covResult.getDetails())
                            .append("]");
                    }
                    switch (covStatus) {
                    case COMPLETE:
                        complete++;
                        break;
                    case NONE:
                        none++;
                        break;
                    case PARTIAL:
                        partial++;
                        break;
                    case UNKNOWN:
                    default:
                        break;
                    }
                }
            }
            if ((none == 0) && (partial == 0) && (complete == 0)) {
                return new CoverageResult(CoverageStatus.UNKNOWN, hint.toString());
            } else if ((none == 0) && (partial == 0)) {
                return new CoverageResult(CoverageStatus.COMPLETE, hint.toString());
            } else if ((partial == 0) && (complete == 0)) {
                return new CoverageResult(CoverageStatus.NONE, hint.toString());
            } else {
                return new CoverageResult(CoverageStatus.PARTIAL, hint.toString());
            }
        } else {
            return new CoverageResult(CoverageStatus.UNKNOWN, "");
        }
    }

    /**
     * Return the consolidated coverage status from a branch coverage data.
     *
     * @param branchCov
     *            the branch coverage data
     * @return the consolidated coverage status
     */
    private CoverageStatus getBranchCoverage(final IBranchCoverage branchCov) {
        if (branchCov instanceof MultipleBranchCoverage) {
            boolean allCovered = true;
            boolean noneCovered = true;
            for (SingleBranchCoverage subBranch :
                ((MultipleBranchCoverage) branchCov).getSubBranches()) {
                if (subBranch.isCovered()) {
                    noneCovered = false;
                } else {
                    allCovered = false;
                }
            }
            if (allCovered) {
                return CoverageStatus.COMPLETE;
            } else if (noneCovered) {
                return CoverageStatus.NONE;
            } else {
                return CoverageStatus.PARTIAL;
            }
        } else {
            if (branchCov.isCovered()) {
                return CoverageStatus.COMPLETE;
            } else {
                return CoverageStatus.NONE;
            }
        }
    }
}
