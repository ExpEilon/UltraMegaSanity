import com.experitest.client.*;
import com.experitest.client.log.Level;
import org.junit.*;

import java.io.File;


public class SafariLongRun extends BaseTest{

    @Test
    public void webLongRun() {
//            client.setLogger(Utils.initDefaultLogger(Level.ALL));
        if(MyProperties.makeReporter)
            client.setReporter("xml", projectBaseDirectory +"//Reporter", "SafariLongRun");
        client.hybridClearCache(true, true);
        client.launch("safari:www.google.com", true, true);
        client.verifyElementFound("Web","xpath=//*[@id='hplogo']", 0);
        client.launch("safari:m.ebay.com", true, true);
//        client.verifyElementFound("Web", "xpath=//*[@id='gh-mlogo']", 0);
    }
}
