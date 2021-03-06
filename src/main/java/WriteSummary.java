import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WriteSummary {
    private static String root;
    private static File summary;

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
                MyThread.writeToFile(root+"//Summary.txt", data, false);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
    }

    public static String getRoot(){
        if(root == null || root.equals(""))
            restartRoot();
        return root;
    }

    static public String getCurrTime(){
        return new SimpleDateFormat("yyyy_MM_dd HH_mm_ss").format(System.currentTimeMillis());
    }
    public static void restartRoot(){
        root = System.getProperty("user.dir") + "//TestResultAt_" + getCurrTime();
    }

    public static void createRoot(){new File(root).mkdir();}

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

    public synchronized static void createSummary(List<String> list){
        summary = new File(root + "//Summary.txt");
        final List lineToUpdate = new ArrayList();
        try {
            if(summary.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(summary));
                br.lines().map(s -> s.split(",")[0]).forEach(e -> lineToUpdate.add(e));
                br.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        List filteredList = list.stream().filter(j -> !lineToUpdate.contains(j)).collect(Collectors.toList());
        if(filteredList.size() > 0) {
            list.stream().filter(j -> !lineToUpdate.contains(j)).forEach(i -> MyThread.writeToFile(summary.getPath(),
                    i + ",0,0,0", true));
        }
    }

    public static File getRootDirectory(){
        return new File(root);
    }

    private static class DeviceSummary{
        private int pass,fail,total;
        private String sn;

        private DeviceSummary(String line){
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
