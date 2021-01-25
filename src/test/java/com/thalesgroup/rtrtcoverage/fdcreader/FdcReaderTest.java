package com.thalesgroup.rtrtcoverage.fdcreader;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FdcReaderTest {

    @Test
    public void test_C() throws IOException {
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
    public void test_Ada() throws IOException {
        final FdcReader reader = new FdcReader();
        FileCoverageDefinition fileCovDef = new FileCoverageDefinition();
        fileCovDef = reader.read(new FilePath(new File(this.getClass()
                .getResource("").getPath()
                + "/CODECOVERAGE.ADB.fdc")));
        Assert.assertEquals("E:\\TEMP\\FDA\\TESTSUITEADA\\SRC", fileCovDef.getSourceDir());
        Assert.assertEquals("CODECOVERAGE.ADB", fileCovDef.getSourceName());
        // il y a 10 noeuds dans le fichier :
        // @NODE TYPE=FILE NAME="CodeCoverage.adb"
        // @NODE TYPE=PACKAGE NAME="CodeCoverage"
        // @NODE TYPE=PROCEDURE NAME="TEST"
        // @NODE TYPE=FUNCTION NAME="SimpleCondition(x: in integer) return integer"
        // @NODE TYPE=PROCEDURE NAME="call"
        // @NODE TYPE=FUNCTION NAME="SimpleConditionWithImplicit(x: in integer) return integer"
        // @NODE TYPE=FUNCTION NAME="AndCondition(x: in integer) return integer"
        // @NODE TYPE=FUNCTION NAME="OrCondition(x: in integer) return integer"
        // @NODE TYPE=FUNCTION NAME="Sum(x: in integer) return natural"
        // @NODE TYPE=FUNCTION NAME="CheckCoverageStatement(cond1, cond2, cond3, cond4, cond5: in boolean) return float"
        Assert.assertEquals(10, fileCovDef.getNodes().size());
        Assert.assertNotNull(fileCovDef.getNode("CodeCoverage.adb"));
        Assert.assertNotNull(fileCovDef.getNode("CodeCoverage"));
        Assert.assertNotNull(fileCovDef.getNode("TEST"));
        Assert.assertNotNull(fileCovDef.getNode("SimpleCondition(x: in integer) return integer"));
        Assert.assertNotNull(fileCovDef.getNode("call"));
        Assert.assertNotNull(fileCovDef.getNode("SimpleConditionWithImplicit(x: in integer) return integer"));
        Assert.assertNotNull(fileCovDef.getNode("AndCondition(x: in integer) return integer"));
        Assert.assertNotNull(fileCovDef.getNode("OrCondition(x: in integer) return integer"));
        Assert.assertNotNull(fileCovDef.getNode("Sum(x: in integer) return natural"));
        NodeDefinition node = fileCovDef.getNode("CheckCoverageStatement(cond1, cond2, cond3, cond4, cond5: in boolean) return float");
        Assert.assertNotNull(node);
        // ce dernier noeud possède les branches suivantes :
        int i = -1;
        // @BRANCH MARK=TP ID=16
        Assert.assertEquals("TP16", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=19
        Assert.assertEquals("TB19", node.getBranchDefinitions().get(++i).getMarkId());
        // @-POPUP BRANCH MARK=TB ID=21
        Assert.assertEquals("TB21", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=20 SUM=20
        Assert.assertEquals("TB20", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=22
        Assert.assertEquals("TB22", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=23
        Assert.assertEquals("TB23", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=24
        Assert.assertEquals("TB24", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=25
        Assert.assertEquals("TB25", node.getBranchDefinitions().get(++i).getMarkId());
        // @-POPUP BRANCH MARK=TB ID=27
        Assert.assertEquals("TB27", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=26
        Assert.assertEquals("TB26", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=31
        Assert.assertEquals("TB31", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=28
        Assert.assertEquals("TB28", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=29
        Assert.assertEquals("TB29", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=30
        Assert.assertEquals("TB30", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=35
        Assert.assertEquals("TB35", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=32
        Assert.assertEquals("TB32", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=33
        Assert.assertEquals("TB33", node.getBranchDefinitions().get(++i).getMarkId());
        // @BRANCH MARK=TB ID=34
        IBranchDefinition branch = node.getBranchDefinitions().get(++i);
        Assert.assertEquals("TB34", branch.getMarkId());
        Assert.assertEquals("TB", branch.getMark());
        Assert.assertEquals("34", branch.getId());
        Assert.assertEquals(BranchType.TB_LOOPS, branch.getType());
        // @BRANCH MARK=TP ID=17
        branch = node.getBranchDefinitions().get(++i);
        Assert.assertEquals("TP17", branch.getMarkId());
        Assert.assertEquals("TP", branch.getMark());
        Assert.assertEquals("17", branch.getId());
        Assert.assertEquals(BranchType.TP_EXITS, branch.getType());
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
