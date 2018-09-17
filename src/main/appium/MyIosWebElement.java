import org.openqa.selenium.*;

import java.util.List;

public class MyIosWebElement implements WebElement {
    private final WebElement delegate;

    public MyIosWebElement(WebElement delegate) {
        this.delegate = delegate;
    }

    @Override
    public void click() {
        long startTime = System.currentTimeMillis();
        delegate.click();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("click",endTime-startTime);
    }

    @Override
    public void submit() {
        long startTime = System.currentTimeMillis();
        delegate.submit();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("submit",endTime-startTime);
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        long startTime = System.currentTimeMillis();
        delegate.sendKeys(charSequences);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("sendKeys",endTime-startTime);
    }

    @Override
    public void clear() {
        long startTime = System.currentTimeMillis();
        delegate.clear();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("clear",endTime-startTime);
    }

    @Override
    public String getTagName() {
        long startTime = System.currentTimeMillis();
        String ret = delegate.getTagName();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getTagName",endTime-startTime);
        return ret;
    }

    @Override
    public String getAttribute(String s) {
        long startTime = System.currentTimeMillis();
        String ret = delegate.getAttribute(s);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getAttribute",endTime-startTime);
        return ret;
    }

    @Override
    public boolean isSelected() {
        long startTime = System.currentTimeMillis();
        boolean bool = delegate.isSelected();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("isSelected",endTime-startTime);
        return bool;
    }

    @Override
    public boolean isEnabled() {
        long startTime = System.currentTimeMillis();
        boolean bool = delegate.isEnabled();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("isEnabled",endTime-startTime);
        return bool;
    }

    @Override
    public String getText() {
        long startTime = System.currentTimeMillis();
        String ret = delegate.getText();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getText",endTime-startTime);
        return ret;
    }

    @Override
    public List<WebElement> findElements(By by) {
        long startTime = System.currentTimeMillis();
        List list = delegate.findElements(by);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("findElements",endTime-startTime);
        return list;
    }

    @Override
    public WebElement findElement(By by) {
        long startTime = System.currentTimeMillis();
        WebElement webElement = delegate.findElement(by);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("findElement",endTime-startTime);
        return webElement;
    }

    @Override
    public boolean isDisplayed() {
        long startTime = System.currentTimeMillis();
        boolean bool = delegate.isDisplayed();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("isDisplayed",endTime-startTime);
        return bool;
    }

    @Override
    public Point getLocation() {
        long startTime = System.currentTimeMillis();
        Point point = delegate.getLocation();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getLocation",endTime-startTime);
        return point;
    }

    @Override
    public Dimension getSize() {
        long startTime = System.currentTimeMillis();
        Dimension dimension = delegate.getSize();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getSize",endTime-startTime);
        return dimension;
    }

    @Override
    public Rectangle getRect() {
        long startTime = System.currentTimeMillis();
        Rectangle ret = delegate.getRect();
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getCssValue",endTime-startTime);
        return ret;
    }

    @Override
    public String getCssValue(String s) {
        long startTime = System.currentTimeMillis();
        String ret = delegate.getCssValue(s);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getCssValue",endTime-startTime);
        return ret;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        long startTime = System.currentTimeMillis();
        X ret = delegate.getScreenshotAs(outputType);
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData("getCssValue",endTime-startTime);
        return ret;

    }
}
