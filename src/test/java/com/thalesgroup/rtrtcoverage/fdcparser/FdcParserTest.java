package com.thalesgroup.rtrtcoverage.fdcparser;

import junit.framework.Assert;

import org.junit.Test;

public class FdcParserTest {

    @Test
    public void testFdcParser1() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/STRINGUTILITIES_STATIC_COPY.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertNotNull(parser);

        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* (C) Copyright 2005 by THALES Avionics                                                   */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* All rights reserved                                                                     */",
                false, 0);
        checkExtractedLine(
                parser,
                "/*                                                                                         */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* This program is the property of THALES Avionics, LE HAILLAN-BORDEAUX FRANCE, and can    */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* only be used and copied with the prior written authorisation of THALES Avionics.        */",
                false, 0);
        checkExtractedLine(
                parser,
                "/*                                                                                         */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* Any whole or partial copy of this program in either its original form or in a modified  */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* form must mention this copyright and its owner.                                         */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* PROJECT: PDS                                                                            */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* FILE NAME: StringUtilities_static_copy.c                                                */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "#include \"StringUtilities.h\"", false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "/* Start of user includes */", false, 0);
        checkExtractedLine(parser, "/* End of user includes */", false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* METHOD DESCRIPTION: [public][class method]                                              */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* -------------------                                                                     */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* ISO_9898_ANSI_C - like strcpy                                                           */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* The copy operation copies the string pointed to by pSrc (including the terminating null */",
                false, 0);
        checkExtractedLine(
                parser,
                "/*  character) into the array pointed to by pDest. If copying takes place between objects  */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* that overlap, the behavior is undefined.                                                */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "void StringUtilities_static_copy(T_char *pDestination, const T_char *pSource)",
                true, 1);
        checkExtractedLine(parser, "{", true, 1);
        checkExtractedLine(parser, "     /* Start of user code */", false, 1);
        checkExtractedLine(parser, "     while ((*pSource) != 0x00)", true, 3);
        checkExtractedLine(parser, "     {", true, 2);
        checkExtractedLine(parser, "         (*pDestination) = (*pSource);",
                true, 2);
        checkExtractedLine(parser, "\n", false, 2);
        checkExtractedLine(parser, "         pSource++;", true, 2);
        checkExtractedLine(parser, "         pDestination++;", true, 2);
        checkExtractedLine(parser, "     }", true, 2);
        checkExtractedLine(parser, "     (*pDestination) = 0x00;\n", true, 1);
        checkExtractedLine(parser, "     /* End of user code */", false, 1);
        checkExtractedLine(parser, "     return;", true, 2);
        checkExtractedLine(parser, "}", true, 1);
        checkExtractedLine(parser, "\n", false, 0);

        Assert.assertFalse(parser.hasNext());

    }

    @Test
    public void testFdcParser2() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/SAMEMED_INIT.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
            // System.out.println("###COVERAGE###");
            // CoverageLine[] cover = parser.getCoverageTypes();
            // for(int c=0; c<parser.getNumberOfCoverageType(); ++c ) {
            // System.out.println(cover[c].getType()+","+cover[c].getNumber());
            // if (cover[c].isConditional())
            // {
            // System.out.println(cover[c].getValue());
            // }
            // }

            // System.out.println("Source: " + parser.getSourceCode());
            // System.out.println("Source must be painted: " +
            // parser.mustBePainted());
            // System.out.println("Source as hits: " +
            // parser.getNumberOfCoverageType());
        }
    }

    @Test
    public void testFdcParser3() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/CHARACTERUTILITIES_STATIC_ISSPACE.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser4() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/LAEXSEQ_MAIN.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser5() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/LAKBSW_A661_SET_KEY_STATE.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * Comments not closed on several lines.
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser6() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/LAME_UPDATE_CURSOR_REFERENCE.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * Comments not closed on several lines. But with text first
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser6b() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/LAME_UPDATE_SLEW_PRP.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser7() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/LBFL_INIT.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser8() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/LBFL_MAIN.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser9() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/LBFL_MNG.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * Commentaires en début de ligne !
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser10() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/LBSTR_ADD.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
            if (parser.mustBePainted()) {
                break;
            }
        }

        checkExtractedLine(
                parser,
                "        /* OUT */        T_char         *par_destination_address,",
                true, 0);
        checkExtractedLine(parser,
                "        /* IN  */        T_char         *par_origin_address)",
                true, 0);
        checkExtractedLine(parser, "{", true, 1);
        checkExtractedLine(
                parser,
                "        /*$ DO WHILE THE END OF THE ORIGIN MEMORY AREA NOT REACHED */",
                false, 1);
        checkExtractedLine(parser, "        while (*par_origin_address != 0)",
                true, 6);
        checkExtractedLine(parser, "        {", true, 2);
        checkExtractedLine(
                parser,
                "                /*$ COPY A BYTE FROM THE ORIGIN MEMORY INTO THE DESTINATION MEMORY */",
                false, 2);
        checkExtractedLine(
                parser,
                "                (*par_destination_address++) = (*par_origin_address++);",
                true, 2);
        checkExtractedLine(parser, "", false, 2);
        checkExtractedLine(parser, "        }", true, 2);
        checkExtractedLine(parser, "        /*$ ENDO WHILE */", false, 1);
        checkExtractedLine(parser, "", false, 1);
        checkExtractedLine(parser, "        *par_destination_address = 0;",
                true, 1);
        checkExtractedLine(parser, "", false, 1);
        checkExtractedLine(parser, "/*$ RETURN */", false, 1);
        checkExtractedLine(parser,
                "           return par_destination_address;", true, 1);
        checkExtractedLine(parser, "}", true, 1);
        checkExtractedLine(parser, "\n", false, 0);

        Assert.assertFalse(parser.hasNext());

    }

    /**
     * @ALT@ block without @POPUP@
     * @throws Exception
     */
    @Test
    public void testFdcParser11() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/LBTRACE_DEBUG_WRITE.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser12() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SACO_GET_CDS_SYSTEM_IDENTIFICATION.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser13() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SACO_RECEIVE_DATA.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * :@POPUP@BRANCH@JUMP in master block.
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser14() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SACR_READ_CAN_STATUS.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * Commentaires en fin de ligne
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser15() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SAEXPINI_QPORT_CREATE.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Assert.assertNotNull(parser);

        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "typedef struct", true, 0);
        checkExtractedLine(parser, "{", true, 0);
        checkExtractedLine(parser,
                "    const T_uint32  ul_sport_nb;    /* number of ports */",
                true, 0);
        checkExtractedLine(
                parser,
                "   const T_qport   *pr_qport_list; /* ports list caracteristics */",
                true, 0);
        checkExtractedLine(
                parser,
                "}T_qport_car;                       /* caracteristics of specific ports of a DU */",
                true, 0);
        checkExtractedLine(parser, "\n", false, 0);
        Assert.assertFalse(parser.hasNext());

    }

    /**
     * :@POPUP@BRANCH@JUMP in nested @POPUP block
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser16() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/SAIDSAEB_INIT.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * @POPUP@BRANCH@POPUP@JUMP block
     * @throws Exception
     */
    @Test
    public void testFdcParser17() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SAMC_DATA_INIT.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * :@COMMENT into @POPUP
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser18() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SAMEDCP_DCP_GETREQFMT.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    /**
     * :@COMMENT into @POPUP
     *
     * @throws Exception
     */
    @Test
    public void testFdcParser19() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SAMEKCCU_CHECK_HEALTH_REFRESH.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser20() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/SATM_RECEIVE.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        while (parser.hasNext()) {
            parser.next();
        }
    }

    @Test
    public void testFdcParser21() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/SAME_SEND_MEDIA_GND_CONFIG.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        int cpt = 0;
        int blockDecision = 0;
        while (parser.hasNext()) {
            parser.next();
            if (parser.hasDecision()) {
                cpt++;
                if (blockDecision == 0) {
                    Assert.assertTrue(parser.hasNewDecision());
                    blockDecision++;
                }
            } else if (blockDecision == 1) {
                Assert.assertTrue(parser.hasRemoveDecision() > 0);
                blockDecision++;
            }
        }
        Assert.assertTrue(cpt == 74);
    }

    @Test
    public void testFdcParserMultiplePopupBlock() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass()
                .getResource("ut/STRINGUTILITIES_STATIC_EQUALS.FDC").toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Assert.assertNotNull(parser);

        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* (C) Copyright 2005 by THALES Avionics                                                   */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* All rights reserved                                                                     */",
                false, 0);
        checkExtractedLine(
                parser,
                "/*                                                                                         */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* This program is the property of THALES Avionics, LE HAILLAN-BORDEAUX FRANCE, and can    */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* only be used and copied with the prior written authorisation of THALES Avionics.        */",
                false, 0);
        checkExtractedLine(
                parser,
                "/*                                                                                         */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* Any whole or partial copy of this program in either its original form or in a modified  */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* form must mention this copyright and its owner.                                         */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* PROJECT: PDS                                                                            */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* FILE NAME: StringUtilities_static_equals.c                                              */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "#include \"StringUtilities.h\"", false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "/* Start of user includes */", false, 0);
        checkExtractedLine(parser, "/* End of user includes */", false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(parser, "\n", false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* METHOD DESCRIPTION: [public][class method]                                              */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* -------------------                                                                     */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* This method returns true if the two specified string are equal, otherwise false.        */",
                false, 0);
        checkExtractedLine(
                parser,
                "/* *************************************************************************************** */",
                false, 0);
        checkExtractedLine(
                parser,
                "T_boolean StringUtilities_static_equals(const T_char *pStr1, const T_char *pStr2)",
                true, 1);
        checkExtractedLine(parser, "{", true, 1);
        checkExtractedLine(parser, "     /* Start of user code */", false, 1);
        checkExtractedLine(parser, "     T_uint16 lIndex = 0;", true, 1);
        checkExtractedLine(parser, "     T_boolean lReturn = K_BOOL_TRUE;",
                true, 1);
        checkExtractedLine(parser, "\n", false, 1);
        checkExtractedLine(
                parser,
                "     while ((lReturn == K_BOOL_TRUE) && (pStr1[lIndex] != '\0') && (pStr2[lIndex] != '\0'))",
                true, 13);
        checkExtractedLine(parser, "     {", true, 2);
        checkExtractedLine(parser,
                "        if (pStr1[lIndex] == pStr2[lIndex])", true, 4);
        checkExtractedLine(parser, "        {", true, 3);
        checkExtractedLine(parser, "            lIndex++;", true, 3);
        checkExtractedLine(parser, "        }", true, 3);
        checkExtractedLine(parser, "        else", true, 3);
        checkExtractedLine(parser, "        {", true, 3);
        checkExtractedLine(parser, "            lReturn = K_BOOL_FALSE;", true,
                3);
        checkExtractedLine(parser, "        }", true, 3);
        checkExtractedLine(parser, "     }", true, 2);
        checkExtractedLine(parser, "     \n", false, 1);
        checkExtractedLine(
                parser,
                "if ((lReturn == K_BOOL_TRUE) && (pStr1[lIndex] != pStr2[lIndex]))",
                true, 9);
        checkExtractedLine(parser, "     {", true, 2);
        checkExtractedLine(parser, "        lReturn = K_BOOL_FALSE;", true, 2);
        checkExtractedLine(parser, "     }", true, 2);
        checkExtractedLine(parser, "    else", true, 2);
        checkExtractedLine(parser, "    {", true, 2);
        checkExtractedLine(parser, "        ;/* nothing to do */", true, 2);
        checkExtractedLine(parser, "    }", true, 2);
        checkExtractedLine(parser, "     \n", false, 1);
        checkExtractedLine(parser, "    return lReturn;", true, 2);
        checkExtractedLine(parser, "     /* End of user code */", false, 1);
        checkExtractedLine(parser, "}", true, 1);
        checkExtractedLine(parser, "\n", false, 0);

        Assert.assertFalse(parser.hasNext());

    }

    @Test
    public void testBadInput1() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/BAD_INIT.FDC")
                .toString();

        Exception exception = null;
        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            exception = e;
        } finally {
            Assert.assertNull(parser);
            Assert.assertNotNull(exception);
        }
    }

    @Test
    public void testBadInput2() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/BAD_INIT2.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            Assert.assertNotNull(parser);
        }

        Exception exception = null;
        try {
            while (parser.hasNext()) {
                parser.next();
            }
        } catch (final FdcException e) {
            exception = e;
        } finally {
            Assert.assertNotNull(exception);
        }
    }

    @Test
    public void testBadInput3() throws Exception {
        FdcParser parser = null;

        final String name = this.getClass().getResource("ut/BAD_INIT3.FDC")
                .toString();

        try {
            parser = new FdcParser(name);
        } catch (final FdcException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            Assert.assertNotNull(parser);
        }

        Exception exception = null;
        try {
            while (parser.hasNext()) {
                parser.next();
            }
        } catch (final FdcException e) {
            exception = e;
        } finally {
            Assert.assertNotNull(exception);
        }
    }

    public void checkExtractedLine(final FdcParser parser,
            final String sourceCode, final boolean painted, final int hits)
                    throws Exception {

        Assert.assertTrue(parser.hasNext());
        parser.next();
        Assert.assertEquals(painted, parser.mustBePainted());
        Assert.assertEquals(hits, parser.getNumberOfCoverageType());
        if (hits > 0) {
            Assert.assertNotNull(parser.getCoverageTypes());
        }
    }
}
