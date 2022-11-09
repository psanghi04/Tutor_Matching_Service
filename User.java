public class User {
  private String accountUsername; // username of user account
  private String password; // password of user account
  private String email; // email of user account
  private User[] blockedList; // list of all blocked users

  public User(String accountUsername, String password, String email, User[] blockedList){
    this.accountUsername = accountUsername; // sets the current instance of accountUsername to the parameter accountUsername
    this.password = password; // sets the current instance of password to the parameter password
    this.email = email; // sets the current instance of email to the parameter email
    this.blockedList = blockedList; // sets the current instance of blockedList to the parameter blockedList
  }
  
  // get account username
  public String getAccountUsername(){
    return accountUsername;
  }

  // get password
  public String getPassword(){
    return password;
  }

  // get email
  public String getEmail(){
    return email;
  }

  // get isBlocked
  public User[] blockedList(){
    return blockedList;
  }

  // set accountUsername
  public void setAccountUsername(String accountUsername){
    this.accountUsername = accountUsername;
  }

  // set password
  public void setPassword(String password){
    this.password = password;
  }

  // set email
  public void setEmail(String email){
    this.email = email;
  }

  // set blocked status 
  public void setBlocked(User[] blockedList){
    this.blockedList = blockedList;
  }

}
