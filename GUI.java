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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void createInterface() {
        JPanel welcomePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel holder = new JPanel(new GridLayout(0, 1));

        JLabel welcomeField = new JLabel("Welcome to Tutoring Center");
        welcomeField.setFont(new Font("Open Sans", Font.PLAIN, 18));

        welcomeField.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeField.setVerticalAlignment(SwingConstants.CENTER);

        Dimension size = welcomeField.getPreferredSize();
        welcomeField.setBounds(0, 0, size.width, size.height);

        JButton createAcc = new JButton("Create an Account");
        JButton signIn = new JButton("Log In");

        welcomePanel.add(welcomeField);
        buttonPanel.add(createAcc);
        buttonPanel.add(signIn);

        holder.add(welcomePanel);
        holder.add(buttonPanel);
        content.add("Welcome", holder);
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

                frame.setTitle("Messenger");
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

                                                JLabel messagePerson = new JLabel();

                                                JButton back = new JButton("Back");

                                                JPanel jpS = new JPanel();

                                                //                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));
                                                if(message.equals("0")){
                                                    JOptionPane.showMessageDialog(null, "No Messages Available", "No Messages Available", JOptionPane.ERROR_MESSAGE);
                                                } else {
                                                    messagePerson.setText(message);
                                                }

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

                                                JLabel messagePerson = new JLabel("What message do you want to send?");

                                                JTextField mP = new JTextField("Send Message");

                                                JButton jb = new JButton("Send Message");
                                                JButton back = new JButton("Back");

                                                JPanel jpS = new JPanel();

                                                //                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                                                jpS.add(messagePerson, BorderLayout.NORTH);
                                                jpS.add(mP);
                                                jpS.add(jb);
                                                jpS.add(back, BorderLayout.SOUTH);

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
                                                writer.flush();

                                                JLabel messagePerson = new JLabel("What message or line number do you want to delete?");

                                                JTextField mP = new JTextField("Delete Message");

                                                JButton delete = new JButton("Delete Message");

                                                JButton back = new JButton("Back");

                                                JPanel jpS = new JPanel();

                                                //                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                                                jpS.add(messagePerson, BorderLayout.NORTH);
                                                jpS.add(mP);
                                                jpS.add(delete);
                                                jpS.add(back, BorderLayout.SOUTH);

                                                content.add("Student Option: Delete Message", jpS);
                                                cl.show(content, "Student Option: Delete Message");
                                                frame.setSize(317, 262);

                                                delete.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(mP.getText());

                                                            if(reader.readUTF().equals("Message Deleted Successfully")){
                                                                JOptionPane.showMessageDialog(null, "Message Deleted Successfully", "Message Deleted", JOptionPane.INFORMATION_MESSAGE);
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    }
                                                });

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

                                    editMsg.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                writer.writeUTF("4");

                                                JLabel messagePerson = new JLabel("What message or line number do you want to edit?");

                                                JTextField oldMessage = new JTextField("Old Message");

                                                JTextField newMessage = new JTextField("New Message");

                                                JButton edit = new JButton("Edit Message");

                                                JButton back = new JButton("Back");

                                                JPanel jpS = new JPanel();

                                                //                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                                                jpS.add(messagePerson, BorderLayout.NORTH);
                                                jpS.add(oldMessage);
                                                jpS.add(newMessage);
                                                jpS.add(edit);
                                                jpS.add(back, BorderLayout.SOUTH);

                                                content.add("Student Option: Edit Message", jpS);
                                                cl.show(content, "Student Option: Edit Message");
                                                frame.setSize(317, 262);

                                                edit.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(oldMessage.getText());

                                                            writer.writeUTF(newMessage.getText());

                                                            if(reader.readUTF().equals("Message Edited Successfully")){
                                                                JOptionPane.showMessageDialog(null, "Message Edited Successfully", "Message Edited", JOptionPane.INFORMATION_MESSAGE);
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    }
                                                });

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

                                    searchMsg.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                writer.writeUTF("5");

                                                JLabel messagePerson = new JLabel("Enter a keyword to search for a message: ");

                                                JTextField keyword = new JTextField("Keyword");

                                                JButton search = new JButton("Search For Message");

                                                JButton back = new JButton("Back");

                                                JPanel jpS = new JPanel();

                                                //                    jpS.setLayout(new BoxLayout(jpS, BoxLayout.Y_AXIS));

                                                jpS.add(messagePerson, BorderLayout.NORTH);
                                                jpS.add(keyword);
                                                jpS.add(search);
                                                jpS.add(back, BorderLayout.SOUTH);

                                                content.add("Student Option: Search Message", jpS);
                                                cl.show(content, "Student Option: Search Message");
                                                frame.setSize(317, 262);

                                                search.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(keyword.getText());

                                                            String searchResult = reader.readUTF();

                                                            if(searchResult.equals("Message not found!")){
                                                                JOptionPane.showMessageDialog(null, "Message not found!", "Unable to Find Message", JOptionPane.ERROR_MESSAGE);
                                                            } else {
                                                                JLabel messagePerson = new JLabel(searchResult);
                                                                JButton searchAgain = new JButton("Search Again");
                                                                JButton backToMenu = new JButton("Back");

                                                                JPanel jIn = new JPanel();

                                                                jIn.add(messagePerson, BorderLayout.CENTER);
                                                                jIn.add(searchAgain, BorderLayout.SOUTH);
                                                                jIn.add(backToMenu, BorderLayout.SOUTH);

                                                                content.add("Student Option: Search Again", jIn);
                                                                cl.show(content, "Student Option: Search Again");
                                                                frame.setSize(317, 262);

                                                                backToMenu.addActionListener(new ActionListener() {
                                                                    @Override
                                                                    public void actionPerformed(ActionEvent e) {
                                                                        cl.show(content, "Student Message Menu");
                                                                    }
                                                                });

                                                                searchAgain.addActionListener(new ActionListener() {
                                                                    @Override
                                                                    public void actionPerformed(ActionEvent e) {
                                                                        try {
                                                                            writer.writeUTF("5");
                                                                            cl.show(content, "Student Option: Search Message");

                                                                        } catch (IOException ex) {
                                                                            throw new RuntimeException(ex);
                                                                        }
                                                                    }
                                                                });


                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    }
                                                });

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

                                    importC.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                writer.writeUTF("6");

                                                JLabel importFile = new JLabel("Please enter the filename");
                                                JTextField jt = new JTextField("File Name");
                                                JButton importF = new JButton("Import");
                                                JButton back = new JButton("Back");

                                                JPanel importPan = new JPanel();

                                                importPan.add(importFile, BorderLayout.NORTH);
                                                importPan.add(jt, BorderLayout.CENTER);
                                                importPan.add(importF, BorderLayout.CENTER);
                                                importPan.add(back, BorderLayout.SOUTH);

                                                content.add("Student Option: Import Conversation", importPan);
                                                cl.show(content, "Student Option: Import Conversation");
                                                frame.setSize(317, 262);

                                                importF.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(jt.getText());
                                                            JOptionPane.showMessageDialog(null, "Successfully Imported File", "Successfully Imported", JOptionPane.INFORMATION_MESSAGE);
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    }
                                                });

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

                                    exportC.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                writer.writeUTF("7");

                                                JLabel exportFile = new JLabel("Enter file name to export conversation into:");
                                                JTextField exportFileName = new JTextField("Export File Name");
                                                JButton export = new JButton("Export");
                                                JButton back = new JButton("Back");

                                                JPanel exportPan = new JPanel();

                                                exportPan.add(exportFile, BorderLayout.NORTH);
                                                exportPan.add(exportFileName, BorderLayout.CENTER);
                                                exportPan.add(export, BorderLayout.CENTER);
                                                exportPan.add(back, BorderLayout.SOUTH);

                                                content.add("Student Option: Export Conversation", exportPan);
                                                cl.show(content, "Student Option: Export Conversation");
                                                frame.setSize(317, 262);

                                                export.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(exportFileName.getText());
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }


                                                        JOptionPane.showMessageDialog(null, "Exported File Successfully", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                                                    }
                                                });


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

                                    quit.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                writer.writeUTF("8");

                                                cl.show(content, "Student Menu");
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
                    editProfilePage("Student");
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

                    changeBlockedStatus("Which user would you like to block?", "Block User", "Student");
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

                    changeBlockedStatus("Which user would you like to unblock?", "Unblock User", "Student");
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

                    invisiblePage("Student");
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

                    thankYouPage();
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
                    editProfilePage("Tutor");
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

                    changeBlockedStatus("Which user would you like to block?", "Block User", "Tutor");
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

                    changeBlockedStatus("Which user would you like to unblock?", "Unblock User", "Tutor");
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

                    invisiblePage("Tutor");
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

                    thankYouPage();
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

    public void changed(String response, String user) {
        JPanel messagePanel = new JPanel();
        JLabel message;
        if (response.equals("Success")) {
            message = new JLabel("Changed Successfully");
        } else {
            message = new JLabel("Failed!");
        }
        messagePanel.add(message);
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(151, 29));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(messagePanel);
        holder.add(buttonPanel);

        content.add("Edit Profile Response", holder);
        cl.show(content, "Edit Profile Response");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(content, user + " Menu");
            }
        });
    }

    public void changeProfile(String action, String user) {
        JLabel prompt = new JLabel("Enter new " + action);
        JTextField pass = new JTextField("");

        JPanel promptPanel = new JPanel();
        promptPanel.add(prompt);
        promptPanel.add(pass);

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(151, 29));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(promptPanel);
        holder.add(buttonPanel);

        content.add("Change " + action, holder);
        cl.show(content, "Change " + action);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF(pass.getText());
                    writer.flush();

                    String response = reader.readUTF();
                    changed(response, user);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void changedVisibility(String response, String panelName, String role) {
        JPanel responsePanel = new JPanel();
        JLabel responseLabel = new JLabel(response);
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        responsePanel.add(responseLabel);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(151, 29));

        buttonPanel.add(okButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(responsePanel);
        holder.add(buttonPanel);

        content.add(panelName, holder);
        cl.show(content, panelName);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(content, role + " Menu");
            }
        });
    }

    public void changeBlockedStatus(String label, String panelName, String role) {
        JPanel promptPanel = new JPanel();
        JLabel prompt = new JLabel(label);
        JTextField answer = new JTextField("");
        promptPanel.add(prompt);
        promptPanel.add(answer);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(151, 29));
        buttonPanel.add(submitButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(promptPanel);
        holder.add(buttonPanel);

        content.add(panelName, holder);
        cl.show(content, panelName);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF(answer.getText());
                    writer.flush();

                    String response = reader.readUTF();
                    if (response.contains("Cannot")) {
                        changedVisibility(response, "Block yourself", role);
                    } else if (response.contains("no")) {
                        changedVisibility(response, "No Blocked User", role);
                    } else {
                        changedVisibility(response, "Successful Block", role);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void editProfilePage(String role) {
        JMenuItem changePwd = new JMenuItem("Change Password");
        JMenuItem changeUsername = new JMenuItem("Change Username");
        JMenuItem changeEmail = new JMenuItem("Change Email");
        JMenuItem delAcc = new JMenuItem("Delete Account");
        JMenuItem changeFilter = new JMenuItem("Change Filter");
        JMenuItem changeFilterWords = new JMenuItem("Change Filter Words");
        JMenuItem displayFilterWords = new JMenuItem("Display Filter Words");
        JMenuItem exit = new JMenuItem("Exit");

        JPanel editProfileMenu = new JPanel();
        editProfileMenu.setLayout(new BoxLayout(editProfileMenu, BoxLayout.Y_AXIS));

        editProfileMenu.add(changePwd);
        editProfileMenu.add(changeUsername);
        editProfileMenu.add(changeEmail);
        editProfileMenu.add(delAcc);
        editProfileMenu.add(changeFilter);
        editProfileMenu.add(changeFilterWords);
        editProfileMenu.add(displayFilterWords);
        editProfileMenu.add(exit);

        content.add("Edit Profile Menu", editProfileMenu);
        cl.show(content, "Edit Profile Menu");

        changePwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("1");
                    writer.flush();
                    changeProfile("Password",role);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        changeUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("2");
                    writer.flush();
                    changeProfile("Username", role);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        changeEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("3");
                    writer.flush();
                    changeProfile("Email", role);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        delAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("4");
                    writer.flush();
                    thankYouPage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        changeFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("5");
                    writer.flush();
                    changed("Filter", role);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        changeFilterWords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("6");
                    writer.flush();

                    JPanel menu = new JPanel();
                    menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

                    JMenuItem addWord = new JMenuItem("Add Words");
                    JMenuItem removeWord = new JMenuItem("Remove Words");
                    JMenuItem back = new JMenuItem("Back");

                    menu.add(addWord);
                    menu.add(removeWord);
                    menu.add(back);

                    content.add("Change Filter Word Page", menu);
                    cl.show(content, "Change Filter Word Page");

                    addWord.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF("1");
                                writer.flush();

                                JPanel promptPanel = new JPanel();
                                JLabel prompt = new JLabel("Enter words to be added [Comma separated]");
                                JTextField answer = new JTextField("");
                                promptPanel.add(prompt);
                                promptPanel.add(answer);

                                JPanel buttonPanel = new JPanel();
                                JButton submitButton = new JButton("Submit");
                                buttonPanel.add(submitButton);

                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                holder.add(promptPanel);
                                holder.add(buttonPanel);

                                content.add("Add Word", holder);
                                cl.show(content, "Add Word");

                                submitButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF(answer.getText());
                                            writer.flush();
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                });


                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });

                    removeWord.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF("2");
                                writer.flush();

                                JPanel promptPanel = new JPanel();
                                JLabel prompt = new JLabel("Enter words to be deleted [Comma separated]");
                                JTextField answer = new JTextField("");
                                promptPanel.add(prompt);
                                promptPanel.add(answer);

                                JPanel buttonPanel = new JPanel();
                                JButton submitButton = new JButton("Submit");
                                submitButton.setPreferredSize(new Dimension(151, 29));
                                buttonPanel.add(submitButton);

                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                holder.add(promptPanel);
                                holder.add(buttonPanel);

                                content.add("Delete Word", holder);
                                cl.show(content, "Delete Word");

                                submitButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            writer.writeUTF(answer.getText());
                                            writer.flush();
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                });


                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });

                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                writer.writeUTF("3");
                                writer.flush();

                                cl.show(content, "Edit Profile Menu");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        displayFilterWords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("7");
                    writer.flush();

                    JPanel censorPanel = new JPanel(new GridLayout(0, 1));
                    int size = Integer.parseInt(reader.readUTF());
                    for (int i = 0; i < size; i++) {
                        JLabel censorWord = new JLabel(reader.readUTF());
                        censorWord.setHorizontalAlignment(SwingConstants.CENTER);
                        censorPanel.add(censorWord);
                    }

                    JButton okButton = new JButton("OK");
                    okButton.setPreferredSize(new Dimension(151, 29));

                    content.add("Censor List", censorPanel);
                    cl.show(content, "Censor List");

                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cl.show(content, role + " Menu");
                        }
                    });
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF("8");
                    writer.flush();

                    cl.show(content, role + " Menu");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void invisiblePage(String role) throws IOException {
        String response = reader.readUTF();

        JPanel promptPanel = new JPanel();
        JLabel prompt = new JLabel(response);
        JTextField answer = new JTextField("");
        promptPanel.add(prompt);
        promptPanel.add(answer);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(151, 29));
        buttonPanel.add(submitButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(promptPanel);
        holder.add(buttonPanel);

        content.add("Invisible User", holder);
        cl.show(content, "Invisible User");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.writeUTF(answer.getText());
                    writer.flush();

                    String response = reader.readUTF();
                    if (response.equals("There are no invisible users")) {
                        changedVisibility(response, "No Invisible Users", role);
                    } else {
                        changedVisibility(response, "Successful Invisibility", role);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void thankYouPage() {
        JPanel thankYouPanel = new JPanel(new GridLayout(0, 1));
        JLabel thankYou = new JLabel("Have a nice day!");
        thankYou.setHorizontalAlignment(SwingConstants.CENTER);
        thankYou.setVerticalAlignment(SwingConstants.CENTER);
        thankYou.setFont(new Font("Open Sans", Font.PLAIN, 18));

        thankYouPanel.add(thankYou);

        frame.setTitle("Thank You");
        content.add("Thank You", thankYouPanel);
        cl.show(content, "Thank You");
    }

}
