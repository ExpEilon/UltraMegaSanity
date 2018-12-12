

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
        dc.setCapability("deviceQuery", "@serialnumber='f0c8509b4fb35257549154f79b0d62a72f76f02d'");
        dc.setCapability("accessKey","eyJ4cC51IjoyMTgxLCJ4cC5wIjoyLCJ4cC5tIjoiTUEiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjQ2NzU2NDMxMDEsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.4ctoGGxmnD7-s1bbdi7HOUrsyPrVNgwx8VgLKZDn26I");
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.Maps" ); // If you wish to continue with the app running on the device, comment this line
        driver = new IOSDriver(new URL("https://qacloud.experitest.com:443/wd/hub"), dc);

    }

    @Test
    public void testUntitled() throws InterruptedException {
        driver.executeScript("seetest:client.setProperty(\"ios.auto.dismiss.alerts\",\"true\")");
        sleep(10000);
        driver.findElement(By.xpath("//*[@text='Tracking']")).click();
    }

    @After
    public void tearDown(){
        driver.quit();
    }

}