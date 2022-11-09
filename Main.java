import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    Message messageClass = new Message();

    boolean login = false;
    ArrayList<User> userList = new ArrayList<User>();
    
    while(login == false){
      System.out.println("Do you want to log in or create a account?");
      System.out.println("C to Create New Account " + "\nL to Login");

      if(scan.nextLine().equals("C")){
        System.out.println("Welcome to Tutoring Center!");

        System.out.println("Enter new username: ");
        String userName = scan.nextLine();
      
        System.out.println("Enter your email: ");
        String email = scan.nextLine();
      
        System.out.println("Create new password: ");
        String password = scan.nextLine();
  
        User user = new User(userName, password, email);

        userList.add(user);
        
      } else {

        System.out.println("Username: ");
        String userName = scan.nextLine();

        System.out.println("Password: ");
        String password = scan.nextLine();

        for(int i = 0; i < userList.size(); i++){
          if(userList.get(i).getAccountUsername().equals(userName)){
            if(userList.get(i).getPassword().equals(password)){
              System.out.println("Successfully Signed In!");
              login = true;
              break;
            }
          }
        }

        if(login){
          System.out.println("Welcome Back");
        } else {
          System.out.println("Invalid Username or Password");
          System.out.println("Try Again");
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
