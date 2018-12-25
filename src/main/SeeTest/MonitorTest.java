import org.junit.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.IntStream;


public class MonitorTest extends SeeTestBase {

    @Test
    public void nonInstrumented(){
        client.setProperty("ios.auto.accept.alerts", "true");
        client.startMonitor("com.apple.Maps");
//        client.deviceAction("Home");
        client.launch("com.apple.Maps", true, true);

        // pinch
        client.startMultiGesture("test");
        // finger 0
        client.multiTouchDownCoordinate(200,600, 0);
        client.multiTouchMoveCoordinate(250, 600, 0);
        client.multiTouchUp(0);

        // finger 1
        client.multiTouchDownCoordinate(170,600, 1);
        client.multiTouchMoveCoordinate(140, 600, 1);
        client.multiTouchUp(1);

        client.performMultiGesture();
        //swipe
        client.startMultiGesture("test");
        // finger 0
        client.multiTouchDownCoordinate(200,600, 0);
        client.multiTouchMoveCoordinate(250, 600, 0);
        client.multiTouchUp(0);
        client.performMultiGesture();

        // swipe
        client.startMultiGesture("test");
        // finger 0
        client.multiTouchDownCoordinate(200,600, 0);
        client.multiTouchMoveCoordinate(250, 600, 0);
        client.multiTouchUp(0);
        client.performMultiGesture();
        Assert.assertTrue("CSV file has empty fields!",checkCSV(client.getMonitorsData(getFilePath(false))));

    }

    @Test
    public void instrumented() {
        String appName = "com.experitest.ExperiBank";
        if(!installedInstrumented(appName))
            client.install(System.getProperty("user.dir")+"\\apps\\EriBank.ipa",true,false);
        client.startMonitor(appName);
        client.launch(appName, true, true);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='usernameTextField']", 0, "company");
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='passwordTextField']", 0, "company");
        client.click("NATIVE","xpath=//*[@accessibilityLabel='Login']", 0, 1);
        client.waitForElement("NATIVE","xpath=//*[@accessibilityLabel='Invalid username or password!']", 0, 2000);
        if (client.isElementFound("NATIVE","xpath=//*[@accessibilityLabel='Invalid username or password!']", 0))
            client.click("NATIVE","xpath=//*[@text='Dismiss']", 0, 1);
        Assert.assertTrue("CSV file has empty fields!", checkCSV(client.getMonitorsData(getFilePath(true))));
    }

    public boolean checkCSV(String csv){
        final int rowsToIgnore = 3; // first sample might be empty, so ignores it
        String modifiedCSV = csv.split("\n",2)[1];
        System.out.println(modifiedCSV);
        int csvRows = modifiedCSV.split("\n").length;
        if(csvRows<rowsToIgnore) return true;
        return !IntStream.range(rowsToIgnore,csvRows)
                .anyMatch(i -> Arrays.asList(modifiedCSV.split("\n")[i].concat("l").split(",")) //adds 'l' to string for case that there are ,,, in the end of the row, so split func gets it.
                        .stream()
                        .anyMatch(j -> j.equals("")));
    }

    private String getFilePath(Boolean instrumented){
        File file = new File(projectBaseDirectory + (instrumented ? "\\MonitorInstrumented" : "\\MonitorNonInstrumented"));
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath() + "\\" + sdFormat.format(new Date()) + ".csv";
    }
}
