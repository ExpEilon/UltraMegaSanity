import org.junit.Test;

public class ShortTest extends BaseTest{

    String appName = "com.apple.Preferences";

    @Test
    public void shortTest() {
        client.startLoggingDevice(projectBaseDirectory + "\\deviceLog.txt");
        client.launch(appName, true, true);
        client.stopLoggingDevice();
    }
}