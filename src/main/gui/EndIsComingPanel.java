import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class EndIsComingPanel extends JPanel implements ActionListener {

    JButton bLog,bStop,bReRun,bDeleteLogFile;
    private static int finished = 0;
    JPanel gridProgress;
    JPanel buttonsPanel;
    ButtonGroup devicesBG;
    Tailer tailer;
    File logFile;
    BottomPanel bottomPanel;
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
                Desktop.getDesktop().open(WriteSummary.getRootDirectory());
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
        bottomPanel = new BottomPanel(device);
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
//    private String stringFromFile(String path){
//        java.util.List<String> lines = null;
//        try {
//            lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            return lines.stream().collect(Collectors.joining("\n"));
//        }
//    }
    private class BottomPanel extends JPanel implements ActionListener{
        JRadioButton bAllResults,bSummary,bClient;
        ButtonGroup logsBG;
        DeviceController device;
        JPanel logPanel;
        JScrollPane scrollFrame;
        JTextArea clientText;

        public BottomPanel(DeviceController device){
            this.device = device;
            this.setLayout(new BorderLayout());
            bAllResults = new JRadioButton("All Results");
            bAllResults.addActionListener(this);
            bSummary = new JRadioButton("Summary");
            bSummary.addActionListener(this);
            bClient = new JRadioButton("Client Log");
            bClient.addActionListener(this);
            logsBG = new ButtonGroup();
            logsBG.add(bClient);
            logsBG.add(bAllResults);
            logsBG.add(bSummary);
            logPanel = new JPanel(new BorderLayout());
            clientText = new JTextArea();
            logPanel.add(clientText,BorderLayout.CENTER);
            JPanel radioPanel = new JPanel(new GridLayout(1,0));
            radioPanel.add(bClient);
            radioPanel.add(bAllResults);
            if(ConfigManager.rounds > 1)
                radioPanel.add(bSummary);
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
            logPanel.remove(clientText);
            clientText = new JTextArea();
            logPanel.add(clientText,BorderLayout.CENTER);
            if(SwingUtilities.getWindowAncestor(this) != null)
                SwingUtilities.getWindowAncestor(this).pack();
        }
        private class MyTailerListener extends TailerListenerAdapter {
            public void handle(String line) {
                JScrollBar vertical = scrollFrame.getVerticalScrollBar();
                clientText.append("\n" + line);
                ManagerOfGui.getInstance().getEndIsComingPanel().repaint();
                if(!vertical.getValueIsAdjusting())
                    vertical.setValue(vertical.getMaximum());
            }
        }
    }

}
