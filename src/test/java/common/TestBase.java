package common;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import library.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.Theme;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class TestBase {

    public static Properties properties;
    public static Date date = new Date();
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    public static String dt = formatter.format(date);
    public static FileReader fileReader;
    protected final Logger logger = LogManager.getLogger(getClass());
    public static ExtentSparkReporter spark;
    public static ExtentTest test;
    public static ExtentReports extent;
    public static JsonFormatter json;
    public static String reportDestination = "reports/report_" + dt + ".html";
    private static final String JSON_ARCHIVE = "target/json/jsonArchive.json";
    public static WebDriver webdriver = new WebDriver();
    public static AndroidDriver driver;
    public static URL url;


    @BeforeTest
    public static void setup() throws MalformedURLException {

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.APPLICATION_NAME, properties.getProperty("APPLICATION_NAME"));
        cap.setCapability(MobileCapabilityType.UDID, properties.getProperty("UDID"));
        cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, properties.getProperty("APP_PACKAGE"));
        cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, properties.getProperty("APP_ACTIVITY"));
        url = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver(url, cap);

    }

    @BeforeSuite(alwaysRun = true)
    public void reportSetup() throws IOException {

        propertiesLoad();
        extentSparkReport();
    }

    @AfterSuite
    public void reportTearDown() {
        extent.flush();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getName() + " test case is failed. " + "<span class='badge badge-danger'> Fail </span>" + result.getThrowable());

        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip(result.getName() + " test case is skipped." + "<span class='badge badge-warning'> Skip </span>");

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass(result.getName() + " test case is Passed." + "<span class='badge badge-success'> Success </span>");
        }
    }

    public void extentSparkReport() {

        spark = new ExtentSparkReporter(reportDestination);
        extent = new ExtentReports();
        extent.attachReporter(spark);
        json = new JsonFormatter(JSON_ARCHIVE);

        extent.setSystemInfo("Environment", properties.getProperty("Environment"));
        extent.setSystemInfo("Device", properties.getProperty("Device"));
        extent.setSystemInfo("ModelName", properties.getProperty("ModelName"));
        spark.config().setDocumentTitle("Dopay Automation Testing Report : Enterprise");
        spark.config().setReportName("Enterprise Automation Test Suite");
        spark.config().setTimelineEnabled(Boolean.TRUE);
        spark.config().setOfflineMode(Boolean.TRUE);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        spark.config().setTimelineEnabled(Boolean.TRUE);
    }

    public void propertiesLoad() throws IOException {

        try {
            fileReader = new FileReader("config/Application.properties");
            properties = new Properties();
            properties.load(fileReader);

        } catch (FileNotFoundException ex) {
            test.info("*************************************************");
            test.info("Property file you are looking for does not exist.");
            test.info("*************************************************");
        }
    }
}
