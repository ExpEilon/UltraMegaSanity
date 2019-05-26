import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class IFramesTest extends SeeTestBase {

    String playButton = "//*[@nodeName='DIV' and ./parent::*[@nodeName='BUTTON' and (./preceding-sibling::* | ./following-sibling::*)[@nodeName='DIV']]]";
    String shareButton = "xpath=//*[@nodeName='BUTTON' and ./*[./*[@nodeName='DIV']] and ./*[@text='Share']]";
    String urlAfterClickedShare = "xpath=//*[@text='https://youtu.be/bmRWyFAe2Cw']";
    String buttonSameDomain = "xpath=//*[@id='btn1']";
    String urlAfterClickSameDoamin = "xpath=//*[@id='showSrc1']";

    @Ignore
    @Test
    public void differentDomainInHousePage(){
        client.launch(PathsMap.Web.IFramePage,false,true);
        client.verifyElementFound("WEB",playButton,0);
        client.click("WEB",shareButton,0,1);
        client.verifyElementFound("WEB",urlAfterClickedShare,0);
        client.click("WEB",buttonSameDomain,0,1);
        client.verifyElementFound("WEB",urlAfterClickSameDoamin,0);
    }

    String userNameField = "xpath=//*[@id='loginForm:usridTxt']";
    String passwordField = "xpath=//*[@id='loginForm:pwdTxt']";
    String credentials = "Test";
    String signInButton = "xpath=//*[@id='Signin']";
    String errorSign = "xpath=//*[@text=' Attention:  Sign-in error.']";
    int timeout = 10000; // 10 sec
    String zipTextField = "xpath=//*[@id='advZIP']";
    String zipCode = "123456";

    @Test
    public void differentDomainPublicPage(){
        client.launch(PathsMap.Web.DifferentFrames,false,true);
        client.elementSendText("WEB",userNameField,0,credentials);
        Assert.assertTrue("Username wasn't send",client.elementGetProperty("WEB",userNameField,0,"value").equals(credentials));
        client.elementSendText("WEB",zipTextField,0,zipCode);
        Assert.assertTrue("Zip code wasn't send",client.elementGetProperty("WEB",zipTextField,0,"value").equals(zipCode));
        client.elementSendText("WEB",passwordField,0,credentials);
        client.click("WEB",signInButton,0,1);
        Assert.assertTrue("Didn't find error message, either a regresion in our product or 3rd party or connection problem",client.waitForElement("WEB",errorSign,0,timeout));
    }
}
