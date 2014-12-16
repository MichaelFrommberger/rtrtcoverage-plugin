package com.thalesgroup.rtrtcoverage.sourcecoloring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A source piece consisting of a block, containing other blocks or direct
 * source code.
 */
class SourceBlock extends SourcePiece {
    /**
     * The block type, among (e.g. COMMENT, NODE, BRANCH, ALT, DECISION, JUMP,
     * POPUP, SHORTCUT...).
     */
    private final String type;
    /**
     * The block attributes (which keys are e.g. NAME, MARK, ID, SUM...).
     */
    private final Map<String, String> attributes;
    /**
     * reference to the current child source piece list.
     */
    private List<SourcePiece> currentContent;
    /**
     * The first child piece list (and the only useful for most block types).
     */
    private List<SourcePiece> content;
    /**
     * The second child piece list (indeed, ALT and POPUP blocks may have two
     * child lists).
     */
    private List<SourcePiece> content2;

    /**
     * Constructor.
     *
     * @param parentBlock
     *            the parent block
     * @param type
     *            the block type
     * @param attributes
     *            the block attributes
     */
    public SourceBlock(final SourceBlock parentBlock, final String type,
            final Map<String, String> attributes) {
        super(parentBlock);
        this.type = type;
        this.attributes = attributes;
        content = new ArrayList<SourcePiece>();
        currentContent = content;
        content2 = null;
    }

    /**
     * Return the block attributes.
     *
     * @return the block attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Return the block type.
     *
     * @return the block type
     */
    public String getType() {
        return type;
    }

    /**
     * After calling this method, children added with {@link#addChild} are added
     * to the second list.
     */
    public void changePart() {
        content2 = new ArrayList<SourcePiece>();
        currentContent = content2;
    }

    /**
     * Add child to the current children list.
     *
     * @param child
     *            child to be added
     */
    public void addChild(final SourcePiece child) {
        currentContent.add(child);
    }

    /**
     * Return the first child list.
     *
     * @return the first child list
     */
    public List<SourcePiece> getContent() {
        return content;
    }

    /**
     * Set the (first) content list.
     *
     * @param pieces
     *            the content list
     */
    public void setContent(final List<SourcePiece> pieces) {
        content = pieces;
        currentContent = content;
    }

    /**
     * Return the second child list.
     *
     * @return the second child list, or null if not applicable for this block
     *         type.
     */
    public List<SourcePiece> getSecondContent() {
        return content2;
    }

    /**
     * Set the second content list.
     *
     * @param pieces
     *            the second content list
     */
    public void setSecondContent(final List<SourcePiece> pieces) {
        content2 = pieces;
    }

    @Override
    public String toRawCode() {
        StringBuilder code = new StringBuilder();
        for (SourcePiece child : getContent()) {
            code.append(child.toRawCode());
        }
        return code.toString();
    }
}
