// Insert your package here
import com.experitest.client.*;
import com.google.gson.JsonArray;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.*;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

public class Temp {

    protected Client client = null;
    protected GridClient gridClient = null;
    private String host = "localhost";
    private int port = 8889;
   @Before
    public void setUp() throws UnirestException {
//        gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
//        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='00008020-000E2D1121F8002E'", 120, TimeUnit.MINUTES.toMillis(2));
//        client.setReporter("xml", "reports" , "Untitled");
//       client = new Client(host, port, true);
//       client.setDevice("ios_app:B0201");
       File f = new File("C:\\Users\\eilon.grodsky\\Downloads\\com.chase.pay.test_ver_4186.ipa");
           HttpResponse<JsonNode> response1 = Unirest.post("http://192.168.2.29:9000"+"/api/v1/applications/new")
                   .basicAuth("admin", "Experitest2012")
                   .queryString("uniqueName","chasepaypos_ios_q5_12202018")
                    .queryString ("project", "default")
                   .field("file",f)
                   .asJson();
       System.out.println(response1.getBody());
   }

    @Test
    public void testUntitled() throws InterruptedException, UnirestException {
//       client.forceTouch("NATIVE","//*[@text='Camera']",0,100,100,0,0,0);
//       client.install("cloud:uniqueName=chasepaypos_ios_q5_12202018",false,false);
//        client.launch("com.apple.Preferences",true,true);
//        client.swipeWhileNotFound("DOWN",100,100,"NATIVE","//*[@text='UICatalog' and @class='UIAView' and @onScreen='true']",0,100,10,true);
//        client.elementGetProperty("NATIVE", "//*[@class='UIASwitch']", 0, "value");
   }

    @After
    public void tearDown() {

//       client.releaseClient();
    }
}