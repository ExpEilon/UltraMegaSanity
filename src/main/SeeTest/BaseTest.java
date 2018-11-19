import com.experitest.client.Utils;
import com.experitest.client.log.Level;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import netscape.javascript.JSObject;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import com.experitest.client.Client;
import com.experitest.client.GridClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseTest {
    boolean isGrid = MyProperties.runOn.isGrid;
    protected static String projectBaseDirectory = ((MyThread)Thread.currentThread()).getDirectory();
    protected Client client = null;
    protected GridClient gridClient = null;
    long start,end,duration;
    String query = ((MyThread)Thread.currentThread()).getQuery();
    boolean isSimulator = query.contains("emulator") ? true : false;
    Map<String, Object> launchOptionsMap;
    static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    boolean createContainer = MyProperties.createContainer && MyProperties.runOn.isGrid;
    String app;

    @Before
    public void setUp() {
        start = System.currentTimeMillis();
        if(isGrid){
            gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
            if(isGrid && MyProperties.videoRecording)
                gridClient.enableVideoRecording();
        }
        else{
            client = new MyClient(MyProperties.runOn.ip,MyProperties.runOn.port);
//            client.setProjectBaseDirectory(projectBaseDirectory);
        }
        if(isGrid)
            client = gridClient.lockDeviceForExecution("TestFile", query, 30, 300000);
        else
            client.waitForDevice(query,300000);
        if(createContainer){
            launchOptionsMap = new HashMap();
            Map envVars = new HashMap();
            envVars.put("EXPERI_ENABLE_LOG_TO_CONTAINER", "true");
            launchOptionsMap.put("launch_env", envVars);
            launchOptionsMap.put("instrument", true); // For now runs only on iOS, so the install is what matters (don't need container for simulators for now)
            launchOptionsMap.put("relaunch", true);
        }
        if(MyProperties.getClientLogsLevel == 2)
            client.setLogger(Utils.initDefaultLogger(Level.ALL));
        if(MyProperties.getClientLogsLevel > 0)
            client.startLoggingDevice(projectBaseDirectory+"//devicelog_"+sdFormat.format(new Date())+".log");
        if(MyProperties.makeReporter)
            client.setReporter("xml", projectBaseDirectory +"//Reporter", this.getClass().getName());
        if(MyProperties.videoRecording)
            client.startVideoRecord();
//        client.setProperty("ios.11.use.old.monitoring.service","true");
    }

    @After
    public void tearDown() throws Exception {
//        client.stopLoggingDevice();
//        String NEW_APP_URL = "/applications/new";
//        String webPage = MyProperties.runOn.getURL() + "/api/v1";
//        File f = new File("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\apps\\EriBank.ipa");
//        HttpResponse<JsonNode> response = Unirest.post (webPage+NEW_APP_URL)
//                .basicAuth (MyProperties.runOn.username, MyProperties.runOn.password)
//                .queryString ("project", "default")
//                .field("file", f)
//                .asJson ();
//        System.out.println(response.getBody().toString());
        if(MyProperties.videoRecording)
            client.getRemoteFile(client.stopVideoRecord(),200000,projectBaseDirectory);
        if(MyProperties.makeReporter)
            client.generateReport(false);
//        client.clearDeviceLog();
        if(MyProperties.getClientLogsLevel > 0)
            client.stopLoggingDevice();
        if(createContainer)
            getContainer(app);
        client.releaseClient();
        end = System.currentTimeMillis();
        duration = ((end-start)/1000);
        ((MyThread)Thread.currentThread()).setDuration(duration);
//        Assert.assertTrue("Test took too long!!\ninstead of less than : " + (MyProperties.testsRunTimes.get(this.getClass())*MyProperties.tests.length)+", it took: " + duration+"\n",
//                (MyProperties.testsRunTimes.get(this.getClass())*MyProperties.tests.length)>duration);
    }
    protected boolean installedInstrumented(String app){
        if(Arrays.asList(client.getInstalledApplications().split("\n")).stream().anyMatch(s -> s.equals(app+";"))){ //add ";" to the end because each row ends with it
            try{
                client.launch(app,true,true);
            }catch (Exception e){
                client.uninstall(app);
                client.install((isGrid ? "cloud:":"") + app,true,false);
            }
            return !client.getDeviceProperty("instrumentation.version").equals("UNKNOWN");
        }
        return false;
    }

    protected static int getDeviceId() throws UnirestException {
        HttpResponse<JsonNode> response1 = Unirest.get(MyProperties.runOn.ip+"/api/v1/devices")
                .basicAuth(MyProperties.runOn.username, MyProperties.runOn.password)
                .asJson();
        JSONArray jsonArray = response1.getBody().getObject().getJSONArray("data");
        return IntStream.range(0,jsonArray.length()).mapToObj(i->jsonArray.getJSONObject(i)).collect(Collectors.toList()).stream()
                .filter(j -> j.getString("udid").equals(Thread.currentThread().getName())).findFirst().map(j -> j.getInt("id")).get();
    }
    public static String getContainer(String app) {
        String path = projectBaseDirectory + "//container_of_" + app + "_at_" + sdFormat.format(new Date()) + ".zip";
        try {
            HttpResponse<InputStream> response1 = Unirest.get(MyProperties.runOn.ip + "/api/v1/devices/" + getDeviceId() + "/app-container/" + app)
                    .basicAuth(MyProperties.runOn.username, MyProperties.runOn.password)
                    .asBinary();
            FileUtils.copyInputStreamToFile(response1.getBody(), new File(path));
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return path;
        }
    }
}
