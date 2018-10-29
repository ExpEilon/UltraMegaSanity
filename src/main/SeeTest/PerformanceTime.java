//package <set your test package>;

import com.experitest.client.Client;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PerformanceTime extends BaseTest{

    String appName = "com.experitest.ExperiBank";
    Map<String,Long> times;
    long startTime;
    @Test
    public void testnew(){
        times = new HashMap<>();
        client.setSpeed("slow");
        startTime = System.currentTimeMillis();
        myTest();
        times.put("slow",(System.currentTimeMillis()-startTime)/1000);
        client.setSpeed("normal");
        startTime = System.currentTimeMillis();
        myTest();
        times.put("normal",(System.currentTimeMillis()-startTime)/1000);
        Assert.assertTrue("Normal speed took to long: " + times,times.get("normal")<100);
        Assert.assertTrue("Normal speed took more than slow speed: " + times,times.get("normal")<times.get("slow"));
        client.setSpeed("fast");
        startTime = System.currentTimeMillis();
        myTest();
        times.put("fast",(System.currentTimeMillis()-startTime)/1000);
        Assert.assertTrue("Fast speed took more than normal speed: " + times,times.get("fast")<times.get("normal"));
    }
    public void myTest(){
//        client.install(appName, true, false);
        client.launch(appName, true, true);
        client.waitForElement("NATIVE", "placeholder=Username", 0, 120000);
        client.elementSendText("NATIVE", "placeholder=Username", 0, "company");
        client.waitForElement("NATIVE", "placeholder=Password", 0, 10000);
        client.elementSendText("NATIVE", "placeholder=Password", 0, "company");
        client.sendText("{ENTER}");
        client.click("NATIVE", "accessibilityLabel=makePaymentButton", 0, 1);
        client.waitForElement("NATIVE", "accessibilityLabel=countryButton", 0, 10000);
        client.click("TEXT", "Select", 0, 1);
        client.elementListSelect("", "text=Tanzania", 0, false);
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Tanzania']", 0, 1);
        client.waitForElement("NATIVE", "accessibilityLabel=cancelButton", 0, 120000);
        client.click("TEXT", "Cancel", 0, 1);
        client.waitForElement("TEXT", "Logout", 0, 10000);
        client.click("NATIVE", "accessibilityLabel=logoutButton", 0, 1);
        client.launch("safari:http://www.wikipedia.org", true, true);
        client.waitForElement("WEB", "id=searchInput", 0, 120000);
        client.elementSendText("WEB", "id=searchInput", 0, "expert");
        client.click("WEB", "xpath=//*[@nodeName='I' and ./parent::*[@nodeName='BUTTON']]", 0, 1);
    }
}
