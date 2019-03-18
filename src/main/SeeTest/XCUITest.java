import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;
import static java.lang.Thread.sleep;

public class XCUITest {
    String urlBase = "https://qacloud.experitest.com";
    String user = "khaleda";
    String password = "Experitest2012";
    HttpResponse<JsonNode> response;
    String url =  urlBase + "/api/v1/test-run/execute-test-run";
    //iOS data
    String iOSPathToTestApp = System.getProperty("user.dir")+"\\tests\\PressTheDotUITests-Runner093.app.zip";//MBJ_UITests-Runner   PressTheDotUITests-Runner101    app_under_testUITests-Runner.app
    String iOSPathToApp = System.getProperty("user.dir")+"\\apps\\PressTheDot093.ipa";//KChing     PressTheDot
    String iOSIgnoredPath = System.getProperty("user.dir") + "\\PressTheDotUITestsOnlyLong.xcscheme";

//    String iOSPathToTestApp = "E:\\117917\\MBJ_UITests-Runner.app.zip";
//    String iOSPathToApp = "E:\\117917\\KChing.ipa";

    //android data
    String androidPathToTestApp = System.getProperty("user.dir")+"\\tests\\EspressoTests.apk";//EspressoTests   app-kbc-debug-androidTest
    String androidPathToApp = System.getProperty("user.dir")+"\\apps\\EriBank.apk";//EriBank  app-kbc-debug
    String androidIgnoredPath = System.getProperty("user.dir")+"\\AndroidIgnoredTests";

    @Test
    public void swift() throws UnirestException, InterruptedException {
        long startTime = System.currentTimeMillis();
//        while (true) {
            System.out.println(runTest("XCUITest",false, false, true,
                    "@serialnumber='e51e57c7ab9ae0140116c606c8e3ad92aeb11a14'"));
//        }
//        System.out.println("Time for 2: " + (System.currentTimeMillis() - startTime)/1000);
//          @os='iOS'
//          @serialnumber='LGH870de6cef0c' or @serialnumber='f574d2e0c150dd65f0f9349b13010340b6978dc9'
//        System.out.println("Time for 1: " + (System.currentTimeMillis() - startTime)/1000);

//        int queued = 4, runningTests = 1;
//        int runId;
//        startTime = System.currentTimeMillis();
//        runId = response.getBody().getObject().getJSONObject("data").getInt("Test Run Id");
//        while (queued != 0 && runningTests != 0){
//            sleep(1000);
//            HttpResponse<JsonNode> response = Unirest.get(urlBase + "/api/v1/test-run/" + runId +"/status")
//                    .basicAuth(user, password)
//                    .asJson();
//            queued = response.getBody().getObject().getJSONObject("data").getInt("Number of queued tests");
//            runningTests = response.getBody().getObject().getJSONObject("data").getInt("Number of running tests");
//        }
//        System.out.println("Time for one device: " + (System.currentTimeMillis() - startTime)/1000);
    }
    @Test
    public void espresso() throws UnirestException, InterruptedException {
//        while (true) {
            System.out.println(runTest("espresso",false,false,true,
                    "@os='android'"));
//        }
//                response.getBody().getObject().getJSONObject("data").getInt("Test Run Id");
//                HttpResponse<JsonNode> response = Unirest.get(urlBase + "/api/v1/test-run/" + runId +"/status")
//                        .basicAuth(user, password)
//                        .asJson();
//                queued = response.getBody().getObject().getJSONObject("data").getInt("Number of queued tests");
//            }
//        }
    }

    @Test
    public void objC() throws UnirestException {
        String pathToTestApp = System.getProperty("user.dir")+"\\tests\\ObjCAppUITests-Runner.app.zip";
        String pathToApp = System.getProperty("user.dir")+"\\apps\\ObjCApp.ipa";
        File app = new File(pathToApp);
        File testApp =  new File(pathToTestApp);
        response = Unirest.post(url)
                .basicAuth(user, password)
                .queryString("executionType", "xcuitest")
                .queryString("runningType", "coverage")
//                .queryString("deviceQueries", "@os='ios'")
                .queryString("deviceQueries", "@serialnumber='00008020-000E2D1121F8002E'")
                .field("app", app)
                .field("testApp", testApp)
                .asJson();
        System.out.println(response.getBody());
    }
    @Test
    public void cancel() throws UnirestException {
        HttpResponse<String> response = Unirest.post(urlBase + "/api/v1/test-run/" + 2051913 +"/cancel")
                .basicAuth(user, password)
                .asString();

        System.out.println(response.getBody());
    }
    @Test
    public void status() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(urlBase + "/api/v1/test-run/" + 3939 +"/status")
                .basicAuth(user, password)
                .asJson();
        System.out.println(response.getBody());
        System.out.println(response.getBody().getObject().getJSONObject("data").getInt("Number of queued tests"));
    }

    /**
     * @param test either XCUITest or espresso
     * @param isAsync
     * @param isFastFeedBack
     * @param ignoreTests
     * @param query
     * @return the Json result
     * @throws UnirestException
     */
    private JsonNode runTest(String test,boolean isAsync,boolean isFastFeedBack,boolean ignoreTests,String query) throws UnirestException {
        File app = new File(test.equals("XCUITest") ? iOSPathToApp : androidPathToApp);
        File testApp =  new File(test.equals("XCUITest") ? iOSPathToTestApp : androidPathToTestApp);
        File schemeFile = new File(test.equals("XCUITest") ? iOSIgnoredPath : androidIgnoredPath);
        Unirest.setTimeouts(0,0);
        response = Unirest.post(url + (isAsync ? "-async":""))
                .basicAuth(user, password)
                .queryString("executionType",test.equals("XCUITest") ? "xcuitest" : "espresso")
                .queryString("runningType", isFastFeedBack ? "fastFeedback" : "coverage")
                .queryString("deviceQueries", query)
                .queryString("creationTimeout",20000)
                .field("app", app)
                .field("testApp", testApp)
                .field(ignoreTests ? "ignoreTestFile" : "dummy",schemeFile)
                .asJson();
        return response.getBody();
    }
}
