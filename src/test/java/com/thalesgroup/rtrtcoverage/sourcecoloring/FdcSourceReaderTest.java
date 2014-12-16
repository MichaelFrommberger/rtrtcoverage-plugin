package com.thalesgroup.rtrtcoverage.sourcecoloring;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.thalesgroup.rtrtcoverage.sourcecoloring.FdcSourceReader;

/**
 * Test suite for FdcSourceReader
 */
public class FdcSourceReaderTest {

    /**
     * Test fdc source parsing
     * 
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        final FdcSourceReader reader = new FdcSourceReader();
        SourceFile source = reader.read(new FilePath(new File(this.getClass()
                .getResource("").getPath()
                + "/DERIV.fdc")));
        Assert.assertEquals("DERIV.C", source.getName());

        int i = 0;
        SourceBlock block, block2, block3, block4;
        for (SourcePiece piece : source.getCode().getContent()) {
            switch (i) {
            case 0:
                // header comment block
                Assert.assertTrue(piece instanceof SourceBlock);
                block = (SourceBlock) piece;
                Assert.assertEquals("COMMENT", block.getType());
                Assert.assertEquals("/* deriv/deriv.c",
                        ((SourceLineChunk) block.getContent().get(0))
                                .getChunk());
                Assert.assertTrue(block.getContent().get(1) instanceof SourceLineEnd);
                Assert.assertEquals(" * Copyright (C) 2004, 2007 Brian Gough",
                        ((SourceLineChunk) block.getContent().get(4))
                                .getChunk());
                break;
            case 1:
            case 2:
                // two line ends
                Assert.assertTrue(piece instanceof SourceLineEnd);
                break;
            case 5:
                // a #include (considered as a comment wrt coverage)
                Assert.assertTrue(piece instanceof SourceBlock);
                block = (SourceBlock) piece;
                Assert.assertEquals("COMMENT", block.getType());
                Assert.assertEquals("#include <stdlib.h>",
                        ((SourceLineChunk) block.getContent().get(0))
                                .getChunk());
                break;
            case 14:
                // a toplevel bit of code
                Assert.assertTrue(piece instanceof SourceLineChunk);
                Assert.assertEquals("static void",
                        ((SourceLineChunk) piece).getChunk());
                break;
            case 16:
                // a function node
                Assert.assertTrue(piece instanceof SourceBlock);
                block = (SourceBlock) piece;
                Assert.assertEquals("NODE", block.getType());
                Assert.assertEquals("FUNCTION",
                        block.getAttributes().get("TYPE"));
                Assert.assertEquals("central_deriv",
                        block.getAttributes().get("NAME"));
                // #0 BRANCH containing a LINK
                Assert.assertTrue(block.getContent().get(0) instanceof SourceBlock);
                block2 = (SourceBlock) block.getContent().get(0);
                Assert.assertEquals("BRANCH", block2.getType());
                Assert.assertEquals("TP", block2.getAttributes().get("MARK"));
                Assert.assertEquals("0", block2.getAttributes().get("ID"));
                Assert.assertEquals("0", block2.getAttributes().get("SUM"));
                Assert.assertTrue(block2.getContent().get(0) instanceof SourceBlock);
                block3 = (SourceBlock) block2.getContent().get(0);
                Assert.assertEquals("LINK", block3.getType());
                Assert.assertEquals("central_deriv", block3.getAttributes()
                        .get("NAME"));
                // #1 code
                Assert.assertTrue(block.getContent().get(1) instanceof SourceLineChunk);
                Assert.assertEquals(
                        " (const gsl_function * f, double x, double h,",
                        ((SourceLineChunk) block.getContent().get(1))
                                .getChunk());
                // #5 BRANCH
                Assert.assertTrue(block.getContent().get(5) instanceof SourceBlock);
                block2 = (SourceBlock) block.getContent().get(5);
                Assert.assertEquals("BRANCH", block2.getType());
                Assert.assertEquals("TB", block2.getAttributes().get("MARK"));
                Assert.assertEquals("0", block2.getAttributes().get("ID"));
                Assert.assertEquals("20", block2.getAttributes().get("SUM"));
                // with code, EOL, code, COMMENT, EOL, EOL, code, ALT
                Assert.assertTrue(block2.getContent().get(7) instanceof SourceBlock);
                block3 = (SourceBlock) block2.getContent().get(7);
                Assert.assertTrue(block3.getContent().get(0) instanceof SourceLineChunk);
                Assert.assertEquals("GSL_FN_EVAL (f, x - h)",
                        ((SourceLineChunk) block3.getContent().get(0))
                                .getChunk());
                Assert.assertTrue(block3.getSecondContent().get(0) instanceof SourceBlock);
                block4 = (SourceBlock) block3.getSecondContent().get(0);
                Assert.assertEquals("BRANCH", block4.getType());
                Assert.assertEquals("TA", block4.getAttributes().get("MARK"));
                Assert.assertEquals("0", block4.getAttributes().get("ID"));
                Assert.assertEquals("10", block4.getAttributes().get("SUM"));
                Assert.assertEquals(
                        "(*((f)->function))(x - h,(f)->params)",
                        ((SourceLineChunk) ((SourceBlock) block3
                                .getSecondContent().get(0)).getContent().get(0))
                                .getChunk());
                break;
            }
            i++;
        }
    }
}
