import com.experitest.client.GridClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyPanel extends JPanel implements ActionListener {
    JLabel lTests,lDevices,lRounds;
    JButton bStart,bExit,bLog;
    ArrayList <JCheckBox> jCheckAllTestsList,jCheckAllDevicesList;
    List<JCheckBox> devicesChosen;
    JCheckBox jCheckAllTests,jCheckAllDevices;
    JTextField numRounds;
    int numOfRows = 2;
    static List<String> listDevices;
    static int rounds = 1;
    private static int finished = 0;

    public MyPanel(List<String> l){
        super();
        listDevices = l;
        numOfRows += Math.max(listDevices.size(),MyProperties.tests.length);
        setLayout(new BorderLayout());
        lTests = new JLabel("Tests: ");
        lDevices = new JLabel("Available devices: ");
        bStart = new JButton("Start!");
        lRounds = new JLabel("Run for rounds: ");
        numRounds = new JTextField("1");
        numRounds.setPreferredSize(new Dimension(50,22));
        bStart.addActionListener(this);
        JPanel testsPanel = new JPanel(new GridLayout(numOfRows,1));
        testsPanel.add(lTests);
        jCheckAllTests = new JCheckBox("All");
        jCheckAllTests.addActionListener(this);
        testsPanel.add(jCheckAllTests);
        jCheckAllTestsList = new ArrayList();
        if(StartPanel.runOn.toString().equals("STA"))
            IntStream.range(0,MyProperties.tests.length).filter(j -> !MyProperties.tests[j].getName().contains("Appium")).forEach(i -> jCheckAllTestsList.add(new JCheckBox(MyProperties.tests[i].getName())));
        else if(StartPanel.runOn.toString().equals("ASE"))
            IntStream.range(0,MyProperties.tests.length).filter(j -> MyProperties.tests[j].getName().contains("Appium")).forEach(i -> jCheckAllTestsList.add(new JCheckBox(MyProperties.tests[i].getName())));
        else IntStream.range(0,MyProperties.tests.length).forEach(i -> jCheckAllTestsList.add(new JCheckBox(MyProperties.tests[i].getName())));
        jCheckAllTestsList.stream().forEach(i -> testsPanel.add(i));
        add(testsPanel,BorderLayout.WEST);

        jCheckAllDevices = new JCheckBox("All");
        jCheckAllDevices.addActionListener(this);
        jCheckAllDevicesList = new ArrayList();
        JPanel devicesPanel= new JPanel(new GridLayout(numOfRows,1));
        devicesPanel.add(lDevices);
        devicesPanel.add(jCheckAllDevices);
        listDevices.stream().forEach(s -> jCheckAllDevicesList.add(new JCheckBox(s)));
        jCheckAllDevicesList.stream().forEach(i -> devicesPanel.add(i));
        add(devicesPanel,BorderLayout.EAST);
        JPanel buttom = new JPanel();
        buttom.add(lRounds);
        buttom.add(numRounds);
        buttom.add(bStart);
        add(buttom,BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jCheckAllTests){
            if(jCheckAllTests.isSelected())
                jCheckAllTestsList.stream().forEach(i -> i.setSelected(true));
            else jCheckAllTestsList.stream().forEach(i -> i.setSelected(false));
        }
        else if(e.getSource() == jCheckAllDevices){
            if(jCheckAllDevices.isSelected())
                jCheckAllDevicesList.stream().forEach(i -> i.setSelected(true));
            else jCheckAllDevicesList.stream().forEach(i -> i.setSelected(false));
        }
        else if(e.getSource() == bStart){
            try{
                rounds = Integer.parseInt(numRounds.getText());
                if(rounds<1)
                    throw new NumberFormatException();
                removeAll();
                devicesChosen = jCheckAllDevicesList.stream().filter(j -> j.isSelected()&& !j.getText().equals("All")).collect(Collectors.toList());
                WriteSummary.createSummary(devicesChosen);
                JPanel gridProgress = new JPanel(new GridLayout(devicesChosen.size(),1));
                int testsNum = jCheckAllTestsList.stream().filter(j -> j.isSelected()&& !j.getText().equals("All")).collect(Collectors.toList()).size();
                devicesChosen.forEach((sn) -> {
                    ProgressBarPanel it = new ProgressBarPanel(testsNum*rounds,sn.getText());
                    gridProgress.add(it);
                    MyThread thread = new MyThread(sn.getText(),testsToClasses(),it,this);
                    thread.setName(sn.getText());
                    thread.start();
                });
                setLayout(new BorderLayout());
                add(gridProgress,BorderLayout.CENTER);
                validate();
                repaint();
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null,
                        "Please enter a positive number in the rounds field",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<Class> testsToClasses(){
        List<Class> temp = new ArrayList<>();
        jCheckAllTestsList.stream().filter(s -> s.isSelected() && !s.getText().equals("All")).forEach(j ->
            temp.add(Arrays.asList(MyProperties.tests).stream().filter(i -> i.getName().equals(j.getText())).findFirst().get())
        );
        return temp;
    }
    synchronized void updateFinished(){
        finished++;
        if(finished==devicesChosen.size()){
            bExit = new JButton("Exit");
            bLog = new JButton("Log File");
//            bFinished.setText("Finished :)");
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
            validate();
            repaint();
        }
    }
}
