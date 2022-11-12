import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    Message messageClass = new Message();

    boolean login = false;
    ArrayList<User> userList = new ArrayList<>();
    String userName;
    String email;
    String password;

    System.out.println("Welcome to the Purdue University Tutoring Center!");

    while (!login) {
      System.out.println("Do you want to create a new account or log in to an existing account?");
      System.out.println("1. Create New Account\n" +
              "2. Login to Existing Account");
      int optionNum = scan.nextInt();
      scan.nextLine();

      switch (optionNum) {
        case 1:
          System.out.println("Create a username: ");
          userName = scan.nextLine();

          System.out.println("Enter your email: ");
          email = scan.nextLine();

          System.out.println("Create a password: ");
          password = scan.nextLine();

          User user = new User(userName, password, email);

          userList.add(user);
          break;

        case 2:
          System.out.println("Enter your username: ");
          userName = scan.nextLine();

          System.out.println("Enter your password: ");
          password = scan.nextLine();

          for (User acc : userList) {
            if (acc.getAccountUsername().equals(userName)) {
              if (acc.getPassword().equals(password)) {
                System.out.println("Log in Successful!");
                login = true;
                break;
              }
            }
          }

          if (login) {
            System.out.println("Welcome Back!");
          } else {
            System.out.println("Invalid Username or Password!");
            System.out.println("Please try again.");
          }
          break;

        default:
          System.out.println("Invalid Option Number!");
          System.out.println("Please try again.");
      }
    }

      String ans = ""; // Assuming options are 1. read, 2. write, 3. delete, 4. edit
      System.out.println("Tutor Name: ");
      String receiver = scan.nextLine();
      System.out.println();
      switch (ans) {
        case "1":
          ArrayList<String> messages = messageClass.readMsg(userName, receiver);
          for (String message : messages) {
            System.out.println(message);
          }
          break;

        case "2":
          System.out.println("Message Body:\n");
          String content = scan.nextLine();
          messageClass.writeMsg(userName, receiver, content);
          System.out.println("Written Successfully");
      }
    }    
  }
}
