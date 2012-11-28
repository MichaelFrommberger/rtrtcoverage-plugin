package com.thalesgroup.rtrtcoverage.serializablerates;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing a list of node rates.
 * @author Bastien Reboulet
 */
public abstract class SourceRate extends CoverageElementRate {

    /**
     * The serial ID.
     */
    private static final long serialVersionUID = -5417689055741086211L;

    /**
     * The source file name.
     */
    private String sourceFileName;

    /**
     * The fdc path linked to this source file.
     */
    private String fdcPath;

    /**
     * The list of all the nodes included in this source file.
     */
    private List<NodeRate> nodeRates;

    /**
     * Default constructor.
     */
    public SourceRate() {
        nodeRates = new ArrayList<NodeRate>();
    }

    /**
     * @return all the nodes included in this source file.
     */
    public final List<NodeRate> getNodeRates() {
        return nodeRates;
    }

    /**
     * @return the name of this source file.
     */
    public final String getSourceFileName() {
        return sourceFileName;
    }

    /**
     * @param newSourceFileName the source file name.
     */
    public final void setSourceFileName(final String newSourceFileName) {
        this.sourceFileName = newSourceFileName;
    }

    /**
     * @return the fdc path of the fdc file matching this source file.
     */
    public final String getFdcPath() {
        return fdcPath;
    }

    /**
     * @param newFdcPath the fdc path of the fdc file matching this source file.
     */
    public final void setFdcPath(final String newFdcPath) {
        this.fdcPath = newFdcPath;
    }

}
