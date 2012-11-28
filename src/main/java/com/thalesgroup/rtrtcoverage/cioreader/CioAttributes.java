package com.thalesgroup.rtrtcoverage.cioreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class that read the file to determine the number of attributes into the
 * files.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class CioAttributes {

    /**
     * has a function state into the file.
     */
    private boolean hasFunction;
    /**
     * has a function state into the file with information.
     */
    private boolean hasInfoFunction;

    /**
     * has a call state into the file.
     */
    private boolean hasCall;
    /**
     * has a call state into the file with information.
     */
    private boolean hasInfoCall;

    /**
     * has a statement block state into the file.
     */
    private boolean hasStatBlock;
    /**
     * has a statement block state into the file with information.
     */
    private boolean hasInfoStatBlock;

    /**
     * has a implicit block state into the file.
     */
    private boolean hasImplBlock;
    /**
     * has a implicit block state into the file with information.
     */
    private boolean hasInfoImplBlock;

    /**
     * has a decision state into the file.
     */
    private boolean hasDecision;
    /**
     * has a decision state into the file with information.
     */
    private boolean hasInfoDecision;

    /**
     * has a loop state into the file.
     */
    private boolean hasLoop;
    /**
     * has a loop state into the file with information.
     */
    private boolean hasInfoLoop;

    /**
     * has a basic condition state into the file.
     */
    private boolean hasBasicCond;
    /**
     * has a basic condition state into the file with information.
     */
    private boolean hasInfoBasicCond;;

    /**
     * has a modified condition state into the file.
     */
    private boolean hasModifCond;
    /**
     * has a modified condition state into the file with information.
     */
    private boolean hasInfoModifCond;

    /**
     * has a multiple condition state into the file.
     */
    private boolean hasMultCond;
    /**
     * has a multiple condition state into the file with information.
     */
    private boolean hasInfoMultCond;

    /**
     * Default Constructor.
     *
     * @param in
     *            input stream on the cio reader
     * @throws CioException
     *             if bad input
     */
    public CioAttributes(final InputStream in) throws CioException {
        hasFunction = false;
        hasCall = false;
        hasStatBlock = false;
        hasImplBlock = false;
        hasDecision = false;
        hasLoop = false;
        hasBasicCond = false;
        hasModifCond = false;
        hasMultCond = false;

        hasInfoFunction = false;
        hasInfoCall = false;
        hasInfoStatBlock = false;
        hasInfoImplBlock = false;
        hasInfoDecision = false;
        hasInfoLoop = false;
        hasInfoBasicCond = false;
        hasInfoModifCond = false;
        hasInfoMultCond = false;

        setFlags(in);
    }

    /**
     * Read a first time the file in order to know the number of arguments into
     * the report.
     *
     * @param in
     *            input stream on the cio reader
     * @throws CioException
     *             if bad input
     */
    private void setFlags(final InputStream in) throws CioException {

        final InputStreamReader ipsr = new InputStreamReader(in);
        final BufferedReader br = new BufferedReader(ipsr);
        String line = null;
        final int maxParams = 6;
        final int twoParams = 2;
        final int threeParams = 3;
        final int fourParams = 4;

        try {

            line = br.readLine();
            line = line.trim();
            // Go the global coverage
            while (!line.equals("G")) {
                line = br.readLine();
                if (line == null) {
                    throw new CioException(
                            "No global coverage into the .cio file");
                }
                line = line.trim();
            }
        } catch (final IOException e1) {
            throw new CioException(e1.getMessage());
        }

        while (line != null) {
            line = line.trim();
            if (line.startsWith("File")) {
                try {
                    line = br.readLine();
                    line = line.trim();
                } catch (final IOException e1) {
                    throw new CioException(e1.getMessage());
                }
            }
            // keep compatibility between v2002 & v7
            // 2002: <coverage type> ........ n% (p/q)[, +m (r/s)]
            // 2002: <coverage type>\tn% (p/q)[, +m (r/s)]
            line = line.replaceAll("\t", " .. ");
            line = line.replaceAll(" {2,}", " ");
            String[] v = new String[maxParams];
            final String cleanLine = line.replace(",", "");
            v = cleanLine.split(" ");
            if (v[0].equals("Functions")) {
                hasFunction = true;
                hasInfoFunction |= (!v[fourParams].equals("none"));
            } else if (v[0].equals("Calls")) {
                hasCall = true;
                hasInfoCall |= (!v[twoParams].equals("none"));
            } else if (v[0].equals("Statement")) {
                hasStatBlock = true;
                hasInfoStatBlock |= (!v[threeParams].equals("none"));
            } else if (v[0].equals("Implicit")) {
                hasImplBlock = true;
                hasInfoImplBlock |= (!v[threeParams].equals("none"));
            } else if (v[0].equals("Decisions")) {
                hasDecision = true;
                hasInfoDecision |= (!v[twoParams].equals("none"));
            } else if (v[0].equals("Loops")) {
                hasLoop = true;
                hasInfoLoop |= (!v[twoParams].equals("none"));
            } else if (v[0].equals("Basic")) {
                hasBasicCond = true;
                hasInfoBasicCond |= (!v[threeParams].equals("none"));
            } else if (v[0].equals("Modified")) {
                hasModifCond = true;
                hasInfoModifCond |= (!v[threeParams].equals("none"));
            } else if (v[0].equals("Multiple")) {
                hasMultCond = true;
                hasInfoMultCond |= (!v[threeParams].equals("none"));
            } else if (v[0].equals("Hit")) {
                // nothing to do
            } else {
                if (v.length > 1) {
                    // It must be a new ratio!
                    throw new CioException(
                            "Unknown statement ratio into .cio file: " + v[0]);
                } // else Other method into current test.
            }

            try {
                line = br.readLine();
            } catch (final IOException e1) {
                throw new CioException(e1.getMessage());
            }
        }
        try {
            br.close();
            ipsr.close();
        } catch (final IOException e) {
            throw new CioException(e.getMessage());
        }

    }

    /**
     * has a function state into the file.
     *
     * @return true if function state exists
     */
    public final boolean hasFunction() {
        return hasFunction;
    }

    /**
     * has a call state into the file.
     *
     * @return true if call state exists
     */
    public final boolean hasCall() {
        return hasCall;
    }

    /**
     * has a statement block state into the file.
     *
     * @return true if statement block state exists
     */
    public final boolean hasStatBlock() {
        return hasStatBlock;
    }

    /**
     * has a implicit block state into the file.
     *
     * @return true if implicit block state exists
     */
    public final boolean hasImplBlock() {
        return hasImplBlock;
    }

    /**
     * has a decision state into the file.
     *
     * @return true if decision state exists
     */
    public final boolean hasDecision() {
        return hasDecision;
    }

    /**
     * has a loop state into the file.
     *
     * @return true if loop state exists
     */
    public final boolean hasLoop() {
        return hasLoop;
    }

    /**
     * has a basic condition state into the file.
     *
     * @return true if basic condition state exists
     */
    public final boolean hasBasicCond() {
        return hasBasicCond;
    }

    /**
     * has a modified condition state into the file.
     *
     * @return true if modified condition state exists
     */
    public final boolean hasModifCond() {
        return hasModifCond;
    }

    /**
     * has a multiple condition state into the file.
     *
     * @return true if multiple condition state exists
     */
    public final boolean hasMultCond() {
        return hasMultCond;
    }

    /**
     * get an array with all the flags.
     *
     * @return the array
     */
    public final boolean[] getAttributesFlags() {

        final boolean[] flags = new boolean[ReportTag.values().length];

        flags[ReportTag.FUNCTION.ordinal()] = hasFunction;
        flags[ReportTag.CALL.ordinal()] = hasCall;
        flags[ReportTag.STATEMENTBLOCK.ordinal()] = hasStatBlock;
        flags[ReportTag.IMPLICITBLOCK.ordinal()] = hasImplBlock;
        flags[ReportTag.DECISION.ordinal()] = hasDecision;
        flags[ReportTag.LOOP.ordinal()] = hasLoop;
        flags[ReportTag.BASICCONDITION.ordinal()] = hasBasicCond;
        flags[ReportTag.MODIFIEDCONDITION.ordinal()] = hasModifCond;
        flags[ReportTag.MULTIPLECONDITION.ordinal()] = hasMultCond;

        return flags;
    }

    /**
     * get an array with all the flags.
     *
     * @return the array
     */
    public final boolean[] getAttributesInformationFlags() {

        final boolean[] flags = new boolean[ReportTag.values().length];

        flags[ReportTag.FUNCTION.ordinal()] = hasInfoFunction;
        flags[ReportTag.CALL.ordinal()] = hasInfoCall;
        flags[ReportTag.STATEMENTBLOCK.ordinal()] = hasInfoStatBlock;
        flags[ReportTag.IMPLICITBLOCK.ordinal()] = hasInfoImplBlock;
        flags[ReportTag.DECISION.ordinal()] = hasInfoDecision;
        flags[ReportTag.LOOP.ordinal()] = hasInfoLoop;
        flags[ReportTag.BASICCONDITION.ordinal()] = hasInfoBasicCond;
        flags[ReportTag.MODIFIEDCONDITION.ordinal()] = hasInfoModifCond;
        flags[ReportTag.MULTIPLECONDITION.ordinal()] = hasInfoMultCond;

        return flags;
    }

}
