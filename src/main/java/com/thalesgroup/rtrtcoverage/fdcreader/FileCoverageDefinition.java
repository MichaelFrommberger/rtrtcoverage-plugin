package com.thalesgroup.rtrtcoverage.fdcreader;

import hudson.FilePath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure for a *.fdc file.
 *
 * @author Bastien Reboulet
 */
public class FileCoverageDefinition {

    /**
     * A mapping between node name and a node.
     */
    private Map<String, NodeDefinition> nodeDefinitions;

    /**
     * the name of the source file (*.C).
     */
    private String sourceName;

    /**
     * the directory where is stored the source file.
     */
    private String sourceDir;

    /**
     * the key extracted from aug file.
     */
    private String key;

    /**
     * the crc extracted from aug file.
     */
    private String crc;

    /**
     * Path (from the build dir) of the fdc file linked to this file coverage definition.
     */
    private String fdcPath;

    /**
     * Default constructor.
     */
    public FileCoverageDefinition() {
        nodeDefinitions = new HashMap<String, NodeDefinition>();
    }

    /**
     * @param node a node definition.
     */
    public final void addNode(final NodeDefinition node) {
        nodeDefinitions.put(node.getNodeName(), node);
    }

    /**
     * @param nodeName the name of the wanted node.
     * @return the node definition with the specified name.
     */
    public final NodeDefinition getNode(final String nodeName) {
        return nodeDefinitions.get(nodeName);
    }

    /**
     * @return a list of all the nodes.
     */
    public final List<NodeDefinition> getNodes() {
        return new ArrayList<NodeDefinition>(nodeDefinitions.values());
    }

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

    /**
     * @return the fdc path.
     */
    public final String getFdcPath() {
        return fdcPath;
    }

    /**
     * @param newFdcPath a fdc path.
     */
    public final void setFdcPath(final String newFdcPath) {
        this.fdcPath = newFdcPath;
    }

}
