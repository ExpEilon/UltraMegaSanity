import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarPanel extends JPanel {
    JProgressBar pBarPass;
    JLabel ratio,fails;
    final int MINIMUM = 0,MAXIMUM = ConfigManager.tests.size()*ConfigManager.rounds;

    private JRadioButton  jRadioButton ;

    public ProgressBarPanel(String sn) {
        // initialize Progress Bar
        pBarPass = new JProgressBar();
        pBarPass.setMinimum(MINIMUM);
        pBarPass.setMaximum(MAXIMUM);
        fails = new JLabel("0");
        fails.setForeground(Color.RED);
        ratio = new JLabel("0/" + MAXIMUM);
        jRadioButton = new JRadioButton(sn);

        jRadioButton.addActionListener((ActionEvent e) -> {
                DeviceController deviceController = ConfigManager.getDevices().stream().filter(d -> d.getSN().equals(jRadioButton.getText())).findFirst().get();
                ManagerOfGui.getInstance().getEndIsComingPanel().setNewBottom(deviceController);
        });
        add(jRadioButton);
        pBarPass.setForeground(Color.BLUE);
        pBarPass.setValue(0);
        add(pBarPass);
        add(ratio);
        add(fails);
    }

    public void updateBar(boolean pass) {
        pBarPass.setValue(pBarPass.getValue()+1);
        int total = Integer.parseInt(ratio.getText().split("/")[0]);
        ratio.setText((total + 1) + "/" + MAXIMUM);
        if(!pass) {
            int fail = Integer.parseInt(fails.getText());
            fails.setText((++fail)+"");
        }
        SwingUtilities.getWindowAncestor(this).pack();
    }
    public JRadioButton getJRadioButton(){return jRadioButton;}
}
