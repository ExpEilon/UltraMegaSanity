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
    public void setUp() {
//        gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
//        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='00008027-0003290034E8002E'", 120, TimeUnit.MINUTES.toMillis(2));
//        client.setReporter("xml", "reports" , "Untitled");
       client = new Client(host, port, true);
       client.setDevice("ios_app:B0201");

    }

    @Test
    public void testUntitled() throws InterruptedException, UnirestException {
//        System.out.println(new GridClient(MyProperties.runOn.AK,MyProperties.runOn.getURL()).getDevicesInformation());
        client.launch("com.apple.Preferences",true,true);
        client.swipeWhileNotFound("DOWN",100,100,"NATIVE","//*[@text='UICatalog' and @class='UIAView' and @onScreen='true']",0,100,10,true);
        client.elementGetProperty("NATIVE", "//*[@class='UIASwitch']", 0, "value");
   }

    @After
    public void tearDown() {
        client.releaseClient();
    }
}