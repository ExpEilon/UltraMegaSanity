import org.junit.Test;

public class DeviceActions extends SeeTestBase {
    @Test
    public void EriTest() throws Exception {
        client.swipe("Left", 0, 400);
        client.deviceAction("Home");
        client.deviceAction("Power");
        client.deviceAction("Unlock");
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
