import com.experitest.client.GridClient;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**Times are based on STA 12.1.76. on device sn 45a8ec778b1e6eb400e828cb989be9934fc03a8b (#B0201):
 * wLaunch: 9941, 701
 * wDump: 3000, 340
 * wClick: 2916, 323
 * nLaunch: 5569, 119
 * nDump: 341, 74
 * nClick: 797, 173
 * install: 4982, 1492
 *
 * For this test, I took the data above * 1.1
 */
public class PerformanceTest extends SeeTestBase {
    private final int ROUNDS = 50;
    private final int TRIES = 5;
    static Map<String,Integer> meanExpected;
    static {
        meanExpected = ImmutableMap.<String,Integer>builder()
                .put("reserve",0)
                .put("wLaunch", 10330)
                .put("wDump", 3556)
                .put("wClick", 3620)
//                .put("nLaunch", 6488)
//                .put("nDump", 921)
//                .put("nClick", 1038)
//                .put("install", 8653)
                .put("release",0)
                .build();
    }
    Map<String,Integer> mean;
    Map<String,Integer> deviation;
    Map<String,ArrayList<Integer>> results;

    @Test
    public void webPerformance(){
        initialize();
        meanExpected.keySet().stream().forEach(k -> results.put(k,new ArrayList<>()));
        IntStream.range(1,ROUNDS).forEach(i -> {
            meanExpected.keySet().stream().forEach(k ->results.get(k).add((int)getCommandTime(k)));
            System.out.println(i + calaulateMeanAndDeviation(i));
        });

        WriteSummary.writeToFile(new File(WriteSummary.getRoot()+"//Performance.txt"),MyThread.currentThread().getName() + calaulateMeanAndDeviation(ROUNDS));
    }

    private void initialize(){
        mean = new HashMap<>();
        deviation= new HashMap<>();
        results = new HashMap<>();
    }

    public long getCommandTime(String command){
        for (int i = 0; i < TRIES; i++) {
            try {
                long start = System.currentTimeMillis();

                if(command.equals("reserve"))
                    lockDevice();
                else if(command.equals("wLaunch"))
                    client.launch(PathsMap.Web.BigPage, false, true);
                else if(command.equals("wDump"))
                    client.getVisualDump("WEB");
                else if(command.equals("wClick"))
                    client.click("WEB", "xpath=//*[@text='16']", 0, 1);
                else if(command.equals("nLaunch"))
                    client.launch("com.apple.Preferences", false, true);
                else if(command.equals("nDump"))
                    client.getVisualDump("NATIVE");
                else if(command.equals("nClick"))
                    client.click("NATIVE", "xpath=//*[@text='Wi-Fi']", 0, 1);
                else if(command.equals("install"))
                    client.install(PathsMap.EriBank, false, false);
                else if(command.equals("release"))
                    client.releaseClient();

                return System.currentTimeMillis() - start;
            }catch (Exception e){
                if(i%2==1) {
                    if (command.equals("wClick"))
                        client.launch(PathsMap.Web.BigPage, false, true);
                    else if (command.equals("nClick"))
                        client.launch("com.apple.Preferences", false, true);
                }
            }
        }
        Assert.fail("Couldn't perform " + command + " command for " + TRIES + "times in a row.\n");
        return 0;
    }
    private String calaulateMeanAndDeviation(int rounds){
        //Calculates mean
        results.keySet().stream().forEach(k -> mean.put(k,results.get(k).stream().mapToInt(a->a).sum()/rounds));
        //Calculates deviation
        results.keySet().stream().forEach(k -> deviation.put(k, (int) Math.sqrt(
                results.get(k).stream().mapToInt(a->
                        (int) Math.pow(a-mean.get(k),2)).sum()/rounds)));

        return "\nMean: " + mean.toString()  +"\nDeviation: " + deviation.toString();
    }
    private void lockDevice(){
        if(isGrid) {
            gridClient = new GridClient(runOn.getAccesskey(), runOn.getURL());
            client = gridClient.lockDeviceForExecution("TestFile", query, 30, 300000);
        }
        else {
            client = new MyClient(runOn.getIp(), runOn.port);
            client.waitForDevice(query,300000);
        }
    }
}
