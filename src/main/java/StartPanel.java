import com.experitest.client.GridClient;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StartPanel extends JPanel implements ActionListener {
    JLabel lRunOn;
    JButton bStart,bAdd,bDelete;
    JComboBox runOnBox;
    DefaultComboBoxModel runOnDefBox;
    StartPanel thisPanel = this;
//    static MyProperties.Connection runOn;
    static ConfigManager.Connection runOn;
    public StartPanel(){
        super();
        lRunOn = new JLabel("What would you like to run on?");
//        List<String> runOnList = Arrays.asList(MyProperties.runOn.values()).stream().map(s -> s.toString()).collect(Collectors.toList());
//        runOnBox = new JComboBox(runOnList.toArray());
//        runOnBox = new JComboBox(ConfigManager.allConnections());
        runOnDefBox = new DefaultComboBoxModel(ConfigManager.allConnections());
        runOnBox = new JComboBox(runOnDefBox);
        bStart = new JButton("Next");
        bAdd = new JButton("Add");
        bDelete = new JButton("Delete");
        bStart.addActionListener(this);
        bAdd.addActionListener(this);
        bDelete.addActionListener(this);
        add(lRunOn);
        add(runOnBox);
        add(bStart);
        add(bAdd);
        add(bDelete);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bStart) {
            List<String> devices;
//            runOn = Arrays.asList(MyProperties.runOn.values()).stream().filter(con -> con.toString().equals(runOnBox.getSelectedItem())).findFirst().get();
            ConfigManager.setRunOn(ConfigManager.getConn(runOnBox.getSelectedItem().toString()).toString());
            runOn = ConfigManager.runOn;
            if(runOn.isGrid) {
                System.getProperties().setProperty("javax.net.ssl.trustStore","C:\\Users\\eilon.grodsky\\Desktop\\new keystore\\truststore.jks");
                System.getProperties().setProperty("javax.net.ssl.trustStorePassword","123456");
            }

            try {
                devices = getDevicesSN(runOn);
                if(devices == null){
                    JOptionPane.showMessageDialog(null,
                            "Please check your credentials.",
                            "Bad Credentials",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(devices.size()>0) {
                    removeAll();
                    add(new MyPanel(devices));
                    validate();
                    repaint();
                }
                else{
                    JOptionPane.showMessageDialog(null,
                            "Couldn't find any available iOS devices in the given environment.",
                            "No Devices",
                            JOptionPane.WARNING_MESSAGE);
                }
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null,
                        "There might be some connection problems.\nPlease check if the server/port is available",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getSource() == bAdd){
            new AddCloudFrame(this);
        }
        else if(e.getSource() == bDelete){
            ConfigManager.deleteConnection(runOnBox.getSelectedItem().toString());
            runOnBox.removeItem(runOnBox.getSelectedItem().toString());
        }
    }

    public void addConn(String name) {
        runOnBox.addItem(name);
    }

    private static List<String> getDevicesSN(MyProperties.Connection runOn) {
        String str = runOn.isGrid ? new GridClient(runOn.AK,runOn.getURL()).getDevicesInformation() :
                new MyClient(runOn.ip,runOn.port).getDevicesInformation();
        if(str.equals("{Authorization=Bad username or password}"))
            return null;
        if(runOn.isGrid)
            return Arrays.asList(str.split("\n")).stream().filter(s -> s.contains("os=\"ios\"") && (s.contains("status=\"unreserved online\"")) ||
            s.contains("reservedtoyou=\"true\"")) //need to add in case reserved for me
                    .map(s -> s.split(" serialnumber=\"")[1].split("\"")[0]).collect(Collectors.toList());
        else{
            return Arrays.asList(str.split("\n")).stream().filter(s -> s.contains("os=\"ios\"") && s.contains ("added=\"true\"") && (s.contains("remote=\"false\"") ||
                    (s.contains("reservedtoyou=\"true\"")) && !s.contains("status=\"reserved offline\"")))
                    .map(s -> s.split(" serialnumber=\"")[1].split("\"")[0]).collect(Collectors.toList());
        }
    }
    private static List<String> getDevicesSN(ConfigManager.Connection runOn) {
        String str = runOn.isGrid ? new GridClient(runOn.accesskey,runOn.getURL()).getDevicesInformation() :
                new MyClient(runOn.ip,runOn.port).getDevicesInformation();
        if(str.equals("{Authorization=Bad username or password}"))
            return null;
        if(runOn.isGrid)
            return Arrays.asList(str.split("\n")).stream().filter(s -> s.contains("os=\"ios\"") && (s.contains("status=\"unreserved online\"")) ||
                    s.contains("reservedtoyou=\"true\"")) //need to add in case reserved for me
                    .map(s -> s.split(" serialnumber=\"")[1].split("\"")[0]).collect(Collectors.toList());
        else{
            return Arrays.asList(str.split("\n")).stream().filter(s -> s.contains("os=\"ios\"") && s.contains ("added=\"true\"") && (s.contains("remote=\"false\"") ||
                    (s.contains("reservedtoyou=\"true\"")) && !s.contains("status=\"reserved offline\"")))
                    .map(s -> s.split(" serialnumber=\"")[1].split("\"")[0]).collect(Collectors.toList());
        }
    }
}
