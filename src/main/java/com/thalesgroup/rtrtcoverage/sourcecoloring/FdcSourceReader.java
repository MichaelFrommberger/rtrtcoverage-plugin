package com.thalesgroup.rtrtcoverage.sourcecoloring;

import hudson.FilePath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Fdc reader for extracting source code blocks.
 */
public class FdcSourceReader {

    /**
     * Read a FDC file to extract the source code structure.
     *
     * @param fdcFile
     *            FDC file to read
     * @return the source code structure
     * @throws IOException
     *             in case of error during file reading.
     */
    public SourceFile read(final FilePath fdcFile) throws IOException {
        final BufferedReader buf = new BufferedReader(new FileReader(
                fdcFile.getRemote()));
        SourceFile source = new SourceFile();
        // skip header section
        String line = buf.readLine();
        while (line != null) {
            if (line.startsWith("FC")) {
                // get source file name and directory
                source.setName(customSplit(line).get(1).toUpperCase());
                source.setDirectory(customSplit(line).get(2));
            }
            if (line.startsWith("@RIK")) {
                line = buf.readLine();
                break;
            }
            line = buf.readLine();
        }
        // skip @RIK attributes
        while (line != null) {
            if (line.startsWith("NODE")) {
                line = line.substring(line.indexOf('@') + 1);
                break;
            }
            line = buf.readLine();
        }
        // From that point, the fdc file structure is regular, we can use
        // generic functions from extractings blocks
        SourceBlock currentBlock = new SourceBlock(null, null, null);
        source.setContent(currentBlock);
        SourceBlock popupInAlt = null;

        // Build source block tree.
        while (line != null) {
            if (line.equals("")) {
                // line end
                currentBlock.addChild(new SourceLineEnd(currentBlock));
                line = buf.readLine();
            } else if (line.startsWith("@/NODE /RIK@")) {
                // robustness: this is the source end
                break;
            } else if (line.startsWith("@/")) {
                // block end: @/TAGNAME@
                int secondIndex = line.indexOf("@", 2);
                String type = line.substring(2, secondIndex);
                if (popupInAlt == currentBlock && type.equals("ALT")) {
                    // special case: deal with POPUP spreading over a ALT block, e.g.:
                    // @ALT@code1@-ALT@altcode1@POPUP@altcode2@/ALT@code2@-POPUP@hint@/POPUP@
                    // ==> do as if it were:
                    // @ALT@code1@-ALT@altcode1 altcode2@/ALT@@POPUP@code2@-POPUP@hint@/POPUP@
                    SourceBlock altBlock = popupInAlt.getParent();
                    List<SourcePiece> newAlts = new ArrayList<SourcePiece>();
                    for (SourcePiece altChild : altBlock.getSecondContent()) {
                        if (altChild == popupInAlt) {
                            // replace the popup with its flat content
                            newAlts.add(new SourceLineChunk(altBlock, popupInAlt.toRawCode()));
                        } else {
                            newAlts.add(altChild);
                        }
                    }
                    altBlock.setSecondContent(newAlts);
                    // unstack the ALT, and use the popup as the current block
                    popupInAlt.setContent(new ArrayList<SourcePiece>());
                    SourceBlock parentBlock = altBlock.getParent();
                    currentBlock = popupInAlt;
                    parentBlock.addChild(popupInAlt);
                    popupInAlt.setParent(parentBlock);
                    popupInAlt = null;
                } else {
                    // regular block ending
                    if (currentBlock.getParent() != null) {
                        // /!\ some tags have implicit closing, e.g.:
                        // @BRANCH ...@@LINK NAME=xx@xx@/BRANCH@
                        // => unstack up to two blocks
                        if (type.equals(currentBlock.getType())) {
                            // the close tag matches the current block
                            if (currentBlock == popupInAlt) {
                                popupInAlt = null;
                            }
                            currentBlock = currentBlock.getParent();
                        } else {
                            // the close tag may correspond to a parent block: find it
                            // (if not found, then is has been implicitly closed before,
                            // so nothing to do)
                            SourceBlock parentBlock = currentBlock.getParent();
                            while (parentBlock != null) {
                                if (type.equals(parentBlock.getType())) {
                                    currentBlock = parentBlock.getParent();
                                    break;
                                } else {
                                    parentBlock = parentBlock.getParent();
                                }
                            }
                        }
                    }
                }
                line = line.substring(secondIndex + 1);
            } else if (line.startsWith("@-")) {
                // block second part: @-TAGNAME@
                if (currentBlock.getParent() != null) {
                    // this tag possibly implicitely closes an embedded
                    // block. For instance, in the following, the @-POPUP@
                    // tag implicitely closes the @ALT@ block:
                    //     @POPUP@...@ALT@foo@-ALT@bar@-POPUP@
                    int secondIndex = line.indexOf("@", 2);
                    String type = line.substring(2, secondIndex);
                    if (!currentBlock.getType().equals(type)) {
                        currentBlock = currentBlock.getParent();
                    }
                }
                currentBlock.changePart();
                line = line.substring(line.indexOf("@", 1) + 1);
            } else if (line.startsWith("@@")) {
                // an escaped @ character => a piece of code
                int index = line.indexOf("@", 2);
                if (index == -1) {
                    currentBlock.addChild(
                            new SourceLineChunk(currentBlock, line));
                    line = "";
                } else {
                    currentBlock.addChild(new SourceLineChunk(currentBlock,
                            line.substring(1, index)));
                    line = line.substring(index);
                }
            } else if (line.startsWith("@")) {
                // new block: @TAGNAME KEY=VALUE ...@
                int secondIndex = line.indexOf("@", 1);
                LinkedList<String> tokens = customSplit(line.substring(1,
                        secondIndex));
                String type = tokens.removeFirst();
                SourceBlock parentBlock = currentBlock;
                currentBlock = new SourceBlock(parentBlock, type,
                        makeAttributes(tokens));
                if (type.equals("POPUP")
                        && parentBlock.getType().equals("ALT")
                        && parentBlock.getSecondContent() != null) {
                    // register popup in @-ALT@ block, in case
                    // the popup spread over the ALT.
                    popupInAlt = currentBlock;
                }
                parentBlock.addChild(currentBlock);
                line = line.substring(secondIndex + 1);
            } else {
                // piece of code (till the possible next @ tag)
                int index = line.indexOf("@");
                if (index == -1) {
                    currentBlock.addChild(new SourceLineChunk(currentBlock,
                            line));
                    line = "";
                } else {
                    currentBlock.addChild(new SourceLineChunk(currentBlock,
                            line.substring(0, index)));
                    line = line.substring(index);
                }
            }
        }
        buf.close();
        return source;
    }

    /**
     * Build an attribute mapping using the given "KEY=VALUE" tokens.
     *
     * @param tokens the token list
     * @return the key/value mapping attributes
     */
    private static Map<String, String> makeAttributes(List<String> tokens) {
        Map<String, String> attributes = new HashMap<String, String>();
        for (String token : tokens) {
            int splitIndex = token.indexOf("=");
            if (splitIndex == -1) {
                attributes.put(token, null);
            } else {
                attributes.put(token.substring(0, splitIndex),
                        token.substring(splitIndex + 1));
            }
        }
        return attributes;
    }

    /**
     * Allows to split a line with a better behavior than default split method.
     * ignore spaces between quotes removes the quotes from tokens
     *
     * @param line
     *            the current line string
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
