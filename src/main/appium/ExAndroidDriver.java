import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eyal.neumann on 7/2/2017.
 */
public class ExAndroidDriver extends AndroidDriver{
    private SimpleDateFormat sdf;

    public ExAndroidDriver(URL url, Capabilities capabilities) {
        super(url, capabilities);
    }

    @Override
    protected void log(SessionId sessionId, String commandName, Object toLog, When when) {
        if (commandName.equals("newSession")) sdf = new SimpleDateFormat("HH:mm:ss");
        super.log(sessionId, commandName, toLog, when);
        System.out.println(sdf.format(new Date(System.currentTimeMillis())) + ": "+ " - " + when + ": " + commandName + " toLog:" + toLog);
//        if (deviceName != null) {
//            utils.writeToDeviceLog(deviceName, sdf.format(new Date(System.currentTimeMillis())) + when + ": " + commandName + " toLog:" + toLog);
//
//        }
    }
}
