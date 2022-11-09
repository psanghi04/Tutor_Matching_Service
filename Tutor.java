public class Tutor extends User {
  private String[] subjects;
  double price;
  
  public Tutor(String accountUsername, String password, String email, boolean[] blockedList, String[] subjects, double price){
    super(accountUsername, password, email, blockedList);
    this.subjects = subjects;
    this.price = price;
  }
  
  public String[] getSubjects(){
    return subjects;
  }
  
  public void setSubjects(String[] subjects){
    this.subjects = subjects;
  }
  
  public double price(){
    return price;
  }
  
  public void setPrice(double price){
    this.price = price;
  }
}
