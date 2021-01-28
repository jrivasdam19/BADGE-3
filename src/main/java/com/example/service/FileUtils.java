package com.example.service;
import com.example.data.Student;

import com.example.data.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    static Logger log = LoggerFactory.getLogger(FileUtils.class);
    private static final String URI = "https://student-chart.herokuapp.com";
    private static Scraping scraping = Scraping.getInstance();
    private static String fileName = String.format("%s\\fbmoll\\students.xml", System.getProperty("user.home"));
    private static List<Student> studentList = new ArrayList<>();


    public static <T extends Serializable> File marshallContent(String path, T classX) {
        File file = new File(path);
        try {
            JAXBContext context = JAXBContext.newInstance(classX.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(classX, file);
        } catch (JAXBException e) {
            log.error("Marshalling file", e);
        }
        return file;
    }

    public static <T extends Serializable> T unmarshallContent(String path, Class<T>
            typeArgumentClass) {
        File file = new File(path);
        try {
            T catalog = typeArgumentClass.newInstance();
            JAXBContext context =
                    JAXBContext.newInstance(catalog.getClass());
            Unmarshaller um = context.createUnmarshaller();
            if (file.exists() && !file.isDirectory()) {
                catalog = (T) um.unmarshal(file);
            }
            log.info("unmarshallContent()", catalog);
            return catalog;
        } catch (Exception e) {
            log.error("Marshalling file", e);
        }
        return null;
    }
    public static List<Student> getDataScraping() {
        List<Student> studentList = new ArrayList<>();
        try {
            studentList = scraping.clickData(URI);
            System.out.println(studentList.get(1));
            System.out.println(studentList.get(1).getNickName());
        } catch (Exception e) {
            log.error("getDataScrapping()", e);
        }
        return studentList;
    }

    /**
     * Realiza el scrapping, marshalling, unmarshalling y el contenido devuelto es puesto en una lista.
     */
    public static void getData() {
        Training training = new Training();
        List<Student> studentList = getDataScraping();
        training.setFormations(studentList);
        FileUtils.marshallContent(fileName, training);
        Training training1 = FileUtils.unmarshallContent(fileName, Training.class);
        studentList = training1.getFormations();
    }

}
