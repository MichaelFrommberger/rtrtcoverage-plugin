package com.thalesgroup.rtrtcoverage.filesmapping;

import hudson.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.thalesgroup.dtkit.util.converter.ConversionException;
import com.thalesgroup.rtrtcoverage.fileidexport.FileIdentitiesModel;
import com.thalesgroup.rtrtcoverage.fileidexport.FileIdentitiesModel.FileIdentityModel;
import com.thalesgroup.rtrtcoverage.tusar.ObjectFactory;

/**
 * Class allowing to export a FilesMapping to an *.xml file.
 * @author Bastien Reboulet
 */
public class FileIdentitiesExport {
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
     * @param mapping the mapping
     * @param outputFile the file we want to write
     */
    public final void export(final FilesMapping mapping, final File outputFile) {
        initJAXB();
        writeXML(convert(mapping.getFileIdentities()), outputFile);
    }

    /**
     * Converts a list of FileIdentity to an xsd model.
     * @param fis a FileIdentity list
     * @return a FileIdentitiesModel
     */
    private FileIdentitiesModel convert(final List<FileIdentity> fis) {
        FileIdentitiesModel fileIdentities = new FileIdentitiesModel();
        for (FileIdentity fi : fis) {
            FileIdentityModel dfi = new FileIdentityModel();
            dfi.setKey(fi.getKey());
            dfi.setCrc(fi.getCrc());
            dfi.setSourceFileName(fi.getSourceFileName());
            for (FilePath tioPath : fi.getAssociedTios()) {
                dfi.getTioFile().add(tioPath.getRemote());
            }
            fileIdentities.getFileIdentityModel().add(dfi);
        }
        return fileIdentities;
    }

    /**
     * @param data
     *            a FileIdentitiesModel data structure
     * @param outputFile
     *            a file where to save the .xml file corresponding to the file identities
     *            input
     */
    private void writeXML(final FileIdentitiesModel data, final File outputFile) {
        try {
            // XML and JAXB
            marshaller.marshal(data, new FileOutputStream(outputFile));

        } catch (final FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException("The file "
                    + outputFile.getAbsolutePath() + " does not exist");
        } catch (final JAXBException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            /* Showing the error into the Hudson console */
            throw new ConversionException(e.getMessage());
        }
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
