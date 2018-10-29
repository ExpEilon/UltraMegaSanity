import com.experitest.client.*;
import com.experitest.client.log.Level;
import org.junit.*;

import java.io.File;


public class SafariLongRun extends BaseTest{

    @Test
    public void webLongRun() {
        client.hybridClearCache(true, true);
        client.launch("safari:www.google.com", true, true);
        Assert.assertTrue("Wasn't found, web dump:\n" + client.getVisualDump("WEB"),client.isElementFound("Web","xpath=//*[@id='hplogo']", 0));
        client.launch("safari:m.ebay.com", true, true);
        client.verifyElementFound("Web", "xpath=//*[@id='gh-mlogo']", 0);
    }
}
