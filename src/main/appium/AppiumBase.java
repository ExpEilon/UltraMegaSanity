import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppiumBase extends BaseTest{

    MyiOSDriver driver;
    DesiredCapabilities dc;

    @Before
    public void setUp(){
        dc = new DesiredCapabilities();
//        dc.setCapability("autoGrantPermissions",true);
//        dc.setCapability("autoDismissAlerts",true);
        dc.setCapability("deviceName","auto");
        dc.setCapability("automationName" ,"XCUITest");
        if(isGrid)
            dc.setCapability("accessKey", runOn.getAccesskey());
        dc.setCapability(MobileCapabilityType.UDID, device.getSN());
        if(ConfigManager.checkIfSetTrue("makeReporter")) {
            dc.setCapability("reportDirectory", "reports");
            dc.setCapability("reportFormat", "xml");
        }
        if(ConfigManager.checkIfSetTrue("appiumDocker")){
            dc.setCapability("appiumVersion", "1.10.0-p0");
        }
//        dc.setCapability("testName", "IOSDemoTest");
        start = System.currentTimeMillis();
    }

    @After
    public void tearDown(){
        driver.quit();
        end = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).setDuration((end-start)/1000);
    }

}
