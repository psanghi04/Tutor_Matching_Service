public class Tutor extends User {
  private String[] subjects;
  double price;
  ArrayList<Store> stores = new ArrayList<Stores>();
  
  public Tutor(String accountUsername, String password, String email, User[] blockedList, String[] subjects, double price, ArrayList<Store> stores){
    super(accountUsername, password, email, blockedList);
    this.subjects = subjects;
    this.price = price;
    this.stores = stores;
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
  
  public ArrayList<Store> getStores(){
    return stores;
  }
  
  public ArrayList<Store> setStores(ArrayList<Store> stores){
    this.stores = new ArrayList<Store>();
  }
  
  public void addStore(Store store){
    stores.add(store);
  }
}
