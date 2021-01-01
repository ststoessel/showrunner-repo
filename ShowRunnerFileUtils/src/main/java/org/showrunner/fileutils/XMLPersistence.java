package org.showrunner.fileutils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@NoArgsConstructor
@Slf4j
public class XMLPersistence<T> {


    /**
     * @param fileName
     * @param clazz
     * @return
     */
    public T restore(String fileName, Class clazz) {
        log.info("Restoring " + clazz.getName() + " from " + fileName);

        T ret = null;
        try {
            ret = (T) clazz.newInstance();
        } catch (Exception e) {
            String msg = "Error creating instance of " + clazz.getName();
            log.error(msg, e);
            throw new FileUtilsException(msg, e);
        }

        if (!new File(fileName).exists()) {
            String msg = fileName + " not found!";
            log.error(msg);
            throw new FileUtilsException(msg);
        }

        try {
            final Unmarshaller unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
            ret = (T) unmarshaller.unmarshal(new File(fileName));
        } catch (final JAXBException e) {
            String msg = "Error restoring from " + fileName + "!";
            log.error(msg, e);
            throw new FileUtilsException(msg, e);
        }

        return ret;
    }


    /**
     * @param fileName
     * @param t
     * @param clazz
     * @return
     */
    public void persist(String fileName, T t, Class clazz) {

        log.info("Persisting " + clazz.getName() + " to " + fileName);

        try {
            final Marshaller marshaller = JAXBContext.newInstance(clazz).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(t, new File(fileName));
        } catch (JAXBException e) {
            String msg = "Error saving " + fileName + "!";
            log.error(msg, e);
            throw new FileUtilsException(msg, e);
        }
    }


}
