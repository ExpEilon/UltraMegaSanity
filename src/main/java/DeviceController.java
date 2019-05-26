
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class DeviceController {
    private HashMap<String,String> properties;
    private String line;
    private ConfigManager.Connection runOn;
    private boolean isLocal;
    private ProgressBarPanel progressBarPanel;
    private String directory;
    private String deviceLogDirectory;
    private int failsInRow;
    enum Service {
        Server,Agent,Signer,Strorage,Proxy
    }
    public DeviceController(String line,ConfigManager.Connection runOn){

        this.line = line;
        isLocal = !runOn.isGrid;
        properties = new HashMap<>();
        properties.put("SN",extractProperty("serialnumber"));
        properties.put("OS",extractProperty("os"));
        properties.put("Version",extractProperty("version"));
        properties.put("Model",extractProperty("model"));
        properties.put("IsSimulator", line.contains("emulator=\"true\"") ? "true" : "false");//
        properties.put("DHM",isLocal ? "Local" : extractProperty("dhmname"));//
        properties.put("Status",isLocal ? "Local" : extractProperty("status"));//
        properties.put("Reservedtoyou",isLocal ? "Local" : extractProperty("reservedtoyou"));//
        properties.put("ConnectedTo",runOn.getName());
        if(!isLocal){
            properties.put("DHMIP",extractProperty("dhminternalhost"));
            properties.put("DHMPort",extractProperty("dhminternalport"));
        }
        this.runOn = runOn;
        this.progressBarPanel = new ProgressBarPanel(getSN());
        directory = WriteSummary.getRoot() + "//" + getSN();
        failsInRow = 0;
//        deviceLogDirectory = directory + "//deviceLog";
//        if(ConfigManager.checkIfSetTrue("deviceLog")){
//            new File(deviceLogDirectory).mkdir();
//        }
    }

    public String getProperty(String property) {
        return properties.get(property);
    }

    public String getSN(){return getProperty("SN");}

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

    public File getServiceDirectory(Service service){
        switch (service){
            case Server: return new File(getDirectory()+"//Server.log");
            case Agent: return new File(getDirectory()+"//Agent.log");
            case Proxy: return new File(getDirectory()+"//Proxy.log");
            case Signer: return new File(getDirectory()+"//Signer.log");
            case Strorage: return new File(getDirectory()+"//Strorage.log");
        }
        return null;
    }
    public int addFail(){
        failsInRow++;
        return failsInRow;
    }
    public void resetFail(){
        failsInRow = 0;
    }

    public File takeThreadDump(){
        File threadDumpDir = new File(directory + "/ThreadDump");
        if(!threadDumpDir.exists())
            threadDumpDir.mkdir();

        String url = getRunOn().getURL() + "/sba/instances/"+getMachineId(Service.Agent)+"/actuator/threaddump";
        String threadDumpFile = threadDumpDir.getAbsolutePath() + WriteSummary.getCurrTime()+".txt";
        try {
            HttpResponse<String> response = Unirest.get(url)
                    .basicAuth(getRunOn().getUsername(), getRunOn().getPassword())
                    .asString();
            MyThread.writeToFile(threadDumpFile,response.getBody(),false);
        } catch (UnirestException e) {
            e.printStackTrace();
        }finally {
            return new File(threadDumpFile);
        }
    }
    //Only server for now
    public String getMachineId(Service service) {
        String url = getRunOn().getURL() + "/sba/instances";
        HttpResponse<JsonNode> response1 = null;
        try {
            response1 = Unirest.get(url)
                    .basicAuth(getRunOn().getUsername(), getRunOn().getPassword())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        int jLength = response1.getBody().getArray().length();
        for (int i = 0; i < jLength; i++) {
            JSONObject temp1 = response1.getBody().getArray().getJSONObject(i);
            JSONObject temp = temp1.getJSONObject("registration");
            if(service == Service.Server) {
                if (temp.getString("name").contains("cloud server"))
                    return temp1.getString("id");
            }
            else if(service == Service.Agent){
                if (temp.getString("serviceUrl").contains(getProperty("DHMIP") + ":" + getProperty("DHMPort")))
                    return temp1.getString("id");
            }
            else if(service == Service.Signer){
                if (temp.getString("name").contains("[application signer]"))
                    return temp1.getString("id");
            }
            else if(service == Service.Strorage){
                if (temp.getString("name").contains("[file storage service]"))
                    return temp1.getString("id");
            }
        }
        return null;
    }

    public String getLog(String id) {
        String url = getRunOn().getURL() + "/sba/instances/"+id+"/actuator/logfile";
        try {
            HttpResponse<String> response = Unirest.get(url)
                    .basicAuth(getRunOn().getUsername(), getRunOn().getPassword())
                    .asString();
            return  response.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
