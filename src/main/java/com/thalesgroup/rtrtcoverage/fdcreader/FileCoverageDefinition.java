package com.thalesgroup.rtrtcoverage.fdcreader;

import hudson.FilePath;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Data structure for a *.fdc file.
 *
 * @author Bastien Reboulet
 */
public class FileCoverageDefinition {

    /**
     * the name of the source file (*.C).
     */
    private String sourceName = null;

    /**
     * the directory where is stored the source file.
     */
    private String sourceDir = null;

    /**
     * the key extracted from aug file.
     */
    private String key;

    /**
     * @return key the key extracted from aug file
     */
    public final String getKey() {
        return key;
    }

    /**
     * @param newKey the key extracted from aug file
     */
    public final void setKey(final String newKey) {
        this.key = newKey;
    }

    /**
     * the crc extracted from aug file.
     */
    private String crc;

    /**
     * @return the crc extracted from aug file
     */
    public final String getCrc() {
        return crc;
    }

    /**
     * @param newCrc the crc extracted from aug file
     */
    public final void setCrc(final String newCrc) {
        this.crc = newCrc;
    }

    /**
     * the map of all the branches of this file.
     */
    private final Map<String, BranchDefinition> branchDefs;

    /**
     * Default constructor.
     */
    public FileCoverageDefinition() {
        branchDefs = new HashMap<String, BranchDefinition>();
    }

    /**
     * @param type
     *            of the branch we are looking for
     * @param id
     *            of the branch we are looking for
     * @return the branch definition corresponding to the type and id
     */
    public final BranchDefinition getBranch(final BranchDefinitionType type,
            final String id) {
        return branchDefs.get(type + id);
    }

    /**
     * @return a collection of all the branches of this file (non ordered)
     */
    public final Collection<BranchDefinition> getBranches() {
        return branchDefs.values();
    }

    /**
     * Adds a branch to the map.
     *
     * @param branchDef
     *            the branch to add
     */
    public final void addBranch(final BranchDefinition branchDef) {
        branchDefs.put(branchDef.getType() + branchDef.getId(), branchDef);
    }

    /**
     * @return the source file name corresponding to this *.fdc file (*.C)
     */
    public final String getSourceName() {
        return sourceName;
    }

    /**
     * @param newSourceName
     *            the source file name corresponding to this *.fdc file (*.C)
     *            extracted from *.fdc file by the parser
     */
    public final void setSourceName(final String newSourceName) {
        this.sourceName = newSourceName;
    }

    /**
     * @return the source folder where source file is
     */
    public final String getSourceDir() {
        return new FilePath(new File(sourceDir)).getRemote();
    }

    /**
     * @param newSourceDir
     *            the source folder where source file is extracted from *.fdc
     *            file by the parser
     */
    public final void setSourceDir(final String newSourceDir) {
        this.sourceDir = newSourceDir;
    }

}
