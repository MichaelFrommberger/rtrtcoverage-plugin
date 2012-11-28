package com.thalesgroup.rtrtcoverage.fdcparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.thalesgroup.rtrtcoverage.tioreader.CoverageLine;

/**
 * Extract from a .fdc file each line of the source code to perform coverage
 * coloring.
 *
 * @author Sebastien Barbier
 * @version 1.0
 */
public class FdcParser {

    /**
     * Where the next line source code is.
     */
    private final BufferedReader currentInfo;

    /**
     * Current line of information.
     */
    private String sourceCode;

    /**
     * The current name of the coverage.
     */
    private final Stack<CoverageLine> lineIdentities;

    /**
     * The hierarchy of the coverage.
     */
    private final Stack<Integer> lastLineIdentities;

    /**
     * The current last name of the coverage.
     */
    private int currentLastLineIdentities;

    /**
     * The tree of the coverage.
     */
    private final Stack<FdcTag> tagTree;

    /**
     * Tag for new decision block.
     */
    private boolean hasNewDecision;

    /**
     * Tag for end of a decision block.
     */
    private int hasRemoveDecision;

    /**
     * Tag for new branch.
     */
    private boolean hasNewBranch;

    /**
     * Specific tag for the popup and/or alt tokens. -1 if not popup-alt block
     * n=0 if popup-alt block n>0 if some BRANCH Decisions blocks add into stack
     */
    private int isPopupAlt;

    /**
     * Flag for finding the end of a source code line. If true => hasNext().
     */
    private boolean newLine;

    /**
     * Flag for initialization.
     */
    private boolean init;

    /**
     * For popping the tag at the end of the current source code line.
     */
    private final Deque<String> toPop;

    /**
     * To record the condition value for the conditional report Use only when a
     * popup-alt block is detected. Must recorded both into POPUP and ALT tokens
     * to have global conditions.
     */
    private String conditionValue;

    /**
     * Flag for treatment of comments into functions on several lines without
     * first known tags.
     */
    private boolean bePainted;

    /**
     * Special case for @POPUP@@POPUP@.
     */
    private boolean popupTuple;

    /*---------------------------------------------
     * CONSTRUCTOR
     *--------------------------------------------*/

    /**
     * Default Constructor. Put the parser on the first line with line source
     * code.
     *
     * @param path
     *            where to find .fdc file
     * @throws FdcException
     *             if some errors during opening and header reading
     */
    public FdcParser(final String path) throws FdcException {
        // Find name
        final String fdcName = path;
        // Opening stream
        File file;
        try {
            file = new File(new URI(fdcName));
        } catch (final Exception e1) {
            file = new File(fdcName);
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            throw new FdcException("The file " + fdcName
                    + " does not exit: intern error into the RTRT process.");
        }
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        // Reading header
        currentInfo = new BufferedReader(inputStreamReader);
        tagTree = new Stack<FdcTag>();
        lineIdentities = new Stack<CoverageLine>();
        sourceCode = new String();
        newLine = false;

        readHeader();

        init = true;
        toPop = new LinkedList<String>();
        isPopupAlt = -1;
        popupTuple = false;
        lastLineIdentities = new Stack<Integer>();
        currentLastLineIdentities = 0;

    }

    /**
     * Read the header of the .fdc file and check some header violations.
     *
     * @throws FdcException
     *             if errors during reading
     */
    private void readHeader() throws FdcException {
        boolean goodPattern = false;

        // Find the beginning of the first token @RIK
        int nextChar;
        while ((nextChar = getNextChar()) != -1) {
            if (nextChar == '@') {
                goodPattern = true;
                break;
            }
        }

        if (!goodPattern) {
            throw new FdcException("The file has no static coverage report.");
        }
    }

    /**
     * Read the header of the .fdc file and check some header violations.
     *
     * @return if another header into file.
     * @throws FdcException
     *             if errors during reading
     */
    private boolean nextHeader() throws FdcException {
        boolean goodPattern = false;

        // Find the beginning of the first token @RIK
        int nextChar;
        while ((nextChar = getNextChar()) != -1) {
            if (nextChar == '@') {
                goodPattern = true;
                break;
            }
        }
        return goodPattern;
    }

    /*---------------------------------------------
     * ACCESSORS
     *--------------------------------------------*/

    /**
     * A line must be painted iff not a comment line or an empty string.
     *
     * @return true if must be painted
     */
    public final boolean mustBePainted() {

        String mayEmptyString = sourceCode.replaceAll(" ", "");
        mayEmptyString = mayEmptyString.replaceAll("\r", "");
        mayEmptyString = mayEmptyString.replaceAll("\n", "");
        mayEmptyString = mayEmptyString.replaceAll("\t", "");

        // Un commentaire commence par // ou /* ou * ou #
        // La liste n'est pas exhaustive et peut �tre redondante avec le test du
        // tag.COMMENT. Pour éviter de considérer les commentaires dans les
        // sources.
        final boolean isCommentsLine = mayEmptyString.startsWith("//")
                || (mayEmptyString.startsWith("/*") && mayEmptyString
                        .endsWith("*/")) || mayEmptyString.startsWith("#")
                        || mayEmptyString.startsWith("*");

        if (tagTree.peek() == FdcTag.COMMENT && isCommentsLine) {
            return false;
        }

        final int outside = 3;
        // Commentaires en début et en fin de fichier avant les corps de
        // fonctions.
        if (tagTree.peek() == FdcTag.COMMENT && tagTree.size() < outside
                && !mayEmptyString.endsWith("*/")) {
            return false;
        }

        // Commentaires dans corps de fonctions sur plusieurs lignes
        if ((toPop.isEmpty()) && tagTree.peek() == FdcTag.COMMENT) {
            return false;
        }

        return (!mayEmptyString.equals("") && bePainted);
    }

    /**
     * Obtain the current line of the source code to paint.
     *
     * @return the current line into the static coverage file
     */
    public final String getSourceCode() {
        return sourceCode;
    }

    /**
     * Give an array of the identity of the lines.
     *
     * @return the array
     */
    public final CoverageLine[] getCoverageTypes() {
        final List<CoverageLine> toSend = new ArrayList<CoverageLine>();
        for (final CoverageLine line : lineIdentities) {
            if (!line.getType().equals("BD")) {
                toSend.add(line);
            }
        }
        return toSend.toArray(new CoverageLine[toSend.size()]);
    }

    /**
     * Give the size of the array for the coverage types of the current line.
     *
     * @return the array
     */
    public final int getNumberOfCoverageType() {
        final List<CoverageLine> toSend = new ArrayList<CoverageLine>();
        for (final CoverageLine line : lineIdentities) {
            if (!line.getType().equals("BD")) {
                toSend.add(line);
            }
        }
        return toSend.size();
    }

    /**
     * Source code is into a decision block ?
     *
     * @return <code>true</code>: line must be painted as conditional.
     */
    public final boolean hasDecision() {
        return tagTree.contains(FdcTag.DECISION);
    }

    /**
     * New decision block?
     *
     * @return <code>true</code>if a new decision block is read.
     */
    public final boolean hasNewDecision() {
        return hasNewDecision;
    }

    /**
     * New branch block?
     *
     * @return <code>true</code>if a new branch block is read.
     */
    public final boolean hasNewBranch() {
        return hasNewBranch;
    }

    /**
     * End of a decision block?
     *
     * @return <code>true</code> if a block is ended.
     */
    public final int hasRemoveDecision() {
        return hasRemoveDecision;
    }

    /**
     * Determine if the current line is first line of the current method.
     *
     * @return <code>true</code> if params of method
     */
    public final boolean isFirstLineOfMethod() {
        final int size = 3;
        return (tagTree.size() == size && tagTree.contains(FdcTag.RIK)
                && tagTree.contains(FdcTag.NODE) && tagTree
                .contains(FdcTag.BRANCH));
    }

    /**
     * Head of lines: only the mark of the current lines without parents.
     *
     * @return head of lines.
     */
    public final CoverageLine[] getHeadCoverageTypes() {
        final List<CoverageLine> toSend = new ArrayList<CoverageLine>();
        final int size = lineIdentities.size() - 1;
        if (!lastLineIdentities.empty()) {
            for (int c = 0; c < lastLineIdentities.peek(); ++c) {
                final CoverageLine line = lineIdentities.get(size - c);
                if (!line.getType().equals("BD")) {
                    toSend.add(line);
                }
            }
        }
        return toSend.toArray(new CoverageLine[toSend.size()]);
    }

    /*---------------------------------------------
     * HAS NEXT and associate methods
     *--------------------------------------------*/

    /**
     * Is there another source code line? Precondition: we are at the beggining
     * of a new line.
     *
     * @return true if exists
     * @throws FdcException
     * @throws FdcException
     *             if error reading
     */
    public final boolean hasNext() throws FdcException {
        // Reset source code
        // Determine ending of token or beginning of new ones.

        sourceCode = "";
        newLine = false;
        isPopupAlt = -1;
        bePainted = true;
        popupTuple = false;
        hasNewDecision = false;
        hasNewBranch = false;
        hasRemoveDecision = 0;

        currentLastLineIdentities = 0;

        // Special case of initialization: pointer is on @RIK token
        if (init) {
            // Must be here as can add piece of source code.
            readConsecutiveTokens();
            init = false;
        } else {
            // Remove previous tags of the previous source code if exists.
            removeOldToken();

            // Must be the end of the file
            if (tagTree.isEmpty()) {
                if (nextHeader()) {
                    init = true;
                    return hasNext();
                } else {
                    try {
                        currentInfo.close();
                    } catch (final IOException e) {
                        throw new FdcException("Fdc file cannot be closed");
                    }
                    return false;
                }
            }

            // otherwise, read some source code at the beginning of the line
            addSourceCode();

            // and it no new line, determine the first token.
            if (!newLine) {
                readConsecutiveTokens();
            }
        }

        return true;
    }

    /**
     * Remove the token associated with the previous source code line.
     *
     * @throws FdcException
     *             if bad input
     */
    private void removeOldToken() throws FdcException {

        // Comments at end of line not closed.
        if (tagTree.peek() == FdcTag.COMMENT && !toPop.isEmpty()
                && !toPop.getFirst().equals(FdcTag.COMMENT.name())) {
            bePainted = false;
            return;
        }
        // Future comment in next line
        if (tagTree.peek() == FdcTag.COMMENT && toPop.isEmpty()) {
            bePainted = false;
            return;
        }

        bePainted = true;
        int poped = 0;
        if (toPop.size() > 0) {
            String token;
            while ((token = toPop.pollFirst()) != null) {
                if (isNotSkippedToken(token)) {
                    if (tagTree.peek() != FdcTag.valueOf(token)) {
                        throw new FdcException(
                                "Bad format of the file. Bad nested tokens: "
                                        + token + " is different of: "
                                        + tagTree.peek());
                    }

                    if (token.equals("DECISION")) {
                        hasRemoveDecision++;
                    }
                    tagTree.pop();
                    if (hasACoverageLine(token)) {
                        lineIdentities.pop();
                        poped++;
                    }
                }
            }
        }
        // Update the pointer on current branches tag.
        if (!lastLineIdentities.empty()) {
            int value = lastLineIdentities.peek();
            // Some branches are not yet removed.
            if (value > poped) {
                lastLineIdentities.pop();
                lastLineIdentities.push(value - poped);
            } else {
                // Pop branches of the current lines and so on.
                while (!lastLineIdentities.empty()
                        && lastLineIdentities.peek() <= poped) {
                    poped -= lastLineIdentities.peek();
                    lastLineIdentities.pop();
                }
                // if root is reached, update value.
                if (!lastLineIdentities.empty()) {
                    value = lastLineIdentities.peek();
                    lastLineIdentities.pop();
                    lastLineIdentities.push(value - poped);
                }
            }
        }
    }

    /**
     * Allow to read consecutive tokens.
     *
     * @throws FdcException
     *             if bad format
     */
    private void readConsecutiveTokens() throws FdcException {
        int nextChar = -1;

        // Let's suppose we are on a @ character.
        // First thing to know: it's the beginning of a new token,
        // or it's the end of the current token.
        // Second Char allow us to know.
        nextChar = getNextChar();

        if (nextChar == -1) {
            throw new FdcException(
                    "Bad token template: a token begins without ending.");
        }

        if (nextChar == '/') {
            readEndingToken();
        } else if (nextChar == '-') {
            if (isPopupAlt < 0) {
                throw new FdcException("Error : no master popup block !");
            }
            readEndPopupBlock(nextChar);
        } else {
            if (nextChar != '@') {
                readBeginToken(nextChar);
            }
        }
    }

    /**
     * Read the ending token @/*@ format.
     *
     * @throws FdcException
     *             if bad format
     */
    private void readEndingToken() throws FdcException {
        int nextChar = getNextChar();
        String token = "";
        while (nextChar != '@') {
            if (nextChar == -1 && tagTree.size() > 1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            }
            token += asciiToChar(nextChar);
            nextChar = getNextChar();
        }

        // Special case for ending
        if (token.equals("NODE /RIK")) {
            token = "RIK";
        }

        // Add token to the list of current tokens. (popped at the end of the
        // line).

        // Special case as comments at end of line.
        // By default, COMMENT is always push at the beginning of the queue.
        if (token.equals("COMMENT")) {
            toPop.offerFirst(token);
        } else {
            toPop.offerLast(token);
        }

        // Determine if end of line syntax tuple('\r','\n') or
        // end of line included in source code.
        // The second must be included into the report.
        nextChar = detectNewLineInSourceCode();

        if (nextChar == '@') {
            readConsecutiveTokens();
        } else {
            sourceCode += asciiToChar(nextChar);
        }
    }

    /**
     * Remove the ending character of the line. Can be only '\n' if in source
     * code file Or '\r\n' if added by the instrumented code at the end of a
     * token. Fix the value of the private flag newLine.
     *
     * @return the next character with source code (or static coverage) meaning
     * @throws FdcException
     *             if bad input
     */
    private int detectNewLineInSourceCode() throws FdcException {
        int nextChar = getNextChar();
        newLine = false;
        if (nextChar == '\n') {
            newLine = true;
            return nextChar;
        }
        if (nextChar == '\r') {
            nextChar = getNextChar();
        } else {
            return nextChar;
        }
        if (nextChar == '\n') {
            newLine = true;
        }
        return nextChar;
    }

    /**
     * Determine if the token can be ignore into the hierarchy. Must be improved
     * to read data of LINK, SHORTCUT and JUMP node. ALT and POPUP have special
     * functions to be treated.
     *
     * @param token
     *            the current token
     * @return true if the token is essential for coverage
     */
    private boolean isNotSkippedToken(final String token) {
        return (FdcTag.valueOf(token) != FdcTag.ALT
                && FdcTag.valueOf(token) != FdcTag.POPUP
                && FdcTag.valueOf(token) != FdcTag.LINK
                && FdcTag.valueOf(token) != FdcTag.SHORTCUT && FdcTag
                .valueOf(token) != FdcTag.JUMP);
    }

    /**
     * Determine if the current ending token has an associated coverage line.
     *
     * @param token
     *            the ending token
     * @return true if as a coverage line
     */
    private boolean hasACoverageLine(final String token) {
        return (FdcTag.valueOf(token) != FdcTag.NODE
                && FdcTag.valueOf(token) != FdcTag.POPUP
                && FdcTag.valueOf(token) != FdcTag.JUMP
                && FdcTag.valueOf(token) != FdcTag.DECISION
                && FdcTag.valueOf(token) != FdcTag.COMMENT && FdcTag
                .valueOf(token) != FdcTag.RIK);
    }

    /**
     * Read begin token @*@ format.
     *
     * @param firstChar
     *            the first char of the token
     * @throws FdcException
     *             if bad format
     */
    private void readBeginToken(final int firstChar) throws FdcException {
        int nextChar = getNextChar();
        String token = asciiToChar(firstChar);
        String mark = "";
        String id = "";
        char whichToken = 0;
        // Parse the tag to extract information
        // Data is useful for BRANCH node.
        // Pattern : TOKEN MARK ID => allow to detect coverage for the current
        // line.
        while (nextChar != '@') {
            if (notSeparator(nextChar)) {
                switch (whichToken) {
                case 0:
                    token += asciiToChar(nextChar);
                    break;
                case 1:
                    mark += asciiToChar(nextChar);
                    break;
                case 2:
                    id += asciiToChar(nextChar);
                    break;
                default:
                    break;
                }
            } else {
                whichToken++;
            }
            nextChar = getNextChar();
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            }
        }

        // Special case for the POPUP, ALT nodes.
        if (token.equals("POPUP") || token.equals("ALT")) {
            isPopupAlt++;
        }

        if (isNotSkippedToken(token)) {
            tagTree.push(FdcTag.valueOf(token));
        }
        if (token.equals("DECISION")) {
            hasNewDecision = true;
        }

        if (token.equals("BRANCH")) {
            hasNewBranch = true;
        }

        // Pertinent arguments of the token are 3 in our case.
        // Must have dummy information if number of arguments is less.
        addMark(whichToken, token, mark, id);

        nextChar = getNextChar();
        if (nextChar == '@') {
            // Direct @ after a @POPUP@ symbol
            // Special treatment
            if (isPopupAlt >= 0) { // must be ==
                popupTuple = true;
                return;
            }
            readConsecutiveTokens();
        } else {
            sourceCode += asciiToChar(nextChar);
            if (isPopupAlt >= 0) { // must be ==
                conditionValue = asciiToChar(nextChar);
            }
        }
    }

    /**
     * Add the coverageLine to the current code line according to pertinent
     * tokens.
     *
     * @param whichToken
     *            Must be 3 for pertinent tokens or 1 if dummy.
     * @param token
     *            the name of the token (used for error message)
     * @param mark
     *            the mark in {TP,TB,TA,TE}
     * @param id
     *            the id
     * @throws FdcException
     *             if some errors during reading
     * @see CoverageLine
     */
    private void addMark(final char whichToken, final String token,
            final String mark, final String id) throws FdcException {
        final int argToken = 3;
        // Add coverage information for future painting.
        if (whichToken == argToken) {
            final CoverageLine cl = new CoverageLine();
            final String[] type = mark.split("=");
            if (!type[0].equals("MARK")) {
                throw new FdcException("Bad (order) arguments for the token "
                        + token + ". Must be MARK first.");
            }
            cl.setType(type[1]);
            final String[] number = id.split("=");
            if (!number[0].equals("ID")) {
                throw new FdcException("Bad (order) arguments for the token "
                        + token + ". Must be ID second.");
            }
            cl.setIntNumber(number[1]);
            lineIdentities.push(cl);
            currentLastLineIdentities++;
        }
        if (token.equals("BRANCH") && whichToken == 1) {
            // dummy coverage
            final CoverageLine cl = new CoverageLine();
            cl.setType("BD");
            cl.setIntNumber("-1");
            lineIdentities.push(cl);
            currentLastLineIdentities++;
        }
    }

    /**
     * Specification of the separators into the .fdc file.
     *
     * @param nextChar
     *            the character to analyze
     * @return true if not a separator
     */
    private boolean notSeparator(final int nextChar) {
        // SP (32), CR (13), NL (10)
        return (nextChar != ' ' && nextChar != '\r' && nextChar != '\n');
    }

    /*---------------------------------------------
     * NEXT
     *--------------------------------------------*/

    /**
     * Go through file to determine the next piece of source code to paint.
     *
     * @throws FdcException
     *             if bad format of the file.
     */
    public final void next() throws FdcException {
        // Suppose that the source code line is already initialized
        // And first token is read.

        // Now read the next part of the source code line
        // if not already reached in the hasNext method
        // Do not read if in the case @POPUP@@...
        if (!popupTuple) {
            nextSourceCode();
        }

        // if not at the end of the line
        while (!newLine) {
            // Must be have here a POPUP ALT token block
            if (isPopupAlt >= 0) { // must be ==

                final int lastPopupAlt = isPopupAlt;
                isPopupAlt = 0;
                // Could be a @ALT@ master block or a @POPUP@ master block.
                if (readPopupBlock(lastPopupAlt)) {
                    isPopupAlt = -1;
                } else {
                    isPopupAlt = lastPopupAlt;
                }
                // @POPUP block was treated
                popupTuple = false;
                // must not be the end of the line!
                if (newLine) {
                    break;
                }
            }
            // Can be then new tokens (beginning tokens or ending tokens)
            readConsecutiveTokens();

            // Read next source code in the line if no ending token
            // detected in the previous call.
            // Do not read if in the case @POPUP@@...
            if (!popupTuple) {
                nextSourceCode();
            }
        }
        // Save current branck mark for the next lines.
        if (currentLastLineIdentities > 0) {
            lastLineIdentities.push(currentLastLineIdentities);
        }
    }

    /**
     * Read the next part of the source code.
     *
     * @throws FdcException
     *             if bad input
     */
    private void nextSourceCode() throws FdcException {
        if (!newLine) {
            if (isPopupAlt >= 0) { // must be ==
                addSourceCodeAndConditionValue();
            } else {
                addSourceCode();
            }
        }
    }

    /**
     * Add the source code line to the current source code line.
     *
     * @throws FdcException
     *             if bad input
     */
    private void addSourceCode() throws FdcException {
        int nextChar = -1;
        while (true) {
            // detect new line
            nextChar = detectNewLineInSourceCode();
            if (newLine) {
                break;
            }
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            } else if (nextChar == '@') {
                break;
            }
            sourceCode += asciiToChar(nextChar);
        }
    }

    /**
     * Adding code source, including new lines. Used only into @POPUP@ blocks.
     *
     * @throws FdcException
     *             if error during parsing
     */
    private void addSourceCodeWithNewLine() throws FdcException {
        int nextChar = -1;
        while (true) {
            // detect new line
            nextChar = getNextChar();
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            } else if (nextChar == '@') {
                break;
            }
            sourceCode += asciiToChar(nextChar);
        }
    }

    /**
     * Add the source code line to the current source code line. Update the
     * condition value for conditional report. Must be call only into popup-alt
     * block.
     *
     * @throws FdcException
     *             if bad input
     */
    private void addSourceCodeAndConditionValue() throws FdcException {
        int nextChar = -1;
        while (true) {
            // detect new line
            nextChar = detectNewLineInSourceCode();
            // no break
            // if (newLine) {
            // break;
            // }
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            } else if (nextChar == '@') {
                break;
            }
            sourceCode += asciiToChar(nextChar);
            conditionValue += asciiToChar(nextChar);
        }
    }

    /*-----------------------------------------------------------------------------------------------------------
     * @POPUP@..[@POPUP@..[@ALT@..@-ALT@]..@-POPUP@..@/POPUP@[@/ALT@]] @-POPUP@..@/POPUP@ block special treatment
     * ---------------------------------------------------------------------------------------------------------*/

    /**
     * Allows the reading of master .@POPUP@. block if exists.
     *
     * @param lastPopupAlt
     *            previous value
     * @return reset block @POPUP
     * @throws FdcException
     *             if bad input
     */
    private boolean readPopupBlock(final int lastPopupAlt) throws FdcException {
        int nextChar = -1;
        boolean toReset = true; // ending of block.
        int currentPopupAlt = lastPopupAlt;

        nextChar = getNextChar();
        nextChar = readHypotheticalTags(nextChar);

        // No more popup block or ending of the master.
        if (nextChar != 'P' && nextChar != '-') {
            // If @ALT block do not add an inside @POPUP block
            // Or if not already include inside a master @POPUP block
            if (readPopupAltInformation(nextChar, true) && lastPopupAlt <= 0) {
                return true;
            }
            nextChar = getNextChar();
            currentPopupAlt++;
            toReset = false;
        }
        // A master POPUP block exists:
        // Multiple conditions are required.
        while (nextChar == 'P' || nextChar == 'A') {
            final boolean popupBlock = (nextChar == 'P');
            // new @POPUP@ block !
            while (nextChar != '@') {
                nextChar = getNextChar();
            }
            conditionValue = "";
            addSourceCodeAndConditionValue();
            nextChar = getNextChar();
            nextChar = readHypotheticalTags(nextChar);
            // End of the popup block ?
            if (!readPopupAltInformation(nextChar, false) && popupBlock) {
                return true;
            }
            // @ALT case with a new POPUP to treat till the end. @ALT @-ALT
            // @POPUP @/ALT @-POPUP @/POPUP
            // not be considered for the usual case @POPUP master case
            if (!popupBlock) {
                currentPopupAlt++;
            }
            nextChar = getNextChar();
            // Could be a comment here!
            nextChar = skipComment(nextChar);
        }
        if (nextChar != '-') {
            throw new FdcException(
                    "Popup token must be followed by a -popup token.");
        }

        final String tokenToSkip = determineMiddleToken();

        final int updatePopupAlt = extractConditionalTokens(tokenToSkip,
                currentPopupAlt);
        if (currentPopupAlt != updatePopupAlt) {
            currentPopupAlt = updatePopupAlt;
            toReset = false;
        }
        // Remove the coverage info nodes dedicated for the block
        // (no associated ending nodes)
        while (isPopupAlt > 0) {
            toPop.offerLast("BRANCH");
            isPopupAlt--;
        }

        // Go to next token with source code information.
        nextChar = detectNewLineInSourceCode();
        while ((!newLine) && nextChar != '@') {
            sourceCode += asciiToChar(nextChar);
            nextChar = detectNewLineInSourceCode();
        }

        // Reset
        return toReset && (currentPopupAlt == 0);
    }

    /**
     * Extract conditional tokens into @POPUP blocks.
     *
     * @param tokenToSkip
     *            the ending token of the current block.
     * @param currentPopupAlt
     *            the number of current child.
     * @return the update number of current child
     * @throws FdcException
     *             if errors during reading
     */
    private int extractConditionalTokens(final String tokenToSkip,
            final int currentPopupAlt) throws FdcException {
        // Read all the tokens into the @-POPUP@ and @/POPUP@ tokens
        // And keep only the @BRANCH token
        String endToken = "";
        String mark = "";
        String id = "";
        String value = "";
        char whichToken = 0;
        int nextChar;
        int updatePopupAlt = currentPopupAlt;

        boolean firstTag = false;
        while (!tokenToSkip.equals(endToken)) {
            // Pattern : TOKEN MARK ID => allow to detect coverage for the
            // current
            // line.
            nextChar = getNextChar();
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            }
            if (nextChar == '@') {
                // New Token
                firstTag = !firstTag;
                if (firstTag) {
                    if (endToken.equals("BRANCH")
                            && (id.split("&").length == 1)) {
                        value = value.replace("@", "");
                        addConditionalMark(whichToken, endToken, mark, id,
                                value);
                    }
                    endToken = "";
                    mark = "";
                    id = "";
                    whichToken = 0;
                } else {

                    // Add the @BRANCH tag linked to the @POPUP token.
                    // Warning: into master popup block:
                    // take into account only id without multiple values.
                    // correct id: ID=6; bad id : ID=6&9
                    if ((endToken.equals("BRANCH"))
                            && (id.split("&").length == 1)) {
                        tagTree.push(FdcTag.valueOf(endToken));
                        isPopupAlt++;
                    }
                    // Special case when into a master @ALT@ block, with a
                    // @POPUP@ block inside!
                    if (endToken.equals("POPUP") && tokenToSkip.equals("/ALT")) {
                        updatePopupAlt++;
                    }
                    value = "";
                    conditionValue = "";
                }
            }

            if (firstTag) {
                // Read the information into the node
                if (notSeparator(nextChar)) {
                    switch (whichToken) {
                    case 0:
                        if (nextChar != '@') {
                            endToken += asciiToChar(nextChar);
                        }
                        break;
                    case 1:
                        mark += asciiToChar(nextChar);
                        break;
                    case 2:
                        id += asciiToChar(nextChar);
                        break;
                    default:
                        break;
                    }
                } else {
                    whichToken++;
                }
            } else {
                if (endToken.equals("BRANCH") && (id.split("&").length == 1)) {
                    value += asciiToChar(nextChar);
                }
            }
        }
        // Remove the ending @ of the token
        nextChar = getNextChar();

        return updatePopupAlt;
    }

    /**
     * Skip a comment into a @popup block.
     *
     * @param currentChar
     *            the current character
     * @return the last character read
     * @throws FdcException
     *             if error during reading
     */
    private int skipComment(final int currentChar) throws FdcException {
        int nextChar = currentChar;
        if (nextChar == 'C') {
            // @COMMENT@
            while (nextChar != '@') {
                nextChar = getNextChar();
            }
            addSourceCode();
            // @/COMMENT@
            nextChar = getNextChar();
            while (nextChar != '@') {
                nextChar = getNextChar();
            }
            addSourceCodeWithNewLine();
            nextChar = getNextChar();
        }
        return nextChar;
    }

    /**
     * Read a hypothetical @BRANCH[@JUMP] block into a @POPUP block.
     *
     * @param currentValue
     *            the current character to test
     * @return current character
     * @throws FdcException
     *             if parsing error
     */
    private int readHypotheticalTags(final int currentValue)
            throws FdcException {
        int nextChar = currentValue;
        // Branch case
        if (nextChar == 'B') {

            // condition reading
            String token = asciiToChar(nextChar);
            String mark = "";
            String id = "";
            char whichToken = 0;
            // Parse the tag to extract information
            // Data is useful for BRANCH node.
            // Pattern : TOKEN MARK ID => allow to detect coverage for the
            // current
            // line.
            while (nextChar != '@') {
                if (notSeparator(nextChar)) {
                    switch (whichToken) {
                    case 0:
                        token += asciiToChar(nextChar);
                        break;
                    case 1:
                        mark += asciiToChar(nextChar);
                        break;
                    case 2:
                        id += asciiToChar(nextChar);
                        break;
                    default:
                        break;
                    }
                } else {
                    whichToken++;
                }
                nextChar = getNextChar();
                if (nextChar == -1) {
                    throw new FdcException(
                            "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
                }
            }

            addMark(whichToken, token, mark, id);
            nextChar = getNextChar(); // @
            nextChar = getNextChar();
            // popup if nested into
            if (nextChar == 'P') {
                while (nextChar != '@') {
                    nextChar = getNextChar();
                }
                addSourceCode();
                readPopupBlock(isPopupAlt);
                if (newLine) {
                    throw new FdcException(
                            "New line cannot be handled by source code.");
                }
                nextChar = getNextChar();
            }
            // jump if exists
            nextChar = readHypotheticalJump(nextChar);
            // End of @BRANCH tag
            nextChar = getNextChar();
            String endToken = "";
            while (nextChar != '@') {
                endToken += asciiToChar(nextChar);
                nextChar = getNextChar();
            }
            if (!endToken.contains("BRANCH")) {
                throw new FdcException(
                        "Bad nested block: @BRANCH is closed by " + endToken);
            }
            // Read source code
            addSourceCodeWithNewLine();
            nextChar = getNextChar();
        } else {
            nextChar = readHypotheticalJump(nextChar);
        }
        return nextChar;
    }

    /**
     * Read a hypothetical @JUMP block into a @POPUP block.
     *
     * @param currentValue
     *            the current character to test
     * @return current character
     * @throws FdcException
     *             if parsing error
     */
    private int readHypotheticalJump(final int currentValue)
            throws FdcException {
        int nextChar = currentValue;
        // Jump case
        if (nextChar == 'J') {
            // Read Jump
            String firstToken = asciiToChar(nextChar);
            while (nextChar != '@') {
                nextChar = getNextChar();
                firstToken += asciiToChar(nextChar);
            }
            firstToken = firstToken.split(" ")[0];
            // Read method name
            addSourceCode();
            // Read next token: on @
            nextChar = getNextChar();
            String endToken = "";
            while (nextChar != '@') {
                nextChar = getNextChar();
                endToken += asciiToChar(nextChar);
            }
            if (!endToken.contains(firstToken)) {
                throw new FdcException("Bad nested block: @JUMP is closed by "
                        + endToken);
            }
            // Read source code
            addSourceCodeWithNewLine();
            nextChar = getNextChar();
            if (nextChar != '-' && nextChar != '/') {
                throw new FdcException(
                        "Bad character: must be - or / instead of "
                                + asciiToChar(nextChar));
            }
        }

        return nextChar;
    }

    /**
     * Allows the reading of master .@POPUP@. block if exists.
     *
     * @param previousChar
     *            must be '-'
     * @throws FdcException
     *             if bad input
     */
    private void readEndPopupBlock(final int previousChar) throws FdcException {
        int nextChar = previousChar;
        isPopupAlt = 0;

        // No more popup block or ending of the master.
        if (nextChar != '-') {
            throw new FdcException(
                    "Popup token must be followed by a -popup token.");
        }

        final String tokenToSkip = determineMiddleToken();

        // Read all the tokens into the @-POPUP@ and @/POPUP@ tokens
        // And keep only the @BRANCH token
        String endToken = "";
        String mark = "";
        String id = "";
        String value = "";
        char whichToken = 0;

        boolean firstTag = false;
        while (!tokenToSkip.equals(endToken)) {
            // Pattern : TOKEN MARK ID => allow to detect coverage for the
            // current
            // line.
            nextChar = getNextChar();
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            }
            if (nextChar == '@') {
                // New Token
                firstTag = !firstTag;
                if (firstTag) {
                    if (endToken.equals("BRANCH")
                            && (id.split("&").length == 1)) {
                        value = value.replace("@", "");
                        addConditionalMark(whichToken, endToken, mark, id,
                                value);
                    }
                    endToken = "";
                    mark = "";
                    id = "";
                    whichToken = 0;
                } else {

                    // Add the @BRANCH tag linked to the @POPUP token.
                    // Warning: into master popup block:
                    // take into account only id without multiple values.
                    // correct id: ID=6; bad id : ID=6&9
                    if ((endToken.equals("BRANCH"))
                            && (id.split("&").length == 1)) {
                        tagTree.push(FdcTag.valueOf(endToken));
                        isPopupAlt++;
                    }
                    value = "";
                    conditionValue = "";
                }
            }

            if (firstTag) {
                // Read the information into the node
                if (notSeparator(nextChar)) {
                    switch (whichToken) {
                    case 0:
                        if (nextChar != '@') {
                            endToken += asciiToChar(nextChar);
                        }
                        break;
                    case 1:
                        mark += asciiToChar(nextChar);
                        break;
                    case 2:
                        id += asciiToChar(nextChar);
                        break;
                    default:
                        break;
                    }
                } else {
                    whichToken++;
                }
            } else {
                if (endToken.equals("BRANCH") && (id.split("&").length == 1)) {
                    value += asciiToChar(nextChar);
                }
            }
        }
        // Remove the ending @ of the token
        nextChar = getNextChar();

        // Remove the coverage info nodes dedicated for the block
        // (no associated ending nodes)
        while (isPopupAlt > 0) {
            toPop.offerLast("BRANCH");
            isPopupAlt--;
        }

        // Reset
        isPopupAlt = -1;
    }

    /**
     * Allows to skip the block.
     * :@POPUP@...[@ALT@...@-ALT@...]@-POPUP@...@\POPUP@[@\ALT@]... But collect
     * coverage markers for the current source code.
     *
     * @param firstChar
     *            the first char of the hypothetical .@ALT@ block
     * @param lastPopupAlt
     *            previous value
     * @return true if inside a master block.
     * @throws FdcException
     *             if bad format
     */
    private boolean readPopupAltInformation(final int firstChar,
            final boolean lastPopupAlt) throws FdcException {
        int nextChar = -1;

        nextChar = readHypotheticalAlt(firstChar);
        if (nextChar != '-') {
            throw new FdcException(
                    "Bad popup-[alt] template blocks. Here must be a @-POPUP@ or @-ALT@ token. Character is "
                            + (char) nextChar
                            + "("
                            + (char) firstChar
                            + ") instead of -. Source code to here is: "
                            + sourceCode + ". ");
        }

        // @-POPUP@ or @-ALT@
        // Remove - and replace by / to match ending token
        final String tokenToSkip = determineMiddleToken();

        // Find the next token with same signature
        // for instance, association of @-ALT@ with @/ALT@
        // or @-POPUP@ with @/POPUP@
        String endToken = "";
        String mark = "";
        String id = "";
        String value = "";
        int endingToken = -1;
        char whichToken = 0;
        boolean toReset = true;

        // Read all the tokens into the @-POPUP@ and @/POPUP@ tokens
        // And keep only the @BRANCH token
        boolean firstTag = false;
        while (!tokenToSkip.equals(endToken)) {

            nextChar = getNextChar();
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            }
            if (nextChar == '@') {
                firstTag = !firstTag;
                if (firstTag) {
                    if (endToken.equals("BRANCH")
                            && (id.split("&").length == 1)) {
                        value = value.replace("@", "");
                        addConditionalMark(whichToken, endToken, mark, id,
                                value);
                    }
                    endToken = "";
                    mark = "";
                    id = "";
                    whichToken = 0;
                } else {

                    // Add the @BRANCH tag linked to the @POPUP token.
                    if (endToken.equals("BRANCH")
                            && (id.split("&").length == 1)) {
                        tagTree.push(FdcTag.valueOf(endToken));
                        isPopupAlt++;
                    }
                    if (endToken.startsWith("-")) {
                        endingToken++;
                    }
                    // Special case when into a master @ALT@ block, with a
                    // @POPUP@ block inside!
                    if (endToken.equals("POPUP") && tokenToSkip.equals("/ALT")) {
                        toReset = false;
                    }
                    value = "";
                }
            }

            if (firstTag) {
                if (notSeparator(nextChar)) {
                    switch (whichToken) {
                    case 0:
                        if (nextChar != '@') {
                            endToken += asciiToChar(nextChar);
                        }
                        break;
                    case 1:
                        mark += asciiToChar(nextChar);
                        break;
                    case 2:
                        id += asciiToChar(nextChar);
                        break;
                    default:
                        break;
                    }
                } else {
                    whichToken++;
                }
            } else {
                if (endToken.equals("BRANCH")) {
                    value += asciiToChar(nextChar);
                }
            }
        }
        // Remove the ending @ of the token
        nextChar = getNextChar();

        // Remove the coverage info nodes dedicated for the block
        // (no associated ending nodes)
        while (isPopupAlt > 0) {
            toPop.offerLast("BRANCH");
            isPopupAlt--;
        }

        if (lastPopupAlt || (toReset && endingToken > 0)) {
            nextChar = detectNewLineInSourceCode();
            while ((!newLine) && nextChar != '@') {
                sourceCode += asciiToChar(nextChar);
                nextChar = detectNewLineInSourceCode();
            }
        } else {
            nextChar = getNextChar();
            while (nextChar != '@') {
                sourceCode += asciiToChar(nextChar);
                nextChar = getNextChar();
            }
        }
        // Go to next token with source code information.
        // DO NOT TAKE INTO ACCOUNT NEW LINE HERE
        // otherwise invariant is violated!
        if (toReset) {
            return (endingToken <= 0);
        }
        return false;
    }

    /**
     * Detect which first middle token exists into the block .@POPUP.[.@ALT.].
     *
     * @return a token for matching with ending matching : /POPUP or /ALT
     * @throws FdcException
     *             if some bad input
     */
    private String determineMiddleToken() throws FdcException {
        String tokenToSkip = "/";
        int nextChar;
        while (true) {
            nextChar = getNextChar();
            if (nextChar == -1) {
                throw new FdcException(
                        "Bad ending of the file: the file is ended in the middle of source code. Is the file corrupted?");
            } else if (nextChar == '@') {
                break;
            }
            tokenToSkip += asciiToChar(nextChar);
        }
        return tokenToSkip;
    }

    /**
     * Read an hypothetical .@ATL@. token into the block .@POPUP@.
     *
     * @param firstChar
     *            first char of the plausible token
     * @return the next good character
     * @throws FdcException
     *             if bad input
     */
    private int readHypotheticalAlt(final int firstChar) throws FdcException {
        int nextChar = firstChar;
        if (nextChar != '-') {
            // @ALT
            while (nextChar != '@') {
                nextChar = getNextChar();
            }
            // Here we are on the first character to add to sourceCode
            // No more things to do.
            nextChar = getNextChar();
            while (nextChar != '@') {
                sourceCode += asciiToChar(nextChar);
                conditionValue += asciiToChar(nextChar);
                nextChar = getNextChar();
            }
            // Here we move to next character. Must be "-"
            nextChar = getNextChar();
        }
        return nextChar;
    }

    /**
     * Add the coverageLine to the current code line according to pertinent
     * tokens.
     *
     * @param whichToken
     *            Must be 3 for pertinent tokens
     * @param token
     *            the name of the token (used for error message)
     * @param mark
     *            the mark. Must be TE or TB or TA
     * @param id
     *            the id
     * @param value
     *            the value of the condition
     * @throws FdcException
     *             if some errors during reading
     * @see CoverageLine
     */
    private void addConditionalMark(final char whichToken, final String token,
            final String mark, final String id, final String value)
                    throws FdcException {
        final int argToken = 3;
        // Pattern: MARK TOKEN ID
        if (whichToken == argToken) {
            final CoverageLine cl = new CoverageLine();
            final String[] type = mark.split("=");
            if (!type[0].equals("MARK")) {
                throw new FdcException("Bad (order) arguments for the token "
                        + token + ". Must be MARK first.");
            }
            cl.setType(type[1]);
            if (!type[1].equals("TE") && !type[1].equals("TB")
                    && !type[1].equals("TA")) {
                throw new FdcException(
                        "Must be a conditional marker (TE or TB) and not "
                                + type[1]);
            }
            final String[] number = id.split("=");
            if (!number[0].equals("ID")) {
                throw new FdcException("Bad (order) arguments for the token "
                        + token + ". Must be ID second.");
            }
            cl.setIntNumber(number[1]);
            cl.setValue(conditionValue + "(" + value + ")");
            lineIdentities.push(cl);
            currentLastLineIdentities++;
        }
    }

    /*---------------------------------------------
     * GENERIC METHODS on ascii reading
     *--------------------------------------------*/

    /**
     * Obtain the next char into the file.
     *
     * @return an integer that can be considered as a char.
     * @throws FdcException
     *             is some error during reading
     */
    private int getNextChar() throws FdcException {
        int nextChar = -1;
        try {
            nextChar = currentInfo.read();
        } catch (final IOException e) {
            throw new FdcException("Bad reading of the file.");
        }

        return nextChar;
    }

    /**
     * ASCII to char conversion.
     *
     * @param value
     *            the ascii value
     * @return its associated char
     */
    private String asciiToChar(final int value) {
        return new Character((char) value).toString();
    }
}
