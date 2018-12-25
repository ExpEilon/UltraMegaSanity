import com.experitest.appium.SeeTestClient;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class ClearApplicationDataTestAp extends AppiumBase {

    String clearPasswordApp = "Experitest.FingerPrintApp";
    String clearContainerApp = "com.experitest.ExperiBank";
    String clearSettingApp = "com.experitest.UICatalog";

    @Test
    public void clearPassword() throws MalformedURLException {
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, clearPasswordApp);
        dc.setCapability("instrumentApp", true);
        driver = new MyiOSDriver(new URL(StartPanel.runOn.getURL() + "/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@accessibilityLabel='Delete Password']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='Finger Print Type III']")).click();
        driver.findElement(By.xpath("//*[@class='_UIAlertControllerTextField']")).sendKeys("123456");
        driver.findElement(By.xpath("//*[@accessibilityLabel='OK']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='Finger Print Type III']")).click();
        Assert.assertTrue("Password wasn't saved!\n",!driver.isElementFound(By.xpath("//*[@accessibilityLabel='Enter password to save']")));
        driver.resetApp();
        SeeTestClient seeTestClient = new SeeTestClient(driver);

        seeTestClient.launch(clearPasswordApp, true, true);
        driver.findElement(By.xpath("//*[@accessibilityLabel='Finger Print Type III']")).click();
        Assert.assertTrue("Keychain wasn't cleared!\n",driver.isElementFound(By.xpath("//*[@accessibilityLabel='Enter password to save']")));
    }
    @Ignore
    @Test
    public void clearContainer() throws IOException {
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, clearContainerApp);
        dc.setCapability("instrumentApp", true);
        driver = new MyiOSDriver(new URL(StartPanel.runOn.getURL() + "/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        if (isGrid && !isSimulator) {//we have api to get container only from cloud, can't use textIn command on simulators
            long containerSize;
            String accountAmount;
//            client.install(System.getProperty("user.dir")+"\\apps\\EriBank.ipa",true,false);
//            loginEriBank();
//            makePayment();
            driver.resetApp();
            containerSize = Files.size(Paths.get(getContainer(clearContainerApp)));
            Assert.assertTrue("Container seems too large\n", containerSize < 1000);
//            loginEriBank();
//            accountAmount = client.getTextIn("NATIVE", StartPanel.instrumented ? "xpath=//*[@text='Make Payment']" : "xpath=//*[@text='makePaymentButton']", 0, StartPanel.instrumented ? "WEB" : "NATIVE", "Up", 0, 0);
//            Assert.assertTrue("Amount of money in EriBank didn't reset\n",!accountAmount.contains("100.00"));
        }
    }

    @Test
    public void clearSettings() throws MalformedURLException {
//        if(!installedInstrumented(clearSettingApp))
//            client.install(clearSettingApp,true,false);
//        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.Preferences");
        driver = new MyiOSDriver(new URL(StartPanel.runOn.getURL() + "/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        SeeTestClient seeTestClient = new SeeTestClient(driver);
//        seeTestClient.swipeWhileNotFound("DOWN",100,100,"NATIVE","//*[@text='UICatalog' and @class='UIAView' and @onScreen='true']",0,100,10,true);
//        if(driver.findElement(By.xpath("//*[@class='UIASwitch']")).getAttribute("value").equals("1"))
//            driver.findElement(By.xpath("//*[@class='UIASwitch']")).click();
        new SeeTestClient(driver).applicationClearData(clearSettingApp);
//        seeTestClient.launch(clearSettingApp,true,true);
//        seeTestClient.launch("com.apple.Preferences",true,true);
//        seeTestClient.swipeWhileNotFound("DOWN",100,100,"NATIVE","//*[@text='UICatalog' and @class='UIAView' and @onScreen='true']",0,100,10,true);
//        Assert.assertTrue("Settings wasn't cleared\n",driver.findElement(By.xpath("//*[@class='UIASwitch']")).getAttribute("value").equals("0"));
    }
//
    private void loginEriBank(){
        driver.findElement(By.xpath("//*[@accessibilityIdentifier='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@accessibilityIdentifier='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@accessibilityLabel='Login']")).click();
//        client.waitForElement("NATIVE","xpath=//*[@text='Make Payment']",0,10000);
    }
//
//    private void makePayment() {
//        client.click("NATIVE", "xpath=//*[@text='Make Payment']", 0, 1);
//        client.waitForElement("NATIVE","xpath=//*[@accessibilityLabel='Phone']",0,10000);
//        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Phone']", 0, "0523898058");
//        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Name']", 0, "Eilon");
//        double payment = new Random().nextInt(2000) - 1000;
//        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Amount']", 0, Double.toString(payment));
//        client.click("NATIVE","xpath=//*[@accessibilityLabel='Select']", 0, 1);
//        client.elementListSelect("accessibilityLabel=conutryView", "accessibilityLabel=Tanzania", 0, true);
//        client.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='Send Payment']", 0, 10000);
//        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Send Payment']", 0, 1);
//        client.waitForElement("NATIVE", "xpath=//*[@text='Yes']", 0, 2000);
//        client.click("NATIVE", "xpath=//*[@text='Yes']", 0, 1);
//    }

}
