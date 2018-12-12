
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import sun.misc.Lock;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MyThread extends Thread {
    String exc = "";
    Date now;
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String query;
    private String directory;
    private long duration;
    private Map<Class, RunSummary> summary;
    CommandsPerformance commandsSum;
    List<Class> tests;
    ProgressBarPanel progress;
    MyPanel endPanel;
    public MyThread(String query, List<Class> tests,ProgressBarPanel pBar,MyPanel panel) {
        this.query = query;
        summary = new HashMap<>();
        commandsSum = new CommandsPerformance();
        this.tests = tests;
        progress = pBar;
        endPanel = panel;
    }

    public String getQuery() {
        return "@serialnumber = '" + query + "'" + (query.split("-").length > 2 ? " and  @emulator='true'":"");
    }

    public String getDirectory() {
        return directory;
    }

    public void setDuration(long d) {
        duration = d;
    }

    @Override
    public void run() {
        directory = System.getProperty("user.dir") + "//TestResult" + "//" + query;
        new File(directory).mkdir();
        //initialize
        for (int i = 0; i < tests.size(); i++)
            summary.put(tests.get(i), new RunSummary(tests.get(i).getName()));
        for (int i = 0; i < MyPanel.rounds* tests.size(); i++)
            runTest(tests.get(i % tests.size()));
        endPanel.updateFinished();
    }

    private void runTest(Class test) {
        now = new Date();
        JUnitCore junit = new JUnitCore();
        now = new Date();
        Result result = junit.run(test);
        exc = result.wasSuccessful() ? test.getName() + " Test Passed" + ", The test took " + duration : test.getName() + " Test failed:\n" +
                result.getFailures().stream().map(e -> e.getException().getMessage()+e.getTrace()).collect(Collectors.joining("\n"));
        summary.put(test, summary.get(test).update(duration, sdFormat.format(now), result));
        writeToSummary(directory + "//AllResults.txt", sdFormat.format(now) + "    " + exc, true);
        if (MyPanel.rounds > 1)
            writeToSummary(directory + "//Summary.txt", summary.entrySet().stream().map(e -> e.getValue().toString()).collect(Collectors.joining("\n")) +
                    "\n" + commandsSum.toString(), false);
        if(WriteSummary.needToWriteSummary())
            WriteSummary.update(query,result.wasSuccessful());
        progress.updateBar(result.wasSuccessful());
//        synchronized (progress) {
//            progress.notify();
//        }
    }

//    public void setProgress(ProgressBarPanel progress){
//        this.progress = new Thread(() -> {
//            for (int i = 0; i <= tests.size(); i++) {
//                final int percent = i;
//                progress.updateBar(percent);
//                try {
//                    synchronized (Thread.currentThread()) {
//                        Thread.currentThread().wait();
//                    }
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//        this.progress.start();
//    }

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
}
