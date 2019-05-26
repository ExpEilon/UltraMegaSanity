import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EndIsComingPanel extends JPanel implements ActionListener {

    JButton bLog,bStop,bReRun,bDeleteLogFile;
    private static int finished = 0;
    JPanel gridProgress;
    JPanel buttonsPanel;
    ButtonGroup devicesBG;
    Tailer tailer;
    File logFile;
//    BottomPanel bottomPanel;
    TheSourceOfAllEvilPanel bottomPanel;
    private Thread tailerThread;

    public EndIsComingPanel(){
        super();
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        gridProgress = new JPanel(new GridLayout(0,1));
        devicesBG = new ButtonGroup();
        ConfigManager.getDevices().stream().forEach(d -> {
            gridProgress.add(d.getProgressBarPanel());
            devicesBG.add(d.getProgressBarPanel().getJRadioButton());
        });
        topPanel.add(gridProgress,BorderLayout.CENTER);

        bLog = new JButton("Log File");
        bLog.addActionListener(this);
        bStop = new JButton("STOP");
        bStop.addActionListener(this);
        bReRun = new JButton("Re-Run");
        bReRun.addActionListener(this);
        bDeleteLogFile = new JButton("Delete log file");
        bDeleteLogFile.addActionListener(this);
        buttonsPanel = new JPanel(new GridLayout(1,0));

        buttonsPanel.add(bStop);

        topPanel.add(buttonsPanel,BorderLayout.SOUTH);

        add(topPanel,BorderLayout.CENTER);
        DeviceController firstInList = ConfigManager.getDevices().get(0);
        firstInList.getProgressBarPanel().getJRadioButton().setSelected(true);
        setNewBottom(firstInList);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bLog){
            try {
                if(new File(WriteSummary.getRoot()).exists())
                    Desktop.getDesktop().open(WriteSummary.getRootDirectory());
                else {
                    JOptionPane.showMessageDialog(null,
                            "Log file doesn't exists",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if(e.getSource() == bReRun){
            ManagerOfGui.letsStart(true);
        }
        else if(e.getSource() == bStop && !ManagerOfGui.getInstance().isTerminated()){
            ManagerOfGui.getInstance().terminateAll();
            bStop.setText("Stopping...");
        }
        else if(e.getSource() == bStop && ManagerOfGui.getInstance().isTerminated()){
            JOptionPane.showMessageDialog(null,
                    "Please wait for the devices to get released.\nReleased " + finished + "/" + ConfigManager.getDevices().size(),
                    "Waiting...",
                    JOptionPane.ERROR_MESSAGE);
        }
        else if(e.getSource() == bDeleteLogFile){
            if(new File(WriteSummary.getRoot()).exists()) {
                try {
                    stopWriting();
                    logFile = null;
                    FileUtils.deleteDirectory(new File(WriteSummary.getRoot()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else{
                JOptionPane.showMessageDialog(null,
                        "File already deleted",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);

            }
        }
    }

    synchronized void updateFinished(){
        finished++;
        if(finished == ConfigManager.getDevices().size()){
            buttonsPanel.remove(bStop);
            buttonsPanel.add(bReRun);
            buttonsPanel.add(bLog);
            buttonsPanel.add(bDeleteLogFile);
            SwingUtilities.getWindowAncestor(this).pack();
        }
    }

    public void setNewBottom(DeviceController device){
        if(bottomPanel != null)
            remove(bottomPanel);
//        bottomPanel = new BottomPanel(device);
        bottomPanel = new TheSourceOfAllEvilPanel(device);
        add(bottomPanel,BorderLayout.SOUTH);
        if(SwingUtilities.getWindowAncestor(this) != null)
            SwingUtilities.getWindowAncestor(this).pack();
    }

    public void stopWriting(){
        tailer.stop();
        try {
            tailerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class BottomPanel extends JPanel implements ActionListener{
        JRadioButton bAllResults,bSummary,bClient,bServer,bAgent;
        ButtonGroup logsBG;
        DeviceController device;
        JPanel logPanel;
        JScrollPane scrollFrame;
        JTextArea logsText;
        JCheckBox bFreeze;
        public BottomPanel(DeviceController device){
            this.device = device;
            this.setLayout(new BorderLayout());
            bAllResults = new JRadioButton("All Results");
            bAllResults.addActionListener(this);
            bSummary = new JRadioButton("Summary");
            bSummary.addActionListener(this);
            bClient = new JRadioButton("Client Log");
            bClient.addActionListener(this);
            bServer = new JRadioButton("Server Log");
            bServer.addActionListener(this);
            logsBG = new ButtonGroup();
            bFreeze = new JCheckBox("Freeze");
            bFreeze.addActionListener(this);
            logsBG.add(bClient);
            logsBG.add(bAllResults);
            logsBG.add(bSummary);
            if(device.getRunOn().isGrid) {
                logsBG.add(bServer);
            }
            logPanel = new JPanel(new BorderLayout());
            logsText = new JTextArea();
            logPanel.add(logsText,BorderLayout.CENTER);
            JPanel radioPanel = new JPanel(new GridLayout(1,0));
            radioPanel.add(bClient);
            radioPanel.add(bAllResults);
            if(ConfigManager.rounds > 1)
                radioPanel.add(bSummary);
            if(device.getRunOn().isGrid) {
                radioPanel.add(bServer);
                radioPanel.add(bFreeze);
            }
            scrollFrame = new JScrollPane(logPanel);
            scrollFrame.setPreferredSize(new Dimension( ConfigManager.WIDTH,ConfigManager.HEIGHT*3/2));
            setAutoscrolls(true);
            bClient.setSelected(true);
            this.add(scrollFrame,BorderLayout.CENTER);
            this.add(radioPanel,BorderLayout.SOUTH);
            writeLogs(device.getClientLog());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == bClient){
                writeLogs(device.getClientLog());
            }
            else if(e.getSource() == bSummary){
                writeLogs(device.getSummaryDirectory());
            }
            else if(e.getSource() == bAllResults){
                writeLogs(device.getAllResultDirectory());
            }
            else if(e.getSource() == bFreeze){
                if(bFreeze.isSelected()){
                    if(tailer != null)
                        stopWriting();
                }
                else restartLogs();
            }
            else if(e.getSource() == bServer){
                File ServerLog = new File(device.getDirectory()+"//Server.log");
                    Thread serviceRunner = new Thread(() -> {
                        boolean started = false;
                        while (bServer.isSelected()) {
                            String currentLog = device.getLog(device.getMachineId(DeviceController.Service.Server));
                            if(started){
                                String lastLine;
                                try (FileReader fr = new FileReader(ServerLog);
                                    BufferedReader br = new BufferedReader(fr)) {
                                    List<String> logsFromFile = br.lines().collect(Collectors.toList());
                                    while (logsFromFile.get(logsFromFile.size()-1).equals(""))
                                        logsFromFile.remove(logsFromFile.size()-1);
                                    lastLine = logsFromFile.get(logsFromFile.size()-1);
                                    List<String> curLogArr = Arrays.asList(currentLog.split("\r\n"));
                                    int lastIndex = curLogArr.lastIndexOf(lastLine);
                                    currentLog = "";
                                    for (int i = lastIndex + 1; i < curLogArr.size(); i++) {
                                        if(!curLogArr.get(i).equals(""))
                                            currentLog += "\r\n" + curLogArr.get(i);
                                    }
                                }catch (Exception e1){
                                    e1.getMessage();
                                }
                            }
                            WriteSummary.writeToFile(ServerLog,currentLog);
                            if(!started){
                               started = !started;
                               writeLogs(ServerLog.getAbsolutePath());
                           }
                        }
                    });
                    serviceRunner.setName("ServiceLogWriter");
                serviceRunner.start();
            }
        }

        public void writeLogs(String fromFile){
            if(tailer != null)
                stopWriting();
            restartLogs();
            logFile = new File(fromFile);
            TailerListener listener = new MyTailerListener();
            tailer = new Tailer(logFile, listener);
            tailerThread = new Thread(tailer);
            tailerThread.setDaemon(true);
            tailerThread.start();
            scrollFrame.getVerticalScrollBar().setValue(scrollFrame.getVerticalScrollBar().getMaximum());
        }

        private void restartLogs(){
            logPanel.remove(logsText);
            logsText = new JTextArea();
            logPanel.add(logsText,BorderLayout.CENTER);
            if(SwingUtilities.getWindowAncestor(this) != null)
                SwingUtilities.getWindowAncestor(this).pack();
        }
        private class MyTailerListener extends TailerListenerAdapter {
            public void handle(String line) {
                JScrollBar vertical = scrollFrame.getVerticalScrollBar();
                logsText.append("\n" + line);
                ManagerOfGui.getInstance().getEndIsComingPanel().repaint();
                if(!vertical.getValueIsAdjusting())
                    vertical.setValue(vertical.getMaximum());
            }
        }
    }

}