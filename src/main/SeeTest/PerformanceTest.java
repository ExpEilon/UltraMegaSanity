import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**Times are based on STA 12.0.95. The upper bound was (on average):
 * wLaunch: 9391
 * wDump: 3236
 * wClick: 3291
 * nLaunch: 5898
 * nDump: 838
 * nClick: 944
 * install: 7867
 *
 * For this test, I took the data above * 1.1
 */
public class PerformanceTest extends BaseTest {
    private final int ROUNDS = 10;
    static Map<String,Integer> timesExpected;
    static {
        timesExpected = ImmutableMap.<String,Integer>builder()
                .put("wLaunch", 10330)
                .put("wDump", 3556)
                .put("wClick", 3620)
                .put("nLaunch", 6488)
                .put("nDump", 921)
                .put("nClick", 1038)
                .put("install", 8653)
                .build();
    }
    static Map<String,Integer> timesActual;
    static {
        timesActual = new HashMap<>();
        timesActual.put("wLaunch", 0);
        timesActual.put("wDump", 0);
        timesActual.put("wClick", 0);
        timesActual.put("nLaunch", 0);
        timesActual.put("nDump", 0);
        timesActual.put("nClick", 0);
        timesActual.put("install", 0);
    }

    @Test
    public void webPerformance(){
        IntStream.range(0,ROUNDS).forEach(i -> timesExpected.keySet().stream().forEach(k -> timesActual.put(k,timesActual.get(k) + (int)getCommandTime(k))));
        timesActual.keySet().stream().forEach(k -> timesActual.put(k,timesActual.get(k)/ROUNDS));
        WriteSummary.writeToFile(new File(WriteSummary.root+"//Performance.txt"),MyThread.currentThread().getName() + " : " + timesActual.toString());
//        timesExpected.keySet().stream().forEach(k ->{
//            long l = timesActual.get(k)/ROUNDS;
//            Assert.assertTrue("Command: " + k + " took " + l +" instead of less than " + timesExpected.get(k),l < timesExpected.get(k));
//
//        });
    }

    public long getCommandTime(String command){
        long start = System.currentTimeMillis();
        switch (command){
            case "wLaunch": client.launch("http://192.168.4.85:8060/html-tests/offsetTest/offsetTestHtml.html", false, true); break;
            case "wDump": client.getVisualDump("WEB"); break;
            case "wClick": client.click("WEB","xpath=//*[@text='16']",0,1); break;
            case "nLaunch": client.launch("com.apple.Preferences",false,true); break;
            case "nDump": client.getVisualDump("NATIVE"); break;
            case "nClick": client.click("NATIVE","xpath=//*[@text='Wi-Fi']",0,1); break;
            case "install": client.install(System.getProperty("user.dir")+"\\apps\\EriBank.ipa",false,false); break;
        }
        long end = System.currentTimeMillis();
        return end-start;
    }
}
