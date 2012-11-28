package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.thalesgroup.dtkit.util.converter.ConversionException;
import com.thalesgroup.rtrtcoverage.fileidexport.FileIdentitiesModel;
import com.thalesgroup.rtrtcoverage.fileidexport.FileIdentitiesModel.FileIdentityModel;
import com.thalesgroup.rtrtcoverage.tusar.ObjectFactory;

/**
 * Class allowing to import FilesMapping data from an *.xml file.
 * @author Bastien Reboulet
 */
public class FileIdentitiesImport {
    // JAXB object
    /**
     * Jaxb unmarshaller.
     */
    private Unmarshaller unmarshaller;

    /**
     * Jaxb factory.
     */
    private ObjectFactory objFactory;

    /**
     * @param inputFile the *.xml input file
     * @return a FilesMapping structure
     */
    public final FilesMapping importXml(final File inputFile) {
        FilesMapping mapping = new FilesMapping();
        initJAXB();
        mapping.setMapping(convert(readXML(inputFile)));
        return mapping;
    }

    /**
     * Converts an xsd data model into a List of FileIdentity.
     * @param fims a FileIdentitiesModel
     * @return a List of FileIdentity
     */
    private List<FileIdentity> convert(final FileIdentitiesModel fims) {
        List<FileIdentity> fileIdentities = new ArrayList<FileIdentity>();
        for (FileIdentityModel fim : fims.getFileIdentityModel()) {
            FileIdentity fi = new FileIdentity(fim.getKey(), fim.getCrc(), fim.getSourceFileName());
            for (String tioPath : fim.getTioFile()) {
                fi.getAssociedTios().add(new FilePath(new File(tioPath)));
            }
            fileIdentities.add(fi);
        }
        return fileIdentities;
    }


    /**
     * @param inputFile an *.xml file we want to get data from.
     * @return a FileIdentitiesModel
     */
    private FileIdentitiesModel readXML(final File inputFile) {
        FileIdentitiesModel fim = new FileIdentitiesModel();
        try {
            // XML and JAXB
            fim = (FileIdentitiesModel) unmarshaller.unmarshal(new FileInputStream(inputFile));

        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException("The file "
                    + inputFile.getAbsolutePath() + " does not exist");
        } catch (final JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException(e.getMessage());
        }

        return fim;

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
                    "com.thalesgroup.rtrtcoverage.fileidexport", this.getClass()
                    .getClassLoader());

            unmarshaller = jaxbContext.createUnmarshaller();

            objFactory = new ObjectFactory();
        } catch (final JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException(e.getMessage());
        }
    }
}
