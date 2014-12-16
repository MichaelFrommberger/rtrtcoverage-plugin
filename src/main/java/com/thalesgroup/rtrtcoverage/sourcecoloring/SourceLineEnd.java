package com.thalesgroup.rtrtcoverage.sourcecoloring;

/**
 * An end of line source piece.
 */
public class SourceLineEnd extends SourcePiece {

    /**
     * Constructor.
     *
     * @param parentBlock
     *            the parent block
     */
    public SourceLineEnd(final SourceBlock parentBlock) {
        super(parentBlock);
    }

    @Override
    public final String toRawCode() {
        return "\n";
    }
}
