import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//public class AddCloudFrame extends JFrame {
//    TheFatherPanel panel;
//    public AddCloudFrame(TheFatherPanel panel) {
//        super("Add Cloud");
//        add(new LocalPanel());
//        pack();
//        setLocationRelativeTo(null);
//        setVisible(true);
//        this.panel = panel;
//    }


    class AddCloudFrame extends JPanel implements ActionListener {
        JButton bAdd;
        JLabel lName, lUrl, lAccesskey, lUsername, lPassword;
        JTextField tName, tUrl, tAccesskey, tUsername, tPassword;

        public AddCloudFrame() {
            super();
            setLayout(new BorderLayout(5,5));
            JPanel lables = new JPanel(new GridLayout(0,1));
            JPanel tfields = new JPanel(new GridLayout(0,1));
//            setLayout(new GridLayout(6, 2));
            lName = new JLabel("Name: ");
            lUrl = new JLabel("Url: ");
            lAccesskey = new JLabel("Accesskey: ");
            lUsername = new JLabel("Username: ");
            lPassword = new JLabel("Password: ");
            tName = new JTextField();
            tUrl = new JTextField();
            tAccesskey = new JTextField();
            tUsername = new JTextField();
            tPassword = new JTextField();
            lables.add(lName);
            tfields.add(tName);
            lables.add(lUrl);
            tfields.add(tUrl);
            lables.add(lAccesskey);
            tfields.add(tAccesskey);
            lables.add(lUsername);
            tfields.add(tUsername);
            lables.add(lPassword);
            tfields.add(tPassword);
            bAdd = new JButton("Save");
            bAdd.addActionListener(this);
            JPanel bPanel = new JPanel(new GridLayout(1,5));
            bPanel.add(new JLabel());
            bPanel.add(new JLabel());
            bPanel.add(bAdd);
            bPanel.add(new JLabel());
            bPanel.add(new JLabel());
            add(bPanel,BorderLayout.SOUTH);
            add(lables,BorderLayout.WEST);
            add(tfields,BorderLayout.CENTER);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bAdd) {
                ConfigManager.addCloud(createCloudJson());
                ManagerOfGui.getInstance().getTheFatherPanel().addConn(tName.getText());
            }
        }

        private JSONObject createCloudJson() {
            String url = tUrl.getText();
            int port;

            if (url.split(":").length == 2) {
                if (url.contains("https"))
                    port = 443;
                else port = 80;
            } else {
                port = Integer.parseInt(url.split(":")[2].split("/")[0]);
                url = url.split(":")[0] + ":" + url.split(":")[1];
            }
            return createCon(tName.getText(), url, port, tAccesskey.getText(), tUsername.getText(), tPassword.getText(), true);
        }



    public static JSONObject createCon(String name, String ip, int port, String accesskey, String username, String password, boolean isGrid) {
        JSONObject json = new JSONObject();
        json.put("name", name)
                .put("ip", ip)
                .put("port", port)
                .put("accesskey", accesskey)
                .put("username", username)
                .put("password", password)
                .put("isGrid", isGrid);
        return json;
    }

}
