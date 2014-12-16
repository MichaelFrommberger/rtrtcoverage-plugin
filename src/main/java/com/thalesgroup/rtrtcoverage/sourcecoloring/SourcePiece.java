package com.thalesgroup.rtrtcoverage.sourcecoloring;

/**
 * A piece of source code.
 */
public abstract class SourcePiece {
    /**
     * the piece parent block.
     */
    private SourceBlock parent;

    /**
     * Constructor.
     *
     * @param parentBlock
     *            the parent block
     */
    public SourcePiece(final SourceBlock parentBlock) {
        parent = parentBlock;
    }

    /**
     * Return the parent block.
     *
     * @return the parent block
     */
    public SourceBlock getParent() {
        return parent;
    }

    /**
     * Set the piece parent.
     *
     * @param parentBlock
     *            piece parent block
     */
    public final void setParent(final SourceBlock parentBlock) {
        parent = parentBlock;
    }

    /**
     * Return the raw piece of source code.
     *
     * @return the raw piece of source code
     */
    public abstract String toRawCode();
}
