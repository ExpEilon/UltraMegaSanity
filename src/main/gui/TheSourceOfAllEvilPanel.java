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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TheSourceOfAllEvilPanel extends JPanel implements ActionListener {
    JRadioButton bAllResults,bSummary,bClient,bServer,bAgent,bSigner,bStorage;
    ButtonGroup logsBG;
    DeviceController device;
    JPanel logPanel;
    JScrollPane scrollFrame;
    JTextArea logsText;
    JCheckBox bFreeze;
    JButton bClear,bThreadDump;
    Tailer tailer;
    private Thread tailerThread;
    private Object lastSelectedLog;
    File ServiceLog;
    HashMap<DeviceController.Service,JRadioButton> serviceToButton;
    DeviceController.Service curService;

    public TheSourceOfAllEvilPanel(DeviceController device){
        this.device = device;
        this.setLayout(new BorderLayout());
        bAllResults = new JRadioButton("All Results");
        bAllResults.addActionListener(this);
        bSummary = new JRadioButton("Summary");
        bSummary.addActionListener(this);
        bClient = new JRadioButton("Client Log");
        bClient.addActionListener(this);
        bServer = new JRadioButton("Server");
        bServer.addActionListener(this);
        bAgent = new JRadioButton("Agent");
        bAgent.addActionListener(this);
        bSigner = new JRadioButton("Signer");
        bSigner.addActionListener(this);
        bStorage = new JRadioButton("Storage");
        bStorage.addActionListener(this);


        serviceToButton = new HashMap(){{
            put(DeviceController.Service.Server,bServer);
            put(DeviceController.Service.Agent,bAgent);
            put(DeviceController.Service.Signer,bSigner);
            put(DeviceController.Service.Strorage,bStorage);
        }};

        logsBG = new ButtonGroup();
        bFreeze = new JCheckBox("Freeze");
        bFreeze.addActionListener(this);
        bClear = new JButton("bClear");
        bClear.addActionListener(this);
        bThreadDump = new JButton("Agent Thread Dump");
        bThreadDump.addActionListener(this);

        logsBG.add(bClient);
        logsBG.add(bAllResults);
        logsBG.add(bSummary);
        if(device.getRunOn().isGrid) {
            logsBG.add(bServer);
            logsBG.add(bAgent);
            logsBG.add(bSigner);
            logsBG.add(bStorage);
        }

        logPanel = new JPanel(new BorderLayout());
        logsText = new JTextArea();
        logsText.setRows(1000);
        logPanel.add(logsText,BorderLayout.CENTER);
        JPanel radioPanel = new JPanel(new GridLayout(1,0));
        radioPanel.add(bClient);
        radioPanel.add(bAllResults);
        if(ConfigManager.rounds > 1)
            radioPanel.add(bSummary);
        if(device.getRunOn().isGrid) {
            radioPanel.add(bServer);
            radioPanel.add(bAgent);
            radioPanel.add(bSigner);
            radioPanel.add(bStorage);
        }
        radioPanel.add(bFreeze);
        radioPanel.add(bClear);
        radioPanel.add(bThreadDump);
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
        if(e.getSource() instanceof JRadioButton) {
            lastSelectedLog = e.getSource();
            startLogging(e.getSource());
        }
        else if(e.getSource() == bFreeze){
            if(bFreeze.isSelected()){
                if(tailer != null)
                    stopWriting();
            }
            else startLogging(lastSelectedLog);
        }
        else if(e.getSource() == bThreadDump){
            try {
                Desktop.getDesktop().open(device.takeThreadDump());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void startLogging(Object obj){
        if(obj == bClient){
            writeLogs(device.getClientLog());
        }
        else if(obj == bSummary){
            writeLogs(device.getSummaryDirectory());
        }
        else if(obj == bAllResults){
            writeLogs(device.getAllResultDirectory());
        }
        //only for services
        else if(obj == bFreeze){
            if(bFreeze.isSelected()){
                if(tailerThread != null) {
                    try {
                        tailerThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                    tailerThread.notify();
                }
            }
        else if(obj == bServer){
            curService = DeviceController.Service.Server;
            loggingFromService(curService);
        }
        else if(obj == bAgent){
            curService = DeviceController.Service.Agent;
            loggingFromService(curService);
        }
        else if(obj == bSigner){
            curService = DeviceController.Service.Signer;
            loggingFromService(curService);
        }
        else if(obj == bStorage){
            curService = DeviceController.Service.Strorage;
            loggingFromService(curService);
        }

    }

    public void loggingFromService(DeviceController.Service service){
        ServiceLog = device.getServiceDirectory(service);
        Thread serviceRunner = new Thread(() -> {
            boolean started = false;
            writeLogs(ServiceLog.getAbsolutePath());
            while (serviceToButton.get(service).isSelected() && !bFreeze.isSelected()) {
                String currentLog = device.getLog(device.getMachineId(service));
                if(started){
                    String lastLine;
                    try (FileReader fr = new FileReader(ServiceLog);
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
                if(!currentLog.equals("") && !currentLog.equals("\n") && !currentLog.equals("\r\n"))
                    WriteSummary.writeToFile(ServiceLog,currentLog);
                if(!started){
                    started = !started;
                    writeLogs(ServiceLog.getAbsolutePath());
                }
            }
        });
        serviceRunner.setName("ServiceLogWriter");
        serviceRunner.start();
    }

    public void stopWriting(){
        tailer.stop();
        try {
            tailerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private boolean isService(JRadioButton button){
        if(button == bAllResults || button == bSummary || button == bClient)
            return false;
        return true;
    }
    public void writeLogs(String fromFile){
        if(tailer != null)
            stopWriting();
        restartLogs();
        TailerListener listener = new MyTailerListener();
        tailer = new Tailer(new File(fromFile), listener);
        tailerThread = new Thread(tailer);
        tailerThread.setDaemon(true);
        tailerThread.start();
        scrollFrame.getVerticalScrollBar().setValue(scrollFrame.getVerticalScrollBar().getMaximum());
    }

    private void restartLogs(){
        logPanel.remove(logsText);
        logsText = new JTextArea();
        logsText.setRows(1000);
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