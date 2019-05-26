import org.junit.Assert;
import org.junit.Test;

public class ShortTest extends SeeTestBase {

    String appName = "com.apple.Preferences";
    String pathToApp = System.getProperty("user.dir")+ "\\apps\\" + (isSimulator ? "ExperiBankSimulator.app.zip" :"EriBank.ipa");
    String app = "com.experitest.ExperiBank";
    @Test
    public void shortTest() {
//        client.install("cloud:uniqueName=chasepaypos_ios_q5_12202018",false,false);
//        client.applicationClearData(app);
//        client.install(pathToApp,true,false);
        client.launch(app, true, true);
//        client.getProperty("automator.version");
//        client.launch("http://www.wikipedia.org", true, false);
//        Assert.assertTrue(client.waitForElement("Web", "xpath=//*[@alt='Wikipedia']", 0, 10000));
//        Assert.assertEquals("{\"resultType\":\"\",\"message\":\"\",\"consoleLog\":\"\",\"succeed\":true}", client.hybridRunJavascript("", 0, "function a(){ return '' }; result = a;")); //

//        client.click("NATIVE","xpath=//*[@accessibilityIdentifier='usernameTextField']",0,1);
//        client.sendText("ml \n");
//        client.launch("com.apple.Preferences",false,true);
//        client.elementSendText("NATIVE", MyProperties.instrumented ? "xpath=//*[@accessibilityIdentifier='usernameTextField']" : "xpath=//*[@placeholder='Username']", 0, "HOQAI rule! \\n");
//        getContainer(app);
//        client.applicationClearData("Experitest.FingerPrintApp");
//        client.collectSupportData(projectBaseDirectory,"","B0076","","","");
//        client.launch("http://192.168.4.85:8060/html-tests/picker-tests/pickerTestHtml.html", false, true);
//        client.applicationClose("com.apple.mobilesafari");
//        client.click("NATIVE","xpath=//*[@id='Start']",0,1);
//        client.click("NATIVE", "xpath=//*[@id='Lap']", 0, 10);
//        client.click("NATIVE","xpath=//*[@id='Stop']",0,1);

//        client.install("com.experitest.ExperiBank",true,true);
//        client.setProperty("ios.instrumentation.log.level", "debug");
//        client.launch("com.experitest.ExperiBank",true,true);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.setProperty("ios.instrumentation.log.level", "none");
//        client.launch("com.experitest.ExperiBank",true,false);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.setProperty("ios.instrumentation.log.level", "info");
//        client.launch("com.experitest.ExperiBank",true,false);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);
//        client.waitForElement("Native","//*[@bla]",0,3000);


//        client.runNativeAPICall("NATIVE", "xpath=//*[@class='UIButtonLabel']", 0, "invokeMethod:'{\"selector\":\"setText:\",\"arguments\":[\"Eilon\"]}'");
    }
}