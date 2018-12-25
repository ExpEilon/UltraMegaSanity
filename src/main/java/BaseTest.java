import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseTest {
    boolean isGrid = StartPanel.runOn.isGrid;
    protected String projectBaseDirectory = ((MyThread)Thread.currentThread()).getDirectory();
    static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    long start,end,duration;
    String query = ((MyThread)Thread.currentThread()).getQuery();
//    boolean isSimulator = query.contains("emulator") ? true : false;
    boolean isSimulator = query.split("-").length > 2 ? true : false;


    protected int getDeviceId() throws UnirestException {
        HttpResponse<JsonNode> response1 = Unirest.get(StartPanel.runOn.ip+"/api/v1/devices")
                .basicAuth(StartPanel.runOn.username, StartPanel.runOn.password)
                .asJson();
        JSONArray jsonArray = response1.getBody().getObject().getJSONArray("data");
        return IntStream.range(0,jsonArray.length()).mapToObj(i->jsonArray.getJSONObject(i)).collect(Collectors.toList()).stream()
                .filter(j -> j.getString("udid").equals(Thread.currentThread().getName())).findFirst().map(j -> j.getInt("id")).get();
    }

    public String getContainer(String app) {
        String path = projectBaseDirectory + "//container_of_" + app + "_at_" + sdFormat.format(new Date()) + ".zip";
        try {
            HttpResponse<InputStream> response1 = Unirest.get(StartPanel.runOn.ip + "/api/v1/devices/" + getDeviceId() + "/app-container/" + app)
                    .basicAuth(StartPanel.runOn.username, StartPanel.runOn.password)
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
