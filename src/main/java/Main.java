import com.experitest.client.Client;
import com.experitest.client.GridClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static int interval = MyProperties.supportDataInterval*60*1000;

    public static void main(String[] args) throws FileNotFoundException {
        MyProperties.lastReboot = System.currentTimeMillis();
        if(MyProperties.saveClientLogToFile) {
            PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
        }
        new File(new File("").getAbsolutePath() + "//TestResult").mkdir();
        List<String> devices = getSerialNumbers();
        if(MyProperties.runOn.isGrid) {
            System.getProperties().setProperty("javax.net.ssl.trustStore","C:\\Users\\eilon.grodsky\\Desktop\\new keystore\\truststore.jks");
            System.getProperties().setProperty("javax.net.ssl.trustStorePassword","123456");
        }
        devices.forEach((sn) -> {
            MyThread thread = new MyThread(sn);
            thread.setName(sn);
            thread.start();
        });
        if(MyProperties.collectSupportData) {
            new Thread(() -> {
                for (int i = 0; i < Integer.MAX_VALUE; i++){
                    try {
//                        collectDataAppium();
//                        copyQACloud();
                        collectData();
                        Thread.sleep(interval);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private static List<String> getSerialNumbers() {
        if(MyProperties.deviecsSN.equals(""))
            return getDevicesSN();
        else
            return Arrays.asList(MyProperties.deviecsSN.split(","));
    }

    public static List<String> getDevicesSN() {
        String str = MyProperties.runOn.isGrid ? new GridClient(MyProperties.runOn.AK,MyProperties.runOn.getURL()).getDevicesInformation() :
                new MyClient(MyProperties.runOn.ip,MyProperties.runOn.port).getDevicesInformation();
        if(MyProperties.runOn.isGrid)
            return Arrays.asList(str.split("\n")).stream().filter(s -> s.contains("os=\"ios\"") && s.contains("status=\"unreserved online\"")) //need to add in case reserved for me
                    .map(s -> s.split(" serialnumber=\"")[1].split("\"")[0]).collect(Collectors.toList());
        else{
            return Arrays.asList(str.split("\n")).stream().filter(s -> s.contains("os=\"ios\"") && (s.contains("remote=\"false\"") ||
                    (s.contains("reservedtoyou=\"true\"")) && !s.contains("status=\"reserved offline\"")))
                    .map(s -> s.split(" serialnumber=\"")[1].split("\"")[0]).collect(Collectors.toList());
        }
    }
    public static void collectData() throws Exception{
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        Unirest.setTimeouts(interval, interval);
        HttpResponse<InputStream> jsonResponse = Unirest.get(MyProperties.runOn.getURL() + "/api/v2/configuration/collect-support-data/false/false/")
                .header("accept", "application/json")
                .basicAuth(MyProperties.runOn.username,MyProperties.runOn.password)
                .asBinary();
        InputStream initialStream = jsonResponse.getBody();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);
        File targetFile = new File(new File("").getAbsolutePath() + "//TestResult//supportData" + sdFormat.format(now) + ".zip");
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
    }
    public static void copyQACloud(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        File source = new File("z:\\Program Files (x86)\\Experitest\\Cloud\\Agent\\logs\\agent.log");
        File dest = new File(new File("").getAbsolutePath() + "//TestResult//agentLog" + sdFormat.format(now) + ".txt");
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        now = new Date();
        source = new File(        "z:\\Program Files (x86)\\Experitest\\Cloud\\Agent\\logs\\sta-output-00d064b580b7e36184819a9ce668f8c9f1d2413f.log");
        dest = new File(new File("").getAbsolutePath() + "//TestResult//STALog" + sdFormat.format(now) + ".txt");
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    public static void collectDataAppium() throws MalformedURLException {
//        DesiredCapabilities dc = new DesiredCapabilities();
//        dc.setCapability("deviceQuery", "@serialnumber='672AAB24-ADC5-4E11-A67A-64000CE74E4E' and  @emulator='true'");
//        AppiumDriver driver = new IOSDriver<>(new URL(MyProperties.runOn.getURL() + "/wd/hub"), new DesiredCapabilities());
//        driver.executeScript("seetest:client.collectSupportData(\"/Users/eilon.grodsky/appiumstudioenterprise\",\"\",\"\",\"\",\"\",\"\");");
//        driver.quit();
//    }
}
