import java.util.ArrayList;
import java.util.UUID;

public class User {
    private String accountUsername; // username of user account
    private String password; // password of user account
    private String email; // email of user account

    private final UUID ID;

    private String filter;

    private ArrayList<String> filterWordList;

    public User(String accountUsername, String password, String email, String filter, ArrayList<String> filterWordList) {
        this.accountUsername = accountUsername; // sets the current instance of accountUsername to the parameter accountUsername
        this.password = password; // sets the current instance of password to the parameter password
        this.email = email; // sets the current instance of email to the parameter email
        this.ID = UUID.randomUUID();
        this.filter = filter;
        this.filterWordList = filterWordList;
    }

    public User(String accountUsername, String password, String email, UUID ID, String filter, ArrayList<String> filterWordList) {
        this.accountUsername = accountUsername; // sets the current instance of accountUsername to the parameter accountUsername
        this.password = password; // sets the current instance of password to the parameter password
        this.email = email; // sets the current instance of email to the parameter email
        this.ID = ID;
        this.filter = filter;
        this.filterWordList = filterWordList;
    }

    public UUID getID() {
        return ID;
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
}
