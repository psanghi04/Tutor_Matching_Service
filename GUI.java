import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class GUI {
    JFrame frame;
    Container content;
    CardLayout cl = new CardLayout();
    DataInputStream reader;
    DataOutputStream writer;

    // temp
    public GUI() {
        frame = new JFrame("Welcome Page");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        content = frame.getContentPane();
        content.setLayout(cl);
    }

    public GUI(DataInputStream reader, DataOutputStream writer) {
        frame = new JFrame("Welcome Page");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        content = frame.getContentPane();
        content.setLayout(cl);
        this.reader =  reader;
        this.writer = writer;
    }

    public void createMainPage() throws Exception {
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

        JButton createAcc = new JButton("Create an Account");
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
                try {
                    writer.writeUTF("1");
                } catch (Exception ex) {
                    System.out.println("error");
                }

                frame.setTitle("Messenger");
                JPanel buttonPanel = new JPanel();
                JPanel promptPanel = new JPanel();
                JPanel holder = new JPanel(new GridLayout(2,1));

                JButton studentButton = new JButton("Student");
                JButton tutorButton = new JButton("Tutor");

                buttonPanel.add(studentButton);
                buttonPanel.add(tutorButton);

                JLabel rolePrompt = new JLabel("Choose a Role");
                rolePrompt.setHorizontalAlignment(SwingConstants.CENTER);
                rolePrompt.setVerticalAlignment(SwingConstants.CENTER);
                rolePrompt.setFont(new Font("Open Sans", Font.PLAIN, 18));

                promptPanel.add(rolePrompt);

                holder.add(rolePrompt);
                holder.add(buttonPanel);

                content.add("Role Page", holder);
                cl.show(content, "Role Page");


                studentButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            writer.writeUTF("1");
                        } catch (IOException ex) {
                            System.out.println("error");
                        }
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
                        usernameField.setText("");

                        JLabel emailPrompt = new JLabel("Email:");
                        JTextField emailField = new JTextField(10);
                        emailField.setText("");

                        JLabel passwordPrompt = new JLabel("Password:");
                        JTextField passwordField = new JTextField(10);
                        passwordField.setText("");

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

                        content.add("Student Create Page", holder);
                        cl.show(content, "Student Create Page");
                        frame.pack();

                        backButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cl.show(content, "Welcome");
                            }
                        });

                        createButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String username = usernameField.getText();
                                String email = emailField.getText();
                                String passwd = passwordField.getText();
                                try {
                                    writer.writeUTF(username);
                                    writer.writeUTF(email);
                                    writer.writeUTF(passwd);
                                } catch (IOException ex) {
                                    System.out.println("error");
                                }
                            }
                        });
                    }
                });

                tutorButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            writer.writeUTF("2");
                        } catch (IOException ex) {
                            System.out.println("error");
                        }
                        JPanel usernamePanel = new JPanel();
                        JPanel emailPanel = new JPanel();
                        JPanel pwdPanel = new JPanel();
                        JPanel subPanel = new JPanel();
                        JPanel pricePanel = new JPanel();
                        JPanel holder = new JPanel(new GridLayout(6,1));
                        JPanel buttonPanel = new JPanel();

                        JButton createButton = new JButton("Create");
                        JButton backButton = new JButton("Back");
                        createButton.setPreferredSize(new Dimension(151, 29));
                        backButton.setPreferredSize(new Dimension(151, 29));

                        JLabel usernamePrompt = new JLabel("Username:");
                        JTextField usernameField = new JTextField(10);
                        usernameField.setText("");

                        JLabel emailPrompt = new JLabel("Email:");
                        JTextField emailField = new JTextField(10);
                        emailField.setText("");

                        JLabel passwordPrompt = new JLabel("Password:");
                        JTextField passwordField = new JTextField(10);
                        passwordField.setText("Password");

                        JLabel subPrompt = new JLabel("Subjects Taught:");
                        JTextField subField = new JTextField(10);
                        subField.setText("");

                        JLabel pricePrompt = new JLabel("Price Charged:");
                        JTextField priceField = new JTextField(10);
                        priceField.setText("");

                        usernamePanel.add(usernamePrompt);
                        usernamePanel.add(usernameField);

                        emailPanel.add(emailPrompt);
                        emailPanel.add(emailField);

                        pwdPanel.add(passwordPrompt);
                        pwdPanel.add(passwordField);

                        subPanel.add(subPrompt);
                        subPanel.add(subField);

                        pricePanel.add(pricePrompt);
                        pricePanel.add(priceField);

                        buttonPanel.add(backButton);
                        buttonPanel.add(createButton);

                        holder.add(usernamePanel);
                        holder.add(emailPanel);
                        holder.add(pwdPanel);
                        holder.add(subPanel);
                        holder.add(pricePanel);

                        holder.add(buttonPanel);

                        content.add("Tutor Create Page", holder);
                        cl.show(content, "Tutor Create Page");
                        frame.pack();

                        backButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cl.show(content, "Welcome");
                            }
                        });

                        createButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String username = usernameField.getText();
                                String email = emailField.getText();
                                String passwd = passwordField.getText();
                                String subjects = subField.getText();
                                String price = priceField.getText();
                                try {
                                    writer.writeUTF(username);
                                    writer.writeUTF(email);
                                    writer.writeUTF(passwd);
                                    writer.writeUTF(subjects);
                                    writer.writeUTF(price);
                                } catch (IOException ex) {
                                    System.out.println("error");
                                }
                            }
                        });
                    }
                });
            }
        });

        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                } catch (IOException ex) {
                    System.out.println("error");
                }
                System.out.println("Log In Selected successfully");
                JPanel usernamePanel = new JPanel();
                JPanel pwdPanel = new JPanel();
                JPanel holder = new JPanel(new GridLayout(3,1));
                JPanel buttonPanel = new JPanel();

                JButton loginButton = new JButton("Login");
                JButton backButton = new JButton("Back");
                loginButton.setPreferredSize(new Dimension(151, 29));
                backButton.setPreferredSize(new Dimension(151, 29));

                JLabel usernamePrompt = new JLabel("Username:");
                JTextField usernameField = new JTextField(10);
                usernameField.setText("");

                JLabel passwordPrompt = new JLabel("Password:");
                JTextField passwordField = new JTextField(10);
                passwordField.setText("");

                usernamePanel.add(usernamePrompt);
                usernamePanel.add(usernameField);

                pwdPanel.add(passwordPrompt);
                pwdPanel.add(passwordField);

                buttonPanel.add(backButton);
                buttonPanel.add(loginButton);

                holder.add(usernamePanel);
                holder.add(pwdPanel);

                holder.add(buttonPanel);

                content.add("Login Page", holder);
                cl.show(content, "Login Page");
                frame.pack();
                frame.setVisible(true);

                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cl.show(content, "Welcome");
                    }
                });

                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String passwd = passwordField.getText();
                        try {
                            writer.writeUTF(username);
                            writer.writeUTF(passwd);
                        } catch (IOException ex) {
                            System.out.println("error");
                        }
                    }
                });
            }
        });
    }
}
