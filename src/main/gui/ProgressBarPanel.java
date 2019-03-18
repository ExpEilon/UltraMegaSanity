import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class ProgressBarPanel extends JPanel {
    JProgressBar pbarPass;
    JLabel ratio;
    final int MINIMUM = 0;

    public ProgressBarPanel(int max,String sn) {
        // initialize Progress Bar
        ratio = new JLabel("0/0");
        pbarPass = new JProgressBar();
        pbarPass.setMinimum(MINIMUM);
        pbarPass.setMaximum(max);
        // add to JPanel
        add(new JLabel(sn));
        pbarPass.setForeground(Color.GREEN);
        pbarPass.setValue(0);
        add(pbarPass);
        add(ratio);
    }

    public void updateBar(boolean pass) {
        pbarPass.setValue(pbarPass.getValue()+1);
        int total = Integer.parseInt(ratio.getText().split("/")[1]);
        int passed = Integer.parseInt(ratio.getText().split("/")[0]);
        if(pass) {
            ratio.setText((passed + 1) + "/" + (total + 1));
        }
        else{
            pbarPass.setForeground(Color.RED);
            ratio.setText(passed+"/"+(total+1));
        }
        validate();
        repaint();
        SwingUtilities.getWindowAncestor(this).pack();
    }

}
