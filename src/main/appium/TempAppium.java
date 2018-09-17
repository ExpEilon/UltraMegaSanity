

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
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

public class TempAppium {

    private String accessKey = "eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVek16SXhNekkzTmpZek9RIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NDg1NzMyNzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.XsuzHTOHBmFawHabJYTOnr5fbjA17HhzQurSjzBBEnw";
    protected IOSDriver<IOSElement> driver = null;
    protected String[] images = {"https://image.ibb.co/c94AMK/00000.jpg"
            ,"https://image.ibb.co/mcUo8z/00002.jpg"
            ,"https://image.ibb.co/dw8Foz/00004.jpg"
            ,"https://image.ibb.co/ftezFe/00006.jpg"
            ,"https://image.ibb.co/kQDgTz/00008.jpg"
            ,"https://image.ibb.co/iNXaoz/00010.jpg"
            ,"https://image.ibb.co/jeV6ve/00012.jpg"
            ,"https://image.ibb.co/bzrDae/00014.jpg"
            ,"https://image.ibb.co/doVvoz/00016.jpg"
            ,"https://image.ibb.co/jjCVMK/00018.jpg"
            ,"https://image.ibb.co/e7C88z/00020.jpg"
            ,"https://image.ibb.co/gw0voz/00022.jpg"
            ,"https://image.ibb.co/mqzo8z/00024.jpg"
            ,"https://image.ibb.co/jv1eFe/00026.jpg"
            ,"https://image.ibb.co/evrPgK/00028.jpg"
            ,"https://image.ibb.co/kjTFoz/00030.jpg"
            ,"https://image.ibb.co/b0Y4gK/00032.jpg"
            ,"https://image.ibb.co/j0i4gK/00034.jpg"
            ,"https://image.ibb.co/eOigTz/00036.jpg"
            ,"https://image.ibb.co/gBVT8z/00038.jpg"
            ,"https://image.ibb.co/mymPgK/00040.jpg"
            ,"https://image.ibb.co/nQytae/00042.jpg"
            ,"https://image.ibb.co/i3ORve/00044.jpg"
            ,"https://image.ibb.co/dKW1Tz/00046.jpg"
            ,"https://image.ibb.co/fPkjgK/00048.jpg"
            ,"https://image.ibb.co/cCcx1K/00050.jpg"
            ,"https://image.ibb.co/b3xaoz/00052.jpg"
            ,"https://image.ibb.co/nj9o8z/00054.jpg"
            ,"https://image.ibb.co/kTkT8z/00056.jpg"
            ,"https://image.ibb.co/du3Foz/00058.jpg"};
    DesiredCapabilities dc = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException {
//        System.getProperties().setProperty("javax.net.ssl.trustStore","C:\\Users\\eilon.grodsky\\Desktop\\new keystore\\truststore.jks");
//        System.getProperties().setProperty("javax.net.ssl.trustStorePassword","123456");

//        dc.setCapability("testName", "Quick Start iOS Native Demo");
//        dc.setCapability("accessKey", accessKey);
//        dc.setCapability("reportDirectory", "reports");
//        dc.setCapability("reportFormat", "xml");
        dc.setCapability(MobileCapabilityType.UDID, "2e663adbadbf0f82b25645a5481139b144246dfb");
//        dc.setCapability(MobileCapabilityType.APP, "cloud:com.sideshowagency.Warrior.uat");
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.sideshowagency.Warrior.uat");
        dc.setCapability("instrumentApp", true);
        driver = new IOSDriver<>(new URL("http://localhost:4723/wd/hub"), dc);
    }

    @Test
    public void quickStartiOSNativeDemo()throws Exception {
//        driver.rotate(ScreenOrientation.LANDSCAPE);
//        driver.context("WEBVIEW_1");
//        System.out.println(driver.getPageSource());
//        System.out.println(driver.executeScript("return 1"));
//        new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='start_journey']")));
//        driver.findElement(By.xpath("//*[@id='start_journey']")).click();
//        new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='search_button']")));
//        driver.findElement(By.xpath("//*[@id='search_button']")).click();
//        new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@for='input_customer_id']")));
//        driver.findElement(By.xpath("//*[@for='input_customer_id']")).click();
//        driver.findElement(By.xpath("//*[@id='input_customer_id']")).sendKeys("102725");
//        driver.findElement(By.xpath("//*[@class='icon-search']")).click();
//        driver.findElement(By.xpath("//*[@class='customer_id' and @text='102725']")).click();
//        new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Customer ID']")));
//        driver.findElement(By.xpath("//*[@id='customer_start']")).click();
//        new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='yes_ditto_consent']")));
//        driver.findElement(By.xpath("//*[@id='yes_ditto_consent']")).click();
        driver.executeScript("seetest:client.simulateCapture(\"C:\\\\demo\\\\pics\\\\00000.jpg\")");
        driver.context("NATIVE_APP_INSTRUMENTED");
        driver.findElement(By.xpath("//*[@text='Start capture']")).click();
        driver.context("WEBVIEW_1");
        for(int i=1;i<30;i++){
            Thread.sleep(500);
                driver.executeScript("seetest:client.simulateCapture("+ images[i]+")");
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}