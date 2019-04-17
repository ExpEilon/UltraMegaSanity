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
import org.boon.Str;
import org.json.JSONArray;
import org.junit.*;
import com.jcraft.jsch.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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

//        gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
//       gridClient.enableVideoRecording();
//       System.out.println(gridClient.getDevicesInformation());
       //{"command":"kill_and_launch","bundleID":"com.experitest.ExperiBank","timeout":10000,"instrumented":true,"relaunch":true}
       //{"command":"tap","point1":{"x":206,"y":161}}
       //{"command":"type","string":"\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\bhttp:\/\/www.vmware.com\/it-priorities\/modernize-data-centers.html \n"}
       //{"command":"kill_and_launch","bundleID":"com.apple.Preferences","timeout":10000,"instrumented":false,"relaunch":true}
//        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='00008020-000844AC3408002E'", 120, TimeUnit.MINUTES.toMillis(2));
//        client.setReporter("xml", "reports" , "Untitled");
       client = new Client(host, port, true);
       client.setDevice("ios_app:B0191");
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
    private void runCommand(String command){
        String url = "http://172.16.16.35:6580/json";
        HttpResponse<JsonNode> response1 = null;
        try {
            response1 = Unirest.get(url)
                    .queryString ("data",command)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        System.out.println(response1.getBody().toString());
    }
    @Test
    public void testUntitled() throws Exception {
        for (int i = 0; i < 100; i++) {
            client.launch("https://www.google.com", false, true);
            client.getVisualDump("WEB");
        }
//        String user = "eilon.grodsky";
//        String password = "1";
//        String host = "192.168.2.29";
//        int port = 22;
//        String remoteFile = "/Documents/tmp.txt";
//
//        try {
//            JSch jsch = new JSch();
//            Session session = jsch.getSession(user, host, port);
//            session.setPassword(password);
//            session.setConfig("StrictHostKeyChecking", "no");
//            System.out.println("Establishing Connection...");
//            session.connect();
//            System.out.println("Connection established.");
//            System.out.println("Crating SFTP Channel.");
//            ChannelShell shell = (ChannelShell)  session.openChannel ("shell");
//            shell.connect();
//            System.out.println("SFTP Channel created.");
//
//            shell.
////            InputStream inputStream = sftpChannel.get(remoteFile);
//
//            try (Scanner scanner = new Scanner(new InputStreamReader(inputStream))) {
//                while (scanner.hasNextLine()) {
//                    String line = scanner.nextLine();
//                    System.out.println(line);
//                }
//            }
//        } catch (JSchException | SftpException e) {
//            e.printStackTrace();
//        }

//        String url = "http://192.168.2.22:9000" + "/api/v1/applications";
//        HttpResponse<JsonNode> response = Unirest.get(url)
//                .basicAuth("admin", "Experitest2012")
//                .queryString("uniqueName","Test")
//                .asJson();
//        System.out.println(response.getBody().getArray().getJSONObject(0).getInt("id"));
//        String pathToApp = System.getProperty("user.dir")+"\\apps\\UICatalog.ipa";
////        String pathToApp = System.getProperty("user.dir")+"\\apps\\KChing.ipa";
//        String pathToEntitlements = "E:\\issue-27106\\entitlementForUICatalog.txt";
//        File app = new File(pathToApp);
//        url = "http://192.168.2.22:9000/api/v1/applications/new";
//        response = Unirest.post(url)
//                .basicAuth("admin", "Experitest2012")
//                .queryString("project","default")
//                .queryString("uniqueName","Test")
//                .field("file", app)
//                .asJson();
//        System.out.println(response.getBody());
//        for (int i = 0; i < 100; i++) {
//            runCommand("{\"command\":\"kill_and_launch\",\"bundleID\":\"com.experitest.ExperiBank\",\"timeout\":10000,\"instrumented\":true,\"relaunch\":true}");
//            runCommand("{\"command\":\"tap\",\"point1\":{\"x\":206,\"y\":161}}");
//            runCommand("{\"command\":\"type\",\"string\":\"\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\b\\bhttp:\\/\\/www.vmware.com\\/it-priorities\\/modernize-data-centers.html \\n\"}");
//            runCommand("{\"command\":\"kill_and_launch\",\"bundleID\":\"com.apple.Preferences\",\"timeout\":10000,\"instrumented\":false,\"relaunch\":true}");
//            }
//       client.startLoggingDevice("E:\\device.log");
//       client.launch("https://www.wikipedia.org",false,true);
//        System.out.println(client.elementGetProperty("WEB","xpath=//*[@text and @nodeName='DIV' and (./preceding-sibling::* | ./following-sibling::*)[@text]]",0,"text"));
//        client.stopLoggingDevice();
//        client.getProperty("automator.version");
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
        public void tearDown () {
            client.releaseClient();
        }
    }