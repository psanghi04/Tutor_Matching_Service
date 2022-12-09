import javax.swing.*;
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
        this.reader = reader;
        this.writer = writer;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void createInterface() {
        JPanel welcomePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel holder = new JPanel(new GridLayout(0, 1));

        JLabel welcomeField = new JLabel("Welcome to Tutoring Center");
        welcomeField.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeField.setVerticalAlignment(SwingConstants.CENTER);
        welcomeField.setFont(new Font("Open Sans", Font.PLAIN, 18));

        JButton createAcc = new JButton("Create an Account");
        createAcc.setPreferredSize(new Dimension(151, 29));
        JButton signIn = new JButton("Log In");
        signIn.setPreferredSize(new Dimension(151, 29));

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
                try {
                    writer.writeUTF("1");
                    writer.flush();
                } catch (Exception ex) {
                    System.out.println("error");
                }

                frame.setTitle("Messenger");
                JPanel buttonPanel = new JPanel();
                JPanel promptPanel = new JPanel();
                JPanel holder = new JPanel(new GridLayout(2, 1));

                JButton studentButton = new JButton("Student");
                studentButton.setPreferredSize(new Dimension(151, 29));
                JButton tutorButton = new JButton("Tutor");
                tutorButton.setPreferredSize(new Dimension(151, 29));

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

                frame.setTitle("Messenger");
                JPanel usernamePanel = new JPanel();
                JPanel pwdPanel = new JPanel();
                JPanel holder = new JPanel(new GridLayout(3, 1));
                JPanel buttonPanel = new JPanel();

                JButton loginButton = new JButton("Login");
                loginButton.setPreferredSize(new Dimension(151, 29));

                JLabel usernamePrompt = new JLabel("Username:");
                JTextField usernameField = new JTextField(10);
                usernameField.setText("");

                JLabel passwordPrompt = new JLabel("Password:");
                JPasswordField passwordField = new JPasswordField(10);
                passwordField.setText("");

                usernamePanel.add(usernamePrompt);
                usernamePanel.add(usernameField);

                pwdPanel.add(passwordPrompt);
                pwdPanel.add(passwordField);

                buttonPanel.add(loginButton);

                holder.add(usernamePanel);
                holder.add(pwdPanel);

                holder.add(buttonPanel);

                content.add("Login Page", holder);
                cl.show(content, "Login Page");
                frame.pack();
                frame.setVisible(true);

                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String passwd = String.valueOf(passwordField.getPassword());
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
                                errorMsg.setVerticalAlignment(SwingConstants.CENTER);

                                JButton tryAgain = new JButton("Try Again");
                                tryAgain.setPreferredSize(new Dimension(151, 29));
                                JButton createButton = new JButton("Create Account");
                                createButton.setPreferredSize(new Dimension(151, 29));
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
        createButton.setPreferredSize(new Dimension(151, 29));

        JLabel usernamePrompt = new JLabel("Username:");
        JTextField usernameField = new JTextField(10);
        usernameField.setText("");

        JLabel emailPrompt = new JLabel("Email:");
        JTextField emailField = new JTextField(10);
        emailField.setText("");

        JLabel passwordPrompt = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(10);

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

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String passwd = String.valueOf(passwordField.getPassword());

                try {
                    writer.writeUTF(username);
                    writer.writeUTF(email);
                    writer.writeUTF(passwd);

                    if (reader.readUTF().equals("Sorry")) {
                        throw new IOException("Sorry! You have the same username/email as another user");
                    }

                    if (role == 'T') {
                        String subjects = subField.getText();
                        String price = priceField.getText();
                        writer.writeUTF(subjects);
                        writer.writeUTF(price);
                    }

                    writer.flush();

                    if (role == 'T') {
                        tutorMenu();
                    } else {
                        studentMenu();
                    }
                } catch (IOException ex) {
                    String label1 = ex.getMessage().split("/")[0];
                    String label2 = "or " + ex.getMessage().split("/")[1];
                    JLabel errorMsg1 = new JLabel(label1);
                    JLabel errorMsg2 = new JLabel(label2);
                    errorMsg1.setHorizontalAlignment(SwingConstants.CENTER);
                    errorMsg1.setVerticalAlignment(SwingConstants.CENTER);
                    errorMsg1.setFont(new Font("Open Sans", Font.PLAIN, 16));

                    errorMsg2.setHorizontalAlignment(SwingConstants.CENTER);
                    errorMsg2.setVerticalAlignment(SwingConstants.CENTER);
                    errorMsg2.setFont(new Font("Open Sans", Font.PLAIN, 16));

                    JPanel errorPanel = new JPanel(new GridLayout(0, 1));
                    errorPanel.add(errorMsg1);
                    errorPanel.add(errorMsg2);

                    JButton tryAgainButton = new JButton("Try Again");
                    tryAgainButton.setPreferredSize(new Dimension(151, 29));
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(tryAgainButton);

                    JPanel holder = new JPanel(new GridLayout(0, 1));
                    holder.add(errorPanel);
                    holder.add(buttonPanel);

                    content.add("Error Login", holder);
                    cl.show(content, "Error Login");

                    tryAgainButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cl.show(content, "Welcome");
                        }
                    });
                }
            }
        });
    }

    public void studentMenu() {
        JMenuItem view = new JMenuItem("View Tutors");
        JMenuItem msg = new JMenuItem("Message a Tutor");
        JMenuItem edit = new JMenuItem("Edit Profile");
        JMenuItem blk = new JMenuItem("Block a Tutor");
        JMenuItem ublk = new JMenuItem("Unblock a Tutor");
        JMenuItem inv = new JMenuItem("Become Invisible to a Tutor");
        JMenuItem exit = new JMenuItem("Sign Out");
        exit.setBackground(new Color(255,0, 0 ));

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
                    prompt.setHorizontalAlignment(SwingConstants.CENTER);
                    prompt.setVerticalAlignment(SwingConstants.CENTER);

                    JTextField username = new JTextField("", 10);
                    JPanel promptPanel = new JPanel();

                    promptPanel.add(prompt);
                    promptPanel.add(username);

                    JButton submitButton = new JButton("Submit");
                    submitButton.setPreferredSize(new Dimension(151, 29));
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
                                String user = username.getText();
                                writer.writeUTF(user);
                                writer.flush();

                                String response = reader.readUTF();

                                if (response.equals("0")) {
                                    JLabel noUser = new JLabel("There are no tutors available");
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    noUser.setVerticalAlignment(SwingConstants.CENTER);

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
                                } else if (response.contains("Unable")) {
                                    JLabel noUser = new JLabel(response);
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    noUser.setVerticalAlignment(SwingConstants.CENTER);
                                    noUser.setFont(new Font("Open Sans", Font.PLAIN, 15));
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
                                    quit.setBackground(new Color(255, 0, 0));
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
                                                writer.flush();
                                                String message = reader.readUTF();

                                                if (message.equals("0")) {
                                                    errorPage("Student", "No Messages Available");
                                                } else {
                                                    String[] messages = reader.readUTF().split(";");
                                                    JPanel displayMsg = new JPanel(new GridLayout(0, 1));
                                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                                    for (String line : messages) {
                                                        JLabel label = new JLabel(line);
                                                        if (line.contains(user)) {
                                                            label.setHorizontalAlignment(SwingConstants.LEFT);
                                                        } else {
                                                            label.setHorizontalAlignment(SwingConstants.RIGHT);
                                                        }
                                                        displayMsg.add(label);
                                                    }

                                                    JScrollPane scrollPane = new JScrollPane();
                                                    scrollPane.setViewportView(displayMsg);
                                                    scrollPane.setAutoscrolls(true);

                                                    JButton okButton = new JButton("OK");
                                                    okButton.setPreferredSize(new Dimension(151, 29));
                                                    JPanel buttonPanel = new JPanel();
                                                    buttonPanel.add(okButton);

                                                    holder.add(scrollPane);
                                                    holder.add(buttonPanel);

                                                    frame.setSize(317, 262);

                                                    content.add("Read Message", holder);
                                                    cl.show(content, "Read Message");

                                                    okButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            cl.show(content, "Student Message Menu");
                                                        }
                                                    });
                                                }
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
                                                writer.flush();

                                                JPanel writeMsgPanel = new JPanel();
                                                JLabel messagePerson = new JLabel("What message do you want to send?");
                                                JTextField message = new JTextField("", 10);

                                                JPanel buttonPanel = new JPanel();
                                                JButton sendButton = new JButton("Send");
                                                sendButton.setPreferredSize(new Dimension(151, 29));

                                                writeMsgPanel.add(messagePerson);
                                                writeMsgPanel.add(message);
                                                buttonPanel.add(sendButton);

                                                JPanel holder = new JPanel(new GridLayout(2, 1));
                                                holder.add(writeMsgPanel);
                                                holder.add(sendButton);

                                                content.add("Write Message", holder);
                                                cl.show(content, "Write Message");

                                                sendButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(message.getText());
                                                            writer.flush();

                                                            successPage("Student", "Written Successfully");

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
                                                writer.flush();

                                                JLabel messagePerson = new JLabel("Which message do you want to delete?");
                                                JTextField delMsg = new JTextField("", 10);

                                                JButton deleteButton = new JButton("Delete");
                                                deleteButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel promptPanel = new JPanel();
                                                promptPanel.add(messagePerson);
                                                promptPanel.add(delMsg);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(deleteButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(promptPanel);
                                                holder.add(buttonPanel);

                                                content.add("Delete Message", holder);
                                                cl.show(content, "Delete Message");

                                                deleteButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(delMsg.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();

                                                            if (response.equals("Message Deleted Successfully")) {
                                                                successPage("Student", "Deleted Successfully");
                                                            } else {
                                                                errorPage("Student", "Message Not Found");
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

                                                int size = Integer.parseInt(reader.readUTF());

                                                if (size != 0) {
                                                    JLabel messagePerson = new JLabel("Which message do you want to edit?");

                                                    JLabel oldLabel = new JLabel("Old");
                                                    JLabel newLabel = new JLabel("New");

                                                    JTextField oldMessage = new JTextField("", 10);
                                                    JTextField newMessage = new JTextField("", 10);

                                                    JButton editButton = new JButton("Edit");
                                                    editButton.setPreferredSize(new Dimension(151, 29));

                                                    JPanel oldPanel = new JPanel();
                                                    oldPanel.add(oldLabel);
                                                    oldPanel.add(oldMessage);

                                                    JPanel newPanel = new JPanel();
                                                    newPanel.add(newLabel);
                                                    newPanel.add(newMessage);

                                                    JPanel promptPanel = new JPanel(new GridLayout(0, 1));
                                                    promptPanel.add(messagePerson);
                                                    promptPanel.add(oldPanel);
                                                    promptPanel.add(newPanel);

                                                    JPanel buttonPanel = new JPanel();
                                                    buttonPanel.add(editButton);

                                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                                    holder.add(promptPanel);
                                                    holder.add(buttonPanel);

                                                    content.add("Edit Message", holder);
                                                    cl.show(content, "Edit Message");

                                                    editButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            try {
                                                                writer.writeUTF(oldMessage.getText());
                                                                writer.flush();

                                                                writer.writeUTF(newMessage.getText());
                                                                writer.flush();

                                                                if (reader.readUTF().equals("Message Edited Successfully")) {
                                                                    successPage("Student", "Edited Successfully");
                                                                } else {
                                                                    errorPage("Student", "Message Not Found");
                                                                }
                                                            } catch (IOException ex) {
                                                                throw new RuntimeException(ex);
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    errorPage("Student", "There are no messages");
                                                }

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
                                                writer.flush();

                                                JLabel messagePerson = new JLabel("Enter a keyword to search for a message");
                                                JTextField keyword = new JTextField("", 10);

                                                JButton searchButton = new JButton("Search");
                                                searchButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel promptPanel = new JPanel();
                                                promptPanel.add(messagePerson);
                                                promptPanel.add(keyword);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(searchButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(promptPanel);
                                                holder.add(buttonPanel);

                                                content.add("Search Message", holder);
                                                cl.show(content, "Search Message");

                                                searchButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(keyword.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();
                                                            if (response.equals("Message not found!")) {
                                                                errorPage("Student", response);
                                                            } else {

                                                                int size = Integer.parseInt(reader.readUTF());

                                                                JPanel resultPanel = new JPanel(new GridLayout(0, 1));
                                                                for (int i = 0; i < size; i++) {
                                                                    String searchResult = reader.readUTF();

                                                                    JLabel display = new JLabel(searchResult);
                                                                    resultPanel.add(display);
                                                                }

                                                                JButton okButton = new JButton("OK");
                                                                okButton.setPreferredSize(new Dimension(151, 29));

                                                                JPanel buttonPanel = new JPanel();
                                                                buttonPanel.add(okButton);

                                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                                holder.add(resultPanel);
                                                                holder.add(buttonPanel);

                                                                content.add("Searched Message", holder);
                                                                cl.show(content, "Searched Message");

                                                                okButton.addActionListener(new ActionListener() {
                                                                    @Override
                                                                    public void actionPerformed(ActionEvent e) {
                                                                        cl.show(content, "Student Message Menu");
                                                                    }
                                                                });
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

                                                JLabel importFile = new JLabel("Please enter the filename");
                                                JTextField filename = new JTextField("", 10);

                                                JButton importButton = new JButton("Import");
                                                importButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel importPanel = new JPanel();
                                                importPanel.add(importFile);
                                                importPanel.add(filename);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(importButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(importPanel);
                                                holder.add(buttonPanel);

                                                content.add("Import Conversation", holder);
                                                cl.show(content, "Import Conversation");

                                                importButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(filename.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();
                                                            if (response.equals("Success")) {
                                                                successPage("Student", "Imported Successfully");
                                                            } else {
                                                                errorPage("Student", "Cannot find/read the file");
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

                                                JLabel exportFile = new JLabel("Enter file name to export conversation into:");
                                                JTextField exportFileName = new JTextField("", 10);

                                                JButton exportButton = new JButton("Export");
                                                exportButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel exportPanel = new JPanel();
                                                exportPanel.add(exportFile);
                                                exportPanel.add(exportFileName);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(exportButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(exportPanel);
                                                holder.add(buttonPanel);

                                                content.add("Export Conversation", holder);
                                                cl.show(content, "Export Conversation");

                                                exportButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(exportFileName.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();
                                                            if (response.equals("Success")) {
                                                                successPage("Student", "Exported Successfully");
                                                            } else {
                                                                errorPage("Student", "Unable to export file");
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

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
        JMenuItem view = new JMenuItem("View Students");
        JMenuItem msg = new JMenuItem("Message a Student");
        JMenuItem edit = new JMenuItem("Edit Profile");
        JMenuItem blk = new JMenuItem("Block a Student");
        JMenuItem ublk = new JMenuItem("Unblock a Student");
        JMenuItem inv = new JMenuItem("Become Invisible to a Student");
        JMenuItem exit = new JMenuItem("Sign Out");
        exit.setBackground(new Color(255,0, 0 ));

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
                    prompt.setHorizontalAlignment(SwingConstants.CENTER);
                    prompt.setVerticalAlignment(SwingConstants.CENTER);

                    JTextField username = new JTextField("", 10);
                    JPanel promptPanel = new JPanel();

                    promptPanel.add(prompt);
                    promptPanel.add(username);

                    JButton submitButton = new JButton("Submit");
                    submitButton.setPreferredSize(new Dimension(151, 29));
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
                                String user = username.getText();
                                writer.writeUTF(user);
                                writer.flush();

                                String response = reader.readUTF();

                                if (response.equals("0")) {
                                    JLabel noUser = new JLabel("There are no students available");
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    noUser.setVerticalAlignment(SwingConstants.CENTER);

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
                                } else if (response.contains("Unable")) {
                                    JLabel noUser = new JLabel(response);
                                    noUser.setHorizontalAlignment(SwingConstants.CENTER);
                                    noUser.setVerticalAlignment(SwingConstants.CENTER);
                                    noUser.setFont(new Font("Open Sans", Font.PLAIN, 15));
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
                                    System.out.println(response);
                                    JMenuItem readMsg = new JMenuItem("Read message");
                                    JMenuItem writeMsg = new JMenuItem("Write message");
                                    JMenuItem deleteMsg = new JMenuItem("Delete message");
                                    JMenuItem editMsg = new JMenuItem("Edit message");
                                    JMenuItem searchMsg = new JMenuItem("Search message");
                                    JMenuItem importC = new JMenuItem("Import conversation");
                                    JMenuItem exportC = new JMenuItem("Export conversation");
                                    JMenuItem quit = new JMenuItem("Quit");
                                    quit.setBackground(new Color(255, 0, 0));
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

                                    content.add("Tutor Message Menu", menuMessage);
                                    cl.show(content, "Tutor Message Menu");
                                    frame.setSize(317, 262);

                                    readMsg.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                writer.writeUTF("1");
                                                writer.flush();
                                                String message = reader.readUTF();

                                                if (message.equals("0")) {
                                                    errorPage("Tutor", "No Messages Available");
                                                } else {
                                                    String[] messages = reader.readUTF().split(";");
                                                    JPanel displayMsg = new JPanel();
                                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                                    displayMsg.setLayout(new GridLayout(0, 1));
                                                    for (String line : messages) {
                                                        JLabel label = new JLabel(line);
                                                        if (line.contains(user)) {
                                                            label.setHorizontalAlignment(SwingConstants.LEFT);
                                                        } else {
                                                            label.setHorizontalAlignment(SwingConstants.RIGHT);
                                                        }
                                                        displayMsg.add(label);
                                                    }

                                                    JScrollPane scrollPane = new JScrollPane();
                                                    scrollPane.setViewportView(displayMsg);
                                                    scrollPane.setAutoscrolls(true);

                                                    JButton okButton = new JButton("OK");
                                                    okButton.setPreferredSize(new Dimension(151, 29));
                                                    JPanel buttonPanel = new JPanel();
                                                    buttonPanel.add(okButton);

                                                    holder.add(scrollPane);
                                                    holder.add(buttonPanel);

                                                    frame.setSize(317, 262);

                                                    content.add("Read Message", holder);
                                                    cl.show(content, "Read Message");

                                                    okButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            cl.show(content, "Tutor Message Menu");
                                                        }
                                                    });
                                                }
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
                                                writer.flush();

                                                JPanel writeMsgPanel = new JPanel();
                                                JLabel messagePerson = new JLabel("What message do you want to send?");
                                                JTextField message = new JTextField("", 10);

                                                JPanel buttonPanel = new JPanel();
                                                JButton sendButton = new JButton("Send");
                                                sendButton.setPreferredSize(new Dimension(151, 29));

                                                writeMsgPanel.add(messagePerson);
                                                writeMsgPanel.add(message);
                                                buttonPanel.add(sendButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(writeMsgPanel);
                                                holder.add(sendButton);

                                                content.add("Write Message", holder);
                                                cl.show(content, "Write Message");

                                                sendButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(message.getText());
                                                            writer.flush();

                                                            successPage("Tutor", "Written Successfully");

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
                                                writer.flush();

                                                JLabel messagePerson = new JLabel("Which message do you want to delete?");
                                                JTextField delMsg = new JTextField("", 10);

                                                JButton deleteButton = new JButton("Delete");
                                                deleteButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel promptPanel = new JPanel();
                                                promptPanel.add(messagePerson);
                                                promptPanel.add(delMsg);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(deleteButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(promptPanel);
                                                holder.add(buttonPanel);

                                                content.add("Delete Message", holder);
                                                cl.show(content, "Delete Message");

                                                deleteButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(delMsg.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();

                                                            if (response.equals("Message Deleted Successfully")) {
                                                                successPage("Tutor", "Deleted Successfully");
                                                            } else {
                                                                errorPage("Tutor", "Message Not Found");
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

                                                int size = Integer.parseInt(reader.readUTF());

                                                if (size != 0) {
                                                    JLabel messagePerson = new JLabel("Which message do you want to edit?");

                                                    JLabel oldLabel = new JLabel("Old");
                                                    JLabel newLabel = new JLabel("New");

                                                    JTextField oldMessage = new JTextField("", 10);
                                                    JTextField newMessage = new JTextField("", 10);

                                                    JButton editButton = new JButton("Edit");
                                                    editButton.setPreferredSize(new Dimension(151, 29));

                                                    JPanel oldPanel = new JPanel();
                                                    oldPanel.add(oldLabel);
                                                    oldPanel.add(oldMessage);

                                                    JPanel newPanel = new JPanel();
                                                    newPanel.add(newLabel);
                                                    newPanel.add(newMessage);

                                                    JPanel promptPanel = new JPanel(new GridLayout(0, 1));
                                                    promptPanel.add(messagePerson);
                                                    promptPanel.add(oldPanel);
                                                    promptPanel.add(newPanel);

                                                    JPanel buttonPanel = new JPanel();
                                                    buttonPanel.add(editButton);

                                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                                    holder.add(promptPanel);
                                                    holder.add(buttonPanel);

                                                    content.add("Edit Message", holder);
                                                    cl.show(content, "Edit Message");

                                                    editButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            try {
                                                                writer.writeUTF(oldMessage.getText());
                                                                writer.flush();

                                                                writer.writeUTF(newMessage.getText());
                                                                writer.flush();

                                                                if (reader.readUTF().equals("Message Edited Successfully")) {
                                                                    successPage("Tutor", "Edited Successfully");
                                                                } else {
                                                                    errorPage("Tutor", "Message Not Found");
                                                                }
                                                            } catch (IOException ex) {
                                                                throw new RuntimeException(ex);
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    errorPage("Tutor", "There are no messages");
                                                }

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

                                    searchMsg.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                try {
                                                    writer.writeUTF("5");
                                                    writer.flush();

                                                    JLabel messagePerson = new JLabel("Enter a keyword to search for a message");
                                                    JTextField keyword = new JTextField("", 10);

                                                    JButton searchButton = new JButton("Search");
                                                    searchButton.setPreferredSize(new Dimension(151, 29));

                                                    JPanel promptPanel = new JPanel();
                                                    promptPanel.add(messagePerson);
                                                    promptPanel.add(keyword);

                                                    JPanel buttonPanel = new JPanel();
                                                    buttonPanel.add(searchButton);

                                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                                    holder.add(promptPanel);
                                                    holder.add(buttonPanel);

                                                    content.add("Search Message", holder);
                                                    cl.show(content, "Search Message");

                                                    searchButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            try {
                                                                writer.writeUTF(keyword.getText());
                                                                writer.flush();

                                                                String response = reader.readUTF();
                                                                if (response.equals("Message not found!")) {
                                                                    errorPage("Tutor", response);
                                                                } else {

                                                                    int size = Integer.parseInt(reader.readUTF());

                                                                    JPanel resultPanel = new JPanel(new GridLayout(0, 1));
                                                                    for (int i = 0; i < size; i++) {
                                                                        String searchResult = reader.readUTF();

                                                                        JLabel display = new JLabel(searchResult);
                                                                        resultPanel.add(display);
                                                                    }

                                                                    JButton okButton = new JButton("OK");
                                                                    okButton.setPreferredSize(new Dimension(151, 29));

                                                                    JPanel buttonPanel = new JPanel();
                                                                    buttonPanel.add(okButton);

                                                                    JPanel holder = new JPanel(new GridLayout(0, 1));
                                                                    holder.add(resultPanel);
                                                                    holder.add(buttonPanel);

                                                                    content.add("Searched Message", holder);
                                                                    cl.show(content, "Searched Message");

                                                                    okButton.addActionListener(new ActionListener() {
                                                                        @Override
                                                                        public void actionPerformed(ActionEvent e) {
                                                                            cl.show(content, "Tutor Message Menu");
                                                                        }
                                                                    });
                                                                }
                                                            } catch (IOException ex) {
                                                                throw new RuntimeException(ex);
                                                            }
                                                        }
                                                    });
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
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
                                                writer.flush();

                                                JLabel importFile = new JLabel("Please enter the filename");
                                                JTextField filename = new JTextField("", 10);

                                                JButton importButton = new JButton("Import");
                                                importButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel importPanel = new JPanel();
                                                importPanel.add(importFile);
                                                importPanel.add(filename);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(importButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(importPanel);
                                                holder.add(buttonPanel);

                                                content.add("Import Conversation", holder);
                                                cl.show(content, "Import Conversation");

                                                importButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(filename.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();
                                                            if (response.equals("Success")) {
                                                                successPage("Tutor", "Imported Successfully");
                                                            } else {
                                                                errorPage("Tutor", "Cannot find/read the file");
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

                                                JLabel exportFile = new JLabel("Enter file name to export conversation into:");
                                                JTextField exportFileName = new JTextField("", 10);

                                                JButton exportButton = new JButton("Export");
                                                exportButton.setPreferredSize(new Dimension(151, 29));

                                                JPanel exportPanel = new JPanel();
                                                exportPanel.add(exportFile);
                                                exportPanel.add(exportFileName);

                                                JPanel buttonPanel = new JPanel();
                                                buttonPanel.add(exportButton);

                                                JPanel holder = new JPanel(new GridLayout(0, 1));
                                                holder.add(exportPanel);
                                                holder.add(buttonPanel);

                                                content.add("Export Conversation", holder);
                                                cl.show(content, "Export Conversation");

                                                exportButton.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        try {
                                                            writer.writeUTF(exportFileName.getText());
                                                            writer.flush();

                                                            String response = reader.readUTF();
                                                            if (response.equals("Success")) {
                                                                successPage("Tutor", "Exported Successfully");
                                                            } else {
                                                                errorPage("Tutor", "Unable to export file");
                                                            }
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
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
                                                writer.flush();

                                                cl.show(content, "Tutor Menu");
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
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

            int size = Integer.parseInt(reader.readUTF());
            JPanel ListPanel = new JPanel(new GridLayout(0, 1));
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    String response = reader.readUTF();

                    JLabel Label = new JLabel((i + 1) + ". " + response);
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
                        if (role.equals("Tutors")) {
                            cl.show(content, "Student Menu");
                        } else {
                            cl.show(content, "Tutor Menu");
                        }
                    }
                });
            } else {
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
                        if (role.equals("Tutors")) {
                            cl.show(content, "Student Menu");
                        } else {
                            cl.show(content, "Tutor Menu");
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
        JTextField text = new JTextField("", 10);

        JPanel promptPanel = new JPanel();
        promptPanel.add(prompt);
        promptPanel.add(text);

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
                    writer.writeUTF(text.getText());
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
        responseLabel.setVerticalAlignment(SwingConstants.CENTER);
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
        JTextField answer = new JTextField("", 10);
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
                    } else if (response.contains("not")) {
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
        exit.setBackground(new Color(255,0, 0 ));

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
                    changeProfile("Password", role);
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
                    changeProfile("Filter", role);
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
                                JTextField answer = new JTextField("", 10);
                                promptPanel.add(prompt);
                                promptPanel.add(answer);

                                JPanel buttonPanel = new JPanel();
                                JButton submitButton = new JButton("Submit");
                                submitButton.setPreferredSize(new Dimension(151, 29));
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

                                            cl.show(content, role + " Menu");
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
                                JTextField answer = new JTextField("", 10);
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
                                            changed("Success", role);
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
                    if (size == 0) {
                        JLabel censorWord = new JLabel("No Words being Filtered");
                        censorWord.setHorizontalAlignment(SwingConstants.CENTER);
                        censorPanel.add(censorWord);
                    } else {
                        for (int i = 0; i < size; i++) {
                            JLabel censorWord = new JLabel(reader.readUTF());
                            censorWord.setHorizontalAlignment(SwingConstants.CENTER);
                            censorPanel.add(censorWord);
                        }
                    }

                    JButton okButton = new JButton("OK");
                    okButton.setPreferredSize(new Dimension(151, 29));

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(okButton);

                    JPanel holder = new JPanel(new GridLayout(0, 1));
                    holder.add(censorPanel);
                    holder.add(buttonPanel);

                    content.add("Censor List", holder);
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
        JPanel promptPanel = new JPanel();
        JLabel prompt = new JLabel("Who would you like to become invisible to?");
        JTextField answer = new JTextField("", 10);
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
                    if (response.equals("User not found")) {
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

    public void successPage(String role, String label) {
        JPanel successPanel = new JPanel();
        JLabel success = new JLabel(label);
        successPanel.add(success);

        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(151, 29));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(successPanel);
        holder.add(buttonPanel);

        content.add("Success Message", holder);
        cl.show(content, "Success Message");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(content, role + " Message Menu");
            }
        });
    }

    public void errorPage(String role, String label) {
        JPanel errorPanel = new JPanel();
        JLabel error = new JLabel(label);
        errorPanel.add(error);

        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(151, 29));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        JPanel holder = new JPanel(new GridLayout(0, 1));
        holder.add(errorPanel);
        holder.add(buttonPanel);

        content.add("Error Message", holder);
        cl.show(content, "Error Message");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(content, role + " Message Menu");
            }
        });
    }

}
