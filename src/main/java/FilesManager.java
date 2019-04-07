import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.IntStream;

public class FilesManager {
    private static final String root = System.getProperty("user.dir") + "//TestResultAt_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
    private static final File rootDirectory = new File(root);
    private static FilesManager ourInstance = new FilesManager();

    public static FilesManager getInstance() {
        return ourInstance;
    }

    private FilesManager() {
        rootDirectory.mkdir();
    }

    public static void initialize(List<String> devices){
        IntStream.range(0,devices.size()).forEach(i -> MyThread.writeToFile(System.getProperty("user.dir") +"//TestResult//Summary.txt",
                devices.get(i)+",0,0,0",true));
    }

    public static void createDirectory(String path){
        File file = new File(root + path);
        if (!file.exists())
            file.mkdir();
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

}
