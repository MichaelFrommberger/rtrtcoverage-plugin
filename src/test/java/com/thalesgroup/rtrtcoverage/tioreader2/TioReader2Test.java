package com.thalesgroup.rtrtcoverage.tioreader2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class TioReader2Test {

    @Test
    public void testReadingFile() {
        TestSuiteTrace testSuiteTrace = readTio("CLASS_TOSMANAGERCOMPONENT.TIO"); 
        Assert.assertNotNull(testSuiteTrace.getTestTraces());
        Assert.assertEquals(14, testSuiteTrace.getTestTraces().size());
        Assert.assertEquals("13", testSuiteTrace.getTestTraces().get(2)
                .getFileTraces("41566919", "1e207343").get(0).getTraces().get(7).getId());
        Assert.assertEquals("TP", testSuiteTrace
                .getTestTraces().get(1).getFileTraces("41566919", "1e207343")
                .get(0).getTraces().get(0).getMark());

    }

    @Test
    public void testReadFile2() {
        TestSuiteTrace testSuiteTrace = readTio("COV_SUT_A429_HWU_SETUP_CCR.TIO");
    }

    private TestSuiteTrace readTio(final String filename) {
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
        try {
            testSuiteTrace = tioReader2.readTio();
        } catch (final TioException e) {
        	Assert.fail(e.getMessage());
        }
        Assert.assertNotNull("le résultat ne devrait pas être nul", testSuiteTrace);
        return testSuiteTrace;
    }
}
