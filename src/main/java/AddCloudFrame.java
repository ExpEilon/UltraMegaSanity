import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class AddCloudFrame extends JFrame {
    StartPanel panel;
    public AddCloudFrame(StartPanel panel) {
        super("Add Cloud");
        add(new LocalPanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        this.panel = panel;
    }


    private class LocalPanel extends JPanel implements ActionListener {
        JButton bAdd;
        JLabel lName, lUrl, lAccesskey, lUsername, lPassword;
        JTextField tName, tUrl, tAccesskey, tUsername, tPassword;

        public LocalPanel() {
            super();
            setLayout(new GridLayout(3, 4));
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
            add(lName);
            add(tName);
            add(lUrl);
            add(tUrl);
            add(lAccesskey);
            add(tAccesskey);
            add(lUsername);
            add(tUsername);
            add(lPassword);
            add(tPassword);
            bAdd = new JButton("Save");
            bAdd.addActionListener(this);
            add(bAdd);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bAdd) {
                ConfigManager.addCloud(createCloudJson());
                panel.addConn(tName.getText());
                dispose();
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
