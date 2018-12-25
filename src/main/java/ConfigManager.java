import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    static Connection runOn;

    private static String directory = System.getProperty("user.dir") + "//Configurations//config.properties";

    public static void addCloud(JSONObject json){
        JSONObject jsonObjectOfArray = new JSONObject((String)getProp("connections"));
        jsonObjectOfArray.put("connArray",jsonObjectOfArray.getJSONArray("connArray").put(json));
        setProp("connections",jsonObjectOfArray);
    }

    public static String[] allConnections(){
        JSONObject json;
        String[] connections;
        try {
            json = new JSONObject((String)getProp("connections"));
        }catch (ClassCastException e){
            json = new JSONObject().put("connArray", new JSONArray());
        }
        int arrayLength = json.getJSONArray("connArray").length();
        connections = arrayLength > 0 ? new String[arrayLength] : null;
        for (int i =0 ; i < arrayLength && connections != null;i++)
            connections[i] = (String) json.getJSONArray("connArray").getJSONObject(i).get("name");
        return connections;
    }

    public static Object getProp(String propName){
        Properties prop = new Properties();
        InputStream input = null;
        Object returnedValue = null;
        try {
            input = new FileInputStream(directory);
            prop.load(input);
            returnedValue = prop.getProperty(propName);
        } catch (IOException ex) {ex.printStackTrace();}
        finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnedValue;
    }
    public static boolean checkIfSetTrue(String prop){
        Object returned = getProp(prop);
        if(returned == null)
            return false;
        if (!returned.toString().equals("true"))
            return false;
        return true;
    }
    public static void setProp(String key,Object value){
        OutputStream output = null;
        FileInputStream in = null;
        Properties props = new Properties();
        try {
            in = new FileInputStream(directory);
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            output = new FileOutputStream(directory);
            props.setProperty(key, String.valueOf(value));
            props.store(output,null);
        } catch (Exception e1) {
            e1.printStackTrace();
        }finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject getConn(String key){
        JSONObject json = new JSONObject((String)getProp("connections"));
        JSONArray array = json.getJSONArray("connArray");
        for (int i = 0; i < array.length(); i++) {
            JSONObject tmp = (JSONObject) array.get(i);
            if(tmp.get("name").equals(key))
                return tmp;
        }
        return null;
    }
    public static JSONObject getConn(String key,JSONObject json){
        JSONArray array = json.getJSONArray("connArray");
        for (int i = 0; i < array.length(); i++) {
            JSONObject tmp = (JSONObject) array.get(i);
            if(tmp.get("name").equals(key))
                return tmp;
        }
        return null;
    }


    public static void setRunOn(String json){
        Gson gson = new Gson();
        runOn = gson.fromJson(json,Connection.class);
    }

    public static void initilizeConn(){
        JSONObject jsonObjectOfArray = new JSONObject((String)getProp("connections"));
        if(!jsonObjectOfArray.has("connArray"))
            jsonObjectOfArray = new JSONObject().put("connArray", new JSONArray());
        if(getConn("STA",jsonObjectOfArray) == null)
            jsonObjectOfArray.put("connArray",jsonObjectOfArray.getJSONArray("connArray").put(
                    AddCloudFrame.createCon("STA","localhost",8889,"","","",false)));// Add STA
        if(getConn("ASE",jsonObjectOfArray) == null)
            jsonObjectOfArray.put("connArray",jsonObjectOfArray.getJSONArray("connArray").put(
                    AddCloudFrame.createCon("ASE","http://localhost",4723,"","","",false)));// Add ASE
        setProp("connections",jsonObjectOfArray);
    }
    public static void deleteConnection(String name){
        JSONObject jsonObject = new JSONObject((String)getProp("connections"));
        JSONArray jsonArray = jsonObject.getJSONArray("connArray");
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            if(!jsonObject.get("name").equals(name))
                newArray.put(jsonObject);
        }
        setProp("connections",new JSONObject().put("connArray",newArray));
    }

    public static class Connection{
        String name,ip,accesskey,username,password;
        int port;
        boolean isGrid;

        public Connection(String name,String ip, int port, String accesskey,String username, String password, boolean isGrid){
            this.name = name;
            this.ip = ip;
            this.accesskey = accesskey;
            this.username = username;
            this.password = password;
            this.port = port;
            this.isGrid = isGrid;
        }
        public String getURL(){
            return ip+":"+port;
        }
    }
}
