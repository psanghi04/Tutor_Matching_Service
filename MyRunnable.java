import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyRunnable implements Runnable {
    private JFrame frame1;
    private Container content;
    private JButton loginButton;
    private JLabel usernamePrompt;
    private JTextField usernameField;
    private JLabel passwordPrompt;
    private JTextField passwordField;
    private int successfulLogin;
    public void createJFrame() {
        frame1 = new JFrame("Tutoring Service");
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        content = frame1.getContentPane();
        content.setLayout(new BorderLayout());
    }
    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 9648);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            createJFrame();
            frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel panelOne = new JPanel(new GridLayout(0,1,10,5));
            JPanel panelTwo = new JPanel();
            content.add(panelOne, BorderLayout.NORTH);
            usernamePrompt = new JLabel("Username:");
            loginButton = new JButton("Login");
            usernameField = new JTextField(10);
            usernameField.setText("Username");
            passwordPrompt = new JLabel("Password:");
            passwordField = new JTextField(10);
            passwordField.setText("Password");
            loginButton.setPreferredSize(new Dimension(40, 40));
            panelOne.add(usernamePrompt);
            panelOne.add(usernameField);
            panelOne.add(passwordPrompt);
            panelOne.add(passwordField);
            panelOne.add(loginButton);
            frame1.setVisible(true);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(successfulLogin);

                    pw.println(1);

                    pw.flush();

                    pw.println(usernameField.getText());

                    pw.flush();

                    pw.println(passwordField.getText());

                    pw.flush();

                    try {
                        successfulLogin = Integer.parseInt(bfr.readLine()); //bfr is not reading from the server
                        System.out.println(successfulLogin);
                        //frame1.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,"Unsuccessful Login", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new MyRunnable());
    }
}
