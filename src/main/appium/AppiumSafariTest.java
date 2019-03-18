import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class AppiumSafariTest extends AppiumBase {

    @Test
    public void testUntitled() throws Exception {
        driver = new MyiOSDriver(new URL(runOn.getURL() + "/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        if(ConfigManager.checkIfSetTrue("makeReporter"))
            System.out.println((String) driver.getCapabilities().getCapability("reportUrl"));
        driver.get("http://www.google.com/");
        driver.context("WEBVIEW_1");
        driver.findElement(By.xpath("//*[@name='q']")).sendKeys("experitest");
        driver.findElement(By.xpath("//*[@nodeName='svg' and ./parent::*[@nodeName='SPAN']]")).click();
        if(driver.isElementFound(By.xpath("//*[@text='Looking for results in English?']")))
            driver.findElement(By.xpath("//*[@name='q']")).click();
        if(driver.isElementFound(By.xpath("//*[@accessibilityLabel='FavoritesGridCollectionView']")))
            driver.findElement(By.xpath("//*[@name='q']")).click();
        driver.findElement(By.xpath("//*[@text='Experitest' and ./parent::*[@nodeName='DIV' and ./parent::*[@nodeName='A']]]"));
        driver.get("https://www.wikipedia.org/");
        if(!driver.isElementFound(By.xpath("//*[@nodeName='IMG']")))
            throw new Exception("Couldn't find wikipedia homepage picture");
        Assert.assertTrue("Didn't get chinese text",
                (driver.findElement(By.xpath("//*[@nodeName='STRONG' and ./parent::*[@id='js-link-box-zh']]")).getText().equals("中文")));
    }
}