import com.experitest.appium.SeeTestClient;
import com.experitest.client.GridClient;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TheFatherPanel extends JPanel implements ActionListener {
    JLabel lRunOn;
    JButton bStart,bAdd,bDelete;

    //To do
    JButton bShowDetails; //for clouds to edit, then open new panel with details and option to edit them.
    JComboBox runOnBox;
    DefaultComboBoxModel runOnDefBox;

    public TheFatherPanel(){
        super();
        setLayout(new BorderLayout());
        lRunOn = new JLabel("What would you like to run on?");
        runOnDefBox = new DefaultComboBoxModel(ConfigManager.allConnections());
        runOnBox = new JComboBox(runOnDefBox);
        bStart = new JButton("Next");
        bAdd = new JButton("Add");
        bDelete = new JButton("Delete");
        bShowDetails = new JButton("Show Details");
        bStart.addActionListener(this);
        bAdd.addActionListener(this);
        bDelete.addActionListener(this);
        bShowDetails.addActionListener(this);
        JPanel topPanel = new JPanel(new GridLayout(1,0,5,5));
        topPanel.add(lRunOn);
        topPanel.add(runOnBox);
//        add(bStart,BorderLayout.SOUTH);
        topPanel.add(bAdd);
        topPanel.add(bDelete);
        topPanel.add(bShowDetails);
        runOnBox.addActionListener(this);
        add(topPanel,BorderLayout.NORTH);
        add(new RunPanel(), BorderLayout.CENTER);
        updateChosen(true);
//        add(new TheChosenPanel(), BorderLayout.SOUTH);
    }
//    private void repaintChosen(){
//        JScrollPane scrollFrame = new JScrollPane(new TheChosenPanel());
//        scrollFrame.setPreferredSize(new Dimension( 1200,300));
////        setAutoscrolls(true);
//        add(scrollFrame,BorderLayout.SOUTH);
//
//    }
    public void updateChosen(boolean first){
        if (((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH) != null)
            remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH));
        JScrollPane scrollFrame = new JScrollPane(new TheChosenPanel());
        scrollFrame.setPreferredSize(new Dimension( 1200,300));
//        setAutoscrolls(true);
        add(scrollFrame,BorderLayout.SOUTH);
        if(!first)
            SwingUtilities.getWindowAncestor(this).pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<DeviceController> devices;
        if (e.getSource() == bAdd) {
            if (((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER) != null)
                remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER));
            add(new AddCloudFrame(),BorderLayout.CENTER);
            SwingUtilities.getWindowAncestor(this).pack();
        }
        else if (e.getSource() == bDelete) {
            ConfigManager.deleteConnection(runOnBox.getSelectedItem().toString());
            runOnBox.removeItem(runOnBox.getSelectedItem().toString());
        }
        else if(e.getSource() == bShowDetails){
            JOptionPane.showMessageDialog(null,
                            "Coming soon....",
                            "",
                            JOptionPane.INFORMATION_MESSAGE);
        }
        else if (e.getSource() == runOnBox) {
            try {
                devices = getDevicesSN(ConfigManager.getRunOnFromJson(ConfigManager.getConn(runOnBox.getSelectedItem().toString()).toString()));
                if(devices == null){
                    JOptionPane.showMessageDialog(null,
                            "Please check your credentials.",
                            "Bad Credentials",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER) != null)
                    remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER));
                add(new RunPanel(devices), BorderLayout.CENTER);
                SwingUtilities.getWindowAncestor(this).pack();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "There might be some connection problems.\nPlease check if the server/port is available",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addConn(String name) {
        runOnBox.addItem(name);
    }

    private static List<DeviceController> getDevicesSN(ConfigManager.Connection runOn) {
        String str = runOn.isGrid ? new GridClient(runOn.accesskey,runOn.getURL()).getDevicesInformation() :
                (runOn.name.equals("ASE") ? getASEDevices(runOn): new MyClient(runOn.ip,runOn.port).getDevicesInformation());
        if(str.equals("{Authorization=Bad username or password}"))
            return null;
        //filter is to remove first and last lines in the list
        return Arrays.asList(str.split("\n")).stream().filter(s ->
                s.contains("serialnumber")
                        && !(s.contains("emulator=\"true\"") && s.contains("status=\"unreserved Available\""))
                        && s.contains("os=\"ios\"")).map(s ->
                new DeviceController(s,runOn)).collect(Collectors.toList());
    }

    private static String extractProperty(String line,String property){
        return line.split(line+"\"")[0].split("\"")[0];
    }

    private static String getASEDevices(ConfigManager.Connection runOn){
        try {
            IOSDriver driver= new IOSDriver(new URL(runOn.getURL() +"/wd/hub"),new DesiredCapabilities());
            String str =  new SeeTestClient(driver).getDevicesInformation();
            driver.quit();
            return str;
        } catch (MalformedURLException e) {
            return "{Authorization=Bad username or password}";
        }
    }
}
