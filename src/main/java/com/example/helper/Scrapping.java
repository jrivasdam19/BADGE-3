package com.example.helper;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Scrapping {
    static Logger log = LoggerFactory.getLogger(Scrapping.class);

    private static Scrapping instance = null;
    private static WebDriver webDriver;
    private static String uri = "https://student-chart.herokuapp.com/";
    private static String driver = "webdriver.chrome.driver";
    private static String driverEXE = "C:\\geckodriver-master\\chromedriver.exe";
    private static ArrayList<StudentScrapping> studentList = new ArrayList<>();
    private static String education[] = {"FP", "FPD"};
    private static String tagListClick[] = {"#rc-tabs-0-tab-subjAccesoDatos", ".ant-pagination-item-2",
            ".ant-pagination-item-3", "rc-tabs-0-tab-subjSAD Dual", ".ant-pagination-item-link"};
    private static String elementTagList[] = {".ant-table-row.ant-table-row-level-0", "ant-pagination-item-link"};

    public static Scrapping getInstance() {
        if (instance == null) {
            instance = new Scrapping();
        }
        return instance;
    }

    private Scrapping() {
        super();
    }

    /**
     * Hace scrapping a un página en particular.
     *
     * @param uri página para hacer srappping.
     * @return lista de StudentScrapping.
     */
    public static List<StudentScrapping> clickData(String uri) {
        try {

            System.setProperty(driver, driverEXE);
            webDriver = new ChromeDriver();
            webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            webDriver.get(uri);
            /*click AccesoDatos*/
            WebElement getClickAccesoDatos = webDriver.findElement(By.cssSelector(tagListClick[0]));
            getClickAccesoDatos.click();
            List<WebElement> data1 = webDriver.findElements(By.cssSelector(elementTagList[0]));
            getData(data1, education[0]);
            /*click botón 2*/
            WebElement getClickAccesoDatosNum2 = webDriver.findElement(By.cssSelector(tagListClick[1]));
            getClickAccesoDatosNum2.click();
            List<WebElement> data2 = webDriver.findElements(By.cssSelector(elementTagList[0]));
            getData(data2, education[0]);
            /*click botón 3*/
            WebElement getClickAccesoDatosNum3 = webDriver.findElement(By.cssSelector(tagListClick[2]));
            getClickAccesoDatosNum3.click();
            List<WebElement> data3 = webDriver.findElements(By.cssSelector(elementTagList[0]));
            getData(data3, education[0]);

            webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            webDriver.get(uri);
            /*click SAD Dual*/
            WebElement getClickDual = webDriver.findElement(By.id(tagListClick[3]));
            getClickDual.click();
            List<WebElement> data4 = webDriver.findElements(By.cssSelector(elementTagList[0]));
            getData(data4, education[1]);
            /*click botón next*/
            List<WebElement> getClickAccesoDatosNext;
            getClickAccesoDatosNext = webDriver.findElements(By.className(elementTagList[1]));
            getClickAccesoDatosNext.get(1).click();
            List<WebElement> data5 = webDriver.findElements(By.cssSelector(elementTagList[0]));
            getData(data5, education[1]);

            webDriver.quit();
            return studentList;
        } catch (Exception e) {
            log.error("clickData()", e);
        }
        return null;
    }

    /**
     * Coge el contenido de cada elemento/fila y lo coloca en cada Student.
     *
     * @param data lista de elementos.
     * @param fp   String que identifica si un Student pertenece a FP O FPD.
     */
    public static void getData(List<WebElement> data, String fp) {
        String valueTagList[] = {".ant-table-cell>a", ".ant-table-cell span", ".badge-status.error > span",
                ".ant-table-cell .ant-row .ant-tag > span"};
        String name, exam, badge, finalMark, trainingData, emptyValue;
        name = StringUtils.EMPTY;
        exam = StringUtils.EMPTY;
        badge = StringUtils.EMPTY;
        finalMark = StringUtils.EMPTY;
        trainingData = StringUtils.EMPTY;
        emptyValue = "-1";

        for (WebElement e : data) {
            StudentScrapping student = new StudentScrapping();

            if (fp.equals(education[0])) {
                log.info("fp");
                name = e.findElement(By.cssSelector(valueTagList[0])).getText();
                name = cleanDataString(name);
                log.info(name);
                exam = e.findElement(By.cssSelector(valueTagList[1])).getText();
                exam = cleanDataNum(exam);
                log.info(exam);
                /* BADGE*/
                List<WebElement> badges = e.findElements(By.cssSelector(valueTagList[2]));
                WebElement span = badges.get(2);
                String fileCreator = span.getText();
                System.out.println("-- badges " + fileCreator + " *** " + span.getTagName() + "  --  " + badges.size());
                badge = cleanDataNum(fileCreator);
                log.info(badge);
                finalMark = e.findElement(By.cssSelector(valueTagList[3])).getText();
                finalMark = cleanDataNum(finalMark);
                log.info(finalMark);
                trainingData = education[0];
//                trainingData = cleanDataNum(finalMark);
                log.info(trainingData);
            } else {
                log.info("fpd");
                name = e.findElement(By.cssSelector(valueTagList[0])).getText();
                name = cleanDataString(name);
                log.info(name);
                exam = e.findElement(By.cssSelector(valueTagList[1])).getText();
                exam = cleanDataNum(exam);
                badge = emptyValue;
                log.info(badge);
                finalMark = e.findElement(By.cssSelector(valueTagList[3])).getText();
                finalMark = cleanDataNum(finalMark);
                log.info(finalMark);
                trainingData = education[1];
//                trainingData = cleanDataNum(finalMark);
                log.info(trainingData);
            }
            student.setNickName(name);
            student.setExam(Integer.parseInt(exam));
            student.setBadge(Integer.parseInt(badge));
            student.setFinalMark(Integer.parseInt(finalMark));
            student.setEducation(trainingData);
            studentList.add(student);
        }
    }

    /**
     * Quita los espacios y coge el varlor indicado.
     *
     * @param dataNum número que contiene otros caracteres.
     * @return un número en String.
     */
    public static String cleanDataNum(String dataNum) {
        String regExNum = "[^0-9]";
        String correctData = "-1";
        boolean slash = StringUtils.containsAny(dataNum, "/");

        String string = RegExUtils.removeAll(dataNum, Pattern.compile(regExNum));
        if (!slash) {
            if (StringUtils.isNumeric(string)) {
                correctData = string;
            }
        } else {
            if (StringUtils.isNumeric(string)) {
                String string0 = string.substring(0, 1);
                correctData = string0;
            }
        }
        return correctData;
    }

    /**
     * Quita los espacios y coge el varlor indicado.
     *
     * @param dataString Cadena de caracteres a limpiar.
     * @return Cadena de caracteres.
     */
    public static String cleanDataString(String dataString) {
        String correctData = "null";

        if (!(dataString == StringUtils.EMPTY) & !(dataString == null)) {
            String string = dataString.trim();
            correctData = string;
        }
        return correctData;
    }


    /**
     * PRORBLEMAS - BORRAR CODIGO - CLICK
     */
    public static void problem(String uri) {
        try {
            System.setProperty(driver, driverEXE);
            WebDriver driver = null;
            driver = new ChromeDriver();
            driver.get(uri);
            WebElement WebElementClick = driver.findElement(By.id(tagListClick[0]));
            WebElementClick.click();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PRORBLEMAS - BORRAR CODIGO - TAG
     */
    public static void problem1(String uri) {
        try {
            System.setProperty(driver, driverEXE);
            WebDriver driver = null;
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            driver.get(uri);
            WebElement WebElementClick = driver.findElement(By.id(tagListClick[0]));
            WebElementClick.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void click(String uri) {
        try {
            System.setProperty(driver, driverEXE);
            webDriver = new ChromeDriver();
            webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            webDriver.get(uri);

            WebElement getClickAccesoDatos = webDriver.findElement(By.cssSelector(tagListClick[0]));
            getClickAccesoDatos.click();
            WebElement getClickAccesoDatosNum2 = webDriver.findElement(By.cssSelector(tagListClick[1]));
            getClickAccesoDatosNum2.click();
            WebElement getClickAccesoDatosNum3 = webDriver.findElement(By.cssSelector(tagListClick[2]));
            getClickAccesoDatosNum3.click();

            WebElement getClickDual = webDriver.findElement(By.id(tagListClick[3]));
            getClickDual.click();
            getClickAccesoDatosNum2 = webDriver.findElement(By.cssSelector(tagListClick[4]));
            getClickAccesoDatosNum2.click();

            webDriver.quit();

        } catch (Exception e) {
            log.error("click()", e);
        }
    }

}
