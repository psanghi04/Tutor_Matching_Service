import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
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
              break;
            }
          }
        }
        
        System.out.println("Welcome Back");
        login = true;
      }
      
    }    
  }
}
