import org.junit.*;

public class RunNativeAPICallTest extends SeeTestBase {
    String app ="com.experitest.ExperiBank";

    @Test
    public void RunNativeAPICall() throws Exception {
        if(!client.getInstalledApplications().contains(app))
            client.install(System.getProperty("user.dir")+"\\apps\\EriBank.ipa",true,false);
        client.launch(app, true, true);
        if(client.getDeviceProperty("instrumentation.version").equals("UNKNOWN"))
            client.install(System.getProperty("user.dir")+"\\apps\\EriBank.ipa",true,false);
        client.launch("com.experitest.ExperiBank", true, false);
        client.runNativeAPICall("NATIVE", "xpath=//*[@class='UIButtonLabel']", 0, "invokeMethod:'{\"selector\":\"setText:\",\"arguments\":[\"Eilon\"]}'");
        assert(client.elementGetProperty("NATIVE", "xpath=//*[@class='UIButtonLabel']", 0, "text").equals("Eilon"));
        }
}
