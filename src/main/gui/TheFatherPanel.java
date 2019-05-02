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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TheFatherPanel extends JPanel implements ActionListener {
    JLabel lRunOn;
    JButton bAdd,bDelete,bShowDetails,bConfig;

    JComboBox runOnBox;
    DefaultComboBoxModel runOnDefBox;

    public TheFatherPanel(){
        super();
        setLayout(new BorderLayout());
        lRunOn = new JLabel("What would you like to run on?");
        runOnDefBox = new DefaultComboBoxModel(ConfigManager.allConnections());
        runOnBox = new JComboBox(runOnDefBox);

        bAdd = new JButton("Add");
        bDelete = new JButton("Delete");
        bShowDetails = new JButton("Show Details");
        bConfig = new JButton("Config");
        bAdd.addActionListener(this);
        bDelete.addActionListener(this);
        bShowDetails.addActionListener(this);
        bConfig.addActionListener(this);
        JPanel topPanel = new JPanel(new GridLayout(1,0,5,5));
        topPanel.add(lRunOn);
        topPanel.add(runOnBox);
        topPanel.add(bAdd);
        topPanel.add(bDelete);
        topPanel.add(bShowDetails);
        topPanel.add(bConfig);
        runOnBox.addActionListener(this);
        add(topPanel,BorderLayout.NORTH);
        add(new RunPanel(), BorderLayout.CENTER);
        updateChosen(true);
    }

    public void updateChosen(boolean first){
        if (((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH) != null)
            remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH));
        JScrollPane scrollFrame = new JScrollPane(new TheChosenPanel());
        scrollFrame.setPreferredSize(new Dimension( 1200,300));
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
            add(new AddCloudFrame(null),BorderLayout.CENTER);
            SwingUtilities.getWindowAncestor(this).pack();
        }
        else if (e.getSource() == bDelete) {
            ConfigManager.deleteConnection(runOnBox.getSelectedItem().toString());
            runOnBox.removeItem(runOnBox.getSelectedItem().toString());
        }
        else if(e.getSource() == bShowDetails){
            if (((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER) != null)
                remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER));
            add(new AddCloudFrame(ConfigManager.getRunOnFromJson(ConfigManager.getConn(runOnBox.getSelectedItem().toString()).toString())),BorderLayout.CENTER);
            SwingUtilities.getWindowAncestor(this).pack();
        }
        else if(e.getSource() == bConfig){
            new ConfigPanel();
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
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "There might be some connection problems.\nPlease check if the server/port is available",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public JComboBox getRunOnBox(){return runOnBox;}
    public void addConn(String name) {
        runOnBox.addItem(name);
    }

    private static List<DeviceController> getDevicesSN(ConfigManager.Connection runOn) {
            String str = runOn.isGrid ? new GridClient(runOn.getAccesskey(),runOn.getURL()).getDevicesInformation() :
                (runOn.getName().equals("ASE") ? getASEDevices(runOn): new MyClient(runOn.getIp(),runOn.port).getDevicesInformation());
        if(str.equals("{Authorization=Bad username or password}") || str.equals(""))
            return null;
        //filter is to remove first and last lines in the list
        return Arrays.asList(str.split("\n")).stream().filter(s ->
                s.contains("serialnumber")
                        && !(s.contains("emulator=\"true\"") && s.contains("status=\"unreserved Available\""))
                        && s.contains("os=\"ios\"")
                        && (s.contains("reservedtoyou=\"true\"") || s.contains("remote=\"false\"") ) ).map(s ->
                new DeviceController(s,runOn)).collect(Collectors.toList());
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

    public void theEndIsComing(){
        removeAll();
        add(ManagerOfGui.getInstance().getEndIsComingPanel());
        SwingUtilities.getWindowAncestor(this).pack();
    }
}
