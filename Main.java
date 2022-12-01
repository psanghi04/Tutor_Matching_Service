import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Message messageClass = new Message();

        boolean login = false;
        ArrayList<User> userList = new ArrayList<>();
        String userName;
        String password;

        // not used
        ArrayList<String> blockedUserList = new ArrayList<>();
        ArrayList<String> invisibleList = new ArrayList<>();

        ArrayList<String> filterList = new ArrayList<>();

        System.out.println("Welcome to Tutoring Center!\n");

        File f = new File("UserDetails.txt");

        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            String userLine = bfr.readLine();
            while (userLine != null) {
                filterList = new ArrayList<>();
                String[] splitLines = userLine.split(",");

                User user;

                if (splitLines[5].equals("Student")) {
                    Collections.addAll(filterList, splitLines[4].split(";"));
                    user = new Student(splitLines[0], splitLines[1], splitLines[2], blockedUserList, invisibleList, splitLines[3], filterList, UUID.fromString(splitLines[6]));
                } else {
                    Collections.addAll(filterList, splitLines[6].split(";"));
                    user = new Tutor(splitLines[0], splitLines[1], splitLines[2], blockedUserList, invisibleList, splitLines[3].split(";"), Double.parseDouble(splitLines[4]), splitLines[5], filterList, UUID.fromString(splitLines[8]));
                }

                userList.add(user);
                userLine = bfr.readLine();
            }

            bfr.close();
        } catch (IOException e) {
            System.out.println("Cannot write to file!");
        }

        User user = null; // currentUser logged in
        boolean signedIn = false;

        while (!login) {
            System.out.println("Log In or Sign Up");
            System.out.println(
                    "1. Sign Up\n" +
                    "2. Login\n" +
                    "3. Exit\n\n" +
                    "Enter Option Number:");
            int optionNum = scan.nextInt();
            scan.nextLine();
            System.out.println();

            switch (optionNum) {
                case 1:
                    System.out.println(
                            "Select a role:\n" +
                            "1. Student\n" +
                            "2. Tutor\n\n" +
                            "Enter Option Number:");
                    int option = scan.nextInt();
                    scan.nextLine();
                    System.out.println();

                    System.out.println("Enter your username:");
                    userName = scan.nextLine();

                    System.out.println("Enter your email:");
                    String email = scan.nextLine();

                    System.out.println("Enter password: ");
                    password = scan.nextLine();

                    boolean accountSimilarity = false;

                    for (User person : userList) {
                        if (person.getAccountUsername().equals(userName)) {
                            accountSimilarity = true;
                            break;
                        }

                        if (person.getEmail().equals(email)) {
                            accountSimilarity = true;
                            break;
                        }
                    }

                    if (option == 1) {
                        if (!accountSimilarity) {
                            user = new Student(userName, password, email, blockedUserList, invisibleList, "****", filterList);

                            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                                pw.write(userName + "," + password + "," + email + "," + "****,," + user + "," + user.getID() + "\n");
                                pw.flush();
                            } catch (IOException e) {
                                System.out.println("Unable to write file");
                            }
                        } else {
                            System.out.println("Sorry, you have the same username or email as another account\n");
                            continue;
                        }
                    } else {
                        if (!accountSimilarity) {
                            // Use commas
                            System.out.println("What subjects are you planning to teach");

                            String subjects = scan.nextLine();
                            String[] newSubjects = subjects.split(",");

                            System.out.println("What is the price you charge?");
                            double price = scan.nextDouble();
                            scan.nextLine();

                            user = new Tutor(userName, password, email, blockedUserList, invisibleList, newSubjects, price, "****", filterList);

                            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                                pw.write(userName + "," + password + "," + email + "," + String.join(";", newSubjects) + "," + price + "," + "****,," + user + "," + user.getID() + "\n");
                                pw.flush();
                            } catch (IOException e) {
                                System.out.println("Unable to write file");
                            }
                        } else {
                            System.out.println("Sorry, you have the same username or email as another account\n");
                            continue;
                        }
                    }

                    userList.add(user);

                    login = true;
                    signedIn = true;

                    System.out.println("Congratulations on creating a new account!!\n");

                    break;

                case 2:
                    while (!login) {
                        System.out.println("Username:");
                        userName = scan.nextLine();

                        System.out.println("Password:");
                        password = scan.nextLine();

                        for (User acc : userList) {
                            if (acc.getAccountUsername().equals(userName)) {
                                if (acc.getPassword().equals(password)) {
                                    System.out.println("Successfully Signed In!\n");
                                    user = acc;
                                    login = true;
                                    break;
                                }
                            }
                        }

                        if (login) {
                            System.out.println("Welcome Back!\n");
                        } else {
                            System.out.println("Invalid Username or Password");
                            System.out.println("Do you want to go back?");
                            System.out.println(
                                    "1. Yes\n" +
                                    "2. No\n\n" +
                                    "Enter Option Number:");
                            String answer = scan.nextLine();
                            if (answer.equals("1")) {
                                break;
                            }
                            System.out.println("Try Again");
                            System.out.println();
                        }
                    }

                    signedIn = true;
                    break;

                case 3:
                    System.out.println("Have a nice day!");
                    login = true;
                    signedIn = false;
                    break;

                default:
                    System.out.println("Invalid Option Number");
                    System.out.println("Please Try Again\n");
            }
        }

        while (signedIn) {
            if (user instanceof Student) {
                // Student Interface
                System.out.println(
                        "Menu\n\n" +
                        "1. View tutors\n" +
                        "2. Message a tutor\n" +
                        "3. Edit profile\n" +
                        "4. Sign out\n" +
                        "5. Block a tutor\n" +
                        "6. Unblock a tutor\n" +
                        "7. Become invisible to a tutor\n\n" +
                        "Enter Option Number:");
                int option = scan.nextInt();
                scan.nextLine();
                System.out.println();

                boolean deletedAccount = false;

                switch (option) {

                    case 1:
                        ArrayList<User> availableTutors = new ArrayList<>();

                        for (User userEl : userList) {
                            if (userEl instanceof Tutor) {

                                if (!messageClass.isBlocked(user, userEl.getAccountUsername()) &&
                                        !messageClass.isInvisible(user, userEl.getAccountUsername())) {
                                    availableTutors.add(userEl);
                                }


                            }
                        }


                        if (availableTutors.size() == 0) {
                            System.out.println("There are no tutors available to message");
                        } else {
                            availability(availableTutors);
                        }

                        break;

                    case 2:

                        int index = -1;
                        boolean unableToMessage = false;

                        System.out.println("Who would you like to message?");
                        String person = scan.nextLine();

                        if (userList.size() == 0) {
                            System.out.println("There are no students available");
                        }

                        if (messageClass.isBlocked(user, person)) {
                            System.out.printf("Unable to message %s\n", person);
                            break;
                        }

                        if (messageClass.isInvisible(user, person)) {
                            System.out.printf("Unable to find %s\n", person);
                            break;
                        }

                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).getAccountUsername().equals(person)) {
                                index = i;

                                if (userList.get(i) instanceof Tutor) {
                                    System.out.println("Person Found");
                                    unableToMessage = false;
                                    break;
                                } else {
                                    System.out.println("You cannot message this person");
                                    unableToMessage = true;
                                }
                            }
                        }


                        if (index == -1 || unableToMessage) {
                            if (index == -1) {
                                System.out.println("Unable to find person.");
                            }

                            break;
                        }


                        boolean quit = true;

                        while (quit) {
                            System.out.println("0. Quit\n1. read message\n2. write a message\n3. delete a message\n4. edit a message\n5. search for a specific message\n6. import to a conversation\n7. export a conversation");
                            int optionMessage = scan.nextInt();
                            scan.nextLine();

                            switch (optionMessage) {
                                case 0:
                                    quit = false;
                                    break;
                                case 1:
                                    ArrayList<String> messages = messageClass.readMsg(user, userList.get(index));

                                    if (messages.size() == 0) {
                                        System.out.println("No Messages Available");
                                        continue;
                                    }

                                    int count = 1;
                                    printMsg(messages, count, user);
                                    break;
                                case 2:
                                    System.out.println("Message Body:");
                                    String content = scan.nextLine();
                                    messageClass.writeMsg(user, userList.get(index), content);
                                    messageClass.export(user, userList.get(index));
                                    System.out.println("Written Successfully");

                                    break;
                                case 3:
                                    System.out.println("What is the message that you would like to delete?");
                                    String message = scan.nextLine();

                                    ArrayList<String> messagesDelete = messageClass.readMsg(user, userList.get(index), true);

                                    boolean dMessageExists = false;
                                    for (String s : messagesDelete) {
                                        if (s.substring(s.lastIndexOf(",") + 1).equals(message)) {
                                            dMessageExists = true;
                                            break;
                                        }
                                    }

                                    if (!dMessageExists) {
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    messageClass.delete(user, userList.get(index), message);
                                    System.out.println("Message Deleted Successfully");

                                    break;

                                case 4:
                                    System.out.println("What is the message you would like to edit?");
                                    String oldMessage = scan.nextLine();

                                    ArrayList<String> allMessages = messageClass.readMsg(user, userList.get(index), true);

                                    if (allMessages.size() == 0) {
                                        System.out.println("There are no messages!");
                                        break;
                                    }

                                    boolean messageExists = false;

                                    for (String messageEl : allMessages) {
                                        if (messageEl.substring(messageEl.lastIndexOf(",") + 1).equals(oldMessage)) {
                                            messageExists = true;
                                            break;
                                        }
                                    }

                                    if (!messageExists) {
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    System.out.println("What would you like your edited message to look like?");
                                    String newMessage = scan.nextLine();


                                    messageClass.edit(user, userList.get(index), oldMessage, newMessage + "\n");
                                    messageClass.export(user, userList.get(index));

                                    break;

                                case 5:
                                    System.out.println("Enter a keyword to search for a message: ");
                                    String keyword = scan.nextLine();

                                    ArrayList<String> searchableMessages = messageClass.readMsg(user, userList.get(index));

                                    boolean foundMessage = false;

                                    for (String searchableMessage : searchableMessages) {
                                        if (searchableMessage.contains(keyword)) {
                                            System.out.println("Found Message: ");
                                            System.out.println(searchableMessage);
                                            foundMessage = true;
                                        }
                                    }

                                    if (!foundMessage) {
                                        System.out.println("Message not found!");
                                    }

                                    break;

                                case 6:
                                    System.out.println("Please list the filename.");
                                    String ifileName = scan.nextLine();

                                    ArrayList<String> importMessages = new ArrayList<>();

                                    importMessage(messageClass, userList, user, index, ifileName, importMessages);

                                    break;

                                case 7:

                                    export(scan, messageClass, userList, user, index);
                                    break;

                            }

                        }

                        break;

                    case 3:
                        System.out.println("1. change password\n2. change username\n3. change email\n4. delete account\n5. change filter\n6. change filter words");
                        int optionProfile = scan.nextInt();
                        scan.nextLine();

                        switch (optionProfile) {
                            case 1:
                                System.out.println("What would you like to change your password to?");
                                String newPassword = scan.nextLine();
                                user.setPassword(newPassword);


                                try {
                                    updateFile(userList, f);
                                    System.out.println("Password has been successfully changed\n");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }


                                break;
                            case 2:

                                System.out.println("What would you like to change your username to?");
                                String newUsername = scan.nextLine();

                                boolean sameUsername = false;
                                for (User users : userList) {
                                    if (users instanceof Student) {
                                        if (users.getAccountUsername().equals(newUsername)) {
                                            sameUsername = true;
                                            break;
                                        }
                                    }
                                }

                                if (!sameUsername) {
                                    user.setAccountUsername(newUsername);
                                } else {
                                    System.out.println("Username exists.");
                                    continue;
                                }

                                try {
                                    updateFile(userList, f);

                                    System.out.println("Username has been successfully changed\n");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                            case 3:
                                System.out.println("What would you like to change your email to?");
                                String newEmail = scan.nextLine();

                                boolean sameEmail = false;
                                for (User users : userList) {
                                    if (users instanceof Student) {
                                        if (users.getEmail().equals(newEmail)) {
                                            sameEmail = true;
                                            break;
                                        }
                                    }
                                }

                                if (!sameEmail) {
                                    user.setEmail(newEmail);
                                } else {
                                    System.out.println("Email exists.");
                                    continue;
                                }


                                try {
                                    updateFile(userList, f);

                                    System.out.println("Email has been successfully changed\n");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                            case 4:
                                System.out.println("Deleting Account...");

                                deletedAccount = true;

                                // Removing User from ArrayList
                                for (int i = 0; i < userList.size(); i++) {
                                    if (userList.get(i).getAccountUsername().equals(user.getAccountUsername())) {
                                        if (userList.get(i).getPassword().equals(user.getPassword())) {
                                            if (userList.get(i).getEmail().equals(user.getEmail())) {
                                                userList.remove(i);
                                            }
                                        }
                                    }
                                }

                                // Rewriting the file again...
                                try {
                                    updateFile(userList, f);

                                    System.out.println("Account has been deleted");

                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }


                                break;
                            case 5:
                                System.out.println("Enter new filter: ");
                                String filter = scan.nextLine();
                                ((Student) user).setFilter(filter);
                                System.out.println("Changed Successfully");

                                try {
                                    BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
                                    ArrayList<String> lines = new ArrayList<>();
                                    String line;
                                    while ((line = bfr.readLine()) != null) {
                                        String[] newLine = line.split(",");
                                        if (newLine[0].equals(user.getAccountUsername())) {
                                            newLine[3] = filter;
                                            line = String.join(",", newLine);
                                        }
                                        lines.add(line);
                                    }

                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
                                    for (String det : lines) {
                                        pw.write(det + "\n");
                                        pw.flush();
                                    }
                                } catch (IOException e) {
                                    System.out.println("Unable to write file");
                                }
                                break;
                            case 6:
                                System.out.println("1. Add words\n" +
                                        "2. Delete words");
                                String answer = scan.nextLine();
                                switch (answer) {
                                    case "1":
                                        System.out.println("Enter words to be added [Comma separated]");
                                        String[] words = scan.nextLine().split(",");
                                        ArrayList<String> pastWords = ((Student) user).getFilterWordList();

                                        Collections.addAll(pastWords, words);

                                        ((Student) user).setFilterWordList(pastWords);
                                        System.out.println("Added Successfully");

                                        updateCensoredWords(user, pastWords);

                                        break;
                                    case "2":
                                        ArrayList<String> filterWordList = ((Student) user).getFilterWordList();

                                        System.out.println("Current Filtered Words: ");
                                        for (String word : filterWordList) {
                                            System.out.println(word);
                                        }

                                        System.out.println("Enter words to be deleted [Comma separated]");
                                        String[] wordList = scan.nextLine().split(",");

                                        filterWordList.removeAll(List.of(wordList));
                                        System.out.println("Deleted Successfully");

                                        ((Student) user).setFilterWordList(filterWordList);

                                        updateCensoredWords(user, filterWordList);
                                }
                        }

                        break;

                    case 4:
                        System.out.println("Have a nice day!");
                        signedIn = false;
                        break;
                    case 5:
                        System.out.println("Which user would you like to block?");
                        String blockUsername = scan.nextLine();

                        if (blockUsername.equals(user.getAccountUsername())) {
                            System.out.println("Cannot block yourself");
                            break;
                        }

                        for (User users : userList) {
                            if (users.getAccountUsername().equals(blockUsername)) {
                                blockedUserList.add(users.getAccountUsername());
                            }
                        }

                        try {
                            File blockedUsers = new File("BlockedUsers.txt");
                            if (!blockedUsers.exists()) {
                                blockedUsers.createNewFile();
                            }

                            FileWriter fr = new FileWriter(blockedUsers, true);
                            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

                            pw.println(user + "," + user.getAccountUsername() + ";" + blockUsername);
                            pw.flush();
                        } catch (IOException e) {
                            System.out.println("There are no blocked users");
                        }


                        break;
                    case 6:
                        System.out.println("Which user would you like to unblock?");
                        String unblockUsername = scan.nextLine();

                        if (unblockUsername.equals(user.getAccountUsername())) {
                            System.out.println("Cannot unblock yourself");
                            break;
                        }

                        for (int i = 0; i < blockedUserList.size(); i++) {
                            if (blockedUserList.get(i).equals(unblockUsername)) {
                                System.out.println(blockedUserList.get(i));
                                blockedUserList.remove(i);
                            }
                        }

                        unblockUser(blockedUserList, user);

                        break;
                    case 7:
                        System.out.println("Which user would you like to make invisible?");
                        String invisiblePerson = scan.nextLine();
                        for (User users : userList) {
                            if (users.getAccountUsername().equals(invisiblePerson)) {
                                invisibleList.add(users.getAccountUsername());
                            }
                        }

                        setInvisible(user, invisiblePerson);

                        break;
                }

                if (deletedAccount) {
                    break;
                }
            } else {
                // Tutor Interface
                System.out.println(
                        "Menu\n\n" +
                        "1. View students\n" +
                        "2. Message a student\n" +
                        "3. Edit profile\n" +
                        "4. Sign out\n" +
                        "5. Block a student\n" +
                        "6. Unblock a student\n" +
                        "7. Become invisible to a student\n\n" +
                        "Enter Option Number:");
                int option = scan.nextInt();
                scan.nextLine();
                System.out.println();

                switch (option) {
                    case 1:
                        ArrayList<User> availableStudents = new ArrayList<>();

                        for (User person : userList) {
                            if (person instanceof Student) {
                                if (!messageClass.isBlocked(user, person.getAccountUsername()) &&
                                        !messageClass.isInvisible(user, person.getAccountUsername())) {
                                    availableStudents.add(person);
                                }

                            }
                        }

                        if (availableStudents.size() == 0) {
                            System.out.println("There are no students available to message");
                        } else {
                            availability(availableStudents);
                        }


                        break;

                    case 2:

                        int index = -1;
                        boolean unableToMessage = false;

                        System.out.println("Who would you like to message?");
                        String person = scan.nextLine();

                        if (userList.size() == 0) {
                            System.out.println("There are no students available");
                        }

                        if (messageClass.isBlocked(user, person)) {
                            System.out.printf("Unable to message %s\n", person);
                            break;
                        }

                        if (messageClass.isInvisible(user, person)) {
                            System.out.printf("Unable to find %s\n", person);
                            break;
                        }

                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).getAccountUsername().equals(person)) {
                                index = i;

                                if ((userList.get(i) instanceof Student)) {
                                    System.out.println("Person Found");
                                    unableToMessage = false;
                                    break;
                                } else {
                                    System.out.println("You cannot message this person");
                                    unableToMessage = true;
                                }
                            }
                        }

                        if (index == -1 || unableToMessage) {
                            if (index == -1) {
                                System.out.println("Unable to find person.");
                            }

                            break;
                        }

                        boolean quit = true;
                        while (quit) {
                            System.out.println("0. Quit\n1. read message\n2. write a message\n3. delete a message\n4. edit a message\n5. search for a specific message\n6. import to a conversation\n7. export a conversation");
                            int optionMessage = scan.nextInt();
                            scan.nextLine();

                            switch (optionMessage) {
                                case 0:
                                    quit = false;
                                    break;
                                case 1:
                                    ArrayList<String> messages = messageClass.readMsg(user, userList.get(index));

                                    if (messages.size() == 0) {
                                        System.out.println("No Messages Available");
                                        continue;
                                    }

                                    int count = 1;
                                    printMsg(messages, count, user);
                                    break;
                                case 2:
                                    System.out.println("Message Body:");
                                    String content = scan.nextLine();
                                    messageClass.writeMsg(user, userList.get(index), content);
                                    messageClass.export(user, userList.get(index));
                                    System.out.println("Written Successfully\n");

                                    break;
                                case 3:
                                    System.out.println("What is the message that you would like to delete?");
                                    String message = scan.nextLine();

                                    ArrayList<String> allDMessages = messageClass.readMsg(user, userList.get(index), true);

                                    boolean DMessageExists = false;
                                    for (String allDMessage : allDMessages) {
                                        if (allDMessage.substring(allDMessage.lastIndexOf(",") + 1).equals(message)) {
                                            DMessageExists = true;
                                            break;
                                        }
                                    }

                                    if (!DMessageExists) {
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    messageClass.delete(user, userList.get(index), message);
                                    System.out.println("Message Deleted Successfully\n");

                                    break;

                                case 4:
                                    System.out.println("What is the message you would like to edit?");
                                    String oldMessage = scan.nextLine();

                                    ArrayList<String> allEMessages = messageClass.readMsg(user, userList.get(index), true);

                                    if (allEMessages.size() == 0) {
                                        System.out.println("There are no messages!");
                                        break;
                                    }

                                    boolean messageEExists = false;
                                    for (String allEMessage : allEMessages) {
                                        if (allEMessage.substring(allEMessage.lastIndexOf(",") + 1).equals(oldMessage)) {
                                            messageEExists = true;
                                            break;
                                        }
                                    }

                                    if (!messageEExists) {
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    System.out.println("What would you like your edited message to look like?");
                                    String newMessage = scan.nextLine();

                                    messageClass.edit(user, userList.get(index), oldMessage, newMessage + "\n");
                                    messageClass.export(user, userList.get(index));
                                    System.out.println("Edit Successful\n");

                                    break;

                                case 5:
                                    System.out.println("Enter a keyword to search for a message: ");
                                    String keyword = scan.nextLine();

                                    ArrayList<String> searchMessages = messageClass.readMsg(user, userList.get(index));

                                    boolean messageFound = false;
                                    for (String searchMessage : searchMessages) {
                                        if (searchMessage.contains(keyword)) {
                                            System.out.println(searchMessage);
                                            messageFound = true;
                                        }
                                    }

                                    if (!messageFound) {
                                        System.out.println("Message not found!");
                                    }

                                    break;
                                case 6:
                                    System.out.println("Please list the filename.");
                                    String ifileName = scan.nextLine();

                                    ArrayList<String> importMessages = new ArrayList<>();

                                    importMessage(messageClass, userList, user, index, ifileName, importMessages);

                                    break;
                                case 7:
                                    export(scan, messageClass, userList, user, index);

                                    break;
                            }
                        }


                        break;

                    case 3:
                        System.out.println("1. change password\n2. change username\n3. change email\n4. delete account\n5. change filter\n6. change filter words");
                        int optionProfile = scan.nextInt();
                        scan.nextLine();

                        switch (optionProfile) {
                            case 1:
                                System.out.println("What would you like to change your password to?");
                                String newPassword = scan.nextLine();
                                user.setPassword(newPassword);

                                try {
                                    updateFile(userList, f);

                                    System.out.println("Password has been changed successfully");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }


                                break;

                            case 2:

                                System.out.println("What would you like to change your username to?");
                                String newUsername = scan.nextLine();

                                boolean similarUsername = false;
                                for (User users : userList) {
                                    if (users instanceof Tutor) {
                                        if (users.getAccountUsername().equals(newUsername)) {
                                            similarUsername = true;
                                            break;
                                        }
                                    }
                                }

                                if (!similarUsername) {
                                    user.setAccountUsername(newUsername);
                                } else {
                                    System.out.println("Username exists.");
                                    continue;
                                }

                                try {
                                    updateFile(userList, f);
                                    System.out.println("Username has been changed successfully!");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                            case 3:
                                System.out.println("What would you like to change your email to?");
                                String newEmail = scan.nextLine();

                                boolean similarEmail = false;
                                for (User users : userList) {
                                    if (users instanceof Tutor) {
                                        if (users.getEmail().equals(newEmail)) {
                                            similarEmail = true;
                                            break;
                                        }
                                    }
                                }

                                if (!similarEmail) {
                                    user.setEmail(newEmail);
                                } else {
                                    System.out.println("Email exists.");
                                    continue;
                                }


                                try {
                                    updateFile(userList, f);

                                    System.out.println("Email has been successfully changed!");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                            case 4:
                                System.out.println("Deleting Account...");

                                // Removing User from ArrayList
                                for (int i = 0; i < userList.size(); i++) {
                                    if (userList.get(i).getAccountUsername().equals(user.getAccountUsername())) {
                                        if (userList.get(i).getPassword().equals(user.getPassword())) {
                                            userList.remove(i);
                                        }
                                    }
                                }

                                // Rewriting the file again...
                                try {
                                    updateFile(userList, f);
                                    System.out.println("Account has been deleted");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;
                            case 5:
                                System.out.println("Enter new filter: ");
                                String filter = scan.nextLine();
                                ((Tutor) user).setFilter(filter);
                                System.out.println("Changed Successfully");

                                try {
                                    BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
                                    ArrayList<String> lines = new ArrayList<>();
                                    String line;
                                    while ((line = bfr.readLine()) != null) {
                                        String[] newLine = line.split(",");
                                        if (newLine[0].equals(user.getAccountUsername())) {
                                            newLine[5] = filter;
                                            line = String.join(",", newLine);
                                        }
                                        lines.add(line);
                                    }

                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
                                    for (String det : lines) {
                                        pw.write(det + "\n");
                                        pw.flush();
                                    }
                                } catch (IOException e) {
                                    System.out.println("Unable to write file");
                                }

                                break;
                            case 6:
                                System.out.println("1. Add words\n" +
                                        "2. Delete words");
                                String answer = scan.nextLine();
                                switch (answer) {
                                    case "1":
                                        System.out.println("Enter words to be added [Comma separated]");
                                        String[] words = scan.nextLine().split(",");
                                        ArrayList<String> pastWords = ((Tutor) user).getFilterWordList();

                                        pastWords.addAll(Arrays.asList(words));

                                        ((Tutor) user).setFilterWordList(pastWords);
                                        System.out.println("Added Successfully");

                                        updateCensorWords(user, pastWords);

                                        break;
                                    case "2":
                                        ArrayList<String> filterWordList = ((Tutor) user).getFilterWordList();

                                        System.out.println("Current Filtered Words: ");
                                        for (String word : filterWordList) {
                                            System.out.println(word);
                                        }

                                        System.out.println("Enter words to be deleted [Comma separated]");
                                        String[] wordList = scan.nextLine().split(",");

                                        filterWordList.removeAll(List.of(wordList));
                                        System.out.println("Deleted Successfully");

                                        ((Tutor) user).setFilterWordList(filterWordList);

                                        updateCensorWords(user, filterWordList);
                                }
                        }

                        break;

                    case 4:
                        System.out.println("Have a nice day!");
                        signedIn = false;
                        break;
                    case 5:
                        System.out.println("Which user would you like to block?");
                        String blockUsername = scan.nextLine();

                        if (blockUsername.equals(user.getAccountUsername())) {
                            System.out.println("Cannot block yourself");
                            break;
                        }

                        for (User users : userList) {
                            if (users.getAccountUsername().equals(blockUsername)) {
                                blockedUserList.add(users.getAccountUsername());
                            }
                        }

                        try {
                            File blockedUsers = new File("BlockedUsers.txt");
                            if (!blockedUsers.exists()) {
                                blockedUsers.createNewFile();
                                System.out.println("Blocked Users file has been created");
                            }

                            FileWriter fr = new FileWriter(blockedUsers, true);
                            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

                            pw.println(user + "," + user.getAccountUsername() + ";" + blockUsername);
                            pw.flush();
                        } catch (IOException e) {
                            System.out.println("There are no blocked users");
                        }


                        break;
                    case 6:
                        System.out.println("Which user would you like to unblock?");
                        String unblockUsername = scan.nextLine();

                        if (unblockUsername.equals(user.getAccountUsername())) {
                            System.out.println("Cannot unblock yourself");
                            break;
                        }

                        for (int i = 0; i < blockedUserList.size(); i++) {
                            if (blockedUserList.get(i).equals(unblockUsername)) {
                                System.out.println(blockedUserList.get(i));
                                blockedUserList.remove(i);
                            }
                        }

                        unblockUser(blockedUserList, user);

                        break;
                    case 7:
                        System.out.println("Which user would you like to make invisible?");
                        String invisiblePerson = scan.nextLine();
                        for (User users : userList) {
                            if (users.getAccountUsername().equals(invisiblePerson)) {
                                invisibleList.add(users.getAccountUsername());
                            }
                        }

                        setInvisible(user, invisiblePerson);

                        break;

                }
            }
        }
    }

    public static void printMsg(ArrayList<String> messages, int count, User user) {
        if (user instanceof Student) {
            for (String message : messages) {
                if (!((Student) user).getFilterWordList().get(0).equals("")) {
                    for (String filterWord : ((Student) user).getFilterWordList()) {
                        message = message.replaceAll(String.format("(?i)%s", filterWord), ((Student) user).getFilter());
                    }
                }
                System.out.printf("[%d]: %s\n", count++, message);
            }
        } else {
            for (String message : messages) {
                if (!((Tutor) user).getFilterWordList().get(0).equals("")) {
                    for (String filterWord : ((Tutor) user).getFilterWordList()) {
                        message = message.replaceAll(String.format("(?i)%s", filterWord), ((Tutor) user).getFilter());
                    }
                }
                System.out.printf("[%d]: %s\n", count++, message);
            }
        }
    }

    public static void updateFile(ArrayList<User> userList, File f) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(f));
        String line;
        int count = 0;
        ArrayList<String[]> lines = new ArrayList<>();
        while ((line = bfr.readLine()) != null) {
            if (line.contains(userList.get(count).getAccountUsername()) ||
                    line.contains(userList.get(count).getEmail())) {
                lines.add(line.split(","));
                count++;
            }
        }
        bfr.close();

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i) instanceof Student) {
                pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," +
                        userList.get(i).getEmail() + "," + lines.get(i)[3] + "," + lines.get(i)[4] + "," + "Student," + userList.get(i).getID() + "\n");
            } else {
                pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() +
                        "," + userList.get(i).getEmail() + "," +
                        String.join(";", ((Tutor) userList.get(i)).getSubjects()) +
                        "," + ((Tutor) userList.get(i)).price() + "," + lines.get(i)[5] + "," + lines.get(i)[6] + "," + "Tutor," + userList.get(i).getID() + "\n");
            }
        }

        pw.flush();
    }

    public static void updateCensorWords(User user, ArrayList<String> pastWords) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] newLine = line.split(",");
                if (newLine[0].equals(user.getAccountUsername())) {
                    newLine[6] = String.join(";", pastWords);
                    line = String.join(",", newLine);
                }
                lines.add(line);
            }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
            for (String det : lines) {
                pw.write(det + "\n");
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("Unable to write file");
        }
    }

    public static void setInvisible(User user, String invisiblePerson) {
        try {
            File invisibleUsers = new File("InvisibleUsers.txt");
            if (!invisibleUsers.exists()) {
                invisibleUsers.createNewFile();
                System.out.println("Invisible Users file has been created");
            }

            FileWriter fr = new FileWriter(invisibleUsers, true);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

            pw.println(user + "," + user.getAccountUsername() + ";" + invisiblePerson);
            pw.flush();
        } catch (IOException e) {
            System.out.println("There are no invisible users");
        }
    }

    public static void unblockUser(ArrayList<String> blockedUserList, User user) {
        try {
            File blockedUsers = new File("BlockedUsers.txt");
            if (!blockedUsers.exists()) {
                blockedUsers.createNewFile();
            }

            FileWriter fr = new FileWriter(blockedUsers, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

            for (String s : blockedUserList) {
                pw.println(user + "," + user.getAccountUsername() + ";" + s);
            }
        } catch (IOException e) {
            System.out.println("User to unblock not found");
        }
    }

    public static void updateCensoredWords(User user, ArrayList<String> pastWords) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] newLine = line.split(",");
                if (newLine[0].equals(user.getAccountUsername())) {
                    newLine[4] = String.join(";", pastWords);
                    line = String.join(",", newLine);
                }
                lines.add(line);
            }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
            for (String det : lines) {
                pw.write(det + "\n");
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("Unable to write file");
        }
    }

    public static void export(Scanner scan, Message messageClass, ArrayList<User> userList, User user, int index) {
        try {
            System.out.println("What is the name of the file which you would like your exported contents to be placed in?");
            String expFileName = scan.nextLine();

            File exportFile = new File(expFileName + ".csv");

            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            FileWriter fw = new FileWriter(exportFile, false);
            BufferedWriter bfw = new BufferedWriter(fw);

            ArrayList<String> pastMessages = messageClass.readMsg(user, userList.get(index), true);
            ArrayList<String> unEditPastMessages = messageClass.readMsg(user, userList.get(index));

            for (int i = 0; i < pastMessages.size(); i++) {
                System.out.printf("[%d]: %s", i + 1, unEditPastMessages.get(i));
                bfw.write(user.getAccountUsername() + ";" + userList.get(index).getAccountUsername() + "," + user.getAccountUsername() + "," + pastMessages.get(i).split(",")[2] + "," + pastMessages.get(i).split(",")[3] + "\n");
            }

            bfw.flush();
            System.out.println("Conversation exported successfully!");
        } catch (IOException e) {
            System.out.println("Unable to export file");
        }
    }

    public static void importMessage(Message messageClass, ArrayList<User> userList, User user, int index, String ifileName, ArrayList<String> importMessages) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(ifileName));

            String line = bfr.readLine();
            while (line != null) {
                importMessages.add(line);
                line = bfr.readLine();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(user.getID() + "_" + userList.get(index).getID(), true));
            for (String importMessage : importMessages) {
                bfw.write(user.getAccountUsername() + ";" + userList.get(index).getAccountUsername() + "," + user.getAccountUsername() + "," + messageClass.getTime() + "," + importMessage + "\n");
            }

            bfr.close();
            bfw.flush();

            messageClass.export(user, userList.get(index));

            System.out.println("Imported conversation successfully!");

        } catch (IOException e) {
            System.out.println("Cannot find or read from file!");
        }
    }

    public static void availability(ArrayList<User> userList) {
        for (int i = 0; i < userList.size(); i++) {
            if (i != userList.size() - 1) {
                System.out.print(userList.get(i).getAccountUsername() + ",");
            } else {
                System.out.print(userList.get(i).getAccountUsername() + "\n");
            }
        }
    }
}
