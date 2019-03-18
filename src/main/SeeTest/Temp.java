// Insert your package here
import com.experitest.client.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Temp {

    protected Client client = null;
    protected GridClient gridClient = null;
    private String host = "localhost";
    private int port = 8889;
   @Before
    public void setUp() throws UnirestException {
        gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
//       System.out.println(gridClient.getDevicesInformation());
        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='f759ec5d8343175b2c68f856c9c47559aa1fc0fc'", 120, TimeUnit.MINUTES.toMillis(2));
//        client.setReporter("xml", "reports" , "Untitled");
//       client = new Client(host, port, true);
//       client.setDevice("ios_app:B0076");
//       File f = new File("C:\\Users\\eilon.grodsky\\Documents\\Cloud Application iOS - Should not sign Ad hoc application by default\\PressTheDotAdHoc.ipa");
//           HttpResponse<JsonNode> response1 = Unirest.post("http://192.168.2.22"+"/api/v1/applications/new")
//                   .basicAuth("admin", "Experitest2012")
////                   .queryString("uniqueName","chasepaypos_ios_q5_12202018")
//                    .queryString ("project", "default")
//                   .field("file",f)
//                   .asJson();
//       System.out.println(response1.getBody());
//       System.out.println(response1.getBody().getObject().getJSONObject("data").getInt("id"));
//       HttpResponse<JsonNode> response1 = Unirest.post("https://qacloud.experitest.com/api/v1/applications/1855921/install")
//               .basicAuth("khaleda", "Experitest2012")
//               .queryString ("deviceId","308817")
//               .queryString("instrument","true")
//               .asJson();
//       System.out.println(response1.getBody());
   }

    @Test
    public void testUntitled() throws InterruptedException, UnirestException {
       client.click("WEB","//*[./*[@id='loginForm:usridTxt']]",0,1);
        client.click("WEB"," //*[@id='Signin']",0,1);

       client.verifyElementFound("WEB","//*[@id='chart']",0);
//        client.getDeviceProperty("instrumentation.version").equals("UNKNOWN");
//        client.applicationClearData("com.experitest.UICatalog");
//        Map<String, Object> launchOptionsMap = new HashMap();
//        launchOptionsMap.put("instrument", true);
//        launchOptionsMap.put("relaunch", true);
//        client.elementSendText("NATIVE", "xpath=//*[@placeholder='<enter text Last>']", 0, "lastElement");
//        client.collectSupportData("E:\\Downloads","","B0201","","","");
//        System.out.println(System.getProperty("os.name"));
//        client.getProperty("automator.version");
//        client.install(System.getProperty("user.dir")+ "\\apps\\EriBank.ipa",true,false);
   }

    @After
    public void tearDown() {
       client.releaseClient();
    }
}