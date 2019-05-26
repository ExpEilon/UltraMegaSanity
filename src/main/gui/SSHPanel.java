import com.jcraft.jsch.*;
import com.sun.javaws.util.JfxHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class SSHPanel{

    public static void main(String[] args) {
            JFrame jFrame = new JFrame("SSH");
            jFrame.setSize(800,600);
            jFrame.add(new LocalPanel());
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
            // X Forwarding
            // channel.setXForwarding(true);

            //channel.setInputStream(System.in);
    }
    private static class LocalPanel extends JPanel implements ActionListener {

        JTextArea jTextArea;
        JTextField commandField;
        JButton bSendCommand;
        Session session;
        Channel channel;
        private LocalPanel(){
            setLayout(new BorderLayout());
            jTextArea = new JTextArea();
            commandField = new JTextField();
            commandField.setMaximumSize(new Dimension(50,20));
            bSendCommand = new JButton("Execute");
            bSendCommand.addActionListener(this);
            JPanel bottomPanel = new JPanel(new GridLayout(1,0));
            bottomPanel.add(commandField);
            bottomPanel.add(bSendCommand);
            JPanel textPanel = new JPanel();
            textPanel.add(jTextArea);
            add(bottomPanel,BorderLayout.SOUTH);
            JScrollPane scrollFrame = new JScrollPane(textPanel);
//            scrollFrame.setPreferredSize(new Dimension( ConfigManager.WIDTH,ConfigManager.HEIGHT*3/2));
            setAutoscrolls(true);
            add(scrollFrame);

            JSch jsch=new JSch();

            String host="eilon.grodsky@192.168.2.29";

//                host=JOptionPane.showInputDialog("Enter username@hostname",
//                        System.getProperty("user.name")+
//                                "@192.168.2.29");
            String user=host.substring(0, host.indexOf('@'));
            host=host.substring(host.indexOf('@')+1);

            try {
                session=jsch.getSession(user, host, 22);
                UserInfo ui = new MyUserInfo();
                session.setUserInfo(ui);
                session.connect();

            } catch (JSchException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == bSendCommand){
                String command = commandField.getText();
                commandField.setText("");
                try {
                    channel = session.openChannel("exec");
                    ((ChannelExec)channel).setCommand(command);

                    channel.setInputStream(null);


                    ((ChannelExec)channel).setErrStream(System.err);

                    InputStream in=channel.getInputStream();

                    channel.connect();

                    byte[] tmp=new byte[1024];
                    while(true){
                        while(in.available()>0){
                            int i=in.read(tmp, 0, 1024);
                            if(i<0)break;
                            jTextArea.append(new String(tmp, 0, i));
                            System.out.println(new String(tmp, 0, i));
                        }
                        if(channel.isClosed()){
                            if(in.available()>0) continue;
    //                        System.out.println("exit-status: "+channel.getExitStatus());
                            break;
                        }
                        try{Thread.sleep(1000);}catch(Exception ee){}
                    }
                }catch (Exception e1){
                        e1.printStackTrace();
                }
            }
        }
    }
    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
        String passwd = "1";
        public String getPassword(){ return "1"; }

        public boolean promptYesNo(String str){
            return true;
//            Object[] options={ "yes", "no" };
//            int foo=JOptionPane.showOptionDialog(null,
//                    str,
//                    "Warning",
//                    JOptionPane.DEFAULT_OPTION,
//                    JOptionPane.WARNING_MESSAGE,
//                    null, options, options[0]);
//            return foo==0;
        }
        JTextField passwordField=(JTextField)new JPasswordField(20);

        public String getPassphrase(){ return null; }
        public boolean promptPassphrase(String message){ return true; }
        public boolean promptPassword(String message){
//            Object[] ob={passwordField};
//            int result=
//                    JOptionPane.showConfirmDialog(null, ob, message,
//                            JOptionPane.OK_CANCEL_OPTION);
//            if(result==JOptionPane.OK_OPTION){
//                passwd=passwordField.getText();
                return true;
//            }
//            else{
//                return false;
//            }
        }
        public void showMessage(String message){
            JOptionPane.showMessageDialog(null, message);
        }
        final GridBagConstraints gbc =
                new GridBagConstraints(0,0,1,1,1,1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0,0,0,0),0,0);
        private Container panel;
        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo){
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts=new JTextField[prompt.length];
            for(int i=0; i<prompt.length; i++){
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]),gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if(echo[i]){
                    texts[i]=new JTextField(20);
                }
                else{
                    texts[i]=new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if(JOptionPane.showConfirmDialog(null, panel,
                    destination+": "+name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    ==JOptionPane.OK_OPTION){
                String[] response=new String[prompt.length];
                for(int i=0; i<prompt.length; i++){
                    response[i]=texts[i].getText();
                }
                return response;
            }
            else{
                return null;  // cancel
            }
        }
    }
}
