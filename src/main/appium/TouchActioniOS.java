
import com.experitest.appium.SeeTestClient;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class TouchActioniOS {
//    protected AndroidDriver<AndroidElement> driver = null;
    AppiumDriver driver;
    SeeTestClient seeTestClient;
    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.UDID, "45a8ec778b1e6eb400e828cb989be9934fc03a8b");
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.UICatalog");
//        dc.setCapability("appiumVersion", "1.8");
//        dc.setCapability("username", "khaleda");
//        dc.setCapability("password", "Experitest2012");
//        dc.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
//        dc.setCapability("startIWDP", true);
        System.out.println("driver will be created");
        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
        System.out.println("driver has created");
        seeTestClient = new SeeTestClient(driver);
    }

    @Test
    public void testUntitled() throws InterruptedException {
        driver.context("WEBVIEW_1");
        WebElement searchBar = driver.findElement(By.xpath("//*[@name='q']"));
        searchBar.sendKeys("Experitest");
        searchBar.sendKeys(Keys.ENTER);

        //AS-25801 test for input String/Int/Null/List/WebElement/boolean/Undefined
//        driver.get("http://www.google.com");
//        driver.context("WEBVIEW_1");
//        List<Long> l = (List<Long>) driver.executeScript("return [1,'abc','abc']");

//        driver.executeScript("return [1,'abc']");
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click()", ele);
//        driver.switchTo().frame("iframeWithSameDomain");//html/body/IFRAME[2]/html/body/div[1]/p
//        driver.get("http://192.168.4.85:8060/html-tests/RegressionHibridApplication/iFramePage.html");
//        WebElement ele = driver.findElement(By.xpath("//*[@id='btn1']"));
//        WebElement ele = (WebElement)driver.executeScript("return document.getElementsByTagName(arguments[0])[arguments[1]]", "p",0);
//        ((JavascriptExecutor)driver).executeScript("if(arguments[0] == 1){\n" +
//                "    alert('hi')\n" +
//                "}",1);

//        WebElement ele = (WebElement)driver.executeScript("return document.getElementsByTagName(arguments[0])[arguments[1]]", "p","1");
//        ((JavascriptExecutor)driver).executeScript("arguments[0].style.color = arguments[1]", ele,"red");
//        System.out.println(driver.executeScript("return document.getElementsByClassName('ctr-p')[0]"));
//        WebElement ele = (WebElement) driver.executeScript("return document.getElementById(arguments[0]);","SBmmZd");
//        ((JavascriptExecutor)driver).executeScript("arguments[0].style.backgroundColor = arguments[1]", ele,"red");
//        sleep(10000);
//        WebElement ele = driver.findElement(By.xpath("//*[@nodeName='BUTTON' and ./parent::*[@nodeName='FIELDSET']]"));
//        ele.click();
//        ((JavascriptExecutor)driver).executeScript("arguments[0]‌.click()", ele);
//        System.out.println(seeTestClient.getVisualDump("Web:class=EjeHYb"));
//        driver.findElement(By.xpath("//*[@accessibilityLabel='Web']")).click();
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

