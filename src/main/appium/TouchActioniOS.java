
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TouchActioniOS {

    AppiumDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
//        dc.setCapability("reportDirectory", "reports");
//        dc.setCapability("reportFormat", "xml");
//        dc.setCapability("testName", "Untitled");
//        boolean local = false;
//        String url = local ? "http://localhost:4723/wd/hub" : "http://192.168.2.22:9000/wd/hub";
//        String sn = local ? "aa3e9a95" : "7b2ae069a67d40cb46075c7ba03c15bbee364f4b";
//        String username = local ? "khaleda" : "admin";
//        String password = local ? "Experitest2012" : "Qwer1234";
//        dc.setCapability(MobileCapabilityType.UDID, sn);
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
//        driver = new IOSDriver<>(new URL(url), dc);
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        dc.setCapability(MobileCapabilityType.UDID, "f0c8509b4fb35257549154f79b0d62a72f76f02d");
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.Maps");
        dc.setCapability("appiumVersion", "1.8");
        dc.setCapability("username", "khaleda");
        dc.setCapability("password", "Experitest2012");
        System.out.println("driver will be created");
//        driver = new IOSDriver(new URL("https://qa-win2016.experitest.com:443/wd/hub"), dc);
        driver = new IOSDriver<>(new URL("http://releasecloud:80/wd/hub"), dc);
        System.out.println("driver has created");
    }

    @Test
    public void testUntitled() {
//        new TouchAction(driver).press(807, 1324).waitAction(Duration.ofMillis(1000)).moveTo(310, 1435).release().perform();
//
//        driver.executeScript("seetest:client.dragCoordinates(\"200\", \"200\", \"400\", \"400\", \"2000\")");
//        driver.executeScript("seetest:client.dragCoordinates(\"600\", \"600\", \"800\", \"800\", \"2000\")");
//        driver.executeScript("seetest:client.dragCoordinates(\"600\", \"600\", \"400\", \"400\", \"2000\")");
        new TouchAction(driver).press(600,600).waitAction(1000).moveTo(800,800).release().perform();
        new TouchAction(driver).press(1000,1000).waitAction(1000).moveTo(1200,1200).release().perform();
        new TouchAction(driver).press(1000,1000).waitAction(1000).moveTo(600,600).release().perform();
        new TouchAction(driver).press(800,800).waitAction(1000).moveTo(400,400).release().perform();

//        new TouchAction(driver).press(600,600).waitAction(1000).moveTo(800,800).release().perform();
//        new TouchAction(driver).press(200,200).waitAction(1000).moveTo(400,400).release().perform();
//        new TouchAction(driver).press(200,200).waitAction(1000).moveTo(400,400).release().perform();
//        new TouchAction(driver).press(200,200).waitAction(1000).moveTo(400,400).release().perform();
//        new TouchAction(driver).press(200,200).waitAction(1000).moveTo(400,400).release().perform();

    }
    @After
    public void tearDown(){
        driver.quit();
    }

}

