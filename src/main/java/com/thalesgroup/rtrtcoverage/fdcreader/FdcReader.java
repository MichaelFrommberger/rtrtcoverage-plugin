package com.thalesgroup.rtrtcoverage.fdcreader;

import hudson.FilePath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
        FileCoverageDefinition fileCoverageDef = new FileCoverageDefinition();
        fileCoverageDef.setFdcPath(fdcFile.getRemote());
        Stack<NodeDefinition> nodes = new Stack<NodeDefinition>();

        boolean isInPopup = false;
        int currentLineNumber = 0;
        int fileStartLineNumber = 0;

        // Read the *.fdc file only until "@RIK" token
        // No need to read further for the data we want
        currentLineString = bf.readLine();
        while (currentLineString != null) {

            if (currentLineString.contains("@POPUP@")) {
                isInPopup = true;
            }
            if (currentLineString.contains("@/POPUP@")) {
                isInPopup = false;
            }
            if (currentLineString.startsWith("FC")) {
            	// RTRT is inconsistent about filename case
            	// => put every source file name to upper case
                fileCoverageDef.setSourceName(customSplit(currentLineString).get(1).toUpperCase());
                fileCoverageDef.setSourceDir(customSplit(currentLineString).get(2));
            }
            if ((fileStartLineNumber == 0)
            		&& (currentLineString.startsWith("NODE")
            				|| currentLineString.startsWith("@@NODE"))) {
                fileStartLineNumber = currentLineNumber;
            }
            if (currentLineString.contains("@NODE")) {
                NodeDefinition nodeDef = new NodeDefinition();
                String nodeAttributes = getAttributesBetween(currentLineString, "@NODE", "@").get(0);
                String nodeName = getAttributeValue(nodeAttributes, "NAME=");
                nodeDef.setNodeName(nodeName);
                nodes.push(nodeDef);
            }
            if (!nodes.isEmpty()) {
            	if (currentLineString.contains("@-POPUP BRANCH")) {
                	// special cas for "implicit else" branches
                    for (String str : getAttributesBetween(currentLineString, "@-POPUP BRANCH", "@")) {
                        IBranchDefinition branch = null;
                        if (convertToBranchDefinitionType(getAttributeValue(str, "SUM=")) != BranchType.TE_MODIFIEDS) {
                            branch = new SingleBranchDefinition();
                        } else {
                            branch = new MultipleBranchDefinition();
                        }
                        branch.setType(convertToBranchDefinitionType(getAttributeValue(str, "SUM=")));
                        branch.setId(getAttributeValue(str, "ID="));
                        branch.setMark(getAttributeValue(str, "MARK="));
                        branch.setLineNumber(currentLineNumber - fileStartLineNumber);
                        nodes.peek().addBranchDefinition(branch);
                    }
                }
                if (currentLineString.contains("@BRANCH")) {
                    for (String str : getAttributesBetween(currentLineString, "@BRANCH", "@")) {
                        IBranchDefinition branch = null;
                        if (convertToBranchDefinitionType(getAttributeValue(str, "SUM=")) != BranchType.TE_MODIFIEDS) {
                            branch = new SingleBranchDefinition();
                        } else {
                            branch = new MultipleBranchDefinition();
                        }
                        branch.setType(convertToBranchDefinitionType(getAttributeValue(str, "SUM=")));
                        branch.setId(getAttributeValue(str, "ID="));
                        branch.setMark(getAttributeValue(str, "MARK="));
                        branch.setLineNumber(currentLineNumber - fileStartLineNumber);
                        nodes.peek().addBranchDefinition(branch);
                    }
                }
            }
            if (currentLineString.contains("@/NODE@")
            		|| currentLineString.contains("@/BRANCH /NODE@")
            		|| currentLineString.contains("@/NODE /RIK@")) {
            	if (!nodes.isEmpty()) {
            		fileCoverageDef.addNode(nodes.pop());
            	}
            }
            currentLineString = bf.readLine();
            if (!isInPopup) {
                currentLineNumber++;
            }
        }
        bf.close();

        // Build links between singles and multi branch
        for (NodeDefinition node : fileCoverageDef.getNodes()) {
            for (IBranchDefinition branchDef : node.getBranchDefinitions()) {
                if (branchDef instanceof MultipleBranchDefinition) {
                    for (String id : branchDef.getId().split("[&|]")) {
                        MultipleBranchDefinition multiBranch = (MultipleBranchDefinition) branchDef;
                        String markId = "TE" + id;
                        if (!multiBranch.getSubBranchMarkIds().contains(markId)) {
                            multiBranch.getSubBranchMarkIds().add(markId);
                            for (IBranchDefinition branchDef2 : node.getBranchDefinitions()) {
                                if (branchDef2.getMarkId().contentEquals(markId)) {
                                    multiBranch.addSubBranch(branchDef2);
                                }
                            }
                        }
                    }
                }
            }
        }

        return fileCoverageDef;
    }

    /**
     * @param sum the "SUM=" value from a branch from an FDC file.
     * @return the BranchType matching the SUM value.
     */
    private static BranchType convertToBranchDefinitionType(final String sum) {

        if (sum.contentEquals("0")) {
            return BranchType.TP_FUNCTIONS;
        } else if (sum.contentEquals("1")) {
            return BranchType.TP_EXITS;
        } else if (sum.contentEquals("10")) {
            return BranchType.TA_CALLS;
        } else if (sum.contentEquals("20")) {
            return BranchType.TB_STATEMENTS;
        } else if (sum.contentEquals("21")) {
            return BranchType.TB_IMPLICIT;
        } else if (sum.contentEquals("22")) {
            return BranchType.TB_LOOPS;
        } else if (sum.contentEquals("30")) {
            return BranchType.TE_BASICS;
        } else if (sum.contentEquals("31")) {
            return BranchType.TE_MODIFIEDS;
        } else if (sum.contentEquals("32")) {
            return BranchType.TE_MULTIPLES;
        }
        return null;
    }

    /**
     * @param attributesString a branch string (without @BRANCH markers).
     * @param attributeName the token before the value we want ("ID=", "MARK=", etc)
     * @return the string right after the input token
     */
	private static String getAttributeValue(final String attributesString,
			final String attributeName) {
		int idx = attributesString.indexOf(attributeName);
		if (idx > -1) {
			int begin = idx + attributeName.length();
			int end = -1;
			if (attributesString.startsWith("\"", begin)) {
				begin ++;
				// a quoted attribute => reach next quote
				end = attributesString.indexOf("\"", begin + 1);
			} else {
				// a non quoted attribute => reach next space or end of string
				end = attributesString.indexOf(" ", begin + 1);
			}
			if (end < begin) {
				return attributesString.substring(begin);
			} else {
				return attributesString.substring(begin, end);
			}
		} else {
			return "";
		}
    }

    /**
     * @param line a full line.
     * @param token1 the start token.
     * @param token2 the end token.
     * @return a list of all the strings contained between specified tokens.
     */
    private static List<String> getAttributesBetween(final String line, final String token1, final String token2) {
        List<String> strings = new ArrayList<String>();
        int lastIdx = 0;
        lastIdx = line.indexOf(token1, lastIdx);
        while (lastIdx != -1)  {
            strings.add(line.substring(lastIdx + token1.length(),
                    line.indexOf(token2, lastIdx + token1.length())).trim());
            lastIdx = line.indexOf(token1, lastIdx + token1.length());
        }
        return strings;
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
