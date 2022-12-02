import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
    private static JFrame frame;
    private static Container content;
    private static JLabel welcomeField;

    private static JButton createAcc;

    private static JButton signIn;
    private static JFrame frame1;
    private static JButton loginButton;
    private static JLabel usernamePrompt;
    private static JTextField usernameField;
    private static JLabel passwordPrompt;
    private static JTextField passwordField;
    private static int successfulLogin;

    public static void createJFrame() {
        frame = new JFrame("Welcome");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
    }
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9648);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            createJFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panelOne = new JPanel();
            JPanel panelTwo = new JPanel();

            content.add(panelOne, BorderLayout.CENTER);
            content.add(panelTwo, BorderLayout.SOUTH);

            welcomeField = new JLabel("Welcome");

            welcomeField.setText("Welcome to Tutoring Center");

            welcomeField.setFont(new Font("Open Sans", Font.PLAIN, 18));

            welcomeField.setHorizontalAlignment(SwingConstants.CENTER);
            welcomeField.setVerticalAlignment(SwingConstants.CENTER);

            Dimension size = welcomeField.getPreferredSize();

            welcomeField.setBounds(0, 0, size.width, size.height);


            createAcc = new JButton("Create a Account");
            signIn = new JButton("Log In");

            panelOne.add(welcomeField, BorderLayout.CENTER);
            panelTwo.add(createAcc, BorderLayout.SOUTH);
            panelTwo.add(signIn, BorderLayout.SOUTH);

            frame.setVisible(true);

            createAcc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Create Account Selected successfully");
                }
            });

            signIn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Log In Selected successfully");
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
                    } catch(Exception ex) {
                        JOptionPane.showMessageDialog(null,"Error","Error",JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
