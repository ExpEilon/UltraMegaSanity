import com.experitest.client.Client;
import com.experitest.client.Utils;
import com.experitest.client.log.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class SetAuthTest extends BaseTest{

    private String[] replies={
            "Success" ,
            "StopMock",
            "AuthenticationFailedError",
            "UserCancelError",
            "UserFallbackError",
            "SystemCancelError",
            "PasscodeNotSetError",
            "TouchIDNotAvailableError",
            "TouchIDNotEnrolledError",
            "TouchIDLockoutError",
            "AppCancelError",
            "InvalidContextError"};
    // private String[] uIReplays = new String[replays.length];
    private String[] expectedReplies={
            "Success",
            "StopMock have No Fixed Replay",
            "Error Code: -1. Application retry limit exceeded.",
            "Error Code: -2. Canceled by user.",
            "Error Code: -3. Fallback authentication mechanism selected.",
            "Error Code: -4. UI canceled by system.",
            "Error Code: -5. Passcode not set.",
            "Error Code: -6. Biometry is not available on this device.",
            "Error Code: -7. No identities are enrolled.",
            "Error Code: -8. Biometry is locked out.",
            "Error Code: -9. kLAErrorAppCancel",
            "Error Code: -10. kLAErrorInvalidContext"};
    private int[] waitTimes = {0,1000,2000,5000,7000,8000,10000,13000,15000,17000,20000};
    private long[] measuredWaitTimes = new long[waitTimes.length];
    @Test
    public void testSetAuthTest_Replies(){
        if(MyProperties.makeReporter)
            client.setReporter("xml", projectBaseDirectory +"//Reporter", "SetAuthTest");
        client.install("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\apps\\UICatalog.ipa",true,false);
        client.launch("com.experitest.UICatalog", true, true);
        client.elementListSelect("", "text=Authentication", 0, true);
        String uIReply;
        String reply;
        for (int i=0;i<replies.length;i++){
            reply=replies[i];
            client.startStepsGroup(reply);
            client.setAuthenticationReply(reply, 0);
            client.click("NATIVE", "text=Request Touch ID Authentication", 0, 1);
            uIReply = client.elementGetText("NATIVE", "xpath=//*[@class='UIView' and @height>0 and ./*[@text='TouchID']]//*[@class='UILabel']", 1);
            //System.out.println(uIReplay);
            if (!reply.equals("StopMock")){
                boolean correctResult=uIReply.equals(expectedReplies[i]);
                if (correctResult){
                    client.report("Correct Result", true);
                }
                else{
                    client.report("Incorrect Result, should be "+expectedReplies[i], false);
                }
                assertTrue(correctResult);
            }
            else{
                client.report("Result for StopMock is : "+uIReply, true);

            }
            client.click("NATIVE", "xpath=//*[@class='_UIAlertControllerActionView']/*/*", 0, 1);
            client.stopStepsGroup();
        }
    }

    @Test
    public void testSetAuthTest_Times(){
//        client.setLogger(Utils.initDefaultLogger(Level.ALL));
//        client.startLoggingDevice(projectBaseDirectory+"//devicelog.log");
        if(MyProperties.makeReporter)
            client.setReporter("xml", projectBaseDirectory +"//Reporter", "SetAuthTest");
//        Map<String, Object> launchOptionsMap = new HashMap();
//        Map envVars = new HashMap();
//        envVars.put("EXPERI_ENABLE_LOG_TO_CONTAINER", "true");
//        launchOptionsMap.put("launch_env", envVars);
//        launchOptionsMap.put("instrument", true);
//        launchOptionsMap.put("relaunch", true);
//        client.launch("com.experitest.UICatalog",launchOptionsMap);

        client.launch("com.experitest.UICatalog", true, true);
        client.elementListSelect("", "text=Authentication", 0, true);
        long time0;
        long measuredWaitTime;
        for (int i=0;i<waitTimes.length;i++){
            client.startStepsGroup("Wait "+(waitTimes[i]/1000)+" Seconds");
            client.setAuthenticationReply("Success", waitTimes[i]);
            client.click("NATIVE", "text=Request Touch ID Authentication", 0, 1);
            time0=System.currentTimeMillis();
            client.waitForElement("NATIVE", "xpath=//*[@class='_UIAlertControllerActionView']/*/*", 0, 100000);
            measuredWaitTime =System.currentTimeMillis()-time0;
            boolean correctResult=(Math.abs(measuredWaitTime-waitTimes[i])<2000);
            if (correctResult){
                client.report("Correct Result", true);
            }
            else{
                client.report("Incorrect Result, should be less than "+(waitTimes[i]+1000), false);
            }
            System.out.println("Round: " + i + " ,waitTime: " + waitTimes[i] + ", measuredWaitTime" + measuredWaitTime);
            assertTrue(correctResult);
            System.out.println("Expected Wait Time   (in mSeconds) :"+waitTimes[i]);
            System.out.println("Measured Waited Time (in mSeconds) :"+measuredWaitTime);
            client.click("NATIVE", "xpath=//*[@class='_UIAlertControllerActionView']/*/*", 0, 1);
        }
    }
}
