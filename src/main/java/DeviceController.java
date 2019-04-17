
import java.util.HashMap;

public class DeviceController {
    private HashMap<String,String> properties;
    private String line;
    private ConfigManager.Connection runOn;
    private boolean isLocal;
    private ProgressBarPanel progressBarPanel;
    private String directory;
    private String deviceLogDirectory;

    public DeviceController(String line,ConfigManager.Connection runOn){

        this.line = line;
        isLocal = !runOn.isGrid;
        properties = new HashMap<>();
        properties.put("SN",extractProperty("serialnumber"));
        properties.put("OS",extractProperty("os"));
        properties.put("Version",extractProperty("version"));
        properties.put("Model",extractProperty("model"));
        properties.put("IsSimulator", isLocal ? "Temp" : extractProperty("emulator"));//
        properties.put("DHM",isLocal ? "Local" : extractProperty("dhmname"));//
        properties.put("Status",isLocal ? "Local" : extractProperty("status"));//
        properties.put("Reservedtoyou",isLocal ? "Local" : extractProperty("reservedtoyou"));//
        properties.put("ConnectedTo",runOn.getName());
        this.runOn = runOn;
        this.progressBarPanel = new ProgressBarPanel(getSN());
        directory = WriteSummary.getRoot() + "//" + getSN();
    }

    public String getProperty(String property) {
        return properties.get(property);
    }

    public String getSN(){return getProperty("SN");}

    public ConfigManager.Connection getConn(){return runOn;}

    public ProgressBarPanel getProgressBarPanel(){return progressBarPanel;}

    private String extractProperty(String property){
        return line.split(property+"=\"")[1].split("\"")[0];
    }

    public ConfigManager.Connection getRunOn(){return runOn;}

    public String getDirectory(){return directory;}

    public String getClientLog(){return directory + "//Client.log";}

    public String getAllResultDirectory(){return directory + "//AllResults.txt";}

    public String getSummaryDirectory(){return directory + "//Summary.txt";}

    public String getDeviceLogDirectory(){return deviceLogDirectory;}

    public void restart(){this.progressBarPanel = new ProgressBarPanel(getSN());}

    public void setDeviceLogDirectory(String deviceLogDirectory) {this.deviceLogDirectory = deviceLogDirectory;}
}
