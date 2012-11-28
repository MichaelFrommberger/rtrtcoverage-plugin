package com.thalesgroup.rtrtcoverage.tioreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unitary Test for TioReader
 * 
 * @author Sebastien Barbier
 * @version 1.0
 * 
 */
public class TioReaderTest {

    @Test
    public void testBaseMethods() throws Exception {

        File file = null;
        InputStream ips = null;
        InputStreamReader ipsr = null;
        BufferedReader br = null;

        try {
            file = new File(this.getClass().getResource("STRINGUTILITIES.TIO")
                    .toURI());
            ips = new FileInputStream(file);
            ipsr = new InputStreamReader(ips);
            br = new BufferedReader(ipsr);
        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        final TioReader tioReader = new TioReader(br);
        tioReader.ReInit(br);

        Token token = tioReader.getNextToken();
        Assert.assertNotNull(token);
        Assert.assertEquals("NT", token.image);

        token = tioReader.getToken(4);
        Assert.assertNotNull(token);
        Assert.assertEquals("DT", token.toString());

        tioReader.enable_tracing();
        tioReader.disable_tracing();

    }

    @Test
    public void testSimpleCharStream() throws Exception {

        File file = null;
        InputStream ips = null;
        InputStream ips2 = null;

        try {
            file = new File(this.getClass().getResource("STRINGUTILITIES.TIO")
                    .toURI());
            ips = new FileInputStream(file);
            ips2 = new FileInputStream(file);

        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        final TioReader tioReader = new TioReader(ips);
        final SimpleCharStream scs = new SimpleCharStream(ips2);
        final TioReaderTokenManager srctm = new TioReaderTokenManager(scs);
        final TioReader tioReaderSecond = new TioReader(srctm);

        tioReader.ReInit(ips);
        Token token, token2, endToken;

        token = tioReader.getNextToken();
        token2 = tioReaderSecond.getNextToken();
        endToken = new Token(0);
        while (token.kind != endToken.kind) {
            Assert.assertEquals(token.image, token2.image);
            token = tioReader.getNextToken();
            token2 = tioReaderSecond.getNextToken();
        }
        Assert.assertEquals(endToken.kind, token.kind);
    }

    @Test
    public void testReadingTest() throws Exception {

        File file = null;
        InputStream ips = null;

        try {
            file = new File(this.getClass().getResource("STRINGUTILITIES.TIO")
                    .toURI());
            ips = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        final TioReader tioReader = new TioReader(ips);
        Exception exception = null;

        CoverageTest coverageTest = null;
        try {
            coverageTest = tioReader.readCoverageForFileIntoTest();
        } catch (final TioException e) {
            exception = e;
        } finally {
            Assert.assertNotNull(coverageTest);
        }

        Assert.assertNotNull(coverageTest.getFileCoverage());
        Assert.assertEquals(3, coverageTest.getFileCoverage().size());
        final ArrayList<CoverageLine> list = (ArrayList<CoverageLine>) coverageTest
                .getFileCoverage();
        Assert.assertEquals("TP", list.get(0).getType());
        Assert.assertEquals(0, list.get(0).getNumber());
        Assert.assertEquals("TP", list.get(1).getType());
        Assert.assertEquals(1, list.get(1).getNumber());
        Assert.assertEquals("TB", list.get(2).getType());
        Assert.assertEquals(0, list.get(2).getNumber());

    }

    @Test
    public void testReadingFile() throws Exception {

        File file = null;
        InputStream ips = null;

        try {
            file = new File(this.getClass().getResource("TOTO.TIO").toURI());
            ips = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        final TioReader tioReader = new TioReader(ips);
        Exception exception = null;

        CoverageFile coverageFile = null;
        try {
            coverageFile = tioReader.readCoverageForFile();
        } catch (final TioException e) {
            exception = e;
        } finally {
            Assert.assertNotNull(coverageFile);
        }

        Assert.assertNotNull(coverageFile.getCoverage());
        Assert.assertEquals(26, coverageFile.getCoverage().size());
        final CoverageLine cl = new CoverageLine();
        cl.setType("TP");
        cl.setNumber("1");
        Assert.assertTrue(coverageFile.getCoverage().contains(cl));
        cl.setType("TA");
        cl.setNumber("0");
        Assert.assertTrue(coverageFile.getCoverage().contains(cl));
        cl.setType("TB");
        cl.setNumber("c");
        Assert.assertTrue(coverageFile.getCoverage().contains(cl));
        cl.setType("TE");
        cl.setNumber("8");
        Assert.assertTrue(coverageFile.getCoverage().contains(cl));
        cl.setType("TP");
        cl.setNumber("1");
        Assert.assertEquals(2, coverageFile.getHit(cl));
        cl.setType("TB");
        cl.setNumber("1");
        Assert.assertEquals(0, coverageFile.getHit(cl));
    }

    @Test
    public void testbadFiles() throws Exception {
        // No NT token
        failedReading("bad/BAD1.TIO");
        // No DT token
        failedReading("bad/BAD2.TIO");
        // No FT token
        failedReading("bad/BAD3.TIO");
        // No DC token
        failedReading("bad/BAD4.TIO");
        // bad NT token argument
        failedReading("bad/BAD5.TIO");
        // bad FT token argument
        failedReading("bad/BAD6.TIO");
        // bad TB token argument
        failedReading("bad/BAD7.TIO");
    }

    public void failedReading(final String inputTioPath) throws Exception {

        final File inputTioFile = new File(this.getClass()
                .getResource(inputTioPath).toURI());
        InputStream ips = null;
        ips = new FileInputStream(inputTioFile);

        final TioReader tioReader = new TioReader(ips);
        CoverageFile coverageFile = null;

        coverageFile = tioReader.readCoverageForFile();

        Assert.assertNull(coverageFile);
    }

}
