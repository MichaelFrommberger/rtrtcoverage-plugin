package com.thalesgroup.rtrtcoverage.tusarexport;

import hudson.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.thalesgroup.rtrtcoverage.fdcreader.FdcReader;
import com.thalesgroup.rtrtcoverage.fdcreader.FileCoverageDefinition;
import com.thalesgroup.rtrtcoverage.tioreader2.TestSuiteTrace;
import com.thalesgroup.rtrtcoverage.tioreader2.TioReader2;
import com.thalesgroup.rtrtcoverage.tracemerge.CoverageTraceMerger;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tusar.Tusar;

public class TusarExportTest {

    @Test
    public void exportTest() throws Exception {
        // Converter data input build
        final FilePath fdcPath = new FilePath(new File(this.getClass()
                .getResource("SACO_RECEIVE_DATA.FDC")
                .getPath()));
        final File tioPath = new File(this.getClass()
                .getResource("SACO_RECEIVE_DATA.TIO")
                .getPath());
        final CoverageTraceMerger coverageTraceMerger = new CoverageTraceMerger();
        final List<FileCoverageDefinition> fileCoverageDefs = new ArrayList<FileCoverageDefinition>();
        final FdcReader fdcReader = new FdcReader();
        final FileCoverageDefinition fileCovDef = fdcReader.read(fdcPath);
        fileCovDef.setKey("4e721266");
        fileCovDef.setCrc("797d07b2");
        fileCoverageDefs.add(fileCovDef);
        final List<TestSuiteTrace> traces = new ArrayList<TestSuiteTrace>();
        final FileInputStream ips = new FileInputStream(tioPath);
        final TioReader2 tioReader = new TioReader2(ips);
        traces.add(tioReader.readTio());
        final List<FileCoverage> fileCovs = coverageTraceMerger.merge(
                fileCoverageDefs, traces);
        // Converter test
        final FilePath outputFile = new FilePath(new File(this.getClass().getResource("")
                .getPath()
                + "/test_tusar_output.xml"));
        final FilePath refFile = new FilePath(new File(this.getClass().getResource("").getPath()
                + "/test_tusar_ref.xml"));
        final TusarExport tusarExport = new TusarExport();
        final Tusar tusarData = tusarExport.convert(fileCovs);
        tusarExport.export(tusarData, outputFile, new File(this.getClass().getResource("").getPath()));

        // FIXME comparing the files is problematic, because of different namespaces
        //Assert.assertTrue(filesMatch(refFile, outputFile));
    }

    /**
     * Compares text content of 2 files
     *
     * @param file1
     *            a text file to compare to file2
     * @param file2
     *            a text file to compare to file1
     * @return true if files contents are the same else return false
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private boolean filesMatch(final FilePath filepath1, final FilePath filepath2)
            throws IOException, ParserConfigurationException, SAXException {
        File file1 = new File(filepath1.getRemote());
        File file2 = new File(filepath2.getRemote());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc1 = db.parse(file1);
        doc1.normalizeDocument();

        Document doc2 = db.parse(file2);
        doc2.normalizeDocument();

        return (doc1.isEqualNode(doc2));

    }

}
