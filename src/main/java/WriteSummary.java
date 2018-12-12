import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WriteSummary {
    public static String root = System.getProperty("user.dir") +"//TestResult";
    private static String directory = root + "//Summary.txt";
    private static File summary = new File(directory);

    public static synchronized void update(String sn, boolean pass){
            try {
                BufferedReader br = new BufferedReader(new FileReader(summary));
                String lineToUpdate = br.lines().filter(s -> s.split(",")[0].equals(sn)).collect(Collectors.joining());
                br.close();
                DeviceSummary deviceSummary = new DeviceSummary(lineToUpdate);
                deviceSummary.inc(pass);
                br = new BufferedReader(new FileReader(summary));
                List updatedList= br.lines().filter(s -> !s.split(",")[0].equals(sn)).collect(Collectors.toList());
                br.close();
                updatedList.add(deviceSummary.newLine());
                String data = (String) updatedList.stream().collect(Collectors.joining("\n"));
                MyThread.writeToSummary(System.getProperty("user.dir") + "//TestResult//Summary.txt", data, false);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
    }

    public static synchronized void writeToFile(File file,String data){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.println(data);
        writer.close();
    }
    public static boolean needToWriteSummary(){
        return summary.exists();
    }

    public static void createSummary(List<JCheckBox> list){
        if(summary.exists())
            summary.delete();
        if(list.size() > 1) {
            IntStream.range(0, list.size()).forEach(i -> MyThread.writeToSummary(summary.getPath(),
                    list.get(i).getText() + ",0,0,0", true));
        }

    }
    public static File getRootDirectory(){
        return new File(root);
    }

    private static class DeviceSummary{
        private int pass,fail,total;
        private String sn;

        private DeviceSummary(String line){
            System.out.println("The line here is: " + line);
            sn = line.split(",")[0];
            pass=Integer.parseInt(line.split(",")[1]);
            fail=Integer.parseInt(line.split(",")[2]);
            total=Integer.parseInt(line.split(",")[3]);
        }
        private void inc(boolean pass){
            total++;
            if(pass)
                this.pass++;
            else
                this.fail++;
        }

        private String newLine(){
            return sn+","+pass+","+fail+","+total;
        }
    }
}
