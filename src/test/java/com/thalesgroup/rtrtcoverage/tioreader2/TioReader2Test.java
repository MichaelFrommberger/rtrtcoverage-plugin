package com.thalesgroup.rtrtcoverage.tioreader2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class TioReader2Test {

    @Test
    public void testReadingFile() throws TioException {
        TestSuiteTrace testSuiteTrace = testReadTio("CLASS_TOSMANAGERCOMPONENT.TIO"); 
        Assert.assertNotNull(testSuiteTrace.getTestTraces());
        Assert.assertEquals(14, testSuiteTrace.getTestTraces().size());
        Assert.assertEquals("13", testSuiteTrace.getTestTraces().get(2)
                .getFileTraces("41566919", "1e207343").get(0).getTraces().get(7).getId());
        Assert.assertEquals("TP", testSuiteTrace
                .getTestTraces().get(1).getFileTraces("41566919", "1e207343")
                .get(0).getTraces().get(0).getMark());

    }

    @Test
    public void testReadFile2() throws TioException {
        testReadTio("COV_SUT_A429_HWU_SETUP_CCR.TIO");
    }

    @Test
    public void testReadFile_withDot() throws TioException {
        testReadTio("CM_AUTOMATE.tio");
    }

    @Test
    public void testReadFile_withDot2() throws TioException {
        testReadTio("EV_CHANGEADDRESSAIRCRAFT.tio");
    }

    @Test
    public void test_Ada() throws TioException {
    	TestSuiteTrace testSuiteTrace = testReadTio("ada.tio");
    	// le fichier tio contient 18 blocks de trace "NT"
        Assert.assertEquals(18, testSuiteTrace.getTestTraces().size());
        // on considère la 15e trace
        //NT "CHECKCOVERAGESTATE/1" D7 B7
        //DT "Thu Apr 30 14:27:26 2015"
        //CO
		//FT   d0877094
		//DC   a667b2c3
		//TP 01 1
		//TP 11 1
		//TB 31 1
		//TB 51 1
		//TB 61 1
		//TB B1 1
		//TB C1 1
		//TB 22 1
		//TB 32 23
        TestTrace trace15 = testSuiteTrace.getTestTraces().get(14);
        Assert.assertEquals("T125 \"CHECKCOVERAGESTATE/1\"", trace15.getName());
        // cette trace convre le fichier key=d0877094 crc=a667b2c3
        List<FileTrace> fileTraces = trace15.getFileTraces("4907780d", "3c2b766a");
        Assert.assertNotNull(fileTraces);
        Assert.assertNotNull(fileTraces.get(0));
        FileTrace traces = fileTraces.get(0);
        // il y a 9 branches traversées dans cette trace
        Assert.assertEquals(9, traces.getTraces().size());
        // on considère la dernière trace : TB 32 23
        BranchTrace branchTrace = traces.getTraces().get(8);
        Assert.assertEquals("TB", branchTrace.getMark());
        // TB 32 => 0x23 => "35"
        Assert.assertEquals("35", branchTrace.getId());
    }
    
    private TestSuiteTrace testReadTio(final String filename) throws TioException {
        File file = null;
        InputStream ips = null;
        try {
            file = new File(this.getClass().getResource(filename).getPath());
            ips = new FileInputStream(file);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        final TioReader2 tioReader2 = new TioReader2(ips);
        TestSuiteTrace testSuiteTrace = null;
        testSuiteTrace = tioReader2.readTio();
        Assert.assertNotNull("le r�sultat ne devrait pas �tre nul", testSuiteTrace);
        return testSuiteTrace;
    }
}
