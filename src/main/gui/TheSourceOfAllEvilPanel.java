import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TheSourceOfAllEvilPanel extends JPanel implements ActionListener {
    JRadioButton bAllResults,bSummary,bClient,bServer,bAgent;
    ButtonGroup logsBG;
    DeviceController device;
    JPanel logPanel;
    JScrollPane scrollFrame;
    JTextArea logsText;
    JCheckBox bFreeze;
    Tailer tailer;
    File logFile;
    private Thread tailerThread;

    public TheSourceOfAllEvilPanel(DeviceController device){
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
                            java.util.List<String> logsFromFile = br.lines().collect(Collectors.toList());
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

    public void stopWriting(){
        tailer.stop();
        try {
            tailerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
