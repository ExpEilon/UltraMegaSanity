//package <set your test package>;
import com.experitest.appium.SeeTestClient;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EriBankAppium extends AppiumBase {

    @Test
    public void testUntitled() throws MalformedURLException, InterruptedException {
        driver = new MyiOSDriver(new URL(runOn.getURL() + "/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        if(driver.isAppInstalled("com.experitest.ExperiBank"))
            driver.executeScript("seetest:client.uninstall(\"com.experitest.ExperiBank\")");
        driver.installApp(isGrid? "cloud:com.experitest.ExperiBank":"com.experitest.ExperiBank");
        driver.executeScript("seetest:client.launch(\"com.experitest.ExperiBank\",\""+MyProperties.instrumented+"\",\"true\")");
//        if(isSimulator) Thread.sleep(10000);
        driver.findElement(By.xpath("//*[@placeholder='Username']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@placeholder='Password']")).sendKeys("company");
        driver.hideKeyboard();
        driver.findElement(By.xpath(MyProperties.instrumented ? "//*[@accessibilityLabel='loginButton']" : "//*[@text='loginButton']")).click();
        double before = Double.parseDouble(driver.findElement(By.xpath("//*[@class='UIAView' and ./*[@class='UIAStaticText']]")).getText().split(":")[1].split("\\$")[0]);
        driver.findElement(By.xpath(MyProperties.instrumented ? "//*[@accessibilityLabel='Make Payment']":"//*[@text='makePaymentButton']")).click();
//        driver.executeScript("seetest:client.setProperty(\"ios.element-send-text.use.xcautomation\", false)");
        driver.findElement(By.xpath("//*[@placeholder='Phone']")).sendKeys("0523898058");
        driver.findElement(By.xpath("//*[@placeholder='Name']")).sendKeys("Eilon");
        double payment = new Random().nextInt(2000) - 1000;
        driver.findElement(By.xpath("//*[@placeholder='Amount']")).sendKeys(Double.toString(payment));
        driver.findElement(By.xpath("//*[@text='countryButton']")).click();
//        driver.swipe(260, 881, 269, 12, 128);
        driver.findElement(By.xpath("//*[@text='Italy']")).click();
        driver.executeScript("seetest:client.setProperty(\"ios.auto.dismiss.alerts\",false)");
        driver.findElement(By.xpath("//*[@text='sendPaymentButton']")).click();
//        if(isSimulator) Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        double after = Double.parseDouble(driver.findElement(By.xpath("//*[@class='UIAView' and ./*[@class='UIAStaticText']]")).getText().split(":")[1].split("\\$")[0]);
        Assert.assertTrue("The values don't match!",before-after==payment);
    }

}