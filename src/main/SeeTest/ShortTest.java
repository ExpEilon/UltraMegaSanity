import org.junit.Test;

public class ShortTest extends SeeTestBase {

    String appName = "com.apple.Preferences";
    String app = "com.experitest.UICatalog";
    @Test
    public void shortTest() {
//        client.install("cloud:uniqueName=chasepaypos_ios_q5_12202018",false,false);
//        client.applicationClearData(app);
//        getContainer(app);
//        client.applicationClearData("Experitest.FingerPrintApp");
//        client.launch("https://secure.sterbcwm-usa.com", false, true);
        System.out.println(client.getVisualDump("WEB").contains("loginForm:usridTxt"));
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