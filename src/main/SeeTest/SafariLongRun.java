import org.junit.*;


public class SafariLongRun extends SeeTestBase {

    @Test
    public void webLongRun() {
        client.hybridClearCache(true, true);
        client.launch("safari:www.google.com", true, true);
        Assert.assertTrue("Wasn't found, web dump:\n" + client.getVisualDump("WEB"),client.isElementFound("Web","xpath=//*[@id='hplogo']", 0));
        client.launch("safari:m.ebay.com", true, true);
        if(!client.isElementFound("Web", "xpath=//*[@id='gh-mlogo']", 0))
            client.verifyElementFound("Web", "xpath=//*[@id='gh-la']", 0);
    }
}
