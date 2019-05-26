/**
 * Created by eyal.neumann on 2/19/2018.
 */
//package <set your test package>;

import org.junit.Assert;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class LocationTest extends SeeTestBase {
    String myLocation = "xpath=//*[@accessibilityLabel='coordinatesInfo']";
    String AfterChangeLocationFromApp;
    String startLocation;
    String AfterChangeLocation;
    String AfterClearLocation;
    int secToClear = 30;
    String latitudeString = "31.1";
    String longitudeString = "13.863896";
    double marginOfError = 0.1;
    enum COORDINATION {
        Latitude,Longitude
    }
    @Test
    public void testSetLocation() {
        client.setProperty("ios.auto.accept.alerts", "true");
        client.setProperty("ios.request.location.permission", "true");
        client.install(PathsMap.UICatalog,true,false);
        startLocation = launchAndGetLocation();
        client.setLocation(latitudeString, longitudeString);
        AfterChangeLocation = client.getLocation();
        AfterChangeLocationFromApp = client.elementGetProperty("NATIVE", myLocation, 0, "text");
        Assert.assertTrue("SetLocation didn't work",
                isCloseEnough(getCordination(AfterChangeLocation,COORDINATION.Latitude),getCordination(AfterChangeLocationFromApp,COORDINATION.Latitude))
                        || isCloseEnough(getCordination(AfterChangeLocation,COORDINATION.Longitude),getCordination(AfterChangeLocationFromApp,COORDINATION.Longitude)));
        client.clearLocation();
        for (int i = 1; i <= secToClear; i++) {
            AfterClearLocation = launchAndGetLocation();
            if(isCloseEnough(getCordination(AfterClearLocation,COORDINATION.Longitude),getCordination(startLocation,COORDINATION.Longitude))
                    && isCloseEnough(getCordination(AfterClearLocation,COORDINATION.Longitude),getCordination(startLocation,COORDINATION.Longitude)))
                break;
            if(i == secToClear)
                Assert.fail("Didn't clear the location after " + secToClear +" launches");
        }
    }

    private double getCordination(String location, COORDINATION coordination){
        if (location.contains("Latitude"))
            return Double.parseDouble(location.split(coordination.name().equals(COORDINATION.Latitude) ? "Latitude=" : "Longitude=")[1].split(",")[0]);
        else
            return Double.parseDouble(location.split(",")[coordination.name().equals(COORDINATION.Latitude) ? 0 : 1]);
    }
    private String launchAndGetLocation(){
        client.launch("com.experitest.UICatalog", true, true);
        client.click("NATIVE", "xpath=//*[./*[@accessibilityLabel='Map']]", 0, 1);
        return client.elementGetProperty("NATIVE", myLocation, 0, "text");
    }
    private boolean isCloseEnough(double num1,double num2){
        return Math.abs(num1 - num2) < marginOfError;
    }
}
