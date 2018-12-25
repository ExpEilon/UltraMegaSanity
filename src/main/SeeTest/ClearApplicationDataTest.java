import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ClearApplicationDataTest extends SeeTestBase {
    String clearPasswordApp = "Experitest.FingerPrintApp";
    String clearContainerApp = "com.experitest.ExperiBank";
    String clearSettingApp = "com.experitest.UICatalog";

    @Test
    public void clearPassword() {
        this.installedInstrumented(clearPasswordApp);
        client.install(System.getProperty("user.dir")+"\\apps\\FingerPrintApp.ipa",true,false);
        client.launch(clearPasswordApp, true, true);
        client.click("NATIVE","//*[@accessibilityLabel='Delete Password']",0,1);
        client.click("NATIVE", "//*[@accessibilityLabel='Finger Print Type III']", 0, 1);
        client.elementSendText("NATIVE", "//*[@class='_UIAlertControllerTextField']", 0, "123456");
        client.click("NATIVE", "//*[@accessibilityLabel='OK']", 0, 1);
        client.click("NATIVE", "//*[@accessibilityLabel='Finger Print Type III']", 0, 1);
        Assert.assertTrue("Password wasn't saved!\n",!client.isElementFound("NATIVE", "//*[@accessibilityLabel='Enter password to save']", 0));
        client.applicationClearData(clearPasswordApp);
        client.launch(clearPasswordApp, true, true);
        client.click("NATIVE", "//*[@accessibilityLabel='Finger Print Type III']", 0, 1);
        Assert.assertTrue("Keychain wasn't cleared!\n",client.isElementFound("NATIVE", "//*[@accessibilityLabel='Enter password to save']", 0));
    }

    @Ignore
    @Test
    public void clearContainer() throws IOException {
        if (isGrid && !isSimulator) {//we have api to get container only from cloud, can't use textIn command on simulators
            long containerSize;
            String accountAmount;
            client.install(System.getProperty("user.dir")+"\\apps\\EriBank.ipa",true,false);
            loginEriBank();
            makePayment();
            client.applicationClearData(clearContainerApp);
            containerSize = Files.size(Paths.get(getContainer(clearContainerApp)));
            Assert.assertTrue("Container seems too large\n", containerSize < 1000);
            loginEriBank();
            accountAmount = client.getTextIn("NATIVE","xpath=//*[@text='Make Payment']", 0, MyProperties.instrumented ? "WEB" : "NATIVE", "Up", 0, 0);
            Assert.assertTrue("Amount of money in EriBank didn't reset\n",!accountAmount.contains("100.00"));
        }
    }

    @Test
    public void clearSettings() throws InterruptedException {
        if(!installedInstrumented(clearSettingApp))
            client.install(clearSettingApp,true,false);
        client.launch("com.apple.Preferences",true,true);
        client.swipeWhileNotFound("DOWN",100,100,"NATIVE","//*[@text='UICatalog' and @class='UIAView' and @onScreen='true']",0,100,10,true);
        sleep(1000);
        if(client.elementGetProperty("NATIVE", "//*[@class='UIASwitch']", 0, "value").equals("1"))
            client.click("NATIVE","//*[@class='UIASwitch']",0,1);
        client.applicationClearData(clearSettingApp);
        client.launch(clearSettingApp,true,true);
        client.launch("com.apple.Preferences",true,true);
        client.swipeWhileNotFound("DOWN",100,100,"NATIVE","//*[@text='UICatalog' and @class='UIAView' and @onScreen='true']",0,100,10,true);
        sleep(1000);
        Assert.assertTrue("Settings wasn't cleared\n",client.elementGetProperty("NATIVE", "//*[@class='UIASwitch']", 0, "value").equals("1"));
    }

    private void loginEriBank(){
        client.launch(clearContainerApp,true,true);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='usernameTextField']", 0, "company");
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='passwordTextField']", 0, "company");
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Login']", 0, 1);
        client.waitForElement("NATIVE","xpath=//*[@text='Make Payment']",0,10000);
    }

    private void makePayment() {
        client.click("NATIVE", "xpath=//*[@text='Make Payment']", 0, 1);
        client.waitForElement("NATIVE","xpath=//*[@accessibilityLabel='Phone']",0,10000);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Phone']", 0, "0523898058");
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Name']", 0, "Eilon");
        double payment = new Random().nextInt(2000) - 1000;
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Amount']", 0, Double.toString(payment));
        client.click("NATIVE","xpath=//*[@accessibilityLabel='Select']", 0, 1);
        client.elementListSelect("accessibilityLabel=conutryView", "accessibilityLabel=Tanzania", 0, true);
        client.waitForElement("NATIVE", "xpath=//*[@accessibilityLabel='Send Payment']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Send Payment']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@text='Yes']", 0, 2000);
        client.click("NATIVE", "xpath=//*[@text='Yes']", 0, 1);
    }
}
