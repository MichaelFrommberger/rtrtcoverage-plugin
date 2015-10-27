package com.thalesgroup.rtrtcoverage.sourcecoloring;

import hudson.FilePath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;

public class SourceColourerTest {
    @Test
    public void testColoring() throws Exception {
        computeColoring("DERIV.fdc", new String[] { "gsl_deriv.tio" },
                "6c161813", "c671b91", "DERIV.fdc.expected.html");
    }

    @Test
    public void testColoring2() throws Exception {
        computeColoring("E_REM_PIO2.fdc", new String[] { "cos.tio", "sin.tio" },
                "6a43412e", "762a1613", "E_REM_PIO2.fdc.expected.html");
    }

    @Test
    public void testColoring3() throws Exception {
        computeColoring("CANMONITOR_REFRESHMONITORINGDATA.FDC",
                new String[] { "CLASS_CANMONITOR.TIO" },
                "51487b1a", "55496766", "CANMONITOR_REFRESHMONITORINGDATA.FDC.expected.html");
    }

    @Test
    public void testColoring4() throws Exception {
        computeColoring("AIRCRAFTLOCALIZATIONCOMPONENT_CONNECT.FDC",
                new String[] { "CLASS_AIRCRAFTLOCALIZATIONCOMPONENT.TIO" },
                "9572331", "127017cc", "AIRCRAFTLOCALIZATIONCOMPONENT_CONNECT.FDC.expected.html");
    }

    @Test
    public void testColoring5() throws Exception {
        computeColoring("CANMONITOR__MONITORKCCU.FDC",
                new String[] { "CLASS_CANMONITOR.TIO" },
                "28076378", "13093ed0", "CANMONITOR__MONITORKCCU.FDC.expected.html");
    }

    @Test
    public void testColoring6() throws Exception {
        computeColoring("INTERACTIVETESTSELECTOR_ARECONNECTIONSSATISFIED.FDC",
                new String[] { "CLASS_INTERACTIVETESTSELECTOR.TIO" },
                "161d3447", "1a09672d", "INTERACTIVETESTSELECTOR_ARECONNECTIONSSATISFIED.FDC.expected.html");
    }

    @Test
    public void testColoring_Ada() throws Exception {
        computeColoring("ada.fdc",
                new String[] { "ada.tio" },
                "4907780d", "3c2b766a", "ada.fdc.expected.html");
    }

    private void computeColoring(final String fdc, final String[] tios,
            final String key, final String crc, final String expectedFileName)
            throws Exception {
        FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("").getPath()
                + "/" + fdc));
        // merge FDC with TIO
        CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        FdcReader fdcReader = new FdcReader();
        FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey(key);
        fileCovDef.setCrc(crc);
        fileCoverageDefs.add(fileCovDef);
        List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        for (String tio: tios) {
            File tioPath = new File(this.getClass().getResource("").getPath() + "/"
                    + tio);
            FileInputStream ips = new FileInputStream(tioPath);
            TioReader2 tioReader = new TioReader2(ips);
            traces.add(tioReader.readTio());
        }
        List<FileCoverage> fileCovs = coverageTraceMerger.merge(
                fileCoverageDefs, traces);
        Assert.assertNotNull(fileCovs);
        Assert.assertNotNull(fileCovs.get(0));
        FileCoverage fileCov = fileCovs.get(0);
        // generate colorified code
        SourceFile source = new FdcSourceReader().read(fdcPath);
        SourceColourer colourer = new SourceColourer();
        String result = colourer.colorSource(source, fileCov);
        File outputFile = new File(this.getClass().getResource("").getPath()
                + "/" + fdc + ".html");
        PrintWriter out = new PrintWriter(outputFile, "UTF-8");
        out.println("<html><head></head><body><table class=\"rtrtcoverage-file\">");
        out.println(result);
        out.println("</table></body></html>");
        out.close();
        File expectedFile = new File(this.getClass().getResource("").getPath()
                + "/" + expectedFileName);
        Assert.assertTrue(contentEqualsIgnoreEOL(expectedFile, outputFile));
    }

    /**
     * Compares the contents of two Readers to determine if they are equal or
     * not, ignoring EOL characters.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedReader</code> if they are not already buffered.
     *
     * From org.apache.commons.io.IOUtils
     *
     * @param input1
     *            the first reader
     * @param input2
     *            the second reader
     * @return true if the content of the readers are equal (ignoring EOL
     *         differences), false otherwise
     * @throws NullPointerException
     *             if either input is null
     * @throws IOException
     *             if an I/O error occurs
     */
    public static boolean contentEqualsIgnoreEOL(final File file1,
            final File file2) throws IOException {
        if (file1 == file2) {
            return true;
        }
        final BufferedReader br1 = new BufferedReader(new FileReader(file1));
        final BufferedReader br2 = new BufferedReader(new FileReader(file2));

        String line1 = br1.readLine();
        String line2 = br2.readLine();
        while (line1 != null && line2 != null && line1.equals(line2)) {
            line1 = br1.readLine();
            line2 = br2.readLine();
        }
        br1.close();
        br2.close();
        return line1 == null ? line2 == null ? true : false : line1
                .equals(line2);
    }
}
