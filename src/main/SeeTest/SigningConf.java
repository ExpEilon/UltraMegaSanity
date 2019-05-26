import com.experitest.client.Client;
import com.experitest.client.GridClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SigningConf extends BaseTest {

    String urlBase = runOn.getURL();
    String user = runOn.getUsername();
    String password = runOn.getPassword();
    HttpResponse<JsonNode> response;
    String serialNumber = device.getSN();
    String appName = "com.experitest.UICatalog";
    String uniqueName;

    @Ignore
    @Test
    public void getAllApplications() throws UnirestException {
        String url = urlBase + "/api/v1/applications";
        response = Unirest.get(url)
                .basicAuth(user, password)
//                .queryString("uniqueName","UICatalogWithEntitlementsAndKeychain")
                .asJson();
        System.out.println(response.getBody());
    }

    @Ignore
    @Test
    public void getApplication() throws UnirestException {
        int id = 70;
        String url = urlBase + "/api/v1/applications/" + id;
        response = Unirest.get(url)
                .basicAuth(user, password)
                .asJson();
        System.out.println(response.getBody());
    }
    @Ignore
    @Test
    public void basicInstrumentation() throws UnirestException{
        Assume.assumeTrue("Can run only on grid",isGrid);
        uniqueName = "UICatalogBasicInstrumentation";
        deleteApp(uniqueName);
        int appId = uploadApp(new File(PathsMap.UICatalog),true,false,null,uniqueName);
        System.out.println(installApp(appId,true));
        installedBasicInstrumented(appName);
    }

    @Test
    public void overrideEntitlements() throws UnirestException{
        Assume.assumeTrue("Can run only on grid",isGrid);
        uniqueName = "UICatalogOverrideEntitlements";
        String pathToEntitlements = System.getProperty("user.dir") + "\\entitlementForUICatalog.txt";
        File entitlements = new File(pathToEntitlements);
        deleteApp(uniqueName);
        int appId = uploadApp(new File(PathsMap.UICatalog),true,false,entitlements,uniqueName);
        System.out.println(installApp(appId,false));
        String pathToSupportData = downLoadSupportData();
//        File signerLog = getSignerFile(pathToSupportData);
    }

//    private File getSignerFile(String pathToSupportData) {
//
//    }

    private String downLoadSupportData() {
        GridClient gridClient = new GridClient(device.getRunOn().getAccesskey(), urlBase);
        Client client = gridClient.lockDeviceForExecution("TestFile", query, 30, 300000);
        String returnedValue = client.collectSupportData(device.getDirectory(),"","","","","");
        client.releaseClient();
        return returnedValue;
    }


//    private int uploadApp(boolean fromURL, boolean basicInstrumentation,boolean fixKeychain,boolean overrideEntitlements,String uniqueName) throws UnirestException {
//        String url = urlBase + "/api/v1/applications/new" + (fromURL ? "-from-url":"");
//        String pathToApp = System.getProperty("user.dir")+"\\apps\\UICatalog.ipa";
////        String pathToApp = System.getProperty("user.dir")+"\\apps\\KChing.ipa";
//        String pathToEntitlements = System.getProperty("user.dir") + "\\entitlementForUICatalog.txt";
//        String appUrl = "http://192.168.2.170:8888/Eilon/UICatalog.ipa";
//        File app = new File(pathToApp);
//        File entitlements = new File(pathToEntitlements);
//        response = Unirest.post(url)
//                .basicAuth(user, password)
//                .queryString(fromURL ? "url" : "dummy",appUrl)
//                .queryString("camera", basicInstrumentation ? false:true)
//                .queryString("touchId",basicInstrumentation ? false:true)
//                .queryString("project","default")
//                .queryString(uniqueName.equals("") ? "dummy" : "uniqueName",uniqueName)
//                .queryString("fixKeychainAccess", fixKeychain)
//                .field(overrideEntitlements ? "overrideEntitlements" : "dummy", entitlements)
//                .field("file", app)
//                .asJson();
//        System.out.println(response.getBody());
//        return response.getBody().getObject().getJSONObject("data").getInt("id");
//    }


    public void installedBasicInstrumented(String app){
        boolean assertion = true;
        GridClient gridClient = new GridClient(device.getRunOn().getAccesskey(), urlBase);
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
