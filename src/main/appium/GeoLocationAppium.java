import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.html5.Location;
import java.net.MalformedURLException;
import java.net.URL;

public class GeoLocationAppium  extends AppiumBase{
    @Test
    public void testUntitled() throws MalformedURLException{
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.UICatalog:2629");
        dc.setCapability("autoGrantPermissions", "true");
        dc.setCapability("autoAcceptAlerts", "true");
        driver = new MyiOSDriver(new URL(runOn.getURL() + "/wd/hub"), dc);

//        driver.setLocation(new Location(32,24,24));
//        Location loc=driver.location();
//        Assert.assertTrue("Location wasn't Set ",loc.getLatitude()==32 && loc.getLongitude()==24 && loc.getAltitude()==24);
        driver.waitForElement(By.id("Map"),5000);
        driver.findElement(By.id("Map")).click();
        driver.setLocation(new Location(49, 123, 10));
        driver.waitForElement(By.id("coordinatesInfo"),5000);
    }
}