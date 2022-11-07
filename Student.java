public class Student extends User {
  private User[] users; // list of users
  private User user; // a specific user

  public Student(String message, String accountUsername, String email, boolean[] blockedList, User[] users, User user){
    super(message, accountUsername, email, blockedList);
    this.users = users;
    this.user = user;
  } 

  // get a list of users
  private User[] getUsers(){
    return users;
  }

  // get a user 
  public User getUser(){
    return user;
  }

  // set a user list
  private void setUsers(User[] users){
    this.users = users;
  }

  // set the user
  public void setUser(User user){
    this.user = user;
  }
  
  
}