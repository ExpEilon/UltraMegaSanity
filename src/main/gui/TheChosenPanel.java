import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TheChosenPanel extends JPanel {
    String[] labels = {"SN","OS", "Version", "IsSimulator", "DHM", "Model", "Status", "Reservedtoyou","ConnectedTo"};
    JLabel testsHeadLine;

    public TheChosenPanel() {
        super();
        testsHeadLine = new JLabel("<html><b>"+"Tests"+"</b></html>");
//        int cols = labels.length; //+1 for checkbox
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());

//        JPanel devicesPanel = new JPanel();

        JPanel testsPanel = new JPanel();

        testsPanel.setLayout(new GridLayout(0,1,5,5));
//        devicesPanel.setLayout(new GridLayout(ConfigManager.devices.size() + 1, cols));
//        JPanel headerPanel = new JPanel(new GridLayout(1, cols));

        testsPanel.add(testsHeadLine);

//        Arrays.asList(labels).stream().map(s -> new JLabel(s)).forEach(l -> headerPanel.add(l));
//        headerPanel.setPreferredSize(new Dimension(1200, 20));
//        headerPanel.setMaximumSize(new Dimension(1200, 20));
//        headerPanel.setMinimumSize(new Dimension(1200, 20));
//        devicesPanel.add(headerPanel);

//        ConfigManager.devices.stream().forEach(d -> {
//            JPanel innerDevicePanel = new JPanel(new GridLayout(1, cols));
//            innerDevicePanel.setPreferredSize(new Dimension(1200, 20));
//            innerDevicePanel.setMaximumSize(new Dimension(1200, 20));
//            innerDevicePanel.setMinimumSize(new Dimension(1200, 20));
//            Arrays.asList(labels).stream().map(s -> new JLabel(d.getProperty(s))).forEach(l -> innerDevicePanel.add(l));
//            devicesPanel.add(innerDevicePanel);
//        });

        ConfigManager.tests.stream().forEach(t -> testsPanel.add(new JLabel(t.getName())));


        DefaultTableModel dtmTests = new DefaultTableModel(new Object[] {testsHeadLine}, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        dtmTests.addRow(new String[] {"<html><b>Tests</b></html>"});
        JTable testsTable = new JTable(dtmTests);

        ConfigManager.tests.stream().forEach(t -> {
            Vector v = new Vector();
            v.add(t.getName());
            dtmTests.addRow(v);
        });


        DefaultTableModel dtm = new DefaultTableModel(labels, 0){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        dtm.addRow(labelsToBold());
        JTable devicesTable = new JTable(dtm);

        ConfigManager.devices.stream().forEach(d -> {
            Vector v = new Vector();
            Arrays.asList(labels).stream().map(s -> d.getProperty(s)).forEach(l -> v.add(l));
            dtm.addRow(v);
        });

        add(devicesTable,BorderLayout.CENTER);
        add(testsTable,BorderLayout.WEST);
    }

    private String[] labelsToBold(){
        String[] returnedArr = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            returnedArr[i] = "<html><b>"+labels[i]+"</b></html>";
        }
        return returnedArr;
    }
}
