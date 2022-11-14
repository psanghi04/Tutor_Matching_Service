import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Message messageClass = new Message();

        boolean login = false;
        ArrayList<User> userList = new ArrayList<User>();
        String userName = "";
        String password;

        System.out.println("Welcome to Tutoring Center!");


        File f = new File("UserDetails.txt");

        try {
            if (!f.exists()) {
                boolean fCreated = f.createNewFile();
                System.out.println("File has been created..");
            }

            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            String userLine = bfr.readLine();
            while (userLine != null) {
                String[] splitLines = userLine.split(",");

                User user;

                if (splitLines[3].equals("Student")) {
                    user = new Student(splitLines[0], splitLines[1], splitLines[2]);
                } else {
                    user = new Tutor(splitLines[0], splitLines[1], splitLines[2], splitLines[3].split(";"), Double.parseDouble(splitLines[4]));
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

                        if(accountSimilarity == false){
                            user = new Student(userName, password, email);
                        }else{
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

                        if(accountSimilarity == false){
                            user = new Tutor(userName, password, email, newSubjects, price);
                        }else{
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
                    while (login == false) {
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
                System.out.println("Tutor Interface\n\n1. View tutors\n2. message someone\n3. edit profile\n4. sign out");
                int option = scan.nextInt();
                scan.nextLine();

                switch (option) {
                    case 1:
                        ArrayList<User> availableTutors = new ArrayList<User>();

                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i) instanceof Tutor) {
                                availableTutors.add(userList.get(i));
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
                            System.out.println("There are no tutors available");
                        }

                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).getAccountUsername().equals(person)) {
                                index = i;

                                // Testing
                                System.out.println(userList.get(i).getAccountUsername());
                                System.out.println(user.getAccountUsername());

                                if ((userList.get(i) instanceof Tutor) && (user instanceof Student)) {
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


                        System.out.println("1. read message\n2. write a message\n3. delete a message\n4. edit a message");
                        int optionMessage = scan.nextInt();
                        scan.nextLine();

                        switch (optionMessage) {

                            case 1:
                                ArrayList<String> messages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());
                                for (String message : messages) {
                                    System.out.println(message);
                                }
                                break;
                            case 2:
                                System.out.println("Message Body:");
                                String content = scan.nextLine();
                                messageClass.writeMsg(userName, userList.get(index).getAccountUsername(), content);
                                messageClass.export(userName, userList.get(index).getAccountUsername());
                                System.out.println("Written Successfully");

                                break;
                            case 3:
                                System.out.println("What is the message that you would like to delete?");
                                String message = scan.nextLine();

                                messageClass.delete(user, userList.get(index).getAccountUsername(), message);
                                System.out.println("Message Deleted Successfully");

                                break;

                            case 4:
                                System.out.println("What is the message you would like to edit?");
                                String oldMessage = scan.nextLine();

                                System.out.println("What would you like your edited message to look like?");
                                String newMessage = scan.nextLine();

                                messageClass.edit(user, userList.get(index).getAccountUsername(), oldMessage, newMessage);
                                messageClass.export(userName, userList.get(index).getAccountUsername());

                                break;
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

                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                        pw.flush();
                                        System.out.println("Password has been successfully changed\n");
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
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

                                if(sameUsername == false){
                                    user.setAccountUsername(newUsername);
                                } else {
                                    System.out.println("Username exists.");
                                    continue;
                                }

                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                        pw.flush();
                                        System.out.println("Username has been successfully changed\n");
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
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

                                if(sameEmail == false){
                                    user.setEmail(newEmail);
                                } else {
                                    System.out.println("Email exists.");
                                    continue;
                                }

                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + ","  + "Tutor\n");
                                        }

                                        pw.flush();
                                        System.out.println("Email has been successfully changed\n");
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
                                }

                                break;

                            case 4:
                                System.out.println("Deleting Account...");

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
                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                        pw.flush();
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
                                }

                                break;

                        }

                        break;

                    case 4:
                        System.out.println("Have a nice day!");
                        signedIn = false;
                        break;
                }
            } else {
                System.out.println("1. View students\n2. message someone\n3. edit profile\n4. sign out");
                int option = scan.nextInt();
                scan.nextLine();

                switch (option) {
                    case 1:
                        ArrayList<User> availableStudents = new ArrayList<User>();

                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i) instanceof Student) {
                                availableStudents.add(userList.get(i));
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

                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).getAccountUsername().equals(person)) {
                                index = i;

                                // Testing
                                System.out.println(userList.get(i).getAccountUsername());
                                System.out.println(user.getAccountUsername());

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


                        System.out.println("1. read message\n2. write a message\n3. delete a message\n4. edit a message");
                        int optionMessage = scan.nextInt();
                        scan.nextLine();

                        switch (optionMessage) {

                            case 1:
                                ArrayList<String> messages = messageClass.readMsg(userName, userList.get(index).getAccountUsername());
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

                                messageClass.delete(user, userList.get(index).getAccountUsername(), message);
                                System.out.println("Message Deleted Successfully\n");

                                break;

                            case 4:
                                System.out.println("What is the message you would like to edit?");
                                String oldMessage = scan.nextLine();

                                System.out.println("What would you like your edited message to look like?");
                                String newMessage = scan.nextLine();

                                messageClass.edit(user, userList.get(index).getAccountUsername(), oldMessage, newMessage);
                                messageClass.export(userName, userList.get(index).getAccountUsername());
                                System.out.println("Edit Successful\n");

                                break;
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

                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() +  ","  + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + ","  + "Tutor\n");
                                        }

                                        pw.flush();
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
                                }

                                break;

                            case 2:

                                System.out.println("What would you like to change your username to?");
                                String newUsername = scan.nextLine();

                                boolean similarUsername = false;
                                for(int i = 0; i < userList.size(); i++){
                                    if(userList.get(i) instanceof Tutor){
                                        if(userList.get(i).getAccountUsername().equals(userName)){
                                            similarUsername = true;
                                            break;
                                        }
                                    }
                                }

                                if(similarUsername == false){
                                    user.setAccountUsername(newUsername);
                                } else {
                                    System.out.println("Username exists.");
                                    continue;
                                }

                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                        pw.flush();
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
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

                                if(similarEmail == false){
                                    user.setEmail(newEmail);
                                } else {
                                    System.out.println("Email exists.");
                                    continue;
                                }

                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + ","  + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + "," + "Tutor\n");
                                        }

                                        pw.flush();
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
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
                                for (int i = 0; i < userList.size(); i++) {
                                    try {
                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                                        if (userList.get(i) instanceof Student) {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + ","  + "Student\n");
                                        } else {
                                            pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor) userList.get(i)).getSubjects().toString().replace(",", ";") + "," + ((Tutor) userList.get(i)).price() + ","  + "Tutor\n");
                                        }

                                        pw.flush();
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }
                                }

                                break;

                        }

                        break;

                    case 4:
                        System.out.println("Have a nice day!");
                        signedIn = false;
                        break;
                }
            }
        }
    }
}
