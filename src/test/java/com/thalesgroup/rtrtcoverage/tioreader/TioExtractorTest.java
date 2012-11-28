package com.thalesgroup.rtrtcoverage.tioreader;

import java.io.File;

import hudson.FilePath;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sebastien Barbier
 * @version 1.0
 */
public class TioExtractorTest {

    @Test
    public void test() throws Exception {

        final TioExtractor tioExtractor = new TioExtractor();
        final FilePath workspace = new FilePath(new File(this.getClass()
                .getResource("").toURI()));

        final FilePath[] paths = tioExtractor.findFilePath(workspace);

        Assert.assertNotNull(paths);
        Assert.assertEquals(10, paths.length);
    }

}
