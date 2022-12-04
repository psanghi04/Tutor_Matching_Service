import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
                        createPage('S');
                    }
                });

                tutorButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            writer.writeUTF("2");
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

                            if(reader.readUTF().equals("Successfully Logged In")){
                                if(reader.readUTF().equals("S")){
                                    studentMenu();
                                } else {
                                    tutorMenu();
                                }
                            }
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

                    writer.flush();

                    if (role == 'T') {
                        String subjects = subField.getText();
                        String price = priceField.getText();
                        writer.writeUTF(subjects);
                        writer.writeUTF(price);
                    }
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

                    String availableTutors = reader.readUTF();

                    JLabel aT = new JLabel(availableTutors);

                    JButton back = new JButton("Back");

                    JPanel jp = new JPanel();

                    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

                    jp.add(aT);

                    jp.add(back);

                    content.add("Student Option: View", jp);
                    cl.show(content, "Student Option: View");
                    frame.setSize(317, 262);

                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cl.show(content, "Student Menu");
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");

                    JLabel messagePerson = new JLabel("Who would you like to message?");

                    JTextField mP = new JTextField("User");

                    JButton jb = new JButton("Message");

                    JButton back = new JButton("Back");

                    JPanel jpS = new JPanel();

//                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                    jpS.add(messagePerson, BorderLayout.NORTH);
                    jpS.add(mP, BorderLayout.CENTER);
                    jpS.add(jb, BorderLayout.SOUTH);
                    jpS.add(back, BorderLayout.SOUTH);

                    content.add("Tutor Option: Message", jpS);
                    cl.show(content, "Tutor Option: Message");
                    frame.setSize(317, 262);
                    frame.pack();

                    jb.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF(mP.getText());

                                if(reader.readUTF().equals("Person Found")){
                                    JOptionPane.showMessageDialog(null, "Person Found", "Person Found", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "You cannot message this person", "Cannot Message User", JOptionPane.ERROR_MESSAGE);
                                }

                                JMenuItem readMsg = new JMenuItem("Read message");
                                JMenuItem writeMsg = new JMenuItem("Write message");
                                JMenuItem deleteMsg = new JMenuItem("Delete message");
                                JMenuItem editMsg = new JMenuItem("Edit message");
                                JMenuItem searchMsg = new JMenuItem("Search message");
                                JMenuItem importC = new JMenuItem("Import conversation");
                                JMenuItem exportC = new JMenuItem("Export conversation");
                                JMenuItem quit = new JMenuItem("Quit");
                                JPanel menuMessage = new JPanel();
                                menuMessage.setLayout(new BoxLayout(menuMessage, BoxLayout.Y_AXIS));

                                menuMessage.add(readMsg);
                                menuMessage.add(writeMsg);
                                menuMessage.add(deleteMsg);
                                menuMessage.add(editMsg);
                                menuMessage.add(searchMsg);
                                menuMessage.add(importC);
                                menuMessage.add(exportC);
                                menuMessage.add(quit);

                                readMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("1");

                                            String message = reader.readUTF();

                                            JLabel messagePerson = new JLabel(message);

                                            JButton back = new JButton("Back");

                                            JPanel jpS = new JPanel();

//                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                                            jpS.add(messagePerson, BorderLayout.NORTH);
                                            jpS.add(back, BorderLayout.SOUTH);

                                            content.add("Student Option: Read Message", jpS);
                                            cl.show(content, "Student Option: Read Message");
                                            frame.setSize(317, 262);

                                            back.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    cl.show(content, "Student Menu");
                                                }
                                            });

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                writeMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("2");

//                                            JLabel messagePerson = new JLabel("What message do you want to send?");
//
//                                            JTextField m
//
//                                            JButton back = new JButton("Back");
//
//                                            JPanel jpS = new JPanel();
//
////                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));
//
//                                            jpS.add(messagePerson, BorderLayout.NORTH);
//                                            jpS.add(back, BorderLayout.SOUTH);
//
//                                            content.add("Student Option: Read Message", jpS);
//                                            cl.show(content, "Student Option: Read Message");
//                                            frame.setSize(317, 262);

                                            back.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    cl.show(content, "Student Menu");
                                                }
                                            });
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                deleteMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("3");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                editMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("4");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                searchMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("5");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                importC.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("6");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                exportC.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("7");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                quit.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("8");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                content.add("Student Message Menu", menuMessage);
                                cl.show(content, "Student Message Menu");
                                frame.setSize(317, 262);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cl.show(content, "Student Menu");
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        blk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        ublk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        inv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

                    String availableStudents = reader.readUTF();

                    JLabel aS = new JLabel(availableStudents);

                    JButton back = new JButton("Back");

                    JPanel jpT = new JPanel();

                    jpT.setLayout(new BoxLayout(jpT, BoxLayout.Y_AXIS));

                    jpT.add(aS);

                    jpT.add(back);

                    content.add("Tutor Option: View", jpT);
                    cl.show(content, "Tutor Option: View");
                    frame.setSize(317, 262);

                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cl.show(content, "Tutor Menu");
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");

                    JLabel messagePerson = new JLabel("Who would you like to message?");

                    JTextField mP = new JTextField("User");

                    JButton jb = new JButton("Message");

                    JButton back = new JButton("Back");

                    JPanel jpM = new JPanel();

                    jpM.add(messagePerson, BorderLayout.NORTH);
                    jpM.add(mP, BorderLayout.CENTER);
                    jpM.add(jb, BorderLayout.SOUTH);
                    jpM.add(back, BorderLayout.SOUTH);

                    content.add("Tutor Option: Message", jpM);
                    cl.show(content, "Tutor Option: Message");
                    frame.setSize(317, 262);

                    jb.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF(mP.getText());

                                if(reader.readUTF().equals("Person Found")){
                                    JOptionPane.showMessageDialog(null, "Person Found", "Person Found", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "You cannot message this person", "Cannot Message User", JOptionPane.ERROR_MESSAGE);
                                }

                                JMenuItem readMsg = new JMenuItem("Read message");
                                JMenuItem writeMsg = new JMenuItem("Write message");
                                JMenuItem deleteMsg = new JMenuItem("Delete message");
                                JMenuItem editMsg = new JMenuItem("Edit message");
                                JMenuItem searchMsg = new JMenuItem("Search message");
                                JMenuItem importC = new JMenuItem("Import conversation");
                                JMenuItem exportC = new JMenuItem("Export conversation");
                                JMenuItem quit = new JMenuItem("Quit");
                                JPanel menuMessage = new JPanel();
                                menuMessage.setLayout(new BoxLayout(menuMessage, BoxLayout.Y_AXIS));

                                menuMessage.add(readMsg);
                                menuMessage.add(writeMsg);
                                menuMessage.add(deleteMsg);
                                menuMessage.add(editMsg);
                                menuMessage.add(searchMsg);
                                menuMessage.add(importC);
                                menuMessage.add(exportC);
                                menuMessage.add(quit);

                                readMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                writeMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                deleteMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                editMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                searchMsg.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                importC.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                exportC.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                quit.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                    }
                                });

                                content.add("Tutor Message Menu", menuMessage);
                                cl.show(content, "Tutor Message Menu");
                                frame.setSize(317, 262);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cl.show(content, "Student Menu");
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        blk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        ublk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        inv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
