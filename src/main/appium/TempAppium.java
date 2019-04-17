

import com.experitest.appium.SeeTestClient;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class TempAppium {

    private String accessKey = "eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek16SXhNekkzTmpZek9RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDg1NzMyNzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.XsuzHTOHBmFawHabJYTOnr5fbjA17HhzQurSjzBBEnw";
    protected IOSDriver<IOSElement> driver = null;
//    protected AndroidDriver<AndroidElement> driver = null;
    DesiredCapabilities dc = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
//        dc.setCapability("reportDirectory", "reports");
//        dc.setCapability("reportFormat", "xml");
//        dc.setCapability("testName", "Untitled");
//        dc.setCapability("automationName" ,"XCUITest");
        dc.setCapability("deviceQuery", "@serialnumber='00008020-000844AC3408002E'");
        dc.setCapability("accessKey","eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVMU5ERXlNREE1TlRJNU9RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4Njk0ODAwOTUsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.8fOZNwz582zhuFbA08p_ysF94L4v9IzSzfTSYXuhGEk");
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank" ); // If you wish to continue with the app running on the device, comment this line
        driver = new IOSDriver(new URL("http://192.168.2.22:9000/wd/hub"), dc);

    }

    @Test
    public void testUntitled() throws InterruptedException {
        driver.get("https://www.wikipedia.org");
//        driver.context("WEBVIEW_1");
        System.out.println(driver.findElement(By.xpath("//*[contains(@text,'e')]")).getText()+"!");
        System.out.println(driver.findElement(By.xpath("//*[contains(@text,'e')]")).getAttribute("text")+"!");
//        driver.get("www.google.com");
//        long start = System.currentTimeMillis();
//        while((System.currentTimeMillis() - start) / 1000 < 600) {
//            try {
//                driver.findElement(By.xpath("//*[@name='qaa']"));
//            } catch (Exception e) {
//            }
//        }
//        driver.executeScript("seetest:client.setProperty(\"ios.auto.dismiss.alerts\",\"true\")");
//        sleep(10000);
//        driver.findElement(By.xpath("//*[@text='Tracking']")).click();
//        driver.resetApp();
//        driver.executeScript("client:client.deviceAction(\"Unlock\")");
//        driver.executeScript("client:client.setDefaultTimeout(30000)");
//        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    @After
    public void tearDown(){
        driver.quit();
    }

}