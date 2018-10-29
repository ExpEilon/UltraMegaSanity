import org.junit.Test;

public class DeviceProperty extends BaseTest{
    @Test
    public void EriTest() throws Exception {
        String str0 = client.getDeviceProperty("orientation");
        String str1 = client.getDeviceProperty("status");
        String str2 = client.getDeviceProperty("device.version");
        String str3 = client.getDeviceProperty("device.time");
        String str4 = client.getDeviceProperty("device.sn");
        String str5 = client.getDeviceProperty("device.screensize");
        String str6 = client.getDeviceProperty("device.remote");
        String str7 = client.getDeviceProperty("device.os");
        String str8 = client.getDeviceProperty("device.name");
        String str9 = client.getDeviceProperty("device.modelname");
        String str10 = client.getDeviceProperty("device.model");
        String str11 = client.getDeviceProperty("device.manufacture");
        String str12 = client.getDeviceProperty("device.host");
    }
}
