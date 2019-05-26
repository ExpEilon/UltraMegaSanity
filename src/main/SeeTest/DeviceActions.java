import org.junit.Assert;
import org.junit.Test;

public class DeviceActions extends SeeTestBase {
    @Test
    public void EriTest() throws Exception {
        client.swipe("Left", 0, 400);
        client.deviceAction("Home");
        client.deviceAction("Power");
        client.deviceAction("Unlock");
        client.launch("com.apple.mobilesafari",false,true);
        client.click("NATIVE","xpath=//*[@accessibilityLabel='URL']",0,1);
        client.deviceAction("BKSP");
        client.sendText("http://192.168.4.85:8060/html-tests/RegressionHibridApplication/HibridApplicationHtml.html");
        client.deviceAction("Enter");
        client.waitForElement("WEB","xpath=//*[@text='Checking page']",0,10000);
        client.verifyElementFound("WEB","xpath=//*[@text='Checking page']",0);
        client.click("NATIVE","xpath=//*[@accessibilityLabel='URL']",0,1);
        client.deviceAction("BKSP");
        Assert.assertTrue(client.elementGetProperty("NATIVE","xpath=//*[@placeholder='Search or enter website name']",0,"value").equals("Search or enter website name"));
        client.launch("com.apple.Maps", true, true);
        client.deviceAction("Landscape");
        client.deviceAction("Portrait");
        client.deviceAction("Change Orientation");
        client.deviceAction("Change Orientation");
        client.deviceAction("Volume Up");
        client.deviceAction("Volume Down");
        client.deviceAction("Power");
        client.deviceAction("Wake");
        if(MyProperties.lastReboot+4*60*60*1000<System.currentTimeMillis() && client.getDeviceProperty("device.sn").equals("45a8ec778b1e6eb400e828cb989be9934fc03a8b")){
            MyProperties.lastReboot = System.currentTimeMillis();
            client.reboot(120000);
        }

    }
}
