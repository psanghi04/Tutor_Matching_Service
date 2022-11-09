public class Student extends User {

  public Student(String message, String accountUsername, String email, boolean[] blockedList, User[] users, User user){
    super(message, accountUsername, email, blockedList);
    this.users = users;
    this.user = user;
  } 
  
}
