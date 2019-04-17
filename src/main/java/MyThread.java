
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
    private DeviceController device;
    private long duration;
    private Map<Class, RunSummary> summary;
    CommandsPerformance commandsSum;
    PrintStream out = null;

    public MyThread(DeviceController device) {
        this.device = device;
        summary = new HashMap<>();
        commandsSum = new CommandsPerformance();
        if(!new File(device.getDirectory()).exists())
            new File(device.getDirectory()).mkdir();
    }
    public DeviceController getDevice(){return device;}

    public String getQuery() {
        return "@serialnumber = '" + device.getSN() + "'" /*+ (query.split("-").length > 2 ? " and  @emulator='true'":"")*/;
    }

    public void setDuration(long d) {
        duration = d;
    }

    @Override
    public void run() {
//        if(ConfigManager.checkIfSetTrue("saveClientLogToFile")) {
            try {
                out = new PrintStream(new FileOutputStream(device.getClientLog()));
                ((ThreadPrintStream)System.out).setThreadOut(out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//        }
        //initialize
        for (int i = 0; i < ConfigManager.tests.size(); i++)
            summary.put(ConfigManager.tests.get(i), new RunSummary(ConfigManager.tests.get(i).getName()));

        for (int i = 0; i < ConfigManager.rounds* ConfigManager.tests.size() && !isInterrupted(); i++)
            runTest(ConfigManager.tests.get(i % ConfigManager.tests.size()));

        ManagerOfGui.getInstance().getEndIsComingPanel().updateFinished();
        out.close();
    }

    private void runTest(Class test) {
        now = new Date();
        JUnitCore junit = new JUnitCore();
        now = new Date();
        Result result = junit.run(test);
        exc = test.getName() +" Test " + (result.wasSuccessful() ?  "Passed" + ", The test took " + duration :
                "failed:\n" + result.getFailures().stream().map(e -> e.getException().getMessage()+e.getTrace()).collect(Collectors.joining("\n")));
//        if(ConfigManager.checkIfSetTrue("saveClientLogToFile"))
            out.append(result.getFailures().stream().map(e ->e.getTrace()).collect(Collectors.joining("\n")));
        summary.put(test, summary.get(test).update(duration, sdFormat.format(now), result));
        writeToFile(device.getAllResultDirectory(), sdFormat.format(now) + "    " + exc, true);
        if (ConfigManager.rounds > 1)
            writeToFile(device.getSummaryDirectory(), summary.entrySet().stream().map(e -> e.getValue().toString()).collect(Collectors.joining("\n")) +
                    "\n" + commandsSum.toString(), false);
        if(WriteSummary.needToWriteSummary())
            WriteSummary.update(device.getSN(),result.wasSuccessful());
        device.getProgressBarPanel().updateBar(result.wasSuccessful());
    }

    public static void writeToFile(String directory, String data, boolean append) {
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
}
