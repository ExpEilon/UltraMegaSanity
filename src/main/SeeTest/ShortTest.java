import org.junit.Test;

public class ShortTest extends BaseTest{

    String appName = "com.apple.Preferences";
    String app = "com.experitest.UICatalog";
    @Test
    public void shortTest() {
        client.applicationClearData(app);
        getContainer(app);
//        client.applicationClearData("Experitest.FingerPrintApp");
//        client.launch(appName, true, true);
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