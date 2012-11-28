package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class FilesMappingTest {

    @Test
    public void test1() throws Exception {

        FilesMapping mapping = new FilesMapping();
        mapping.build(
                new FilePath(new File(this.getClass().getResource("./tios").getPath())),
                new FilePath(new File(this.getClass().getResource("").getPath())),
                "*/**/*_aug.c");
        Assert.assertEquals("2b406919", mapping.get("AIRPORTMANAGER__CHECKDATABASESWAP.C").getKey());
        Assert.assertEquals("435f06c1", mapping.get("AIRPORTMANAGER__CHECKDATABASESWAP.C").getCrc());
        Assert.assertEquals(1,
                mapping.get("AIRPORTMANAGER_STATIC_GETINSTANCE.C").getAssociedTios().size());
        Assert.assertEquals("CLASS_AIRPORTMANAGER_4.TIO",
                mapping.get("AIRPORTMANAGER_STATIC_GETINSTANCE.C").getAssociedTios().get(0).getName());

    }

    @Test
    public void exportTest() throws Exception {
        File outputFile = new File(this.getClass().getResource("").getPath()+"/file_identities_output.xml");
        FilesMapping refMapping = new FilesMapping();
        refMapping.build(
                new FilePath(new File(this.getClass().getResource("./tios").getPath())),
                new FilePath(new File(this.getClass().getResource("").getPath())),
                "*/**/*_aug.c");
        (new FileIdentitiesExport()).export(refMapping, outputFile);
    }

    @Test
    public void importTest() throws Exception {
        File inputFile = new File(this.getClass().getResource("").getPath()+"/file_identities_output.xml");
        FilesMapping refMapping = new FilesMapping();
        refMapping.build(
                new FilePath(new File(this.getClass().getResource("./tios").getPath())),
                new FilePath(new File(this.getClass().getResource("").getPath())),
                "*/**/*_aug.c");
        (new FileIdentitiesExport()).export(refMapping, inputFile);

        FilesMapping importedMapping = new FilesMapping();
        importedMapping = (new FileIdentitiesImport()).importXml(inputFile);

        Assert.assertTrue(refMapping.equals(importedMapping));
    }
}
