// Insert your package here
import com.experitest.client.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.json.JSONArray;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class Temp {

    protected Client client = null;
    protected GridClient gridClient = null;
    private String host = "localhost";
    private int port = 8889;
   @Before
    public void setUp() throws UnirestException {
        gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
//       gridClient.enableVideoRecording();
//       System.out.println(gridClient.getDevicesInformation());
        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='2ec34da9c45eedbeea2bfaf2253a3b567ce45699'", 120, TimeUnit.MINUTES.toMillis(2));
//        client.setReporter("xml", "reports" , "Untitled");
//       client = new Client(host, port, true);
//       client.setDevice("ios_app:iPad air 2");
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
    public void testUntitled() throws Exception {
        client.getProperty("automator.version");
//        client.deviceAction("BKSP");
        //        client.startVideoRecord();
//        client.launch("http://192.168.4.85:8060/html-tests/picker-tests/pickerTestHtml.html", false, true);
//        client.applicationClose("com.apple.mobilesafari");
//        client.getRemoteFile(client.stopVideoRecord(), 200000, System.getProperty("user.dir") + "//TestResult");
//       client.click("WEB","//*[./*[@id='loginForm:usridTxt']]",0,1);
//        client.click("WEB"," //*[@id='Signin']",0,1);
//        client.launch("com.apple.mobiletimer", false, true);
//        Path paths = Paths.get("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\TestResult\\2ec34da9c45eedbeea2bfaf2253a3b567ce45699\\AllResults.txt");
//        try (Stream<String> lines = Files.lines(paths)) {
//            lines.forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File file = new File("C:\\Program Files\\Experitest\\Cloud\\Server\\logs\\server.log");
//        TailerListener listener = new MyTailerListener();
//        Tailer tailer = new Tailer(file, listener);
//        Thread thread = new Thread(tailer);
//        thread.setDaemon(true);
//        thread.start();
//        sleep(10000);
//        int n_lines = 1000;
//        int counter = 0;
//        ReversedLinesFileReader object = new ReversedLinesFileReader(file);
//        while(counter < n_lines) {
//            String str = object.readLine();
//            if (str == null)
//                break;
//            System.out.println(str);
//            counter++;
//        }

//       client.verifyElementFound("WEB","//*[@id='chart']",0);
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
//   }
//    class MyTailerListener extends TailerListenerAdapter {
//        public void handle(String line) {
//            System.out.println(line);
//        }
    }

    @After
    public void tearDown() {
       client.releaseClient();
    }
}