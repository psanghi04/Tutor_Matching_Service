public class Tutor extends User {
  private String[] subjects;
  
  public Tutor(String accountUsername, String password, String email, boolean[] blockedList, String[] subjects){
    super(accountUsername, password, email, blockedList);
    this.subjects = subjects;
  }
  
  public String[] getSubjects(){
    return subjects;
  }
  
  public void setSubjects(String[] subjects){
    this.subjects = subjects;
  }
}
