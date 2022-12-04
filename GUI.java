import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI {
    JFrame frame;
    Container content;
    CardLayout cl = new CardLayout();
    DataInputStream reader;
    DataOutputStream writer;

    public GUI(DataInputStream reader, DataOutputStream writer) {
        frame = new JFrame("Welcome Page");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        content = frame.getContentPane();
        content.setLayout(cl);
        this.reader =  reader;
        this.writer = writer;
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
                    writer.flush();
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
                            writer.flush();
                        } catch (IOException ex) {
                            System.out.println("error");
                        }
                        createPage('S');
                    }
                });

                tutorButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            writer.writeUTF("2");
                            writer.flush();
                        } catch (IOException ex) {
                            System.out.println("error");
                        }
                        createPage('T');
                    }
                });
            }
        });

        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                    writer.flush();
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
                            writer.flush();
                        } catch (IOException ex) {
                            System.out.println("error");
                        }
                    }
                });
            }
        });
    }

    public void createPage(char role) {
        JPanel usernamePanel = new JPanel();
        JPanel emailPanel = new JPanel();
        JPanel pwdPanel = new JPanel();
        JPanel subPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel holder;
        if (role == 'T') {
            holder = new JPanel(new GridLayout(6, 1));
        } else {
            holder = new JPanel(new GridLayout(4, 1));
        }

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

        JTextField subField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        if (role == 'T') {
            JLabel subPrompt = new JLabel("Subjects Taught:");
            subField.setText("");

            JLabel pricePrompt = new JLabel("Price Charged:");
            priceField.setText("");

            subPanel.add(subPrompt);
            subPanel.add(subField);

            pricePanel.add(pricePrompt);
            pricePanel.add(priceField);
        }

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
        if (role == 'T') {
            holder.add(subPanel);
            holder.add(pricePanel);
        }

        holder.add(buttonPanel);

        if (role == 'T') {
            content.add("Tutor Create Page", holder);
            cl.show(content, "Tutor Create Page");
        } else {
            content.add("Student Create Page", holder);
            cl.show(content, "Student Create Page");
        }
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

                    if (role == 'T') {
                        String subjects = subField.getText();
                        String price = priceField.getText();
                        writer.writeUTF(subjects);
                        writer.writeUTF(price);
                    }

                    writer.flush();
                } catch (IOException ex) {
                    System.out.println("error");
                }

                if (role == 'T') {
                    tutorMenu();
                } else {
                    studentMenu();
                }
            }
        });
    }

    public void studentMenu() {
//        "Menu\n\n" +
//                "1. View tutors\n" +
//                "2. Message a tutor\n" +
//                "3. Edit profile\n" +
//                "4. Block a tutor\n" +
//                "5. Unblock a tutor\n" +
//                "6. Become invisible to a tutor\n" +
//                "7. Sign out\n\n" +
//                "Enter Option Number:");

        JMenuItem view = new JMenuItem("View Tutors");
        JMenuItem msg = new JMenuItem("Message a Tutor");
        JMenuItem edit = new JMenuItem("Edit Profile");
        JMenuItem blk = new JMenuItem("Block a Tutor");
        JMenuItem ublk = new JMenuItem("Unblock a Tutor");
        JMenuItem inv = new JMenuItem("Become Invisible to a Tutor");
        JMenuItem exit = new JMenuItem("Sign Out");
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        menu.add(view);
        menu.add(msg);
        menu.add(edit);
        menu.add(blk);
        menu.add(ublk);
        menu.add(inv);
        menu.add(exit);
        content.add("Student Menu", menu);
        cl.show(content, "Student Menu");
        frame.setSize(317, 262);

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("1");
                    writer.flush();

                    // read from server
                    int size = Integer.parseInt(reader.readUTF());
                    JPanel tutorListPanel = new JPanel(new GridLayout(0, 1));
                    ;
                    if (size != 0) {
                        for (int i = 0; i < size; i++) {
                            String response = reader.readUTF();

                            JLabel tutorLabel = new JLabel(response);
                            tutorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            tutorListPanel.add(tutorLabel);
                        }
                        content.add("View Tutors", tutorListPanel);
                        cl.show(content, "View Tutors");
                    }
                    else {
                        JLabel noTutors = new JLabel("No Tutors Available to Message");
                        noTutors.setHorizontalAlignment(SwingConstants.CENTER);
                        JButton okButton = new JButton("OK");
                        okButton.setPreferredSize(new Dimension(151, 29));
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(okButton);
                        tutorListPanel.add(noTutors);
                        tutorListPanel.add(buttonPanel);
                        content.add("View Tutors Error", tutorListPanel);
                        cl.show(content, "View Tutors Error");

                        okButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cl.show(content, "Student Menu");
                            }
                        });
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("3");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        blk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("4");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ublk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("5");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        inv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("6");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("7");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void tutorMenu() {
//        "Menu\n\n" +
//                "1. View students\n" +
//                "2. Message a student\n" +
//                "3. Edit profile\n" +
//                "4. Block a student\n" +
//                "5. Unblock a student\n" +
//                "6. Become invisible to a student\n" +
//                "7. Sign out\n\n" +
//                "Enter Option Number:");

        JMenuItem view = new JMenuItem("View Students");
        JMenuItem msg = new JMenuItem("Message a Student");
        JMenuItem edit = new JMenuItem("Edit Profile");
        JMenuItem blk = new JMenuItem("Block a Student");
        JMenuItem ublk = new JMenuItem("Unblock a Student");
        JMenuItem inv = new JMenuItem("Become Invisible to a Student");
        JMenuItem exit = new JMenuItem("Sign Out");
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        menu.add(view);
        menu.add(msg);
        menu.add(edit);
        menu.add(blk);
        menu.add(ublk);
        menu.add(inv);
        menu.add(exit);
        content.add("Tutor Menu", menu);
        cl.show(content, "Tutor Menu");
        frame.setSize(317, 262);

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("1");
                    writer.flush();

                    // read from server
                    int size = Integer.parseInt(reader.readUTF());
                    JPanel studentListPanel = new JPanel(new GridLayout(0, 1));
                    ;
                    if (size != 0) {
                        for (int i = 0; i < size; i++) {
                            String response = reader.readUTF();

                            JLabel tutorLabel = new JLabel(response);
                            tutorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            studentListPanel.add(tutorLabel);
                        }
                        content.add("View Students", studentListPanel);
                        cl.show(content, "View Students");
                    }
                    else {
                        JLabel noStudents = new JLabel("No Students Available to Message");
                        noStudents.setHorizontalAlignment(SwingConstants.CENTER);
                        JButton okButton = new JButton("OK");
                        okButton.setPreferredSize(new Dimension(151, 29));
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(okButton);
                        studentListPanel.add(noStudents);
                        studentListPanel.add(buttonPanel);
                        content.add("View Students Error", studentListPanel);
                        cl.show(content, "View Students Error");

                        okButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cl.show(content, "Student Menu");
                            }
                        });
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("3");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        blk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("4");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ublk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("5");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        inv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("6");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("7");
                    writer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
