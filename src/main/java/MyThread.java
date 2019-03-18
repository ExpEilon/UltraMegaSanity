
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MyThread extends Thread {
    String exc = "";
    Date now;
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String query;
    private DeviceController device;
    private String directory;
    private long duration;
    private Map<Class, RunSummary> summary;
    CommandsPerformance commandsSum;
    List<Class> tests;
    ProgressBarPanel progress;
    RunPanel endPanel;
    boolean stopped = false;
    PrintStream out = null;
    public MyThread(DeviceController device, List<Class> tests, ProgressBarPanel pBar, RunPanel panel) {
        this.device = device;
        this.query = device.getSN();
        summary = new HashMap<>();
        commandsSum = new CommandsPerformance();
        this.tests = tests;
        progress = pBar;
        endPanel = panel;
        directory = System.getProperty("user.dir") + "//TestResult" + "//" + query;
        if(!new File(directory).exists())
            new File(directory).mkdir();

    }
    public DeviceController getDevice(){return device;}
    public String getQuery() {
        return "@serialnumber = '" + query + "'" /*+ (query.split("-").length > 2 ? " and  @emulator='true'":"")*/;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDuration(long d) {
        duration = d;
    }

    @Override
    public void run() {
        if(ConfigManager.checkIfSetTrue("saveClientLogToFile")) {
            try {
                out = new PrintStream(new FileOutputStream(directory + "//Client.log"));
                System.setOut(out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //initialize
        for (int i = 0; i < tests.size(); i++)
            summary.put(tests.get(i), new RunSummary(tests.get(i).getName()));
        for (int i = 0; i < RunPanel.rounds* tests.size() && !stopped; i++)
            runTest(tests.get(i % tests.size()));
        endPanel.updateFinished();
    }

    private void runTest(Class test) {
        now = new Date();
        JUnitCore junit = new JUnitCore();
        now = new Date();
        Result result = junit.run(test);
        exc = test.getName() +" Test " + (result.wasSuccessful() ?  "Passed" + ", The test took " + duration :
                "failed:\n" + result.getFailures().stream().map(e -> e.getException().getMessage()+e.getTrace()).collect(Collectors.joining("\n")));
        if(ConfigManager.checkIfSetTrue("saveClientLogToFile"))
            out.append(result.getFailures().stream().map(e ->e.getTrace()).collect(Collectors.joining("\n")));
        summary.put(test, summary.get(test).update(duration, sdFormat.format(now), result));
        writeToSummary(directory + "//AllResults.txt", sdFormat.format(now) + "    " + exc, true);
        if (RunPanel.rounds > 1)
            writeToSummary(directory + "//Summary.txt", summary.entrySet().stream().map(e -> e.getValue().toString()).collect(Collectors.joining("\n")) +
                    "\n" + commandsSum.toString(), false);
        if(WriteSummary.needToWriteSummary())
            WriteSummary.update(query,result.wasSuccessful());
        progress.updateBar(result.wasSuccessful());
    }

    public static void writeToSummary(String directory, String data, boolean append) {
        PrintWriter writer = null;
        File writeTo = new File(directory);
        try {
            writer = new PrintWriter(new FileWriter(writeTo, append));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(data);
        writer.close();
    }

    public void terminate(){
        stopped = true;
    }
}
