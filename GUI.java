import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JFrame frame;
    Container content;


    public GUI() {
        frame = new JFrame("Welcome");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
    }

    public void createMainPage() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelOne = new JPanel();
        JPanel panelTwo = new JPanel();

        content.add(panelOne, BorderLayout.NORTH);
        content.add(panelTwo, BorderLayout.SOUTH);

        JLabel welcomeField = new JLabel("Welcome to Tutoring Center");
        welcomeField.setFont(new Font("Open Sans", Font.PLAIN, 18));

        welcomeField.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeField.setVerticalAlignment(SwingConstants.CENTER);

        Dimension size = welcomeField.getPreferredSize();
        welcomeField.setBounds(0, 0, size.width, size.height);


        JButton createAcc = new JButton("Create a Account");
        JButton signIn = new JButton("Log In");

        panelOne.add(welcomeField, BorderLayout.CENTER);
        panelTwo.add(createAcc, BorderLayout.SOUTH);
        panelTwo.add(signIn, BorderLayout.SOUTH);

        frame.setVisible(true);

        createAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Account Selected successfully");
                JPanel panel1 = new JPanel();
                JPanel panel2 = new JPanel();
                panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

                content.add(new JPanel(), BorderLayout.NORTH);
                content.add(new JPanel(), BorderLayout.EAST);
                content.add(new JPanel(), BorderLayout.WEST);
                content.add(panel1, BorderLayout.CENTER);
                content.add(panel2, BorderLayout.SOUTH);

                JButton loginButton = new JButton("Login");
                JButton backButton = new JButton("Back");
                loginButton.setPreferredSize(new Dimension(151, 29));
                backButton.setPreferredSize(new Dimension(151, 29));

                JLabel usernamePrompt = new JLabel("Username:");
                JTextField usernameField = new JTextField(5);
                usernameField.setText("Username");
                usernameField.setMaximumSize(new Dimension(300, 50));
                usernamePrompt.setHorizontalAlignment(SwingConstants.CENTER);
                usernamePrompt.setVerticalAlignment(SwingConstants.CENTER);

                JLabel emailPrompt = new JLabel("Email:");
                JTextField emailField = new JTextField(5);
                emailField.setText("Email");
                emailField.setMaximumSize(new Dimension(300, 50));

                JLabel passwordPrompt = new JLabel("Password:");
                JTextField passwordField = new JTextField(5);
                passwordField.setText("Password");
                passwordField.setMaximumSize(new Dimension(300, 50));

                panel1.add(usernamePrompt);
                panel1.add(usernameField);

                panel1.add(emailPrompt);
                panel1.add(emailField);

                panel1.add(passwordPrompt);
                panel1.add(passwordField);

                panel2.add(backButton);
                panel2.add(loginButton);

                panel1.setAlignmentX(Component.CENTER_ALIGNMENT);

                panelOne.setVisible(false);
                panelTwo.setVisible(false);
                frame.setVisible(true);
            }
        });

        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Log In Selected successfully");
                SwingUtilities.invokeLater(new MyRunnable());
            }
        });
    }
}
