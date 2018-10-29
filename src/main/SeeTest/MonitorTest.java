import com.experitest.client.Client;
import com.sun.jna.StringArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MonitorTest extends BaseTest{


    @Test
    public void battery(){
        String file = projectBaseDirectory + "\\monitor.csv";
        client.setProperty("ios.auto.accept.alerts", "true");
        client.startMonitor("com.apple.Maps");
        client.deviceAction("Home");
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
        Assert.assertTrue("CSV file has empty fields!",checkCSV(client.getMonitorsData(file)));

    }
    public boolean checkCSV(String csv){
        final int rowsToIgnore = 2; // first sample might be empty, so ignores it
        String modifiedCSV = csv.split("\n",2)[1];
        System.out.println(modifiedCSV);
        int csvRows = modifiedCSV.split("\n").length;
        if(csvRows<rowsToIgnore) return true;
        return !IntStream.range(rowsToIgnore,csvRows)
                .anyMatch(i -> Arrays.asList(modifiedCSV.split("\n")[i].concat("l").split(",")) //adds 'l' to string for case that there are ,,, in the end of the row, so split func gets it.
                        .stream()
                        .anyMatch(j -> j.equals("")));
    }
}
