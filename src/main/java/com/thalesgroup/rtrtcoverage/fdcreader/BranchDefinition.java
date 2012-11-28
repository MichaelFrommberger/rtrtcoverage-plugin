package com.thalesgroup.rtrtcoverage.fdcreader;

/**
 * Data structure for a branch extracted from a *.fdc file.
 * @author Bastien Reboulet
 */
public class BranchDefinition {

    /**
     * content of the line.
     */
    private String name;

    /**
     * type of the branch ("BL, "PR", ...).
     */
    private BranchDefinitionType type;

    /**
     * id of the branch.
     */
    private String id;

    /**
     * function name of the branch.
     */
    private String fctName;

    /**
     * path of the branch (ex: "/else/then").
     */
    private String path;

    /**
     * sub type of the branch (ex: "simple", "return", "proc").
     */
    private String subType;

    /**
     * Line number where the function starts.
     */
    private int startLineNumber;

    /**
     * Line number where the function ends.
     */
    private int endLineNumber;

    /**
     * @return the type of the branch
     */
    public final BranchDefinitionType getType() {
        return type;
    }

    /**
     * @param newType the type of the branch
     */
    public final void setType(final BranchDefinitionType newType) {
        this.type = newType;
    }

    /**
     * @return the id of the branch
     */
    public final String getId() {
        return id;
    }

    /**
     * @param newId the id of the branch
     */
    public final void setId(final String newId) {
        this.id = newId;
    }

    /**
     * @return the name of the function
     */
    public final String getFctName() {
        return fctName;
    }

    /**
     * @param newFctName the name of the function
     */
    public final void setFctName(final String newFctName) {
        this.fctName = newFctName;
    }

    /**
     * @return the path of the branch
     */
    public final String getPath() {
        return path;
    }

    /**
     * @param newPath the path of the branch
     */
    public final void setPath(final String newPath) {
        this.path = newPath;
    }

    /**
     * @return the sub type of the branch
     */
    public final String getSubType() {
        return subType;
    }

    /**
     * @param newSubType the sub type of the branch
     */
    public final void setSubType(final String newSubType) {
        this.subType = newSubType;
    }

    /**
     * @return the line number corresponding to the beginning of the branch
     */
    public final int getStartLineNumber() {
        return startLineNumber;
    }

    /**
     * @param newStartLineNumber the line number corresponding to the beginning of the branch
     */
    public final void setStartLineNumber(final int newStartLineNumber) {
        this.startLineNumber = newStartLineNumber;
    }

    /**
     * @return the line number corresponding to the end of the branch
     */
    public final int getEndLineNumber() {
        return endLineNumber;
    }

    /**
     * @param newEndLineNumber the line number corresponding to the end of the branch
     */
    public final void setEndLineNumber(final int newEndLineNumber) {
        this.endLineNumber = newEndLineNumber;
    }

    /**
     * @return the line string
     */
    public final String getName() {
        return name;
    }

    /**
     * @param newName the line string
     */
    public final void setName(final String newName) {
        this.name = newName;
    }

}
