import com.experitest.client.Client;
import com.experitest.client.GridClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Issue27106 {
    String urlBase = "http://192.168.2.22:9000";
    String user = "admin";
    String password = "Experitest2012";
    HttpResponse<JsonNode> response;
    String serialNumber = "2ec34da9c45eedbeea2bfaf2253a3b567ce45699";
    String appName = "com.experitest.UICatalog";

    @Test
    public void getAllApplications() throws UnirestException {
        String url = urlBase + "/api/v1/applications";
        response = Unirest.get(url)
                .basicAuth(user, password)
//                .queryString("uniqueName","UICatalogWithEntitlementsAndKeychain")
                .asJson();
        System.out.println(response.getBody());
    }
    @Test
    public void getApplication() throws UnirestException {
        int id = 70;
        String url = urlBase + "/api/v1/applications/" + id;
        response = Unirest.get(url)
                .basicAuth(user, password)
                .asJson();
        System.out.println(response.getBody());
    }
//UICatalogWithKeychain UICatalogWithEntitlements   UICatalogWithEntitlementsAndKeychain    UICatalogWithEntitlementsNotDebug   UICatalogNotBasic   UICatalogEntitlementsBool
    //UICatalogKeychainString   UICatalogMEntitlements  UICatalogMKeychain
//UICatalogNoNeedToSignWithKeychain UICatalogNoNeedToSignWithEntitlements
    @Test
    public void basicInstrumentation() throws UnirestException{
        int appId = uploadApp(false,false,false,false,"KChing");
        System.out.println(installApp(appId,true));
//        installedBasicInstrumented(appName);
//        Assert.assertTrue(installedBasicInstrumented(appName) == instrumented);
//        GridClient gridClient = new GridClient("eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVME5ERXdNVEUzTXpneE1nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NTk0NjExNzQsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.jYD-urvjQ6cqlBL03EwyfYFWV4E8R2QMkCoqGJ1neA8", urlBase);
//        Client client = gridClient.lockDeviceForExecution("TestFile", "@serialnumber = '" + serialNumber +"'", 30, 300000);
//        client.install("cloud:uniqueName=UICatalogNoNeedToSignWithKeychain",false,false);
//        client.releaseClient();
    }

    private int uploadApp(boolean fromURL, boolean basicInstrumentation,boolean fixKeychain,boolean overrideEntitlements,String uniqueName) throws UnirestException {
        String url = urlBase + "/api/v1/applications/new" + (fromURL ? "-from-url":"");
//        String pathToApp = System.getProperty("user.dir")+"\\apps\\UICatalog.ipa";
        String pathToApp = System.getProperty("user.dir")+"\\apps\\KChing.ipa";
        String pathToEntitlements = "E:\\issue-27106\\entitlementForUICatalog.txt";
        String appUrl = "http://192.168.2.170:8888/Eilon/UICatalog.ipa";
        File app = new File(pathToApp);
        File entitlements = new File(pathToEntitlements);
        response = Unirest.post(url)
                .basicAuth(user, password)
                .queryString(fromURL ? "url" : "dummy",appUrl)
                .queryString("camera", basicInstrumentation ? false:true)
                .queryString("touchId",basicInstrumentation ? false:true)
                .queryString("project","default")
                .queryString(uniqueName.equals("") ? "dummy" : "uniqueName",uniqueName)
                .queryString("fixKeychainAccess", fixKeychain)
                .field(overrideEntitlements ? "overrideEntitlements" : "dummy", entitlements)
                .field("file", app)
                .asJson();
        System.out.println(response.getBody());
        return response.getBody().getObject().getJSONObject("data").getInt("id");
    }

    @Test
    public void newApplication() throws UnirestException {
        String url = urlBase + "/api/v1/applications/new";
        String pathToApp = System.getProperty("user.dir")+"\\apps\\UICatalog.ipa";
        File app = new File(pathToApp);
        response = Unirest.post(url)
                .basicAuth(user, password)
                .queryString("camera", false)
                .queryString("touchId",false)
                .queryString("project","default")
//                .queryString("uniqueName","UICatalogBasic")
                .queryString("fixKeychainAccess", true)
//                .queryString("overrideEntitlements", true)
                .field("file", app)
                .asJson();
        System.out.println(response.getBody());
        int appId = response.getBody().getObject().getJSONObject("data").getInt("id");
//        System.out.println(installApp(appId,instrumented));
//        Assert.assertTrue(installedInstrumented(appName) == instrumented);
    }

    @Test
    public void newApplicationURL() throws UnirestException {
        String url = urlBase + "/api/v1/applications/new-from-url";
        String appUrl = "http://192.168.2.170:8888/Eilon/PressTheDot093.ipa";
        response = Unirest.get(url)
                .basicAuth(user, password)
                .queryString("url", appUrl)
//                .queryString("fixKeychainAccess", true)
//                .queryString("overrideEntitlements", true)
                .asJson();
        System.out.println(response.getBody());
    }

    public int getDeviceId(String sn) throws UnirestException {
        HttpResponse<JsonNode> response1 = Unirest.get(urlBase+"/api/v1/devices")
                .basicAuth(user, password)
                .asJson();
        JSONArray jsonArray = response1.getBody().getObject().getJSONArray("data");
        return IntStream.range(0,jsonArray.length()).mapToObj(i->jsonArray.getJSONObject(i)).collect(Collectors.toList()).stream()
                .filter(j -> j.getString("udid").equals(sn)).findFirst().map(j -> j.getInt("id")).get();
    }

    public JsonNode installApp(int appId,boolean instrumented) throws UnirestException {
        String urlInstall = urlBase + "/api/v1/applications/"+ appId +"/install";
        response = Unirest.post(urlInstall)
                .basicAuth(user, password)
                .queryString("deviceId", getDeviceId(serialNumber))
                .queryString("instrument", instrumented)
                .asJson();
        return response.getBody();
    }


    public void installedBasicInstrumented(String app){
        boolean assertion = true;
        GridClient gridClient = new GridClient("eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVMU16WTVNakkyTmpVNU9BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4NjkwNTIyNjYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.JlPI1RSBX5M4U9-DrOL5f53dk0O5_fJY-rsLWxidyYE", urlBase);
        Client client = gridClient.lockDeviceForExecution("TestFile", "@serialnumber = '" + serialNumber +"'", 30, 300000);
        try {
            client.launch(app, true, true);
            Assert.assertFalse(client.getDeviceProperty("instrumentation.version").equals("UNKNOWN"));
            client.elementListSelect("", "text=Authentication", 0, true);
            client.setAuthenticationReply("Success", 0);
//            client.click("NATIVE", "text=Request Touch ID Authentication", 0, 1);
//            client.verifyElementFound("NATIVE", "//*[@text='Error Code: -7. No identities are enrolled.']", 0);
        }catch (com.experitest.client.InternalException e){assertion = false;}
        finally {
            client.releaseClient();
        }
        Assert.assertFalse(assertion);
    }
}
