import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class DevicesPanel extends JPanel implements ActionListener{
    ArrayList<DeviceController> devices;
    ArrayList<JCheckBox> jCheckBoxesDevices;
    JCheckBox jCheckAllDevices;

    public DevicesPanel(ArrayList<DeviceController> devices) {
        super();
        this.devices = devices;

        String[] labels = {"OS", "Version", "IsSimulator", "DHM", "Model", "Status", "Reservedtoyou"};


        int cols = labels.length + 1; //+1 for checkbox

        setLayout(new GridLayout(devices.size() + 3, cols));
        JPanel headerPanel = new JPanel(new GridLayout(1, cols));
        jCheckAllDevices = new JCheckBox("All");
        jCheckAllDevices.addActionListener(this);

        headerPanel.add(jCheckAllDevices);

        jCheckBoxesDevices = new ArrayList<>();

        Arrays.asList(labels).stream().map(s -> new JLabel(s)).forEach(l -> headerPanel.add(l));
        headerPanel.setPreferredSize(new Dimension(1200, 20));
        headerPanel.setMaximumSize(new Dimension(1200, 20));
        headerPanel.setMinimumSize(new Dimension(1200, 20));
        add(headerPanel);


        devices.stream().forEach(d -> {
            JPanel devicePanel = new JPanel(new GridLayout(1, cols));
            devicePanel.setPreferredSize(new Dimension(1200, 20));
            devicePanel.setMaximumSize(new Dimension(1200, 20));
            devicePanel.setMinimumSize(new Dimension(1200, 20));
            JCheckBox temp = new JCheckBox(d.getProperty("SN"));
            if(ConfigManager.devices.stream().anyMatch(d2 -> d2.getProperty("SN").equals(d.getProperty("SN"))))
                temp.setSelected(true);
            temp.addActionListener(this);
            jCheckBoxesDevices.add(temp);
            devicePanel.add(temp);
            Arrays.asList(labels).stream().map(s -> new JLabel(d.getProperty(s))).filter(l -> !l.getText().contains(d.getProperty("SN"))).forEach(l -> devicePanel.add(l));
            add(devicePanel);
        });


        JPanel buttonPanel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 0));
        add(buttonPanel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Optional<JCheckBox> pressedBox = jCheckBoxesDevices.stream().filter(d -> d == e.getSource()).findFirst();
        if(pressedBox.isPresent()){
            if(pressedBox.get().isSelected())
                ConfigManager.addDevice(devices.stream().filter(d -> d.getProperty("SN").equals(pressedBox.get().getText())).findFirst().get());
            else ConfigManager.removeDevice(devices.stream().filter(d -> d.getProperty("SN").equals(pressedBox.get().getText())).findFirst().get());
            System.out.println(ConfigManager.devices);
        }
//        else if (e.getSource() == bChoose) {
//            ConfigManager.addDevices(jCheckBoxesDevices.stream().filter(JCheckBox::isSelected).map(j -> j.getText()).collect(Collectors.toList()));
//            RunPanel.devicesChosen = jCheckBoxesDevices.stream().filter(JCheckBox::isSelected).map(j -> j.getText()).collect(Collectors.toList());
//                dispose();
         else if (e.getSource() == jCheckAllDevices) {
            if (jCheckAllDevices.isSelected())
                jCheckBoxesDevices.stream().forEach(i -> {
                    ConfigManager.addDevice(devices.stream().filter(d -> d.getProperty("SN").equals(i.getText())).findFirst().get());
                    i.setSelected(true);
                });
            else jCheckBoxesDevices.stream().forEach(i -> {
                ConfigManager.removeDevice(devices.stream().filter(d -> d.getProperty("SN").equals(i.getText())).findFirst().get());
                i.setSelected(false);
            });
        }
        ManagerOfGui.getInstance().getTheFatherPanel().updateChosen(false);
    }
}