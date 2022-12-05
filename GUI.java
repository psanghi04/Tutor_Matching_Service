import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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

                            String response = reader.readUTF();
                            if (response.equals("Success")) {
                                String role = reader.readUTF();
                                if (role.equals("Student")) {
                                    studentMenu();
                                } else {
                                    tutorMenu();
                                }
                            } else {
                                JPanel buttonPanel = new JPanel();
                                JLabel errorMsg = new JLabel("Invalid Username or Password");
                                errorMsg.setHorizontalAlignment(SwingConstants.CENTER);
                                JButton tryAgain = new JButton("Try Again");
                                JButton createButton = new JButton("Create Account");
                                buttonPanel.add(tryAgain);
                                buttonPanel.add(createButton);
                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                holder.add(errorMsg);
                                holder.add(buttonPanel);
                                content.add("Invalid Login Page", holder);
                                cl.show(content, "Invalid Login Page");

                                tryAgain.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("2");
                                            writer.flush();
                                            cl.show(content, "Login Page");
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                });

                                createButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF("1");
                                            writer.flush();
                                            cl.show(content, "Welcome");
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                });
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
                viewUserPage("Tutors");
            }
        });
        msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                    writer.flush();

                    JLabel prompt = new JLabel("Who would you like to message:");
                    JTextField username = new JTextField("");
                    JPanel promptPanel = new JPanel();

                    promptPanel.add(prompt);
                    promptPanel.add(username);

                    JButton submitButton = new JButton("Submit");
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(submitButton);

                    JPanel holder = new JPanel(new GridLayout(0, 1));
                    holder.add(promptPanel);
                    holder.add(buttonPanel);

                    content.add("Message Prompt", holder);
                    cl.show(content, "Message Prompt");

                    submitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF(username.getText());
                                writer.flush();

                                String response = reader.readUTF();

                                if (response.equals("0")) {
                                    JLabel noUser = new JLabel("There are no tutors available");
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    JPanel noPanel = new JPanel();
                                    noPanel.add(noUser);

                                    JButton okButton = new JButton("OK");
                                    okButton.setPreferredSize(new Dimension(151, 29));
                                    JPanel buttonPanel = new JPanel();
                                    buttonPanel.add(okButton);

                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                    holder.add(noPanel);
                                    holder.add(buttonPanel);

                                    content.add("Unavailable", holder);
                                    cl.show(content, "Unavailable");

                                    okButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            cl.show(content, "Student Menu");
                                        }
                                    });
                                } else if(response.equals("Fail")) {
                                    JLabel noUser = new JLabel("You cannot message this person");
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    JPanel noPanel = new JPanel();
                                    noPanel.add(noUser);

                                    JButton okButton = new JButton("OK");
                                    okButton.setPreferredSize(new Dimension(151, 29));
                                    JPanel buttonPanel = new JPanel();
                                    buttonPanel.add(okButton);

                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                    holder.add(noPanel);
                                    holder.add(buttonPanel);

                                    content.add("Cannot Message", holder);
                                    cl.show(content, "Cannot Message");

                                    okButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            cl.show(content, "Student Menu");
                                        }
                                    });
                                } else if(response.contains("Unable")) {
                                    JLabel noUser = new JLabel(response);
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    JPanel noPanel = new JPanel();
                                    noPanel.add(noUser);

                                    JButton okButton = new JButton("OK");
                                    okButton.setPreferredSize(new Dimension(151, 29));
                                    JPanel buttonPanel = new JPanel();
                                    buttonPanel.add(okButton);

                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                    holder.add(noPanel);
                                    holder.add(buttonPanel);

                                    content.add("Unable to find", holder);
                                    cl.show(content, "Unable to find");

                                    okButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            cl.show(content, "Student Menu");
                                        }
                                    });
                                } else {
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

                                    content.add("Student Message Menu", menuMessage);
                                    cl.show(content, "Student Message Menu");
                                    frame.setSize(317, 262);

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
                                                        cl.show(content, "Student Message Menu");
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

//                                                back.addActionListener(new ActionListener() {
//                                                    @Override
//                                                    public void actionPerformed(ActionEvent e) {
//                                                        cl.show(content, "Student Menu");
//                                                    }
//                                                });

                                                JLabel messagePerson = new JLabel("What message do you want to send?");

                                                //                                                back.addActionListener(new ActionListener() {
                                                //                                                    @Override
                                                //                                                    public void actionPerformed(ActionEvent e) {
                                                //                                                        cl.show(content, "Student Menu");
                                                //                                                    }
                                                //                                                });
                                                JTextField mP = new JTextField("Send Message");

                                                JButton jb = new JButton("Send Message");

                                                JPanel jpS = new JPanel();

                                                //                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                                                jpS.add(messagePerson, BorderLayout.NORTH);
                                                jpS.add(mP);
                                                jpS.add(jb);

                                                content.add("Student Option: Write Message", jpS);
                                                cl.show(content, "Student Option: Write Message");
                                                frame.setSize(317, 262);

                                                jb.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(mP.getText());

                                                            if(reader.readUTF().equals("Written Successfully")){
                                                                JOptionPane.showMessageDialog(null, "Written Successfully", "Written Successfully", JOptionPane.INFORMATION_MESSAGE);
                                                            }

                                                            cl.show(content, "Student Message Menu");
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                        }
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
                                }

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
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
                viewUserPage("Students");
            }
        });
        msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                    writer.flush();

                    JLabel prompt = new JLabel("Who would you like to message:");
                    JTextField username = new JTextField("");
                    JPanel promptPanel = new JPanel();

                    promptPanel.add(prompt);
                    promptPanel.add(username);

                    JButton submitButton = new JButton("Submit");
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(submitButton);

                    JPanel holder = new JPanel(new GridLayout(0, 1));
                    holder.add(promptPanel);
                    holder.add(buttonPanel);

                    content.add("Message Prompt", holder);
                    cl.show(content, "Message Prompt");

                    submitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF(username.getText());
                                writer.flush();

                                String response = reader.readUTF();

                                if (response.equals("0")) {
                                    JLabel noUser = new JLabel("There are no students available");
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    JPanel noPanel = new JPanel();
                                    noPanel.add(noUser);

                                    JButton okButton = new JButton("OK");
                                    okButton.setPreferredSize(new Dimension(151, 29));
                                    JPanel buttonPanel = new JPanel();
                                    buttonPanel.add(okButton);

                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                    holder.add(noPanel);
                                    holder.add(buttonPanel);

                                    content.add("Unavailable", holder);
                                    cl.show(content, "Unavailable");

                                    okButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            cl.show(content, "Tutor Menu");
                                        }
                                    });
                                } else if (response.equals("Fail")) {
                                    JLabel noUser = new JLabel("You cannot message this person");
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    JPanel noPanel = new JPanel();
                                    noPanel.add(noUser);

                                    JButton okButton = new JButton("OK");
                                    okButton.setPreferredSize(new Dimension(151, 29));
                                    JPanel buttonPanel = new JPanel();
                                    buttonPanel.add(okButton);

                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                    holder.add(noPanel);
                                    holder.add(buttonPanel);

                                    content.add("Cannot Message", holder);
                                    cl.show(content, "Cannot Message");

                                    okButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            cl.show(content, "Tutor Menu");
                                        }
                                    });
                                } else if (response.contains("Unable")) {
                                    JLabel noUser = new JLabel(response);
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    JPanel noPanel = new JPanel();
                                    noPanel.add(noUser);

                                    JButton okButton = new JButton("OK");
                                    okButton.setPreferredSize(new Dimension(151, 29));
                                    JPanel buttonPanel = new JPanel();
                                    buttonPanel.add(okButton);

                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                    holder.add(noPanel);
                                    holder.add(buttonPanel);

                                    content.add("Unable to find", holder);
                                    cl.show(content, "Unable to find");

                                    okButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            cl.show(content, "Tutor Menu");
                                        }
                                    });
                                } else {
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

                                                content.add("Tutor Option: Read Message", jpS);
                                                cl.show(content, "Tutor Option: Read Message");
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

//                                                back.addActionListener(new ActionListener() {
//                                                    @Override
//                                                    public void actionPerformed(ActionEvent e) {
//                                                        cl.show(content, "Student Menu");
//                                                    }
//                                                });
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

                                    content.add("Tutor Message Menu", menuMessage);
                                    cl.show(content, "Tutor Message Menu");
                                    frame.setSize(317, 262);
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                } catch (Exception ex) {
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

    public void viewUserPage(String role) {
        try {
            writer.writeUTF("1");
            writer.flush();

            // read from server
            int size = Integer.parseInt(reader.readUTF());
            JPanel ListPanel = new JPanel(new GridLayout(0, 1));
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    String response = reader.readUTF();

                    JLabel Label = new JLabel(response);
                    Label.setHorizontalAlignment(SwingConstants.CENTER);
                    ListPanel.add(Label);
                }
                JButton okButton = new JButton("OK");
                okButton.setPreferredSize(new Dimension(151, 29));
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(okButton);
                ListPanel.add(buttonPanel);
                content.add("View " + role, ListPanel);
                cl.show(content, "View " + role);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(role.equals("Students")) {
                            cl.show(content, "Tutor Menu");
                        } else {
                            cl.show(content, "Student Menu");
                        }
                    }
                });
            }
            else {
                JLabel noUser = new JLabel("No " + role + " Available to Message");
                noUser.setHorizontalAlignment(SwingConstants.CENTER);
                JButton okButton = new JButton("OK");
                okButton.setPreferredSize(new Dimension(151, 29));
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(okButton);
                ListPanel.add(noUser);
                ListPanel.add(buttonPanel);
                content.add("View " + role + " Error", ListPanel);
                cl.show(content, "View " + role + " Error");

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(role.equals("Students")) {
                            cl.show(content, "Tutor Menu");
                        } else {
                            cl.show(content, "Student Menu");
                        }
                    }
                });
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
