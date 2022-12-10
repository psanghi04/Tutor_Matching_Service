import java.util.ArrayList;
import java.util.UUID;

public class Tutor extends User {
    private String[] subjects;
    double price;

    public Tutor(String accountUsername, String password, String email,
                 String[] subjects, double price, String filter,
                 ArrayList<String> filterWordList) {
        super(accountUsername, password, email, filter, filterWordList);
        this.subjects = subjects;
        this.price = price;
    }

    public Tutor(String accountUsername, String password, String email,
                 String[] subjects, double price, String filter,
                 ArrayList<String> filterWordList, UUID ID) {
        super(accountUsername, password, email, ID, filter, filterWordList);
        this.subjects = subjects;
        this.price = price;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public double price() {
        return price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Tutor";
    }
}
