import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.*;
import java.net.URL;
import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.fail;

public class ContextSwitchTestAndroid {
    /*	private String host = "localhost";
        private int port = 8889;*/
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "ContextSwitchTest";
    protected ExAndroidDriver driver = null;
    //private Client client=null;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability(MobileCapabilityType.UDID, "32049b2612d2b1b9");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        dc.setCapability("instrumentApp", true);

        driver = new ExAndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);


        //driver.context("NATIVE_APP");
        driver.findElement(By.xpath("//*[@hint='Username']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@hint='Password']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@text='Login']")).click();
        //client=driver.getClient();
    }

    @Test
    public void testContext_WEB_NOIN_IN() {

        driver.context("WEBVIEW_1");
        verifyContextIsWeb();

        driver.context("NATIVE_APP_NON_INSTRUMENTED");
        verifyContextIsNonInstrumented();

        driver.context("NATIVE_APP_INSTRUMENTED");
        verifyContextIsInstrumented();
    }
    @Test
    public void testContext_WEB_NOIN_WEB() {

        driver.context("WEBVIEW_1");
        verifyContextIsWeb();

        driver.context("NATIVE_APP_NON_INSTRUMENTED");
        verifyContextIsNonInstrumented();

        driver.context("WEBVIEW_1");
        verifyContextIsWeb();
    }

    @Test
    public void testContext_WEB_IN_WEB() {

        driver.context("WEBVIEW_1");
        verifyContextIsWeb();

        driver.context("NATIVE_APP_INSTRUMENTED");
        verifyContextIsInstrumented();

        driver.context("WEBVIEW_1");
        verifyContextIsWeb();
    }

    @Test
    public void testContext_IN_NOIN_IN() {
        driver.context("NATIVE_APP_INSTRUMENTED");
        verifyContextIsInstrumented();

        driver.context("NATIVE_APP_NON_INSTRUMENTED");
        verifyContextIsNonInstrumented();

        driver.context("NATIVE_APP_INSTRUMENTED");
        verifyContextIsInstrumented();

    }
    @Test
    public void testContext_NOIN_IN_NOIN() {
        driver.context("NATIVE_APP_NON_INSTRUMENTED");
        verifyContextIsNonInstrumented();

        driver.context("NATIVE_APP_INSTRUMENTED");
        verifyContextIsInstrumented();

        driver.context("NATIVE_APP_NON_INSTRUMENTED");
        verifyContextIsNonInstrumented();

    }



    @After
    public void tearDown() {
        driver.quit();
    }


    /**
     *
     */
    private void verifyContextIsNonInstrumented() {
        verifyNonInstrumentedNativeElementIsFound();
        verifyWebElementIsNotFound();
        verifyInstrumentedNativeElementIsNotFound();
    }

    /**
     *
     */
    private void verifyContextIsInstrumented() {
        verifyInstrumentedElementIsFound();
        verifyWebElementIsNotFound();
        verifyNonInstrumentedNativeElementIsNotFound();
    }

    /**
     *
     */
    private void verifyContextIsWeb() {
        verifyWebElementIsFound();
        verifyInstrumentedNativeElementIsNotFound();
        verifyNonInstrumentedNativeElementIsNotFound();
    }

    /**
     *
     */
    private void verifyNonInstrumentedNativeElementIsFound() {
        driver.findElement(By.xpath("//*[@class='android.view.View']"));
    }

    /**
     *
     */
    private void verifyInstrumentedElementIsFound() {
        driver.findElement(By.xpath("//*[@class='android.view.ViewStub']"));
    }
    /**
     *
     */
    private void verifyWebElementIsFound() {
        driver.findElement(By.xpath("//*[@nodeName='H1']")).click();
    }

    /**
     *
     */
    private void verifyInstrumentedNativeElementIsNotFound() {
        try {
            verifyInstrumentedElementIsFound();
            System.out.println("Instrumented Native Element Found when it shouldn't");
            //client.report("Instrumented Native Element Found when it shouldn't", false);
            fail("Instrumented Native Element Found when it shouldn't");

        } catch (Exception e) {
            System.out.println("Instrumented Native Element Not Found when it shouldn't");
            //client.report("Instrumented Native Element Not Found when it shouldn't", true);

        }
    }
    private void verifyNonInstrumentedNativeElementIsNotFound() {
        try {
            verifyNonInstrumentedNativeElementIsFound();
            System.out.println("Non Instrument Native Element Found when it shouldn't");
            //client.report("Non Instrument Native Element Found when it shouldn't", false);
            fail("Non Instrument Native Element Found when it shouldn't");

        } catch (Exception e) {
            System.out.println("Non Instrument Native Element Not Found when it shouldn't");
            //client.report("Non Instrument Native Element Not Found when it shouldn't", true);

        }
    }
    private void verifyWebElementIsNotFound() {
        try {
            verifyWebElementIsFound();
            System.out.println("Non Instrument Native Element Found when it shouldn't");
            //client.report("Non Instrument Native Element Found when it shouldn't", false);
            fail("Non Instrument Native Element Found when it shouldn't");

        } catch (Exception e) {
            System.out.println("Non Instrument Native Element Not Found when it shouldn't");
            //client.report("Non Instrument Native Element Not Found when it shouldn't", true);

        }
    }

}