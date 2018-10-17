import org.junit.Assert;
import org.junit.Test;

public class SafariMenu extends BaseTest {
    @Test
    public void SafariMenuTest() {
        if(MyProperties.makeReporter)
            client.setReporter("xml", projectBaseDirectory +"//Reporter", "SafariMenu");
        client.launch("safari:http://imgur.com/upload", true, true);
        client.waitForElement("Web","xpath=//*[@text='Browse']", 0,30000);
        client.click("WEB", "xpath=//*[@text='Browse']", 0, 1);
        client.verifyElementFound("NATIVE","xpath=//*[@class='UIAView' and ./*[@text='Take Photo or Video']]",0);
        client.click("NATIVE","xpath=//*[@class='UIAView' and ./*[@text='Photo Library']]",0,1);
        Assert.assertTrue("Please take some photos for this test",!client.elementGetProperty("NATIVE","xpath=//*[@text='Camera Roll' and @class='UIAView']",0,"value").contains("0"));
        if(client.isElementFound("NATIVE","xpath=//*[@text='Camera Roll' and @class='UIAView']",0))
            client.click("NATIVE","xpath=//*[@text='Camera Roll' and @class='UIAView']",0,1);
        else client.click("NATIVE","xpath=//*[@text='Camera Roll' and @class='UIAButton']",0,1);
        client.verifyElementFound("NATIVE","xpath=(//*[@text='PhotosGridView']/*[contains(text(),'')])",0);
    }
}
