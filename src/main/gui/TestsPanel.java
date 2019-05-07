import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TestsPanel extends JFrame {

    enum TYPE{
        SANITY, SHORT, SPECIAL
    }
    private static final Class[] TESTS_SANITY = {EriBankTest.class,SafariLongRun.class,JavaScriptTest.class,DeviceProperty.class,DeviceActions.class,RunNativeAPICallTest.class,PressTheDotTest.class,
            CounterCommands.class,SetAuthTest.class,MonitorTest.class,SafariSameTab.class, LocationTest.class,EriBankAppium.class,
            AppiumSafariTest.class,JSAppiumTest.class};
    private static final Class[] TESTS_SHORT ={ShortTestAppium.class,ShortTest.class};
    private static final Class[] TESTS_SPECIAL ={PerformanceTest.class,XCUITest.class,ClearApplicationDataTestAp.class,ClearApplicationDataAutomation.class,SafariMultipleTabs.class,
            SigningConf.class,GeoLocationAppium.class,IFramesTest.class};

    static final HashMap<TYPE,Class[]> TESTS = new HashMap(){{
        put(TYPE.SANITY,TESTS_SANITY);
        put(TYPE.SHORT,TESTS_SHORT);
        put(TYPE.SPECIAL,TESTS_SPECIAL);
    }};

    public TestsPanel(TYPE type) {
        super("Tests");
        setContentPane(new TestsPanel.LocalPanel(type));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private class LocalPanel extends JPanel implements ActionListener {
        ArrayList<JCheckBox> jCheckBoxesTests;
        JButton bChoose;
        JCheckBox jCheckAllTests;
        TYPE type;
        public LocalPanel(TYPE type) {
            super();
            this.type = type;
            setLayout(new GridLayout(0, 1));
            jCheckAllTests = new JCheckBox("All");
            jCheckAllTests.addActionListener(this);
            add(jCheckAllTests);
            jCheckBoxesTests = new ArrayList<>();

            Arrays.asList(TESTS.get(type)).stream().map(s -> new JCheckBox(s.getName())).forEach(l -> jCheckBoxesTests.add(l));
            jCheckBoxesTests.stream().forEach(j -> {
                if(ConfigManager.tests.stream().map(c -> c.getName()).anyMatch(s -> s.equals(j.getText())))
                    j.setSelected(true);
                this.add(j);
            });
            JPanel buttonPanel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 0));
            bChoose = new JButton("Choose");

            bChoose.addActionListener(this);

            buttonPanel.add(bChoose);
            add(buttonPanel);

        }

        @Override
        public void actionPerformed (ActionEvent e) {
            if (e.getSource() == bChoose) {
                ConfigManager.updateTests(jCheckBoxesTests.stream().filter(JCheckBox::isSelected).map(j -> textToClass(j.getText())).collect(Collectors.toList()),type);
                ManagerOfGui.getInstance().getTheFatherPanel().updateChosen(false);
                dispose();
            } else if (e.getSource() == jCheckAllTests) {
                if (jCheckAllTests.isSelected())
                    jCheckBoxesTests.stream().forEach(i -> i.setSelected(true));
                else jCheckBoxesTests.stream().forEach(i -> i.setSelected(false));
            }

        }

        private Class textToClass(String s){
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
