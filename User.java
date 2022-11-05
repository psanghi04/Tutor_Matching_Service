public class User {
  private String accountUsername;
  private String password;
  private String email;
  private boolean[] blockedList;

  public User(String accountUsername, String password, String email, boolean[] blockedList){
    this.accountUsername = accountUsername;
    this.password = password;
    this.email = email;
    this.blockedList = blockedList;
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
  public boolean[] blockedList(){
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
  public void setBlocked(boolean[] blockedList){
    this.blockedList = blockedList;
  }

}
