import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract public class BaseTest {
    protected String projectBaseDirectory = ((MyThread)Thread.currentThread()).getDevice().getDirectory();
    protected static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    protected long start,end,duration;
    protected String query = ((MyThread)Thread.currentThread()).getQuery();
    protected boolean isSimulator = query.split("-").length > 2 ? true : false;
    protected DeviceController device = ((MyThread)Thread.currentThread()).getDevice();
    protected HashMap<String,String> bundleToPath = new HashMap<>();
    protected ConfigManager.Connection runOn = device.getRunOn();
    protected boolean isGrid = runOn.isGrid;
    HttpResponse<JsonNode> jsonResponse;

    protected int getDeviceId() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(runOn.getURL()+"/api/v1/devices")
                .basicAuth(runOn.getUsername(), runOn.getPassword())
                .asJson();
        JSONArray jsonArray = response.getBody().getObject().getJSONArray("data");
        return IntStream.range(0,jsonArray.length()).mapToObj(i->jsonArray.getJSONObject(i)).collect(Collectors.toList()).stream()
                .filter(j -> j.getString("udid").equals(device.getSN())).findFirst().map(j -> j.getInt("id")).get();
    }

    public String getContainer(String app) {
        String path = projectBaseDirectory + "//container_of_" + app + "_at_" + sdFormat.format(new Date()) + ".zip";
        try {
            HttpResponse<InputStream> response = Unirest.get(runOn.getURL() + "/api/v1/devices/" + getDeviceId() + "/app-container/" + app)
                    .basicAuth(runOn.getUsername(), runOn.getPassword())
                    .asBinary();
            FileUtils.copyInputStreamToFile(response.getBody(), new File(path));
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return path;
        }
    }

    public void uploadIfMissing(String bundleID){
        String url = device.getRunOn().getURL() + "/api/v1/applications";
        String user =  device.getRunOn().getUsername();
        String password = device.getRunOn().getPassword();
        try {
            jsonResponse = Unirest.get(url)
                    .basicAuth(user, password)
                    .asJson();
//            IntStream.range(0,response.getBody().getArray().length()).filter(j -> response.getBody().getArray().getJSONObject(j).get("").equals(bundleID)).findFirst().isPresent()
            if(!jsonResponse.getBody().toString().contains(bundleID)){
                Unirest.post(url)
                        .basicAuth(user, password)
                        .queryString("project","default")
                        .field("file", bundleToPath.get(bundleID))
                        .asJson();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    protected void deleteApp(String uniqueName) {
        String url = runOn.getURL() + "/api/v1/applications";
        try {
            jsonResponse = Unirest.get(url)
                    .basicAuth(device.getRunOn().getUsername(), device.getRunOn().getPassword())
                    .queryString("uniqueName", uniqueName)
                    .asJson();
            System.out.println(jsonResponse.getBody());
            if (jsonResponse.getBody().getArray().length() > 0) {
                //get app id
                int id = jsonResponse.getBody().getArray().getJSONObject(0).getInt("id");
                url = url + "/" + id + "/delete";
                jsonResponse = Unirest.post(url)
                        .basicAuth(device.getRunOn().getUsername(), device.getRunOn().getPassword())
                        .asJson();
                System.out.println(jsonResponse.getBody());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
    protected void deleteApp(int id) {
        HttpResponse<JsonNode> response;
        try {
                String url = runOn.getURL() + "/" + id + "/delete";
                response = Unirest.post(url)
                        .basicAuth(device.getRunOn().getUsername(), device.getRunOn().getPassword())
                        .asJson();
                System.out.println(response);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    protected int uploadApp(Object app, boolean basicInstrumentation,boolean fixKeychain,File entitlements,String uniqueName) throws UnirestException {
        boolean fromURL = app instanceof File ? false : true;
        String url = runOn.getURL() + "/api/v1/applications/new" + (fromURL ? "-from-url":"");
        jsonResponse = Unirest.post(url)
                .basicAuth(device.getRunOn().getUsername(), device.getRunOn().getPassword())
                .queryString(fromURL ? "url" : "dummy",app)
                .queryString("camera", basicInstrumentation ? false:true)
                .queryString("touchId",basicInstrumentation ? false:true)
                .queryString("project","default")
                .queryString(uniqueName.equals("") ? "dummy" : "uniqueName",uniqueName)
                .queryString("fixKeychainAccess", fixKeychain)
                .field(entitlements != null ? "overrideEntitlements" : "dummy", entitlements)
                .field(fromURL ? "dummy" : "file", app)
                .asJson();
        System.out.println(jsonResponse.getBody());
        return jsonResponse.getBody().getObject().getJSONObject("data").getInt("id");
    }

    protected JsonNode installApp(int appId,boolean instrumented) throws UnirestException {
        String urlInstall = runOn.getURL() + "/api/v1/applications/"+ appId +"/install";
        jsonResponse = Unirest.post(urlInstall)
                .basicAuth(device.getRunOn().getUsername(), device.getRunOn().getPassword())
                .queryString("deviceId", getDeviceId())
                .queryString("instrument", instrumented)
                .asJson();
        return jsonResponse.getBody();
    }

}
