import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class EriBankTest extends BaseTest {
    String str0,str;
    String appName = "com.experitest.ExperiBank";

    @Test
    public void EriTest() throws Exception {
        if(client.getInstalledApplications().contains(appName))
            client.uninstall(appName);
        client.install("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\apps\\EriBank.ipa",false,false);
//        client.install(isGrid ? "cloud:" + appName : appName, MyProperties.instrumented ? true : false, false);
//        HttpResponse<JsonNode> response;
//            String NEW_APP_URL = "/applications/new";
//            String webPage = MyProperties.runOn.getURL() + "/api/v1";
//            File f = new File("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\apps\\EriBank.ipa");
//            response = Unirest.post (webPage+NEW_APP_URL)
//                    .basicAuth (MyProperties.runOn.username, MyProperties.runOn.password)
//                    .queryString ("project", "default")
//                    .field("file", f)
//                    .asJson ();
//            int id = Integer.parseInt(response.getBody ().toString().split("d\":\"")[1].split("\"")[0]);
//        System.out.println(id + response.getBody ().toString());
//            Thread.sleep(2000);
//            String INSTALL_APP_URL = "/applications/"+id+"/install";
//        response = Unirest.post (webPage+INSTALL_APP_URL)
//                    .basicAuth (MyProperties.runOn.username, MyProperties.runOn.password)
//                    .queryString("deviceId","46")
//                    .asJson ();
//        System.out.println(response.getBody ().toString());
        client.launch(appName, MyProperties.instrumented ? true : false, true);
        client.elementSendText("NATIVE", MyProperties.instrumented ? "xpath=//*[@accessibilityIdentifier='usernameTextField']" : "xpath=//*[@placeholder='Username']", 0, "company");
        client.elementSendText("NATIVE", MyProperties.instrumented ? "xpath=//*[@accessibilityIdentifier='passwordTextField']": "xpath=//*[@placeholder='Password']", 0, "company");
        client.click("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Login']" : "xpath=//*[@text='loginButton']", 0, 1);
        client.waitForElement("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Invalid username or password!']" : "xpath=//*[@text='Invalid username or password!']", 0, 2000);
        if (client.isElementFound("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Invalid username or password!']" : "xpath=//*[@text='Invalid username or password!']", 0))
            client.click("NATIVE",MyProperties.instrumented ? "xpath=//*[@text='Dismiss']" : "xpath=//*[@text='Dismiss']", 0, 1);

        str0 = client.getTextIn("NATIVE",MyProperties.instrumented ? "xpath=//*[@text='Make Payment']" : "xpath=//*[@text='makePaymentButton']", 0, MyProperties.instrumented ? "WEB" : "NATIVE", "Up", 0, 0);
        str = str0.split(":")[1].split("\\$")[0];

//        double oldD = Double.parseDouble(str);
        client.click("NATIVE", MyProperties.instrumented ?"xpath=//*[@text='Make Payment']" : "xpath=//*[@text='makePaymentButton']", 0, 1);
        client.elementSendText("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Phone']" : "xpath=//*[@placeholder='Phone']", 0, "0523898058");
        client.elementSendText("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Name']" : "xpath=//*[@placeholder='Name']", 0, "Eilon");
        double payment = new Random().nextInt(2000) - 1000;
        client.elementSendText("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Amount']" : "xpath=//*[@placeholder='Amount']", 0, Double.toString(payment));
        client.closeKeyboard();
        if(MyProperties.instrumented) {
            client.click("NATIVE", MyProperties.instrumented ?"xpath=//*[@accessibilityLabel='Select']" : "xpath=//*[@text='countryButton']", 0, 1);
            client.elementListSelect("accessibilityLabel=conutryView", "accessibilityLabel=Tanzania", 0, true);
        }
        else
            client.elementSendText("NATIVE","xpath=//*[@placeholder='Country']",0,"Israel");
        client.waitForElement("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Send Payment']" : "xpath=//*[@text='sendPaymentButton']",0,10000);
        client.click("NATIVE",MyProperties.instrumented ?"xpath=//*[@accessibilityLabel='Send Payment']" : "xpath=//*[@text='sendPaymentButton']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@text='Yes']", 0, 2000);
        if(client.isElementFound("NATIVE", "xpath=//*[@text='Yes']"))
            client.click("NATIVE", "xpath=//*[@text='Yes']", 0, 1);
        str0 = client.getTextIn("NATIVE",MyProperties.instrumented ? "xpath=//*[@text='Make Payment']" : "xpath=//*[@text='makePaymentButton']", 0, MyProperties.instrumented ? "WEB" : "NATIVE", "Up", 0, 0);
        str = str0.split(":")[1].split("\\$")[0];
        client.applicationClose("com.experitest.ExperiBank");
//        double newD = Double.parseDouble(str);
//        if(oldD - newD != payment)
//            throw new Exception("The values before and after payment do not match");
    }
}

