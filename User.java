import java.util.ArrayList;
public class User {
  private String accountUsername; // username of user account
  private String password; // password of user account
  private String email; // email of user account
  private ArrayList<User> blockedList; // list of all blocked users
  private ArrayList<User> invisibleList;

  public User(String accountUsername, String password, String email, ArrayList<User> blockedList, ArrayList<User> invisibleList) {
    this.accountUsername = accountUsername; // sets the current instance of accountUsername to the parameter accountUsername
    this.password = password; // sets the current instance of password to the parameter password
    this.email = email; // sets the current instance of email to the parameter email
    this.blockedList = blockedList; // sets the current instance of blockedList to the parameter blockedList
    this.invisibleList = invisibleList;
  }
  
  // get account username
  public String getAccountUsername() {
    return accountUsername;
  }

  // get password
  public String getPassword() {
    return password;
  }

  // get email
  public String getEmail() {
    return email;
  }

  // get isBlocked
  public ArrayList<User> getBlockedList() {
    return blockedList;
  }

  // set accountUsername
  public void setAccountUsername(String accountUsername) {
    this.accountUsername = accountUsername;
  }

  // set password
  public void setPassword(String password) {
    this.password = password;
  }

  // set email
  public void setEmail(String email) {
    this.email = email;
  }

  // set blocked status 
  public void setBlocked(ArrayList<User> blockedList) {
    this.blockedList = blockedList;
  }

  public ArrayList<User> getInvisibleList() {
    return invisibleList;
  }

  public void setInvisibleList(ArrayList<User> invisibleList) {
    this.invisibleList = invisibleList;
  }
}
