import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

import static java.lang.Thread.sleep;

public class RecoveryTest extends BaseTest {
    final int port = 6580;
    final String killCyder = "{\"command\":\"kill_application\",\"bundleID\":\"com.experitest.Cyder\"}";
    final int timeout = 10; // Cyder time to revive in sec.
    HttpResponse<JsonNode> response;
    String ip, url;
    boolean os10;


    @Test
    public void CyderRecovery() throws InterruptedException {
        os10 = Integer.parseInt(client.getDeviceProperty("os.version").split("\\.")[0]) <11 ? true:false;
        ip = getInternetConnection();
        url = "http://" + ip + ":" + port;
        try {
            response = Unirest.post(url +"/ping").asJson();
            System.out.println(response.getBody());
        } catch (UnirestException e) {
            Assert.fail("Couldn't ping to Cyder on ip: " + url);
        }
        if(os10)
            killPaltielX();
        else {
            try {
                Unirest.post(url+ "/json")
                        .queryString("data", killCyder)
                        .asJson();
            } catch (UnirestException e) {
                System.out.println("Cyder closed");
            }
        }
        response.getBody().getObject().put("ok","");//for while loop to start
        //Recovery time
        Long startRecover = System.currentTimeMillis();
        while (!response.getBody().toString().equals("{\"ok\":\"result\"}")) {
            try {
                response = Unirest.post(url+ "/ping").asJson();
            } catch (UnirestException e) {
                System.out.println("Waiting to Cyder to recover");
                Assert.assertTrue("Cyder didn't revive or it took more than: " + timeout + " sec", ((System.currentTimeMillis()-startRecover)/1000) < timeout);
            }
        }

        System.out.println("Took for Cyder " + (System.currentTimeMillis() - startRecover) + " milis to recover");
    }

    private String getInternetConnection() throws InterruptedException {
        String password = "0547899446";
        String network,ip;
        client.launch("com.apple.Preferences",false,true);
        client.click("NATIVE","xpath=//*[@text='Wi-Fi' and @class='UIAView']",0,1);
        network = os10 ? client.elementGetProperty("NATIVE","xpath=(//*[@class='UIATable']/*[@class='UIAView' and ./*[@class='UIAButton']])[1]//*[1]",0,"text") :
                client.elementGetProperty("NATIVE","xpath=//*[@text and @class='UIAView' and ./*[./*[@text]]]//*[@class='UIAStaticText']",0,"text");
        if(!network.equals("Network")){
            client.click("NATIVE","xpath=//*[@text='Network']",0,1);
            sleep(5000);
            if(client.isElementFound("NATIVE","xpath=//*[@id='Enter Password' and @class='UIAView']",0)){
                client.elementSendText("NATIVE","xpath=//*[@id='Password']",0,password);
                client.click("NATIVE","xpath=//*[@id='Join']",0,1);
                sleep(7000);
            }
        }
        client.click("NATIVE","xpath=//*[@text='Network']",0,1);
        ip = os10 ? client.elementGetProperty("NATIVE","xpath=(//*[@class='UIATable']/*[@class='UIATextField'])[1]",0,"text"):
                    client.elementGetProperty("NATIVE","xpath=//*[@class='UIAView' and ./*[@text='IP Address']]//*[@text!='IP Address' and @class='UIAStaticText']",0,"text");
        return ip;
    }
    private void killPaltielX(){
        for (int i = 0; i < 3; i++) {
            try {
                client.launch("abc", false, true);
            } catch (Exception e) {}
        }
    }
}
