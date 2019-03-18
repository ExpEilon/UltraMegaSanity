
import java.util.HashMap;

public class DeviceController {
    private HashMap<String,String> properties;
    private String line;
    private ConfigManager.Connection runOn;

    public DeviceController(String line,ConfigManager.Connection runOn){

        this.line = line;
        properties = new HashMap<>();
        properties.put("SN",extractProperty("serialnumber"));
        properties.put("OS",extractProperty("os"));
        properties.put("Version",extractProperty("version"));
        properties.put("IsSimulator", extractProperty("emulator"));
        properties.put("DHM",extractProperty("dhmname"));
        properties.put("Model",extractProperty("model"));
        properties.put("Status",extractProperty("status"));
        properties.put("Reservedtoyou", extractProperty("reservedtoyou"));
        properties.put("ConnectedTo",runOn.name);
        this.runOn = runOn;
    }

    public String getProperty(String property) {
        return properties.get(property);
    }

    public String getSN(){return getProperty("SN");}

    public ConfigManager.Connection getConn(){return runOn;}

    private String extractProperty(String property){
        return line.split(property+"=\"")[1].split("\"")[0];
    }

}
