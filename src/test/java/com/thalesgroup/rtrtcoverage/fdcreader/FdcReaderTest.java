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
                + "/LAME_APPLY_ACCELERATION_LAW.FDC")));
        Assert.assertEquals(
                "D:\\JENKINS\\JOBS\\A400M\\WORKSPACE\\CDS_A400M_DEV\\LA\\LAME\\SRC",
                fileCovDef.getSourceDir());
        Assert.assertEquals("Statement Blocks",
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getType().toString());
        Assert.assertEquals("4",
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getId());
        Assert.assertEquals("simple",
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getSubType());
        Assert.assertEquals("lame_apply_acceleration_law",
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getFctName());
        Assert.assertEquals("/then",
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getPath());
        Assert.assertEquals(140,
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getStartLineNumber());
        Assert.assertEquals(145,
                fileCovDef.getBranch(BranchDefinitionType.BLOCK, "4").getEndLineNumber());
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
