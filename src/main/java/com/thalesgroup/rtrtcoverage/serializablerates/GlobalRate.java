package com.thalesgroup.rtrtcoverage.serializablerates;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing rates data for a job.
 * @author Bastien Reboulet
 */
public class GlobalRate extends CoverageElementRate {

    /**
     * The serial ID.
     */
    private static final long serialVersionUID = -1534522721809042980L;

    /**
     * The list of all the files for this job.
     */
    private List<FileRate> fileRates;

    /**
     * Default constructor.
     */
    public GlobalRate() {
        fileRates = new ArrayList<FileRate>();
    }

    /**
     * @return all the files rates included in this job.
     */
    public final List<FileRate> getFileRates() {
        return fileRates;
    }

}
