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

        try{
            if(!f.exists()){
                boolean fCreated = f.createNewFile();
                System.out.println("File has been created..");
            }
        } catch (IOException e){
            System.out.println("Cannot write to file!");
        }


        while (login != true) {
            
            System.out.println("Do you want to log in or create a account?");
            System.out.println("1. Create New Account\n" +
                    "2. Login");
            int optionNum = scan.nextInt();
            scan.nextLine();

            User user = null; // currentUser logged in

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
                        user = new Student(userName, password, email);

                        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))){
                            pw.write(userName + "," + password + "," + email + "\n");
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

                        user = new Tutor(userName, password, email, newSubjects, price);

                        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))){
                            pw.write(userName + "," + password + "," + email + "," + newSubjects + "," + price + "\n");
                            pw.flush();
                        } catch (IOException e) {
                            System.out.println("Unable to write file");
                        }
                    }

                    userList.add(user);

                    System.out.println("Congratulations on creating a new account!!\n");

                    continue;

                case 2:
                    while(login == false){
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


            int index = -1;
            boolean unableToMessage = false;
            int optionSignIn = 0;

            while(index == -1 || unableToMessage){
                for (int i = 0; i < userList.size(); i++) {
                    System.out.println("Who would you like to message?");
                    String person = scan.nextLine();

                    if (userList.get(i).getAccountUsername().equals(person)) {
                        index = i;

                        // Testing
                        System.out.println(userList.get(i).getAccountUsername());
                        System.out.println(user.getAccountUsername());

                        if(((userList.get(i) instanceof Student) && (user instanceof Tutor)) || ((userList.get(i) instanceof Tutor) && (user instanceof Student))){
                            System.out.println("Person Found");
                            unableToMessage = false;
                            break;
                        } else {
                            System.out.println("You cannot message this person");
                            unableToMessage = true;
                        }
                    }
                }

                if(index == -1 || unableToMessage){
                    if(index == -1){
                        System.out.println("Unable to find person.");
                    }

                    System.out.println("Want to try again?");
                    System.out.println("1. Yes\n2. No");

                    int optionTryAgain = scan.nextInt();
                    scan.nextLine();

                    if(optionTryAgain == 2){
                        System.out.println("Do you want to sign in or create a new account?");
                        System.out.println("1. Yes\n2. No");
                        optionSignIn = scan.nextInt();
                        break;
                    }
                }
            }

            if(optionSignIn == 1){
                login = false;
                continue;
            }


            while (true) {
                System.out.println("What would you like to do?");
                System.out.println("1. read message\n2. write a message\n3. delete a message\n4. edit a message\n5. change password\n6. change username\n7. change email\n8. delete account");
                int ans = scan.nextInt();
                scan.nextLine();

                switch (ans) {
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

                    case 5:
                        System.out.println("What would you like to change your password to?");
                        String newPassword = scan.nextLine();
                        user.setPassword(newPassword);

                        for(int i = 0; i < userList.size(); i++){
                            try{
                                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
                                if(userList.get(i) instanceof Student){
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "\n");
                                } else {
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor)userList.get(i)).getSubjects() + "," + ((Tutor)userList.get(i)).price());
                                }

                                pw.flush();
                            } catch (IOException e){
                                System.out.println("Can't write to the file!");
                            }
                        }

                    case 6:
                        System.out.println("What would you like to change your username to?");
                        String newUsername = scan.nextLine();
                        user.setAccountUsername(newUsername);

                        for(int i = 0; i < userList.size(); i++){
                            try{
                                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
                                if(userList.get(i) instanceof Student){
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "\n");
                                } else {
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor)userList.get(i)).getSubjects() + "," + ((Tutor)userList.get(i)).price());
                                }

                                pw.flush();
                            } catch (IOException e){
                                System.out.println("Can't write to the file!");
                            }
                        }

                    case 7:
                        System.out.println("What would you like to change your email to?");
                        String newEmail = scan.nextLine();
                        user.setEmail(newEmail);

                        for(int i = 0; i < userList.size(); i++){
                            try{
                                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
                                if(userList.get(i) instanceof Student){
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "\n");
                                } else {
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor)userList.get(i)).getSubjects() + "," + ((Tutor)userList.get(i)).price());
                                }

                                pw.flush();
                            } catch (IOException e){
                                System.out.println("Can't write to the file!");
                            }
                        }

                    case 8:
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
                        for(int i = 0; i < userList.size(); i++){
                            try{
                                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
                                if(userList.get(i) instanceof Student){
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "\n");
                                } else {
                                    pw.write(userList.get(i).getAccountUsername() + ","  + userList.get(i).getPassword() + "," + userList.get(i).getEmail() + "," + ((Tutor)userList.get(i)).getSubjects() + "," + ((Tutor)userList.get(i)).price());
                                }

                                pw.flush();
                            } catch (IOException e){
                                System.out.println("Can't write to the file!");
                            }
                        }
                }

                System.out.println("Would you like to choose again");
                System.out.println("1. Yes\n2. No");
                int option = scan.nextInt();

                if (option == 2) {
                    break;
                }
            }

            System.out.println("Would you like to sign out or exit the application?");
            System.out.println("1. Sign Out\n2. Exit the Application");

            int optionSignOut = scan.nextInt();

            if(optionSignOut == 2){
                break;
            }

        }

    }
}
