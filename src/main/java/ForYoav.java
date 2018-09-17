import com.experitest.client.*;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class ForYoav {
    GridClient gridClient = null;
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;
    String curApp = "dfs.PressTheDot";
    int curRun = 1;
    String failed = "";
    @Before
    public void setUp(){
//        client = new Client(host, port, true);

    }

    @Test
    public void testUntitled() throws InterruptedException, IOException {


//        while(true) {
//            Runtime.getRuntime().exec("cmd /c start  net STOP ecaservice");
//            Thread.sleep(6000);
//            Runtime.getRuntime().exec("cmd /c start  net START ecaservice");
////            Thread.sleep(30000);
//            gridClient = new GridClient("eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVeU1Ea3pNVGs1TlRjeU9BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4MzYyOTE5OTUsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.8txP0YRh1ARltJM9mLjHXhXIzDn8Qi1UbK8tNi56-xA",
//                    "https://192.168.2.22:11111");
//            client = gridClient.lockDeviceForExecution("TestFile", "@serialnumber='1c18924584f05c3594d431229a7ceb2c5897b769'", 30, 300000);
//            client.hybridClearCache(true,true);
//            boolean installFromPass = new Random().nextBoolean();
//            boolean installInstrumentation = new Random().nextBoolean();
//            boolean launch = new Random().nextBoolean();
//            boolean install = new Random().nextBoolean();
//            client = new Client(host, port, true);
//            client.setDevice("ios_app:erez akri’s iPad#B0018");
//            client = gridClient.lockDeviceForExecution("TestFile", "@serialnumber='b386670b67fa917c2a65a9f2d70470347457678b'", 30, 300000);
//            if(install) {
//                if (installFromPass)
//                    client.install("C:\\Users\\eilon.grodsky\\Downloads\\a.ipa", installInstrumentation, false);
//                else
//                    client.install("cloud:" + curApp, installInstrumentation, false);
//            }
//            if(client.getInstalledApplications().contains(curApp)){
//                client.launch(curApp,launch,true);
//                if(!client.getCurrentApplicationName().equals(curApp)){
//                    failed += "Run num: "+ curRun + "installedAtBegining? " + install + " installFromPass: " + installFromPass + " installInstrumentation: " + " launch: " + launch +"\n";
//    //                System.out.println("Run num: "+ curRun +" installFromPass: " + installFromPass + " installInstrumentation: " + " launch: " + launch);
//                }
//            }
//            if(install)
//                client.uninstall(curApp);
//            client.releaseClient();
//            curRun++;
//            System.out.println(failed);
//        }
    }

    @After
    public void tearDown(){
        client.releaseClient();
    }
}
