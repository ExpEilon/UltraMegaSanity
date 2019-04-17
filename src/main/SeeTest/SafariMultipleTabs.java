import org.junit.Assert;
import org.junit.Test;

public class SafariMultipleTabs extends SeeTestBase{

    @Test
    public void MultipleTabsWithSameURL(){
        try{
            final int numOfTabs = 2;
            String safariPrefix = "safari:";
            String urlRegualarTestingPage = "http://192.168.4.85:8060/html-tests/"
                    + "RegressionHibridApplication/HibridApplicationHtml.html";

            String urlScrolingTestingPage = "http://192.168.4.85:8060/"
                    + "html-tests/ScrollingRegression/Scrolling.html";

            closeSafariTabs();

            client.launch(urlScrolingTestingPage,false,false);

            runScenario(safariPrefix + urlScrolingTestingPage);

            for (int i = 0; i < numOfTabs; i++) {

                double startTime = System.currentTimeMillis();

                launchAnotherTab(urlRegualarTestingPage);
                runScenario(safariPrefix + urlRegualarTestingPage  );
                client.elementSendText("web", "id=firstName", 0, String.valueOf(i));
                String num = client.elementGetProperty("web", "id=firstName", 0, "text");
                Assert.assertEquals(i, Integer.parseInt(num));
//                log.info("--- Total: #" + (i+1) + " Tab Run Time: " +	(System.currentTimeMillis()-startTime)/1000 + " Seconds ---\n");
            }
            closeSafariTabs();
        }catch (Exception e){
            closeSafariTabs();
            throw e;
        }finally {
            if(client.isElementFound("NATIVE", "xpath=//*[@value='Done']", 0)){
                client.click("NATIVE", "xpath=//*[@value='Done']", 0, 1);
            }
        }

    }

    private void closeSafariTabs() {
        if(!client.getCurrentApplicationName().equalsIgnoreCase("com.apple.mobilesafari")) {
            client.launch("com.apple.mobilesafari", false, false);
        }
        client.click("NATIVE", "xpath=//*[@text='Pages' or @text='Tabs']", 0, 1);
        String CloseTabXpath = "xpath=//*[@id='closeTabButton']";

        double startTime = System.currentTimeMillis();
        while (client.isElementFound("NATIVE", CloseTabXpath) &&   System.currentTimeMillis() - startTime  < 30000) {
            client.click("NATIVE", CloseTabXpath, 0, 1);
        }

        if(client.waitForElement("NATIVE", "xpath=//*[@text='Done']", 0, 5000)){
            client.click("NATIVE", "xpath=//*[@text='Done']", 0, 1);
        }

    }
    private void runScenario(String url){
        switch(url){
            case "safari:http://192.168.4.85:8060/html-tests/RegressionHibridApplication/HibridApplicationHtml.html":

                client.waitForElement("Web","xpath=//*[@text='Checking page' and @onScreen='true']",0,5000);
                client.clickIn("WEB", "xpath=//*[@id='carlist']", 0, "Up", "WEB",
                        "xpath=//*[@id='Click Me visibile Up' or @text='Click Me!']", 1000,0 );
                client.sleep(1500);
                Assert.assertTrue(client.waitForElement("Web","xpath=//*[@text='Hellow' and @onScreen='true' and @top='true']",0,5000));

                break;
            case "safari:http://192.168.4.85:8060/html-tests/ScrollingRegression/Scrolling.html":

                client.click("WEB", "id=Click Me 1 Up",0, 1);
                client.sleep(1000);
                String str0 = client.elementGetText("WEB", "id=Checking text", 0);
                Assert.assertTrue(str0.contains("Presssed on scroll 1 up"));

                break;
            case "safari:http://www.google.co.il":

                client.waitForElement("WEB", "xpath=//*[@id='hplogo']", 0, 10000);
                client.elementSendText("WEB", "xpath=//*[@id='lst-ib' or @type='search']", 0, "nadav");

                Assert.assertEquals("nadav", client.elementGetText("WEB", "xpath=//*[@id='lst-ib' or @type='search']", 0));
                client.sendText("{ESC}");
                break;
        }
    }
    private void launchAnotherTab(String URL) {
        if(client.waitForElement("NATIVE", "xpath=//*[@text='Pages' or @text='Tabs']", 0, 10000)){
            client.click("NATIVE", "xpath=//*[@text='Pages' or @text='Tabs']", 0, 1);
        }
        if(client.waitForElement("NATIVE", "xpath=//*[@text='New tab' or @text='New page']", 0, 10000)) {
            client.click("NATIVE", "xpath=//*[@text='New tab' or @text='New page']", 0, 1);
        }
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='URL']", 0,1);
        client.syncElements(1000,5000);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='URL']", 0, URL);
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Go']", 0,1);
    }

}
