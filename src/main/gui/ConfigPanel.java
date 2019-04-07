import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigPanel extends JFrame {

    public ConfigPanel() {
        super("Configuration");
        setContentPane(new ConfigPanel.LocalPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class LocalPanel extends JPanel implements ActionListener {
        ArrayList<JCheckBox> jCheckBoxesConfigs;
        JButton bSet;

        public LocalPanel() {
            super();
            setLayout(new GridLayout(0, 1));
            jCheckBoxesConfigs = new ArrayList<>();

            ConfigManager.getProperties().stringPropertyNames().stream().filter(c -> !c.equals("connections")).forEach(p -> {
                JCheckBox temp = new JCheckBox(p);
                temp.setSelected(ConfigManager.getProp(p).equals("true"));
                jCheckBoxesConfigs.add(temp);
            });

            jCheckBoxesConfigs.stream().forEach(j -> add(j));

            JPanel buttonPanel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 0));
            bSet = new JButton("Set");

            bSet.addActionListener(this);

            buttonPanel.add(bSet);
            add(buttonPanel);
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            if (e.getSource() == bSet) {
                jCheckBoxesConfigs.stream().forEach(p -> {
                    if(p.isSelected())
                        ConfigManager.setProp(p.getText(),"true");
                    else ConfigManager.setProp(p.getText(),"false");
                    dispose();
                });
            }
        }
    }
}
