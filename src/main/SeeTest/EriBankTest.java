import org.junit.Test;

import java.util.Random;

public class EriBankTest extends SeeTestBase {
    String str0,str;
    double oldD,newD;
    @Test
    public void EriTest() throws Exception {
        app = "com.experitest.ExperiBank";
        pathToApp = isSimulator ? PathsMap.EriBankSimulator : PathsMap.EriBank;
        bundleToPath.put(app,pathToApp);
        if(client.getInstalledApplications().contains(app))
            client.uninstall(app);
        if(ConfigManager.checkIfSetTrue("installFromPath"))
            client.install(pathToApp ,false,false);
        else {
            if(isGrid)
                uploadIfMissing(app);
            client.install(isGrid ? "cloud:" + app : app, MyProperties.instrumented ? true : false, false);
        }
        launch();
        client.elementSendText("NATIVE", MyProperties.instrumented ? "xpath=//*[@accessibilityIdentifier='usernameTextField']" : "xpath=//*[@placeholder='Username']", 0, "company");
        client.elementSendText("NATIVE", MyProperties.instrumented ? "xpath=//*[@accessibilityIdentifier='passwordTextField']": "xpath=//*[@placeholder='Password']", 0, "company");
        client.click("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Login']" : "xpath=//*[@text='loginButton']", 0, 1);
        client.waitForElement("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Invalid username or password!']" : "xpath=//*[@text='Invalid username or password!']", 0, 2000);
        if (client.isElementFound("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Invalid username or password!']" : "xpath=//*[@text='Invalid username or password!']", 0))
            client.click("NATIVE",MyProperties.instrumented ? "xpath=//*[@text='Dismiss']" : "xpath=//*[@text='Dismiss']", 0, 1);
        if(!isSimulator) { //We don't support "in" command in simulators
            str0 = client.elementGetProperty("NATIVE", MyProperties.instrumented ? "xpath=//*[@text='Make Payment']" : "xpath=(//*/*[@class='UIAStaticText' and @knownSuperClass='NSObject' and not(contains(text(),'balance'))])", 0,"text");
            str = str0.split("\\$")[0];
            oldD = Double.parseDouble(str);
        }
        client.click("NATIVE", MyProperties.instrumented ?"xpath=//*[@text='Make Payment']" : "xpath=//*[@text='makePaymentButton']", 0, 1);
        client.elementSendText("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Phone']" : "xpath=//*[@placeholder='Phone']", 0, "0523898058");
        client.elementSendText("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Name']" : "xpath=//*[@placeholder='Name']", 0, "Eilon");
        double payment = new Random().nextInt(2000) - 1000;
        client.elementSendText("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Amount']" : "xpath=//*[@placeholder='Amount']", 0, Double.toString(payment));
        client.closeKeyboard();
        if(MyProperties.instrumented) {
            client.click("NATIVE", MyProperties.instrumented ?"xpath=//*[@accessibilityLabel='Select']" : "xpath=//*[@text='countryButton']", 0, 1);
            client.elementListSelect("accessibilityLabel=conutryView", "accessibilityLabel=Tanzania", 0, true);
        }
        else
            client.elementSendText("NATIVE","xpath=//*[@placeholder='Country']",0,"Israel");
        client.waitForElement("NATIVE",MyProperties.instrumented ? "xpath=//*[@accessibilityLabel='Send Payment']" : "xpath=//*[@text='sendPaymentButton']",0,10000);
        client.click("NATIVE",MyProperties.instrumented ?"xpath=//*[@accessibilityLabel='Send Payment']" : "xpath=//*[@text='sendPaymentButton']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@text='Yes']", 0, 2000);
        if(client.isElementFound("NATIVE", "xpath=//*[@text='Yes']"))
            client.click("NATIVE", "xpath=//*[@text='Yes']", 0, 1);
        if(!isSimulator) {
            str0 = client.elementGetProperty("NATIVE", MyProperties.instrumented ? "xpath=//*[@text='Make Payment']" : "xpath=(//*/*[@class='UIAStaticText' and @knownSuperClass='NSObject' and not(contains(text(),'balance'))])", 0,"text");
            str = str0.split("\\$")[0];
            newD = Double.parseDouble(str);
            if(oldD - newD != payment)
                throw new Exception("The values before and after payment do not match");
        }
        client.applicationClose("com.experitest.ExperiBank");
    }
}

