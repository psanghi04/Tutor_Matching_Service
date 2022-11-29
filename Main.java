import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Message messageClass = new Message();

        boolean login = false;
        ArrayList<User> userList = new ArrayList<>();
        String userName = "";
        String password;

        ArrayList<String> blockedUserList = new ArrayList<>();
        ArrayList<String> invisibleList = new ArrayList<>();

        System.out.println("Welcome to Tutoring Center!");

        File f = new File("UserDetails.txt");

        try {
            if (!f.exists()) {
                f.createNewFile();
                System.out.println("File has been created..");
            }

            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            String userLine = bfr.readLine();
            while (userLine != null) {
                String[] splitLines = userLine.split(",");

                User user;

                if (splitLines[3].equals("Student")) {
                    user = new Student(splitLines[0], splitLines[1], splitLines[2], blockedUserList, invisibleList);
                } else {
                    user = new Tutor(splitLines[0], splitLines[1], splitLines[2], blockedUserList, invisibleList, splitLines[3].split(";"), Double.parseDouble(splitLines[4]));
                }

                userList.add(user);
                userLine = bfr.readLine();
            }

            bfr.close();
        } catch (IOException e) {
            System.out.println("Cannot write to file!");
        }

        User user = null; // currentUser logged in

        while (!login) {

            System.out.println("Do you want to log in or create a account?");
            System.out.println("1. Create New Account\n" +
                    "2. Login");
            int optionNum = scan.nextInt();
            scan.nextLine();

            switch (optionNum) {
                case 1:
                    System.out.println("Would you like to be a student or a tutor?");
                    System.out.println("1. Student\n2. Tutor");
                    int option = scan.nextInt();

                    scan.nextLine();

                    System.out.println("Enter new username: ");
                    userName = scan.nextLine();

                    System.out.println("Enter your email: ");
                    String email = scan.nextLine();

                    System.out.println("Create new password: ");
                    password = scan.nextLine();

                    if (option == 1) {
                        boolean accountSimilarity = false;

                        for(int i = 0; i < userList.size(); i++){
                            if(userList.get(i) instanceof Student){
                                if(userList.get(i).getAccountUsername().equals(userName)){
                                    accountSimilarity = true;
                                    break;
                                }

                                if(userList.get(i).getEmail().equals(email)){
                                    accountSimilarity = true;
                                    break;
                                }
                            }
                        }

                        if(!accountSimilarity){
                            user = new Student(userName, password, email, blockedUserList, invisibleList);
                        } else{
                            System.out.println("Sorry, you have the same username or email as another account\n");
                            continue;
                        }

                        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                            pw.write(userName + "," + password + "," + email + "," + "Student\n");
                            pw.flush();
                        } catch (IOException e) {
                            System.out.println("Unable to write file");
                        }

                    } else {
                        // Use commas
                        System.out.println("What subjects are you planning to teach");

                        String subjects = scan.nextLine();
                        String[] newSubjects = subjects.split(",");

                        System.out.println("What is the price you charge?");
                        double price = scan.nextDouble();

                        scan.nextLine();

                        boolean accountSimilarity = false;

                        for(int i = 0; i < userList.size(); i++){
                            if(userList.get(i) instanceof Tutor){
                                if(userList.get(i).getAccountUsername().equals(userName)){
                                    accountSimilarity = true;
                                    break;
                                }

                                if(userList.get(i).getEmail().equals(email)){
                                    accountSimilarity = true;
                                    break;
                                }
                            }
                        }

                        if(!accountSimilarity){
                            user = new Tutor(userName, password, email, blockedUserList, invisibleList, newSubjects, price);
                        } else{
                            System.out.println("Sorry, you have the same username or email as another account\n");
                            continue;
                        }

                        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                            pw.write(userName + "," + password + "," + email + "," + subjects.replace(",", ";") + "," + price + "," + "Tutor\n");
                            pw.flush();
                        } catch (IOException e) {
                            System.out.println("Unable to write file");
                        }
                    }

                    userList.add(user);

                    login = true;

                    System.out.println("Congratulations on creating a new account!!\n");

                    break;

                case 2:
                    while (!login) {
                        System.out.println("Username: ");
                        userName = scan.nextLine();

                        System.out.println("Password: ");
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
                            System.out.println("Welcome Back");
                        } else {
                            System.out.println("Invalid Username or Password");
                            System.out.println("Do you want to go back? [Y/N]");
                            String answer = scan.nextLine();
                            if (answer.equals("Y")) {
                                break;
                            }
                            System.out.println("Try Again");
                        }

                    }

                    break;

                default:
                    System.out.println("Invalid Option Number");
                    System.out.println("Please Try Again\n");
            }
        }

        boolean signedIn = true;

        while (signedIn) {

            if (user instanceof Student) {
                System.out.println("Student Interface\n\n1. View tutors\n2. message a user\n3. edit profile\n4. sign out\n5. block a user\n6. Become Invisible");
                int option = scan.nextInt();
                scan.nextLine();

                boolean deletedAccount = false;

                switch (option) {

                    case 1:
                        ArrayList<User> availableTutors = new ArrayList<>();

                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i) instanceof Tutor) {
//                                try {
//                                    File blockedUsers = new File(user.getAccountUsername() + "_" + userList.get(i).getAccountUsername());
//
//                                    if(!blockedUsers.exists()){
//                                        boolean fCr = blockedUsers.createNewFile();
//                                    }
//
//                                    FileReader fr = new FileReader(blockedUsers);
//                                    BufferedReader bfr = new BufferedReader(fr);
//
//                                    String line = bfr.readLine();
//                                    boolean blockedUser = false;
//                                    while(line != null) {
//                                        if(line.equals("This person is blocked")){
//                                            blockedUser = true;
//                                            break;
//                                        }
//
//                                        line = bfr.readLine();
//
//                                    }

//                                    if(!blockedUser){
//                                        if(userList.get(i) instanceof Tutor){
//                                            availableTutors.add(userList.get(i));
//                                        }
//                                    } else{
//                                        userList.get(i).setAccountUsername(userList.get(i).getAccountUsername() + "(blocked)");
//                                        availableTutors.add(userList.get(i));
//                                    }

                                User person = userList.get(i);
                                if(!messageClass.isBlocked(user, person.getAccountUsername()) &&
                                        !messageClass.isInvisible(user, person.getAccountUsername())) {
                                    availableTutors.add(userList.get(i));
                                }

//                                    bfr.close();
//                                } catch(IOException e){
//                                    e.printStackTrace();
//                                }

                            }
                        }

                        for (int i = 0; i < availableTutors.size(); i++) {
                            if (i != availableTutors.size() - 1) {
                                System.out.print(availableTutors.get(i).getAccountUsername() + ",");
                            } else {
                                System.out.print(availableTutors.get(i).getAccountUsername() + "\n");
                            }
                        }

                        if(availableTutors.size() == 0){
                            System.out.println("There are no tutors available to message");
                        }

                        break;

                    case 2:

                        int index = -1;
                        boolean unableToMessage = false;
                        int optionSignIn = 0;

                        System.out.println("Who would you like to message?");
                        String person = scan.nextLine();

                        if(userList.size() == 0){
                            System.out.println("There are no students available");
                        }

//                        boolean userBlocked = false;

//                        try {
//                            File convos = new File(user.getAccountUsername() + "_" + person);
//
//                            if(!convos.exists()){
//                                boolean fCr = convos.createNewFile();
//                            }
//
//                            FileReader fr = new FileReader(convos);
//                            BufferedReader bfr = new BufferedReader(fr);
//
//                            String line = bfr.readLine();
//                            while(line != null) {
//                                if (line.equals("This person is blocked")) {
//                                    userBlocked = true;
//                                    break;
//                                }
//
//                                line = bfr.readLine();
//                            }
//
//                            bfr.close();
                        if(messageClass.isBlocked(user, person)){
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

                        while(quit){
                            System.out.println("0. Quit\n1. read message\n2. write a message\n3. delete a message\n4. edit a message\n5. search for a specific message\n6. import to a conversation\n7. export a conversation");
                            int optionMessage = scan.nextInt();
                            scan.nextLine();

                            switch (optionMessage) {
                                case 0:
                                    quit = false;
                                    break;
                                case 1:
                                    ArrayList<String> messages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    if(messages.size() == 0){
                                        System.out.println("No Messages Available");
                                        continue;
                                    }

                                    for (String message : messages) {
                                        System.out.println(message);
                                    }
                                    break;
                                case 2:
                                    System.out.println("Message Body:");
                                    String content = scan.nextLine();
                                    messageClass.writeMsg(userName, userList.get(index).getAccountUsername(), content);
                                    messageClass.export(userName, userList.get(index).getAccountUsername());
                                    System.out.println(userList.get(index).getAccountUsername());
                                    System.out.println("Written Successfully");

                                    break;
                                case 3:
                                    System.out.println("What is the message that you would like to delete?");
                                    String message = scan.nextLine();

                                    ArrayList<String> messagesDelete = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    boolean dMessageExists = false;
                                    for(int i = 0; i < messagesDelete.size(); i++){
                                        if(messagesDelete.get(i).substring(messagesDelete.get(i).lastIndexOf(",") + 1).equals(message)){
                                            dMessageExists = true;
                                        }
                                    }

                                    if(!dMessageExists){
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    messageClass.delete(user, userList.get(index).getAccountUsername(), message);
                                    System.out.println("Message Deleted Successfully");

                                    break;

                                case 4:
                                    System.out.println("What is the message you would like to edit?");
                                    String oldMessage = scan.nextLine();

                                    ArrayList<String> allMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    if(allMessages.size() == 0){
                                        System.out.println("There are no messages!");
                                        break;
                                    }

                                    boolean messageExists = false;

                                    for(int i = 0; i < allMessages.size(); i++){
                                        if(allMessages.get(i).substring(allMessages.get(i).lastIndexOf(",") + 1).equals(oldMessage)){
                                            messageExists = true;
                                        }
                                    }

                                    if(!messageExists){
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    System.out.println("What would you like your edited message to look like?");
                                    String newMessage = scan.nextLine();



                                    messageClass.edit(user, userList.get(index).getAccountUsername(), oldMessage, newMessage + "\n");
                                    messageClass.export(userName, userList.get(index).getAccountUsername());

                                    break;

                                case 5:
                                    System.out.println("Enter a keyword to search for a message: ");
                                    String keyword = scan.nextLine();

                                    ArrayList<String> searchableMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    boolean foundMessage = false;

                                    for(int i = 0; i < searchableMessages.size(); i++){
                                        if(searchableMessages.get(i).contains(keyword)) {
                                            System.out.println(searchableMessages.get(i));
                                            foundMessage = true;
                                        }
                                    }

                                    if(!foundMessage){
                                        System.out.println("Message not found!");
                                    }

                                    break;

                                case 6:
                                    System.out.println("Please list the filename.");
                                    String ifileName = scan.nextLine();

                                    ArrayList<String> importMessages = new ArrayList<String>();

                                    try{
                                        File importFile = new File(ifileName);
                                        FileReader fr = new FileReader(importFile);
                                        BufferedReader bfr = new BufferedReader(fr);

                                        String line = bfr.readLine();
                                        while(line != null){
                                            importMessages.add(line);
                                            line = bfr.readLine();
                                        }

                                        BufferedWriter bfw = new BufferedWriter(new FileWriter(userName + "_" + userList.get(index).getAccountUsername(), true));
                                        for(int i = 0; i < importMessages.size(); i++){
                                            bfw.write(userName + ";" + userList.get(index).getAccountUsername() + "," + userName + "," + messageClass.getTime() + "," + importMessages.get(i) + "\n");
                                        }

                                        bfr.close();
                                        bfw.flush();

                                        messageClass.export(userName, userList.get(index).getAccountUsername());

                                        System.out.println("Imported conversation successfully!");

                                    } catch (IOException e){
                                        System.out.println("Cannot find or read from file!");
                                    }

                                    break;

                                case 7:

                                    try{
                                        System.out.println("What is the name of the file which you would like your exported contents to be placed in?");
                                        String expFileName = scan.nextLine();

                                        File exportFile = new File(expFileName + ".csv");

                                        if(!exportFile.exists()){
                                            exportFile.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(exportFile, false);
                                        BufferedWriter bfw = new BufferedWriter(fw);

                                        ArrayList<String> pastMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                        for(int i = 0; i < pastMessages.size(); i++){
                                            System.out.println(pastMessages.get(i));
                                            bfw.write(userName + ";" + userList.get(index).getAccountUsername() + "," + userName + "," + messageClass.getTime() + "," + pastMessages.get(i) + "\n");
                                        }

                                        bfw.flush();
                                        System.out.println("Conversation exported successfully!");
                                    } catch (IOException e){
                                        System.out.println("Unable to export file");
                                    }

                                    break;

                            }

                        }

                        break;

                    case 3:
                        System.out.println("1. change password\n2. change username\n3. change email\n4. delete account");
                        int optionProfile = scan.nextInt();
                        scan.nextLine();

                        switch (optionProfile) {
                            case 1:
                                System.out.println("What would you like to change your password to?");
                                String newPassword = scan.nextLine();
                                user.setPassword(newPassword);


                                try {
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }
                                    }

                                    pw.flush();
                                    System.out.println("Password has been successfully changed\n");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }


                                break;
                            case 2:

                                System.out.println("What would you like to change your username to?");
                                String newUsername = scan.nextLine();

                                boolean sameUsername = false;
                                for(int i = 0; i < userList.size(); i++){
                                    if(userList.get(i) instanceof Student){
                                        if(userList.get(i).getAccountUsername().equals(newUsername)){
                                            sameUsername = true;
                                            break;
                                        }
                                    }
                                }

                                if(!sameUsername){
                                    user.setAccountUsername(newUsername);
                                } else {
                                    System.out.println("Username exists.");
                                    continue;
                                }

                                try {
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }
                                    }

                                    pw.flush();

                                    System.out.println("Username has been successfully changed\n");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                            case 3:
                                System.out.println("What would you like to change your email to?");
                                String newEmail = scan.nextLine();

                                boolean sameEmail = false;
                                for(int i = 0; i < userList.size(); i++){
                                    if(userList.get(i) instanceof Student){
                                        if(userList.get(i).getEmail().equals(newEmail)){
                                            sameEmail = true;
                                            break;
                                        }
                                    }
                                }

                                if(!sameEmail){
                                    user.setEmail(newEmail);
                                } else {
                                    System.out.println("Email exists.");
                                    continue;
                                }


                                try {
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }
                                    }

                                    pw.flush();

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
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }
                                    }

                                    pw.flush();

                                    System.out.println("Account has been deleted");

                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }


                                break;

                        }

                        break;

                    case 4:
                        System.out.println("Have a nice day!");
                        signedIn = false;
                        break;
                    case 5:
                        System.out.println("Which user would you like to block?");
                        String blockUsername = scan.nextLine();

                        for(int i = 0; i < userList.size(); i++){
                            if (userList.get(i).getAccountUsername().equals(blockUsername)){
                                blockedUserList.add(userList.get(i).getAccountUsername());
                            }
                        }
//

//                        try {
//                            File blockedUsers = new File("BlockedUsers.txt");
//                            FileReader fr = new FileReader(blockedUsers);
//                            BufferedReader bfr = new BufferedReader(fr);
//                            System.out.println(blockUsername);
//
//                            String line = bfr.readLine();
//
//                            while(line != null){
//                                String[] lines = line.split(";");
//                                if(lines[0].contains(user.getAccountUsername())){
//                                    blockedUserList.add(lines[1]);
//                                }
//                                line = bfr.readLine();
//                            }
//
//                            bfr.close();
//
//                            user.setBlockedList(blockedUserList);
//
//                            if(messageClass.isBlocked(user, blockUsername)){
//                                System.out.println("User has been blocked");
//                            }
//                        } catch (IOException e){
//                            e.printStackTrace();
//                        }

                        try {
                            File blockedUsers = new File("BlockedUsers.txt");
                            if(!blockedUsers.exists()){
                                blockedUsers.createNewFile();
//                                System.out.println("Blocked Users file has been created");
                            }

                            FileWriter fr = new FileWriter(blockedUsers, true);
                            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

//                            for(int i = 0; i < blockedUserList.size(); i++){
//                                pw.println(user + "," + user.getAccountUsername() + ";" + blockUsername);
//                                pw.flush();
//                            }
                            pw.println(user + "," + user.getAccountUsername() + ";" + blockUsername);
                            pw.flush();
                        } catch (IOException e){
                            System.out.println("There are no blocked users");
                        }


                        break;
                    case 6:
                        System.out.println("Which user would you like to make invisible?");
                        String invisiblePerson = scan.nextLine();
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getAccountUsername().equals(invisiblePerson)) {
                                invisibleList.add(userList.get(i).getAccountUsername());
                            }
                        }

                        try {
                            File invisibleUsers = new File("InvisibleUsers.txt");
                            if(!invisibleUsers.exists()){
                                invisibleUsers.createNewFile();
                                System.out.println("Invisible Users file has been created");
                            }

                            FileWriter fr = new FileWriter(invisibleUsers, true);
                            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

                            pw.println(user + "," + user.getAccountUsername() + ";" + invisiblePerson);
                            pw.flush();
                        } catch (IOException e){
                            System.out.println("There are no invisible users");
                        }

                        break;
                }

                if(deletedAccount){
                    break;
                }
            } else {
                System.out.println("Tutor Interface\n\n1. View students\n2. message a user\n3. edit profile\n4. sign out\n5. block a user\n6. Become Invisible");
                int option = scan.nextInt();
                scan.nextLine();

                switch (option) {
                    case 1:
                        ArrayList<User> availableStudents = new ArrayList<User>();

                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i) instanceof Student) {
//                                try {
//                                    File blockedUsers = new File(user.getAccountUsername() + "_" + userList.get(i).getAccountUsername());
//
//                                    if(!blockedUsers.exists()){
//                                        boolean fCreated = blockedUsers.createNewFile();
//                                    }
//
//                                    FileReader fr = new FileReader(blockedUsers);
//                                    BufferedReader bfr = new BufferedReader(fr);
//
//                                    String line = bfr.readLine();
//                                    boolean blockedUser = false;
//                                    while(line != null){
//                                        if(line.equals("This person is blocked")){
//                                            blockedUser = true;
//                                            break;
//                                        }
//
//                                        line = bfr.readLine();
//
//                                    }
//
//                                    bfr.close();
                                    User person = userList.get(i);
                                if(!messageClass.isBlocked(user, person.getAccountUsername()) &&
                                        !messageClass.isInvisible(user, person.getAccountUsername())) {
                                        availableStudents.add(userList.get(i));
                                    }
//                                    else{
//                                        userList.get(i).setAccountUsername(userList.get(i).getAccountUsername() + "(blocked)");
//                                        availableStudents.add(userList.get(i));
//                                    }

//                                } catch(IOException e){
//                                    e.printStackTrace();
//                                }

                            }
                        }

                        for (int i = 0; i < availableStudents.size(); i++) {
                            if (i != availableStudents.size() - 1) {
                                System.out.print(availableStudents.get(i).getAccountUsername() + ",");
                            } else {
                                System.out.print(availableStudents.get(i).getAccountUsername() + "\n");
                            }
                        }

                        if(availableStudents.size() == 0){
                            System.out.println("There are no students available to message");
                        }

                        break;

                    case 2:

                        int index = -1;
                        boolean unableToMessage = false;
                        int optionSignIn = 0;

                        System.out.println("Who would you like to message?");
                        String person = scan.nextLine();

                        if(userList.size() == 0){
                            System.out.println("There are no students available");
                        }

//                        boolean userBlocked = false;

//                        try {
//                            File convos = new File(user.getAccountUsername() + "_" + person);
//
//                            if(!convos.exists()){
//                                boolean fCr = convos.createNewFile();
//                            }
//
//                            FileReader fr = new FileReader(convos);
//                            BufferedReader bfr = new BufferedReader(fr);
//
//                            String line = bfr.readLine();
//                            while(line != null) {
//                                if (line.equals("This person is blocked")) {
//                                    userBlocked = true;
//                                    break;
//                                }
//
//                                line = bfr.readLine();
//                            }
//
//                            bfr.close();
                        if(messageClass.isBlocked(user, person)){
                            System.out.printf("Unable to message %s\n", person);
                            break;
                        }

                        if (messageClass.isInvisible(user, person)) {
                            System.out.printf("Unable to find %s\n", person);
                            break;
                        }
//                            if(userBlocked){
//                                System.out.println("Cannot message a blocked user");
//                                break;
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).getAccountUsername().equals(person)) {
                                index = i;

                                if ((userList.get(i) instanceof Student) && (user instanceof Tutor)) {
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
                        while(quit){
                            System.out.println("0. Quit\n1. read message\n2. write a message\n3. delete a message\n4. edit a message\n5. search for a specific message\n6. import to a conversation\n7. export a conversation");
                            int optionMessage = scan.nextInt();
                            scan.nextLine();

                            switch (optionMessage) {
                                case 0:
                                    quit = false;
                                    break;
                                case 1:
                                    ArrayList<String> messages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    if(messages.size() == 0){
                                        System.out.println("No Messages Available");
                                        continue;
                                    }

                                    for (String message : messages) {
                                        System.out.println(message);
                                    }
                                    break;
                                case 2:
                                    System.out.println("Message Body:");
                                    String content = scan.nextLine();
                                    messageClass.writeMsg(userName, userList.get(index).getAccountUsername(), content);
                                    messageClass.export(userName, userList.get(index).getAccountUsername());
                                    System.out.println("Written Successfully\n");

                                    break;
                                case 3:
                                    System.out.println("What is the message that you would like to delete?");
                                    String message = scan.nextLine();

                                    ArrayList<String> allDMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    boolean DMessageExists = false;
                                    for(int i = 0; i < allDMessages.size(); i++){
                                        if(allDMessages.get(i).substring(allDMessages.get(i).lastIndexOf(",") + 1).equals(message)){
                                            DMessageExists = true;
                                        }
                                    }

                                    if(!DMessageExists){
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    messageClass.delete(user, userList.get(index).getAccountUsername(), message);
                                    System.out.println("Message Deleted Successfully\n");

                                    break;

                                case 4:
                                    System.out.println("What is the message you would like to edit?");
                                    String oldMessage = scan.nextLine();

                                    ArrayList<String> allEMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    if(allEMessages.size() == 0){
                                        System.out.println("There are no messages!");
                                        break;
                                    }

                                    boolean messageEExists = false;
                                    for(int i = 0; i < allEMessages.size(); i++){
                                        if(allEMessages.get(i).substring(allEMessages.get(i).lastIndexOf(",") + 1).equals(oldMessage)){
                                            messageEExists = true;
                                        }
                                    }

                                    if(!messageEExists){
                                        System.out.println("Message not found!");
                                        break;
                                    }

                                    System.out.println("What would you like your edited message to look like?");
                                    String newMessage = scan.nextLine();

                                    messageClass.edit(user, userList.get(index).getAccountUsername(), oldMessage, newMessage + "\n");
                                    messageClass.export(userName, userList.get(index).getAccountUsername());
                                    System.out.println("Edit Successful\n");

                                    break;

                                case 5:
                                    System.out.println("Enter a keyword to search for a message: ");
                                    String keyword = scan.nextLine();

                                    ArrayList<String> searchMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                    boolean messageFound = false;
                                    for(int i = 0; i < searchMessages.size(); i++){
                                        if(searchMessages.get(i).contains(keyword)) {
                                            System.out.println(searchMessages.get(i));
                                            messageFound = true;
                                        }
                                    }

                                    if(!messageFound){
                                        System.out.println("Message not found!");
                                    }

                                    break;
                                case 6:
                                    System.out.println("Please list the filename.");
                                    String ifileName = scan.nextLine();

                                    ArrayList<String> importMessages = new ArrayList<String>();

                                    try{
                                        File importFile = new File(ifileName);
                                        FileReader fr = new FileReader(importFile);
                                        BufferedReader bfr = new BufferedReader(fr);

                                        String line = bfr.readLine();
                                        while(line != null){
                                            importMessages.add(line);
                                            line = bfr.readLine();
                                        }

                                        BufferedWriter bfw = new BufferedWriter(new FileWriter(userName + "_" + userList.get(index).getAccountUsername(), true));
                                        for(int i = 0; i < importMessages.size(); i++){
                                            bfw.write(userName + ";" + userList.get(index).getAccountUsername() + "," + userName + "," + messageClass.getTime() + "," + importMessages.get(i) + "\n");
                                        }

                                        bfr.close();
                                        bfw.flush();

                                        messageClass.export(userName, userList.get(index).getAccountUsername());

                                        System.out.println("Imported conversation successfully!");

                                    } catch (IOException e){
                                        System.out.println("Cannot find or read from file!");
                                    }

                                    break;
                                case 7:
                                    try{
                                        System.out.println("What is the name of the file which you would like your exported contents to be placed in?");
                                        String exportFileName = scan.nextLine();

                                        File exportFile = new File(exportFileName + ".csv");

                                        if(!exportFile.exists()){
                                            exportFile.createNewFile();
                                        }

                                        FileWriter fw = new FileWriter(exportFile, false);
                                        BufferedWriter bfw = new BufferedWriter(fw);

                                        ArrayList<String> pastMessages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());

                                        for(int i = 0; i < pastMessages.size(); i++){
                                            System.out.println(pastMessages.get(i));
                                            bfw.write(userName + ";" + userList.get(index).getAccountUsername() + "," + userName + "," + messageClass.getTime() + "," + pastMessages.get(i) + "\n");
                                        }

                                        bfw.flush();
                                        System.out.println("Conversation exported successfully!");
                                    } catch (IOException e){
                                        System.out.println("Unable to export file");
                                    }

                                    break;
                            }
                        }


                        break;

                    case 3:
                        System.out.println("1. change password\n2. change username\n3. change email\n4. delete account");
                        int optionProfile = scan.nextInt();
                        scan.nextLine();

                        switch (optionProfile) {
                            case 1:
                                System.out.println("What would you like to change your password to?");
                                String newPassword = scan.nextLine();
                                user.setPassword(newPassword);

                                try {
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }
                                    }

                                    pw.flush();

                                    System.out.println("Password has been changed successfully");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }


                                break;

                            case 2:

                                System.out.println("What would you like to change your username to?");
                                String newUsername = scan.nextLine();

                                boolean similarUsername = false;
                                for(int i = 0; i < userList.size(); i++){
                                    if(userList.get(i) instanceof Tutor){
                                        if(userList.get(i).getAccountUsername().equals(newUsername)){
                                            similarUsername = true;
                                            break;
                                        }
                                    }
                                }

                                if(!similarUsername){
                                    user.setAccountUsername(newUsername);
                                } else {
                                    System.out.println("Username exists.");
                                    continue;
                                }

                                try {
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {

                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                    }

                                    pw.flush();
                                    System.out.println("Username has been changed succesfully!");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                            case 3:
                                System.out.println("What would you like to change your email to?");
                                String newEmail = scan.nextLine();

                                boolean similarEmail = false;
                                for(int i = 0; i < userList.size(); i++){
                                    if(userList.get(i) instanceof Tutor){
                                        if(userList.get(i).getEmail().equals(newEmail)){
                                            similarEmail = true;
                                            break;
                                        }
                                    }
                                }

                                if(!similarEmail){
                                    user.setEmail(newEmail);
                                } else {
                                    System.out.println("Email exists.");
                                    continue;
                                }


                                try {
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                    }

                                    pw.flush();

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
                                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + Arrays.toString(((Tutor) userList.get(i)).getSubjects()).replace(",", ";").replace(" ", "").replace("[", "").replace("]", "") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }
                                    }

                                    pw.flush();
                                    System.out.println("Account has been deleted");
                                } catch (IOException e) {
                                    System.out.println("Can't write to the file!");
                                }

                                break;

                        }

                        break;

                    case 4:
                        System.out.println("Have a nice day!");
                        signedIn = false;
                        break;
                    case 5:
                        System.out.println("Which user would you like to block?");
                        String blockUsername = scan.nextLine();

                        for(int i = 0; i < userList.size(); i++){
                            if (userList.get(i).getAccountUsername().equals(blockUsername)){
                                blockedUserList.add(userList.get(i).getAccountUsername());
                            }
                        }
//

//                        try {
//                            File blockedUsers = new File("BlockedUsers.txt");
//                            FileReader fr = new FileReader(blockedUsers);
//                            BufferedReader bfr = new BufferedReader(fr);
//                            System.out.println(blockUsername);
//
//                            String line = bfr.readLine();
//
//                            while(line != null){
//                                String[] lines = line.split(";");
//                                if(lines[0].contains(user.getAccountUsername())){
//                                    blockedUserList.add(lines[1]);
//                                }
//                                line = bfr.readLine();
//                            }
//
//                            bfr.close();
//
//                            user.setBlockedList(blockedUserList);
//
//                            if(messageClass.isBlocked(user, blockUsername)){
//                                System.out.println("User has been blocked");
//                            }
//                        } catch (IOException e){
//                            e.printStackTrace();
//                        }

                        try {
                            File blockedUsers = new File("BlockedUsers.txt");
                            if(!blockedUsers.exists()){
                                boolean blockedFile = blockedUsers.createNewFile();
                                System.out.println("Blocked Users file has been created");
                            }

                            FileWriter fr = new FileWriter(blockedUsers, true);
                            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

//                            for(int i = 0; i < blockedUserList.size(); i++){
//                                pw.println(user + "," + user.getAccountUsername() + ";" + blockUsername);
//                                pw.flush();
//                            }
                            pw.println(user + "," + user.getAccountUsername() + ";" + blockUsername);
                            pw.flush();
                        } catch (IOException e){
                            System.out.println("There are no blocked users");
                        }


                        break;
                    case 6:
                        System.out.println("Which user would you like to make invisible?");
                        String invisiblePerson = scan.nextLine();
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getAccountUsername().equals(invisiblePerson)) {
                                invisibleList.add(userList.get(i).getAccountUsername());
                            }
                        }

                        try {
                            File invisibleUsers = new File("InvisibleUsers.txt");
                            if(!invisibleUsers.exists()){
                                invisibleUsers.createNewFile();
                                System.out.println("Invisible Users file has been created");
                            }

                            FileWriter fr = new FileWriter(invisibleUsers, true);
                            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

                            pw.println(user + "," + user.getAccountUsername() + ";" + invisiblePerson);
                            pw.flush();
                        } catch (IOException e){
                            System.out.println("There are no invisible users");
                        }

                }
            }
        }
    }
}
