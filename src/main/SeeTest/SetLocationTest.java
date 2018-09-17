/**
 * Created by eyal.neumann on 2/19/2018.
 */
//package <set your test package>;

import com.experitest.client.Client;
import com.experitest.client.GridClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class SetLocationTest extends BaseTest{
    // Xpaths in use :
    String trackingButton = "xpath=//*[@text='Tracking']";
    String myLocation = "xpath=//*[@name='My Location']";
    String close = "xpath=//*[@text='Close']";
    String longitudeAndLatitude = "xpath=//*[contains(@text,'Latitude')]";
    String locationString;
    String messuredLatitudeString;
    String messuredLongitudeString;
    String[] splitLocationString;
    double latitudeDifference;
    double longitudeDifference;
    int timeout = 100000;
    double marginOfError = 0.00001;
    String setLatitudeString = "31.290174";
    String setLongitudeString = "33.863896";

    @Test
    public void testSetLocationIOS(){
        if(MyProperties.makeReporter)
            client.setReporter("xml", projectBaseDirectory +"//Reporter", "SetLocationTest");
        client.setProperty("ios.auto.accept.alerts", "true");
        client.launch("com.apple.Maps", true, true);
        client.clearLocation();
        client.click("NATIVE", trackingButton, 0, 1);
        assertTrue(client.waitForElementToVanish("NATIVE","xpath=//*[@value='Determining Location…']",0,timeout));
        client.waitForElement("NATIVE", myLocation, 0, timeout);
        client.click("NATIVE", myLocation, 0, 1);
        locationString = client.elementGetText("NATIVE", longitudeAndLatitude, 0);
        System.out.println(" After Clear Location :"+locationString);
        client.click("NATIVE", close, 0, 1);
        splitLocationString = locationString.replaceAll(" ", "").split(",");

        messuredLatitudeString = splitLocationString[1];
        messuredLongitudeString = splitLocationString[3];
        Double messuredStartLatitude;
        try {
            messuredStartLatitude = Double.parseDouble(messuredLatitudeString);
        }catch (Exception e){
            messuredStartLatitude = Double.parseDouble(splitLocationString[2]);
        }
        Double messuredStartLongitude =Double.parseDouble(messuredLongitudeString);

        //**************************************

        client.setLocation(setLatitudeString, setLongitudeString);
        client.click("NATIVE", trackingButton, 0, 1);
        assertTrue(client.waitForElementToVanish("NATIVE","xpath=//*[@value='Determining Location…']",0,timeout));
        client.waitForElement("NATIVE", myLocation, 0, timeout);
        client.click("NATIVE", myLocation, 0, 1);
        locationString = client.elementGetText("NATIVE", longitudeAndLatitude, 0);
        client.click("NATIVE", close, 0, 1);
        System.out.println(" After Set Location :"+locationString);
        splitLocationString = locationString.replaceAll(" ", "").split(",");

        messuredLatitudeString = splitLocationString[1];
        messuredLongitudeString = splitLocationString[3];

        Double messuredLatitude;
        try {
            messuredLatitude = Double.parseDouble(messuredLatitudeString);
        }catch (Exception e){
            messuredLatitude = Double.parseDouble(splitLocationString[2]);
        }

        Double messuredLongitude =Double.parseDouble(messuredLongitudeString);

        Double setLatitude= Double.parseDouble(setLatitudeString);
        Double setLongitude= Double.parseDouble(setLongitudeString);

        latitudeDifference = Math.abs(setLatitude - messuredLatitude);
        longitudeDifference = Math.abs(setLongitude - messuredLongitude);
        assertTrue((latitudeDifference*latitudeDifference+longitudeDifference*longitudeDifference)< marginOfError);

        //********************************************

        client.clearLocation();
        client.click("NATIVE", trackingButton, 0, 1);
        assertTrue(client.waitForElementToVanish("NATIVE","xpath=//*[@value='Determining Location…']",0,timeout));
        client.waitForElement("NATIVE", myLocation, 0, timeout);
        client.click("NATIVE", myLocation, 0, 1);
        locationString = client.elementGetText("NATIVE", longitudeAndLatitude, 0);
        System.out.println(" After Clear Location :"+locationString);
        client.click("NATIVE", close, 0, 1);
        splitLocationString = locationString.replaceAll(" ", "").split(",");


        messuredLatitudeString = splitLocationString[1];
        messuredLongitudeString = splitLocationString[3];
        Double messuredEndLatitude;
        try {
            messuredEndLatitude = Double.parseDouble(messuredLatitudeString);
        }catch (Exception e){
            messuredEndLatitude = Double.parseDouble(splitLocationString[2]);
        }

        Double messuredEndLongitude =Double.parseDouble(messuredLongitudeString);


        latitudeDifference = Math.abs(setLatitude - messuredLatitude);
        longitudeDifference = Math.abs(setLongitude - messuredLongitude);
        assertTrue((latitudeDifference*latitudeDifference+longitudeDifference*longitudeDifference)< marginOfError);
    }
    public String afterLocChanged(){
        client.click("NATIVE", trackingButton, 0, 1);
        assertTrue(client.waitForElementToVanish("NATIVE","xpath=//*[@value='Determining Location…']",0,timeout));

        client.waitForElement("NATIVE", myLocation, 0, timeout);

        client.click("NATIVE", myLocation, 0, 1);
        locationString = client.elementGetText("NATIVE", longitudeAndLatitude, 0);
        System.out.println(" After Clear Location :"+locationString);
        client.click("NATIVE", close, 0, 1);
        splitLocationString = locationString.replaceAll(" ", "").split(",");

        messuredLatitudeString = splitLocationString[1];
        messuredLongitudeString = splitLocationString[3];

        Double messuredStartLatitude;
        try {
            messuredStartLatitude = Double.parseDouble(messuredLatitudeString);
        }catch (Exception e){
            messuredStartLatitude = Double.parseDouble(splitLocationString[2]);
        }

        Double messuredStartLongitude =Double.parseDouble(messuredLongitudeString);
        return messuredStartLatitude +","+messuredStartLongitude;
    }
}
