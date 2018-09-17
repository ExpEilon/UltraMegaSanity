// Insert your package here
import com.experitest.client.*;
import org.junit.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Temp {

    protected Client client = null;
    protected GridClient gridClient = null;
    private String host = "localhost";
    private int port = 8889;
   @Before
    public void setUp() {
        // In case your user is assigned to a single project leave projectName as empty string, otherwise please specify the project name
        gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
//        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='2e663adbadbf0f82b25645a5481139b144246dfb'", 120, TimeUnit.MINUTES.toMillis(2));
//        client.setReporter("xml", "reports" , "Untitled");

    }

    @Test
    public void testUntitled() throws InterruptedException {
//        for (int i = 1; i < 100; i++) {
            client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='6d83af5fee3f5f393e9d9eaa99aaa5f7a839b911'", 120, TimeUnit.MINUTES.toMillis(2));
            client.startLoggingDevice(new File("").getAbsolutePath() + "deviceLog");
            client.launch("com.apple.Preferences",false,true);
            client.stopLoggingDevice();
            client.releaseClient();
//        }
   }

    @After
    public void tearDown() {
//        client.releaseClient();
    }
}