package com.thalesgroup.rtrtcoverage.fdcreader;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class FdcReaderTest {

    @Test
    public void test1() throws IOException {
        final FdcReader reader = new FdcReader();
        FileCoverageDefinition fileCovDef = new FileCoverageDefinition();
        fileCovDef = reader.read(new FilePath(new File(this.getClass()
                .getResource("").getPath()
                + "/SACO_RECEIVE_DATA.FDC")));
        Assert.assertEquals(
                "D:\\JENKINS\\JOBS\\A400M\\WORKSPACE\\CDS_A400M_DEV\\SA\\SACO\\SRC",
                fileCovDef.getSourceDir());

        Assert.assertEquals(4, fileCovDef.getNodes().size());

        Assert.assertEquals(87, fileCovDef.getNode("saco_receive_data").getBranchDefinitions().get(0).getLineNumber());

    }

    @Test
    public void testCustomSplit() throws IOException {
        final FdcReader reader = new FdcReader();
        final String[] expected = { "AP", "0", "A661Button_setEnable",
                "CA661Protocol-> ...", "31", "31" };
        final List<String> ref = Arrays.asList(expected);

        final List<String> result = reader
                .customSplit("AP 0 \"A661Button_setEnable\" \"CA661Protocol-> ...\" 31 31");

        Assert.assertEquals(ref, result);
    }

}
