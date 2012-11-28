package com.thalesgroup.rtrtcoverage.fdcreader;

import hudson.FilePath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Parser of *.fdc file.
 *
 * @author Bastien Reboulet
 */
public class FdcReader {
    /**
     * Gets all the data needed from a *.fdc file.
     * and fill a FileCoverageDefinition
     *
     * @param fdcFile
     *            the *.fdc file path we want to parse
     * @return the FileCoverageDefinition filled by the parser
     * @throws IOException an IOException
     */
    public final FileCoverageDefinition read(final FilePath fdcFile)
            throws IOException {
        final BufferedReader bf = new BufferedReader(new FileReader(
                fdcFile.getRemote()));
        String currentLineString = new String();
        String first2letters = new String();
        final FileCoverageDefinition fileCoverageDef = new FileCoverageDefinition();

        // Read the *.fdc file only until "@RIK" token
        // No need to read further for the data we want
        currentLineString = bf.readLine();
        while (!currentLineString.contains("@RIK")) {
            first2letters = currentLineString.substring(0, 2);
            if (first2letters.contentEquals("FC")) {
                final LinkedList<String> parsedLine = customSplit(currentLineString);
                parsedLine.remove();
                fileCoverageDef.setSourceName(parsedLine.poll());
                fileCoverageDef.setSourceDir(parsedLine.poll());
            } else if (first2letters.contentEquals("BL")
                    || first2letters.contentEquals("PR")
                    || first2letters.contentEquals("AP")) {
                final BranchDefinition branchDef = new BranchDefinition();
                branchDef.setName(currentLineString.replace("\"", ""));
                final LinkedList<String> parsedLine = customSplit(currentLineString);
                parsedLine.remove();                                                        // 0 0
                branchDef.setId(Integer.toHexString(Integer.parseInt(parsedLine.poll())));  // 1 1
                branchDef.setSubType(parsedLine.poll());                                    // 2 2
                branchDef.setFctName(parsedLine.poll());                                    // 3 3
                if (first2letters.contentEquals("BL")) {
                    branchDef.setPath(parsedLine.poll());                                   // 4
                    if (branchDef.getSubType().contentEquals("simple")) {
                        branchDef.setType(BranchDefinitionType.BLOCK);
                    } else if (branchDef.getSubType().contentEquals("logical")) {
                        branchDef.setType(BranchDefinitionType.LOOP);
                    }
                } else if (first2letters.contentEquals("PR")) {
                    branchDef.setType(BranchDefinitionType.PROC);
                } else if (first2letters.contentEquals("AP")) {
                    branchDef.setType(BranchDefinitionType.CALL);
                }
                branchDef.setStartLineNumber(Integer.parseInt(parsedLine.poll()));          // 5 4
                branchDef.setEndLineNumber(Integer.parseInt(parsedLine.poll()));            // 6 5
                fileCoverageDef.addBranch(branchDef);
            }
            currentLineString = bf.readLine();
        }
        bf.close();
        return fileCoverageDef;
    }


    /**
     * Allows to split a line with a better behavior than default split method.
     * ignore spaces between quotes
     * removes the quotes from tokens
     * @param line the current line string
     * @return a list of all the tokens extracted from the line
     */
    public final LinkedList<String> customSplit(final String line) {
        final LinkedList<String> result = new LinkedList<String>();
        boolean isBetweenQuotes = false;
        StringBuffer currentToken = new StringBuffer();
        final int length = line.length();
        for (int i = 0; i < length; i++) {
            final char c = line.charAt(i);
            final boolean isLast = (i == length - 1);
            if (isLast) {
                if (c != '\"') {
                    currentToken.append(c);
                }
                result.offer(currentToken.toString());
            } else if (c == '\"') {
                isBetweenQuotes = !isBetweenQuotes;
            } else if ((c == ' ') && !isBetweenQuotes) {
                result.offer(currentToken.toString());
                currentToken = new StringBuffer();
            } else {
                currentToken.append(c);
            }
        }
        return result;
    }
}
