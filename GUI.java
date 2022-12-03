import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JFrame frame;
    Container content;
    CardLayout cl = new CardLayout();

    public GUI() {
        frame = new JFrame("Welcome");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        content = frame.getContentPane();
        content.setLayout(cl);
    }

    public void createMainPage() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel welcomePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel panelThree = new JPanel(new BorderLayout());

        JLabel welcomeField = new JLabel("Welcome to Tutoring Center");
        welcomeField.setFont(new Font("Open Sans", Font.PLAIN, 18));

        welcomeField.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeField.setVerticalAlignment(SwingConstants.CENTER);

        Dimension size = welcomeField.getPreferredSize();
        welcomeField.setBounds(0, 0, size.width, size.height);


        JButton createAcc = new JButton("Create a Account");
        JButton signIn = new JButton("Log In");

        welcomePanel.add(welcomeField, BorderLayout.CENTER);
        buttonPanel.add(createAcc, BorderLayout.SOUTH);
        buttonPanel.add(signIn, BorderLayout.SOUTH);

        panelThree.add(welcomePanel, BorderLayout.NORTH);
        panelThree.add(buttonPanel, BorderLayout.SOUTH);
        content.add("Welcome", panelThree);
        cl.show(content, "Welcome");

        frame.setVisible(true);

        createAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Account Selected successfully");
                JPanel usernamePanel = new JPanel();
                JPanel emailPanel = new JPanel();
                JPanel pwdPanel = new JPanel();
                JPanel holder = new JPanel(new GridLayout(4,1));
                JPanel buttonPanel = new JPanel();

                JButton createButton = new JButton("Create");
                JButton backButton = new JButton("Back");
                createButton.setPreferredSize(new Dimension(151, 29));
                backButton.setPreferredSize(new Dimension(151, 29));

                JLabel usernamePrompt = new JLabel("Username:");
                JTextField usernameField = new JTextField(10);
                usernameField.setText("Username");

                JLabel emailPrompt = new JLabel("Email:");
                JTextField emailField = new JTextField(10);
                emailField.setText("Email");

                JLabel passwordPrompt = new JLabel("Password:");
                JTextField passwordField = new JTextField(10);
                passwordField.setText("Password");

                usernamePanel.add(usernamePrompt);
                usernamePanel.add(usernameField);

                emailPanel.add(emailPrompt);
                emailPanel.add(emailField);

                pwdPanel.add(passwordPrompt);
                pwdPanel.add(passwordField);

                buttonPanel.add(backButton);
                buttonPanel.add(createButton);

                holder.add(usernamePanel);
                holder.add(emailPanel);
                holder.add(pwdPanel);

                holder.add(buttonPanel);

                content.add("Create Acc", holder);
                cl.show(content, "Create Acc");
                frame.pack();
                frame.setVisible(true);

                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cl.show(content, "Welcome");
                    }
                });
            }
        });

//        signIn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Log In Selected successfully");
//                SwingUtilities.invokeLater(new MyRunnable());
//            }
//        });
    }
}
