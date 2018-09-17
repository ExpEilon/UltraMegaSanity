import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JSAppiumTest extends AppiumBaseTest{

    @Test
    public void testApple() throws IOException, InterruptedException {
        dc.setJavascriptEnabled(true);
        driver = new MyiOSDriver(new URL(MyProperties.runOn.getURL() + "/wd/hub"), dc);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://www.google.com/");
        String jQuery;
        Assert.assertTrue("executeScript didn't work for boolean returned value", driver.executeScript("return window == null;") instanceof Boolean);
        Assert.assertTrue("executeScript result variable", driver.executeScript("result = window == null;") instanceof Boolean);
        Assert.assertTrue("executeScript didn't work for Double returned value", driver.executeScript("return 1.2;") instanceof Double);
        Assert.assertTrue("executeScript didn't work for WebElement returned value", driver.executeScript("return document.getElementById('hplogo');") instanceof WebElement);
        Assert.assertTrue("executeScript didn't work for String returned value", driver.executeScript("return 'some string'") instanceof String);
//        driver.get("https://www.wikipedia.org/");
//        Assert.assertTrue("executeScript didn't work for List returned value", driver.executeScript("return document.getElementsByTagName('style');") instanceof List);
//        Assert.assertTrue("executeScript didn't work for List, the list doesn't have WebElements", ((List)driver.executeScript("return document.getElementsByTagName('style');")).get(0) instanceof WebElement);
        driver.get("http://www.google.com/");
        try (FileReader fr = new FileReader("C:\\Users\\eilon.grodsky\\IdeaProjects\\UltraMegaSanity\\jquery.txt");
             BufferedReader br = new BufferedReader(fr)) {
            jQuery = br.lines().collect(Collectors.joining("\n"));
        }
        driver.executeScript(jQuery);
        Assert.assertTrue("JQuery didn't work for double returned value",driver.executeScript("return $( window ).height();") instanceof Long);
        Assert.assertTrue("JQuery didn't work for WebElement returned value", driver.executeScript("return $( \"#hplogo\" );") instanceof WebElement);
    }
}
