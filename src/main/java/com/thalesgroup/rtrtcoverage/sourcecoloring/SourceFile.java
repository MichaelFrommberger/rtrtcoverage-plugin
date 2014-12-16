package com.thalesgroup.rtrtcoverage.sourcecoloring;

/**
 * A source file, with structured code blocks.
 */
public class SourceFile {
    /**
     * the file (upcased) name.
     */
    private String name;
    /**
     * the file directory.
     */
    private String directory;
    /**
     * the root code block.
     */
    private SourceBlock code;

    /**
     * Set the root code block.
     *
     * @param codeBlock
     *            the root code block
     */
    public final void setContent(final SourceBlock codeBlock) {
        this.code = codeBlock;
    }

    /**
     * Return the file directory.
     *
     * @return the file directory
     */
    public final String getDirectory() {
        return directory;
    }

    /**
     * Set the file directory.
     *
     * @param directoryPath
     *            the file directory
     */
    public final void setDirectory(final String directoryPath) {
        this.directory = directoryPath;
    }

    /**
     * Return the file (upcased) name.
     *
     * @return the file (upcased) name
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the file (upcased) name.
     *
     * @param fileName
     *            the file (upcased) name
     */
    public final void setName(final String fileName) {
        this.name = fileName;
    }

    /**
     * Return the code root block.
     *
     * @return the code root block
     */
    public final SourceBlock getCode() {
        return code;
    }

}
