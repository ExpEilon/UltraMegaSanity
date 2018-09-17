
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    public MyThread(String query) {
        this.query = query;
        summary = new HashMap<>();
        commandsSum = new CommandsPerformance();
    }

    public String getQuery() {
        return query;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDuration(long d) {
        duration = d;
    }

    @Override
    public void run() {
        new File(new File("").getAbsolutePath() + "//TestResult" + "//" + query).mkdir();
        directory = new File(new File("").getAbsolutePath()) + "//TestResult" + "//" + query;
        //initialize
        for (int i = 0; i < MyProperties.tests.length; i++)
            summary.put(MyProperties.tests[i], new RunSummary(MyProperties.tests[i].getName()));
        for (int i = 0; MyProperties.allNighter ? i < Integer.MAX_VALUE : i < MyProperties.tests.length; i++)
            runTest(MyProperties.tests[i % MyProperties.tests.length]);
    }

    private void runTest(Class test) {
        now = new Date();
        JUnitCore junit = new JUnitCore();
        now = new Date();
        Result result = junit.run(test);
        exc = result.wasSuccessful() ? test.getName() + " Test Passed" + ", The test took " + duration : test.getName() + " Test failed:\n" +
                result.getFailures().stream().map(e -> e.getException().getMessage()+e.getTrace()).collect(Collectors.joining("\n"));
        summary.put(test, summary.get(test).update(duration, sdFormat.format(now), result));
        writeToSummary(new File(directory + "//AllResults.txt"), sdFormat.format(now) + "    " + exc, true);
        if (MyProperties.allNighter)
            writeToSummary(new File(directory + "//Summary.txt"), summary.entrySet().stream().map(e -> e.getValue().toString()).collect(Collectors.joining("\n")) +
                    "\n" + commandsSum.toString(), false);
    }

    private static void writeToSummary(File file, String s, boolean append) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file, append));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(s);
        writer.close();
    }
}
