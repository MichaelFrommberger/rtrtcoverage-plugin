package com.thalesgroup.rtrtcoverage.tusarexport;

import hudson.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.thalesgroup.dtkit.util.converter.ConversionException;
import com.thalesgroup.rtrtcoverage.tracemerge.FileCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.IBranchCoverage;
import com.thalesgroup.rtrtcoverage.tracemerge.NodeCoverage;
import com.thalesgroup.rtrtcoverage.tusar.BranchCoverageComplexType;
import com.thalesgroup.rtrtcoverage.tusar.BranchCoverageComplexType.Resource;
import com.thalesgroup.rtrtcoverage.tusar.BranchCoverageComplexType.Resource.Line;
import com.thalesgroup.rtrtcoverage.tusar.BranchCoverageComplexType.Resource.Line.Branches.Branch;
import com.thalesgroup.rtrtcoverage.tusar.ObjectFactory;
import com.thalesgroup.rtrtcoverage.tusar.Tusar;

/**
 * Class allowing to convert and export data from file coverages to a tusar (*.xml) file.
 * @author Bastien Reboulet
 */
public class TusarExport {

    // JAXB object
    /**
     * Jaxb marshaller.
     */
    private Marshaller marshaller;

    /**
     * Jaxb factory.
     */
    private ObjectFactory objFactory;

    /**
     * Coverts a list of file coverages into a Tusar data structure.
     *
     * @param fileCoverages
     *            a list of filled FileCoverages
     * @return a Tusar data structure filled with input file coverages
     */
    public final Tusar convert(final List<FileCoverage> fileCoverages) {
        initJAXB();
        final Tusar tusar = objFactory.createTusar();
        tusar.setCoverage(objFactory.createCoverageComplexType());
        final BranchCoverageComplexType tusarBranchCov = objFactory
                .createBranchCoverageComplexType();
        tusar.getCoverage().setBranchCoverage(tusarBranchCov);
        for (final FileCoverage fileCov : fileCoverages) {
            final BranchCoverageComplexType.Resource resource = objFactory
                    .createBranchCoverageComplexTypeResource();
            tusarBranchCov.getResource().add(resource);
            resource.setFullname(fileCov.getFileCoverageDefinition()
                    .getSourceDir()
                    + "\\"
                    + fileCov.getFileCoverageDefinition().getSourceName());
            final Map<Integer, Line> lineMap = new HashMap<Integer, Line>();
            for (NodeCoverage nodeCov : fileCov.getNodes()) {
                for (IBranchCoverage branchCov : nodeCov.getAllGlobalCoverages()) {
                    Line tusarLine = lineMap.get(branchCov.getLineNumber());
                    if (tusarLine == null) {
                        tusarLine = objFactory
                                .createBranchCoverageComplexTypeResourceLine();
                        tusarLine
                        .setBranches(objFactory
                                .createBranchCoverageComplexTypeResourceLineBranches());
                        tusarLine.setNumber(String.valueOf(branchCov.getLineNumber()));
                        lineMap.put(branchCov.getLineNumber(), tusarLine);
                        tusarLine.setNumberOfBranches("0");
                        tusarLine.setUncoveredBranches("0");
                    }

                    final Branch tusarBranch = objFactory
                            .createBranchCoverageComplexTypeResourceLineBranchesBranch();
                    if (branchCov.getType() != null) {
                        tusarBranch.setType(branchCov.getType().toString());
                    }
                    tusarBranch.setName(nodeCov.getNodeName() + " "
                            + branchCov.getMark() + " "
                            + branchCov.getId());
                    if (branchCov.isCovered()) {
                        tusarBranch.setCoverage("100");
                    } else {
                        tusarBranch.setCoverage("0");
                    }
                    tusarLine.setNumberOfBranches(String.valueOf(Integer
                            .parseInt(tusarLine.getNumberOfBranches()) + 1));
                    if (!branchCov.isCovered()) {
                        tusarLine
                        .setUncoveredBranches(String.valueOf(Integer
                                .parseInt(tusarLine
                                        .getUncoveredBranches()) + 1));
                    }
                    final int numberOfBranches = Integer.parseInt(tusarLine
                            .getNumberOfBranches());
                    final int uncoveredBranches = Integer.parseInt(tusarLine
                            .getUncoveredBranches());
                    // *1000/10 instead of *100 to get decimal value with 1 digit
                    final double thousand = 1000.0;
                    final double ten = 10.0;
                    tusarLine.setBranchesCoverage(String.valueOf(Math
                            .round((numberOfBranches - uncoveredBranches)
                                    * thousand / numberOfBranches) / ten));
                    tusarLine.getBranches().getBranch().add(tusarBranch);

                    // sort branches by name
                    final List<Branch> branches = new ArrayList<Branch>(
                            tusarLine.getBranches().getBranch());
                    Collections.sort(branches, new Comparator<Branch>() {
                        public int compare(final Branch branch0,
                                final Branch branch1) {
                            return new String(branch0.getName())
                            .compareTo(branch1.getName());
                        }
                    });
                    tusarLine.getBranches().getBranch().clear();
                    for (final Branch branch : branches) {
                        tusarLine.getBranches().getBranch().add(branch);
                    }
                }
            }
            // sort lines by start number
            final List<Line> lines = new ArrayList<Line>(lineMap.values());
            Collections.sort(lines, new Comparator<Line>() {
                public int compare(final Line line0, final Line line1) {
                    final int int0 = Integer.parseInt(line0.getNumber());
                    final int int1 = Integer.parseInt(line1.getNumber());
                    return new Integer(int0).compareTo(int1);
                }
            });
            for (final Line line : lines) {
                resource.getLine().add(line);
            }
        }

        // sort resources by fullname
        final List<Resource> resources = new ArrayList<Resource>(
                tusarBranchCov.getResource());
        Collections.sort(resources, new Comparator<Resource>() {
            public int compare(final Resource resource0,
                    final Resource resource1) {
                return new String(resource0.getFullname()).compareTo(resource1
                        .getFullname());
            }
        });
        tusarBranchCov.getResource().clear();
        for (final Resource resource : resources) {
            tusarBranchCov.getResource().add(resource);
        }

        return tusar;
    }

    /**
     * @param tusarData
     *            a Tusar data structure
     * @param outputFile
     *            a file where to save the .xml file corresponding to the tusar
     *            input
     * @param buildDir
     *            the buildDir where to store the tusar temporary file
     * @throws InterruptedException an InterruptedException
     * @throws IOException an IOException
     */
    public final void export(final Tusar tusarData, final FilePath outputFile, final File buildDir)
            throws IOException, InterruptedException {
        File tusarTemp = new File(buildDir.getPath()
                + System.getProperty("file.separator") + "tusartemp.xml");
        try {
            // XML and JAXB
            marshaller.marshal(tusarData, new FileOutputStream(tusarTemp));

        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException("The file "
                    + tusarTemp.getAbsolutePath() + " does not exist");
        } catch (final JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException(e.getMessage());
        }
        if (tusarTemp.exists()) {
            outputFile.copyFrom(new FilePath(tusarTemp));
        }
        tusarTemp.delete();
    }

    /**
     * Initialization of the JAXB context Mainly, initialize the ObjectFactory.
     *
     * @see ObjectFactory
     */
    private void initJAXB() {
        try {
            // WARNING: 2nd argument is necessary to instaure context. Without
            // the relative path is unknown and throwed Exception.
            final JAXBContext jaxbContext = JAXBContext.newInstance(
                    "com.thalesgroup.rtrtcoverage.tusar", this.getClass()
                    .getClassLoader());

            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            objFactory = new ObjectFactory();
        } catch (final JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException(e.getMessage());
        }

    }
}
