
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.xpath.operations.Bool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Costumer {

    AppiumDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        boolean local = false;
        String url = local ? "https://qacloud.experitest.com/wd/hub" : "https://192.168.2.22/wd/hub";
        String username = local ? "khaleda" : "admin";
        String password = local ? "Experitest2012" : "Qwer1234";
        dc.setCapability("username", username);
        dc.setCapability("password", password);
//        dc.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
        dc.setCapability("deviceQuery", "@e306fb3224fe2fbef2d1eb60118ee4ad7b7bf902'");
//        dc.setCapability(MobileCapabilityType.UDID, "DDF5D982-95B7-4D7B-9230-4CA877B266D6");
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.Maps");
//        dc.setCapability("appiumVersion", "1.8");
//        dc.setCapability("newCommandTimeout", "600");
        // In case your user is assigned to a single project leave empty, otherwise please specify the project name
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.mobilesafari");
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
//        dc.setCapability("reportDirectory", "reports");
//        dc.setCapability("reportFormat", "xml");
//        dc.setCapability("testName", "IOSDemoTest");
        System.out.println(dc.toString());
        driver = new IOSDriver<>(new URL(url), dc);
        System.out.println("driver has created");
//        System.out.println((String) driver.getCapabilities().getCapability("reportUrl"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void testUntitled() throws InterruptedException {
        System.out.println(driver.isAppInstalled("com.experitest.ExperiBank"));
        driver.rotate(ScreenOrientation.LANDSCAPE);
        System.out.println(driver.getOrientation());
//        driver.executeScript("seetest:client.setShowReport(false)");
//        driver.findElement(By.xpath("//*[@id='Settings']")).click();
//        driver.executeScript("seetest:client.setShowReport(true)");
//        driver.findElement(By.xpath("//*[@id='Setti12ngs']")).click();
//        driver.get("http://facebook.com");//*[@name='q']
//        System.out.println(driver.executeScript("result = window==null") instanceof Boolean);
//        driver.findElement(By.xpath("//*[@id='m_login_email']")).isEnabled();
//        driver.findElement(By.xpath("//*[@id='m_login_email']")).sendKeys("abc");
//        driver.findElement(By.xpath("//*[@id='u_0_5']")).click();
//        Thread.currentThread().sleep(5000);
//        new TouchAction(driver).press(807, 1324).waitAction(Duration.ofMillis(1000)).moveTo(310, 1435).release().perform();
//
//        driver.executeScript("seetest:client.dragCoordinates(\"200\", \"200\", \"400\", \"400\", \"2000\")");
//        driver.executeScript("seetest:client.dragCoordinates(\"600\", \"600\", \"800\", \"800\", \"2000\")");
//        driver.executeScript("seetest:client.dragCoordinates(\"600\", \"600\", \"400\", \"400\", \"2000\")");
//        new TouchAction(driver).press(600,600).waitAction(1000).moveTo(800,800).release().perform();
//        new TouchAction(driver).press(1000,1000).waitAction(1000).moveTo(1200,1200).release().perform();
//        new TouchAction(driver).press(1000,1000).waitAction(1000).moveTo(600,600).release().perform();
//        new TouchAction(driver).press(800,800).waitAction(1000).moveTo(400,400).release().perform();

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

