import java.util.ArrayList;
import java.util.UUID;

public class Tutor extends User {
    private String[] subjects;
    double price;
    String filter;
    ArrayList<String> filterWordList;

    public Tutor(String accountUsername, String password, String email,
                 String[] subjects, double price, String filter,
                 ArrayList<String> filterWordList) {
        super(accountUsername, password, email);
        this.subjects = subjects;
        this.price = price;
        this.filter = filter;
        this.filterWordList = filterWordList;
    }

    public Tutor(String accountUsername, String password, String email,
                 String[] subjects, double price, String filter,
                 ArrayList<String> filterWordList, UUID ID) {
        super(accountUsername, password, email, ID);
        this.subjects = subjects;
        this.price = price;
        this.filter = filter;
        this.filterWordList = filterWordList;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    public double price() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ArrayList<String> getFilterWordList() {
        return filterWordList;
    }

    public void setFilterWordList(ArrayList<String> filterWordList) {
        this.filterWordList = filterWordList;
    }

    @Override
    public String toString() {
        return "Tutor";
    }
}
