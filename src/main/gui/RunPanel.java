import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RunPanel extends JPanel implements ActionListener {
    JLabel lRounds;
    JButton bStart,bExit,bLog;
    JTextField numRounds;
    HashMap<TestsPanel.TYPE,JButton> testsButtons;
    static List<DeviceController> listDevices;
    private static int finished = 0;

    public RunPanel(){
        JScrollPane scrollFrame = new JScrollPane(new JPanel());
        scrollFrame.setPreferredSize(new Dimension( ConfigManager.WIDTH,ConfigManager.HEIGHT));
        setAutoscrolls(true);
        add(scrollFrame,BorderLayout.CENTER);
    }

    public RunPanel(List<DeviceController> l){
        removeAll();
        listDevices = l;
        testsButtons = new HashMap<>();
        Arrays.asList(TestsPanel.TYPE.values()).stream().forEach(t -> testsButtons.put(t,new JButton(t.name())));
        setLayout(new BorderLayout());
        bStart = new JButton("Start!");
        lRounds = new JLabel("Run for rounds: ");
        numRounds = new JTextField("1");
        numRounds.setPreferredSize(new Dimension(50,22));
        bStart.addActionListener(this);
        JPanel testsPanel = new JPanel(new GridLayout(testsButtons.size(),1,5,5));
        testsButtons.values().stream().forEach(t ->{
            t.addActionListener(this);
            testsPanel.add(t);
        });

        add(testsPanel,BorderLayout.WEST);
        JPanel buttom = new JPanel();
        buttom.add(lRounds);
        buttom.add(numRounds);
        buttom.add(bStart);
        add(buttom,BorderLayout.SOUTH);
        JScrollPane scrollFrame = new JScrollPane(new DevicesPanel((ArrayList<DeviceController>) listDevices));
        scrollFrame.setPreferredSize(new Dimension( ConfigManager.WIDTH,ConfigManager.HEIGHT));
        setAutoscrolls(true);
        add(scrollFrame,BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bStart){
            if(!validRounds())
                return;
            removeAll();
            ThreadPrintStream.replaceSystemOut();
            WriteSummary.createRoot();
            WriteSummary.createSummary(ConfigManager.devicesToStringArray());
            ManagerOfGui.letsStart(false);
         }
         else{
            new TestsPanel(testsButtons.entrySet().stream().filter(b -> e.getSource() == b.getValue()).findFirst().get().getKey());
         }
        }

    private void createErrorDialog(String message){
        JOptionPane.showMessageDialog(null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean validRounds(){
        try {
            ConfigManager.rounds = Integer.parseInt(numRounds.getText());
        }catch (NumberFormatException ex) {
            createErrorDialog("Please enter a number in the rounds field");
            return false;
        }
        if(ConfigManager.rounds<1) {
            createErrorDialog("Please enter a positive number in the rounds field");
            return false;
        }
        if(ConfigManager.devicesToStringArray().size() == 0){
            createErrorDialog("No devices chosen, please choose at least one device");
            return false;
        }
        if(ConfigManager.tests.size() == 0){
            createErrorDialog("No tests chosen, please choose at least one test");
            return false;
        }
        return true;
    }


    synchronized void updateFinished(){
        finished++;
        if(finished==ConfigManager.devicesToStringArray().size()){
            bExit = new JButton("Exit");
            bLog = new JButton("Log File");
            bExit.addActionListener(e -> System.exit(0));
            bLog.addActionListener(e -> {
                try {
                    Desktop.getDesktop().open(WriteSummary.getRootDirectory());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            JPanel buttomPanel = new JPanel(new GridLayout(1,2));
            buttomPanel.add(bLog);
            buttomPanel.add(bExit);
            add(buttomPanel,BorderLayout.SOUTH);
            SwingUtilities.getWindowAncestor(this).pack();
        }
    }
}
