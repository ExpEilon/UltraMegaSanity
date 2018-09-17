import com.experitest.client.Client;
import com.experitest.client.InternalException;


public class MyClient extends Client {
    public MyClient(String host, int port) {
        super(host, port);
    }

    @Override
    public boolean install(String path, boolean instrument, boolean keepData) {
        long startTime = System.currentTimeMillis();
        boolean bool =  super.install(path, instrument, keepData);
        writeToSum(startTime,"install");
        return bool;
    }

    @Override
    public String waitForDevice(String query, int timeout) {
        long startTime = System.currentTimeMillis();
        String s = super.waitForDevice(query, timeout);
        writeToSum(startTime,"waitForDevice");
        return s;
    }

    @Override
    public void click(String zone, String element, int index, int clickCount) {
        long startTime = System.currentTimeMillis();
        super.click(zone, element, index, clickCount);
        writeToSum(startTime,"click");

    }

    @Override
    public void closeAllApplications() {
        long startTime = System.currentTimeMillis();
        super.closeAllApplications();
        writeToSum(startTime,"closeAllApplications");
    }

    @Override
    public void closeKeyboard() {
        long startTime = System.currentTimeMillis();
        super.closeKeyboard();
        writeToSum(startTime,"closeKeyboard");
    }

    @Override
    public String elementGetProperty(String zone, String element, int index, String property) {
        long startTime = System.currentTimeMillis();
        String s = super.elementGetProperty(zone, element, index, property);
        writeToSum(startTime,"elementGetProperty");
        return s;
    }

    @Override
    public void elementSendText(String zone, String element, int index, String text) {
        long startTime = System.currentTimeMillis();
        super.elementSendText(zone, element, index, text);
        writeToSum(startTime,"elementSendText");
    }

    @Override
    public String elementSetProperty(String zone, String element, int index, String property, String value) {
        long startTime = System.currentTimeMillis();
        String s = super.elementSetProperty(zone, element, index, property, value);
        writeToSum(startTime,"elementSetProperty");
        return s;
    }

    @Override
    public String generateReport() {
        long startTime = System.currentTimeMillis();
        String s = super.generateReport();
        writeToSum(startTime,"generateReport");
        return s;
    }

    @Override
    public String runNativeAPICall(String zone, String element, int index, String script) {
        long startTime = System.currentTimeMillis();
        String s = super.runNativeAPICall(zone, element, index, script);
        writeToSum(startTime,"runNativeAPICall");
        return s;
    }

    @Override
    public String getCounter(String counterName) {
        long startTime = System.currentTimeMillis();
        String s = super.getCounter(counterName);
        writeToSum(startTime,"getCounter");
        return s;
    }

    @Override
    public String getCurrentApplicationName() {
        long startTime = System.currentTimeMillis();
        String s = super.getCurrentApplicationName();
        writeToSum(startTime,"getCurrentApplicationName");
        return s;
    }

    @Override
    public String getDevicesInformation() {
        long startTime = System.currentTimeMillis();
        String s = super.getDevicesInformation();
        writeToSum(startTime,"getDevicesInformation");
        return s;
    }

    @Override
    public String getInstalledApplications() {
        long startTime = System.currentTimeMillis();
        String s = super.getInstalledApplications();
        writeToSum(startTime,"getInstalledApplications");
        return s;
    }

    @Override
    public String getMonitorsData() {
        long startTime = System.currentTimeMillis();
        String s = super.getMonitorsData();
        writeToSum(startTime,"getMonitorsData");
        return s;
    }

    @Override
    public String getMonitorsData(String cSVfilepath) {
        long startTime = System.currentTimeMillis();
        String s = super.getMonitorsData(cSVfilepath);
        writeToSum(startTime,"getMonitorsData(String cSVfilepath)");
        return s;
    }

    @Override
    public String getTextIn(String zone, String element, int index, String textZone, String direction, int width, int height) {
        long startTime = System.currentTimeMillis();
        String s = super.getTextIn(zone, element, index, textZone, direction, width, height);
        writeToSum(startTime,"getTextIn");
        return s;
    }

    @Override
    public String getVisualDump(String type) {
        long startTime = System.currentTimeMillis();
        String s = super.getVisualDump(type);
        writeToSum(startTime,"getVisualDump");
        return s;
    }

    @Override
    public void hybridClearCache(boolean clearCookies, boolean clearCache) throws InternalException {
        long startTime = System.currentTimeMillis();
        super.hybridClearCache(clearCookies, clearCache);
        writeToSum(startTime,"hybridClearCache");
    }

    @Override
    public String hybridRunJavascript(String webViewLocator, int index, String script) {
        long startTime = System.currentTimeMillis();
        String s = super.hybridRunJavascript(webViewLocator, index, script);
        writeToSum(startTime,"hybridRunJavascript");
        return s;
    }

    @Override
    public void launch(String activityURL, boolean instrument, boolean stopIfRunning) {
        long startTime = System.currentTimeMillis();
        super.launch(activityURL, instrument, stopIfRunning);
        writeToSum(startTime,"launch");
    }

    @Override
    public void releaseClient() {
        long startTime = System.currentTimeMillis();
        super.releaseClient();
        writeToSum(startTime,"releaseClient");
    }
    private void writeToSum(long start,String op){
        long endTime = System.currentTimeMillis();
        ((MyThread)Thread.currentThread()).commandsSum.addData(op,endTime-start);

    }
}
