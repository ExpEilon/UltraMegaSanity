import com.experitest.client.Utils;
import com.experitest.client.log.Level;
import org.junit.After;
import org.junit.Before;
import com.experitest.client.Client;
import com.experitest.client.GridClient;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class SeeTestBase extends BaseTest{
    protected Client client = null;
    protected GridClient gridClient = null;
    Map<String, Object> launchOptionsMap;
    boolean createContainer = MyProperties.createContainer && StartPanel.runOn.isGrid;
    String app;

    @Before
    public void setUp() {
        if (!(this instanceof PerformanceTest)) {
            start = System.currentTimeMillis();
            if (isGrid) {
                gridClient = new GridClient(StartPanel.runOn.accesskey, StartPanel.runOn.getURL());
                if (isGrid && ConfigManager.checkIfSetTrue("videoRecording"))
                    gridClient.enableVideoRecording();
            } else
                client = new MyClient(StartPanel.runOn.ip, StartPanel.runOn.port);
            if (isGrid)
                client = gridClient.lockDeviceForExecution("TestFile", query, 30, 300000);
            else
                client.waitForDevice(query, 300000);
            if (createContainer) {
                launchOptionsMap = new HashMap();
                Map envVars = new HashMap();
                envVars.put("EXPERI_ENABLE_LOG_TO_CONTAINER", "true");
                launchOptionsMap.put("launch_env", envVars);
                launchOptionsMap.put("instrument", true); // For now runs only on iOS, so the install is what matters (don't need container for simulators for now)
                launchOptionsMap.put("relaunch", true);
            }
            if (ConfigManager.checkIfSetTrue("extenedClientLog"))
                client.setLogger(Utils.initDefaultLogger(Level.ALL));
            if (ConfigManager.checkIfSetTrue("deviceLog"))
                client.startLoggingDevice(projectBaseDirectory + "//deviceLog//deviceLog_" + this.getClass().getName() + "_" + sdFormat.format(new Date()) + ".log");
            if (ConfigManager.checkIfSetTrue("makeReporter"))
                client.setReporter("xml", projectBaseDirectory + "//Reporter", this.getClass().getName());
            if (ConfigManager.checkIfSetTrue("videoRecording"))
                client.startVideoRecord();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (!(this instanceof PerformanceTest)) {
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
            if (ConfigManager.checkIfSetTrue("videoRecording"))
                client.getRemoteFile(client.stopVideoRecord(), 200000, projectBaseDirectory);
            if (ConfigManager.checkIfSetTrue("makeReporter"))
                client.generateReport(false);
            if (ConfigManager.checkIfSetTrue("deviceLog"))
                client.stopLoggingDevice();
            if (createContainer)
                getContainer(app);
            client.releaseClient();
            end = System.currentTimeMillis();
            duration = ((end - start) / 1000);
            ((MyThread) Thread.currentThread()).setDuration(duration);
        }
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

}
