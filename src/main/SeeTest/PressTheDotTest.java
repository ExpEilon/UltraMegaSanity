/*import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;*/
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class PressTheDotTest extends BaseTest{
    String csvUserName = "";
    String csvPassword = "";

    @Test
    public void PressTheDotTest() throws InterruptedException, FileNotFoundException, KeyManagementException, NoSuchAlgorithmException {
//            if(MyProperties.url.contains("https")){
//                System.getProperties().setProperty("javax.net.ssl.trustStore","C:\\Users\\eilon.grodsky\\Desktop\\new keystore\\truststore.jks");
//                System.getProperties().setProperty("javax.net.ssl.trustStorePassword","123456");
//            }
//            HttpResponse<JsonNode> response;
//            String NEW_APP_URL = "/applications/new";
//            String webPage = MyProperties.url + "/api/v1";
//            File f = new File("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\apps\\PressTheDot.ipa");
//            response = Unirest.post (webPage+NEW_APP_URL)
//                    .basicAuth (MyProperties.username, MyProperties.password)
//                    .queryString ("project", "default")
//                    .field("file", f)
//                    .asJson ();
//            int id = Integer.parseInt(response.getBody ().toString().split("d\":\"")[1].split("\"")[0]);
//            Thread.sleep(2000);
//            String INSTALL_APP_URL = "/applications/"+id+"/install";
//            Unirest.post (webPage+INSTALL_APP_URL)
//                    .basicAuth (MyProperties.username, MyProperties.password)
//                    .queryString("deviceId","15")
//                    .asJson ();
        client.install("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\apps\\PressTheDot.ipa",false,false);
        client.launch("dfs.PressTheDot2", false, true);
        client.setProperty("ios.auto.accept.alerts", "true");
        client.elementSendText("NATIVE","xpath=((//*[@text='PressTheDot']/*/*[@class='UIAView' and ./parent::*[@class='UIAWindow']])[1]/*[@class='UIATextField'])", 0, "Eilon");
        client.elementSendText("NATIVE","xpath=((//*/*/*[@class='UIAView' and ./parent::*[@class='UIAWindow']])[1]/*[@class='UIATextField'])", 1, "Eilon");
        client.click("NATIVE","xpath=//*[@text='Login']", 0, 1);
        client.setProperty("ios.auto.accept.alerts", "false");
        if (client.isElementFound("NATIVE","xpath=//*[@text='Play']", 0))
        client.click("NATIVE","xpath=//*[@text='Play']", 0, 1);
        for (int i = 0; i < 3; i++) {
            Random rnd = new Random();
            int tmp =  rnd.nextInt(6);
            for (int j = 0; j < tmp; j++) {
                client.click("NATIVE","xpath=//*[@text='eyepic']", 0, 1);
            }
            client.waitForElement("NATIVE","xpath=//*[@text='Yes']", 0, 10000);
            if (i < 2)
                client.click("NATIVE","xpath=//*[@text='Yes']", 0, 1);
        }
        client.click("NATIVE","xpath=//*[@text='No']", 0, 1);
    }
}