import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import org.junit.Test;
import org.openqa.selenium.ScreenOrientation;

import java.net.MalformedURLException;
import java.net.URL;

public class ShortTestAppium extends AppiumBase {
    @Test
    public void ShortTestAppium() throws MalformedURLException {
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.mobilesafari" );
        driver = new MyiOSDriver(new URL("http://192.168.2.22:9000/wd/hub"), dc);
        driver.context("WEBVIEW_1");
        System.out.println(driver.getPageSource());
//        driver.executeScript("client:client.deviceAction(\"Unlock\")");
//        driver.executeScript("client:client.setDefaultTimeout(30000)");
//        driver.rotate(ScreenOrientation.PORTRAIT);
    }
}
