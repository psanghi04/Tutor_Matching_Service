import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
    private static JFrame frame;
    private static Container content;
    private static JLabel welcomeField;

    private static JButton createAcc;

    private static JButton signIn;

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
                    SwingUtilities.invokeLater(new CreateNewAccount());
                }
            });

            signIn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Log In Selected successfully");
                    SwingUtilities.invokeLater(new MyRunnable());
                }
            });
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
