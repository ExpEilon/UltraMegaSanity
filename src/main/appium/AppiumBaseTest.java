import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
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

public class AppiumBaseTest {

    MyiOSDriver driver;
    DesiredCapabilities dc;
    String query = ((MyThread)Thread.currentThread()).getQuery();
    long start,end;

    boolean isGrid = MyProperties.runOn.isGrid;
    boolean isSimulator = query.contains("emulator") ? true : false;
    @Before
    public void setUp(){
        dc = new DesiredCapabilities();
//        dc.setCapability("autoGrantPermissions",true);
//        dc.setCapability("autoDismissAlerts",true);
        if(isGrid)
            dc.setCapability("accessKey", MyProperties.runOn.AK);
        dc.setCapability("deviceQuery", query);
        if(MyProperties.makeReporter) {
            dc.setCapability("reportDirectory", "reports");
            dc.setCapability("reportFormat", "xml");
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
