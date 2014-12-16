package com.thalesgroup.rtrtcoverage.sourcecoloring;

/**
 * A bit of literal source code.
 */
class SourceLineChunk extends SourcePiece {
    /**
     * the chunk of source code.
     */
    private final String chunk;

    /**
     * Constructor.
     *
     * @param parent
     *            the parent block
     * @param line
     *            the piece of source code
     */
    public SourceLineChunk(final SourceBlock parent, final String line) {
        super(parent);
        this.chunk = line;
    }

    /**
     * Return the source code chunk.
     *
     * @return the source code bit.
     */
    public String getChunk() {
        return chunk;
    }

    @Override
    public String toRawCode() {
        return getChunk();
    }
}
