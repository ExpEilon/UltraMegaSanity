import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MyiOSDriver extends IOSDriver{

    public MyiOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }
    @Override
    public void installApp(String appPath){
        long startTime = System.currentTimeMillis();
        super.installApp(appPath);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("installApp",endTime-startTime);
    }
    @Override
    public Object executeScript(String script, Object... args){
        long startTime = System.currentTimeMillis();
        Object e = super.executeScript(script,args);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("executeScript",endTime-startTime);

        return e;

    }
    @Override
    public void hideKeyboard() {
        long startTime = System.currentTimeMillis();
        super.hideKeyboard();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("hideKeyboard",endTime-startTime);
    }

    @Override
    public WebElement findElement(By by) {
        long startTime = System.currentTimeMillis();
        WebElement e = super.findElement(by);
        long endTime = System.currentTimeMillis();
        MyIosWebElement iosWebElement = new MyIosWebElement(e);
        ((MyThread)Thread.currentThread()).commandsSum.addData("findElement",endTime-startTime);

        return iosWebElement;
    }

    @Override
    public List<WebElement> findElements(By by) {
        List<MyIosWebElement> els = super.findElements(by);
        return els.stream().map(MyIosWebElement::new).collect(Collectors.toList());
    }

    public boolean isElementFound(By by) {
        try {
            this.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}