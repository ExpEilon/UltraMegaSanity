import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;

public class BruteForce {
    static String newPass = "";
    static String chars = "0123456789aABbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyzZ";

    public static void main(String[] args) throws UnirestException {
        Unirest.setTimeouts(10000000, 1000000);
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                HttpResponse<JsonNode> response;
                while (true) {
                    try {
                        /*response =*/ Unirest.post("https://qacloud.experitest.com/api/v2/status/user")
                                .basicAuth("a", "a")
                                .asJson();
//                        System.out.println(response.getBody().getObject().getInt("status"));
//                        HttpResponse<JsonNode> response;
                String NEW_APP_URL = "/applications/new";
                String webPage = MyProperties.runOn.getURL() + "/api/v1";
                File f = new File(System.getProperty("user.dir")+"\\apps\\EriBank.ipa");
                /*response =*/ Unirest.post (webPage+NEW_APP_URL)
                        .basicAuth (MyProperties.runOn.username, MyProperties.runOn.password)
                        .queryString ("project", "default")
                        .field("file", f)
                        .asJson ();
//            System.out.println(response.getBody ().getObject().getString("status"));
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }


    }
}
