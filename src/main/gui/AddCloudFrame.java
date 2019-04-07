import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    class AddCloudFrame extends JPanel implements ActionListener {
        JButton bAdd;
        JLabel lName, lUrl, lAccesskey, lUsername, lPassword;
        JTextField tName, tUrl, tAccesskey, tUsername, tPassword;
        ConfigManager.Connection conn;
        public AddCloudFrame(ConfigManager.Connection conn) {
            super();
            this.conn = conn;
            setLayout(new BorderLayout(5,5));
            JPanel labels = new JPanel(new GridLayout(0,1));
            JPanel tFields = new JPanel(new GridLayout(0,1));
            lName = new JLabel("Name: ");
            lUrl = new JLabel("Url: ");
            lAccesskey = new JLabel("Accesskey: ");
            lUsername = new JLabel("Username: ");
            lPassword = new JLabel("Password: ");
            if(conn == null)
                tName = new JTextField();
            else {
                tName = new JTextField(conn.getName());
                tName.setEditable(false);
            }
            tUrl = conn == null ? new JTextField() : new JTextField(conn.getURL());
            tAccesskey = conn == null ? new JTextField(2) : new JTextField(conn.getAccesskey(),2);
            tUsername = conn == null ? new JTextField() : new JTextField(conn.getUsername());
            tPassword = conn == null ? new JTextField() : new JTextField(conn.getPassword());
            labels.add(lName);
            tFields.add(tName);
            labels.add(lUrl);
            tFields.add(tUrl);
            labels.add(lAccesskey);
            tFields.add(tAccesskey);
            labels.add(lUsername);
            tFields.add(tUsername);
            labels.add(lPassword);
            tFields.add(tPassword);
            bAdd = conn == null ? new JButton("Save") : new JButton("Save Changes");
            bAdd.addActionListener(this);
            JPanel bPanel = new JPanel(new GridLayout(1,5));
            bPanel.add(new JLabel());
            bPanel.add(new JLabel());
            bPanel.add(bAdd);
            bPanel.add(new JLabel());
            bPanel.add(new JLabel());
            JPanel centerPanel = new JPanel(new BorderLayout());
            add(bPanel,BorderLayout.SOUTH);
            centerPanel.add(labels,BorderLayout.WEST);
            centerPanel.add(tFields,BorderLayout.CENTER);
            JPanel emptyPanel = new JPanel();
            emptyPanel.setPreferredSize(new Dimension(370,ConfigManager.HEIGHT-180));
            JPanel emptyPane2 = new JPanel();
            emptyPane2.setPreferredSize(new Dimension(350,ConfigManager.HEIGHT-180));

            add(emptyPanel,BorderLayout.EAST);
            add(centerPanel,BorderLayout.CENTER);
            add(emptyPane2,BorderLayout.WEST);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bAdd) {
                if(tName.getText().equals(""))
                    createErrorDialog("Must enter name!");
                else if(tUrl.getText().equals(""))
                    createErrorDialog("Must enter an URL!");
                else if(tAccesskey.getText().equals(""))
                    createErrorDialog("Must enter accesskey!");
                else if(ConfigManager.getConn(tName.getText()) != null && conn == null)
                    createErrorDialog("Name exists, please choose other name");
                else {
                    if(tUsername.getText().equals("") || tPassword.getText().equals("")) {
                        int response = JOptionPane.showConfirmDialog(null,
                                "Didn't enter username or password!\nSome tests might not work\nDo you want to save anyway?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(response == JOptionPane.YES_OPTION)
                            saveConn();
                    }
                    else
                        saveConn();
                }
            }
        }

        private void saveConn(){
            if(conn != null)
                ConfigManager.deleteConnection(conn.getName());
            ConfigManager.addCloud(createCloudJson());
            if(conn == null)
                ManagerOfGui.getInstance().getTheFatherPanel().addConn(tName.getText());
            JOptionPane.showMessageDialog(null,
                    "Saved Successfully",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
//            ManagerOfGui.getInstance().getTheFatherPanel().getRunOnBox().setSelectedItem(tName.getText());
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
        private void createErrorDialog(String message){
            JOptionPane.showMessageDialog(null,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
