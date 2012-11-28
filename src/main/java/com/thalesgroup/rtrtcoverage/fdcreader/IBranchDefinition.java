package com.thalesgroup.rtrtcoverage.fdcreader;

/**
 * Interface for a Branch Definition.
 * @author Bastien Reboulet
 */
public interface IBranchDefinition {

    /**
     * @return concatenation of mark and id fields.
     */
    String getMarkId();

    /**
     * @return the type of the branch.
     */
    BranchType getType();

    /**
     * @param newType a type of branch.
     */
    void setType(BranchType newType);

    /**
     * @return the mark of this branch.
     */
    String getMark();

    /**
     * @param newMark a branch mark.
     */
    void setMark(String newMark);

    /**
     * @return the ID of this branch.
     */
    String getId();

    /**
     * @param newId a branch ID
     */
    void setId(String newId);

    /**
     * @return the line number of this branch.
     */
    int getLineNumber();

    /**
     * @param newLineNumber the line number of this branch.
     */
    void setLineNumber(int newLineNumber);
}
