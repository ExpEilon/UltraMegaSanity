import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import netscape.javascript.JSObject;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import com.experitest.client.Client;
import com.experitest.client.GridClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class BaseTest {
    boolean isGrid = MyProperties.runOn.isGrid;
    protected String projectBaseDirectory = ((MyThread)Thread.currentThread()).getDirectory();
    protected Client client = null;
    protected GridClient gridClient = null;
    long start,end;
    String query = ((MyThread)Thread.currentThread()).getQuery();

    @Before
    public void setUp() {
        start = System.currentTimeMillis();
        if(isGrid){
            gridClient = new GridClient(MyProperties.runOn.AK, MyProperties.runOn.getURL());
        }
        else{
            client = new MyClient(MyProperties.runOn.ip,MyProperties.runOn.port);
//            client.setProjectBaseDirectory(projectBaseDirectory);
        }
        if(isGrid)
            client = gridClient.lockDeviceForExecution("TestFile", query, 30, 300000);
        else
            client.waitForDevice(query,300000);
    }

    @After
    public void tearDown() throws UnirestException, IOException {
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
//        HttpResponse<InputStream> response1 = Unirest.get(MyProperties.runOn.ip+"/api/v1/devices/48/app-container/com.experitest.UICatalog")
//                .basicAuth(MyProperties.runOn.username, MyProperties.runOn.password)
//                .asBinary();
//        FileUtils.copyInputStreamToFile(response1.getBody(), new File(projectBaseDirectory+"//container.zip"));
//        System.out.println(response1.getBody().toString());

        if(MyProperties.makeReporter)
            client.generateReport(false);
        client.releaseClient();
        end = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).setDuration((end-start)/1000);
    }
}
