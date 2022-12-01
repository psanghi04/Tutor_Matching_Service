import java.util.ArrayList;
import java.util.UUID;

public class User {
    private String accountUsername; // username of user account
    private String password; // password of user account
    private String email; // email of user account
    private ArrayList<String> blockedList; // list of all blocked users
    private ArrayList<String> invisibleList;

    private UUID ID;

    public User(String accountUsername, String password, String email, ArrayList<String> blockedList, ArrayList<String> invisibleList) {
        this.accountUsername = accountUsername; // sets the current instance of accountUsername to the parameter accountUsername
        this.password = password; // sets the current instance of password to the parameter password
        this.email = email; // sets the current instance of email to the parameter email
        this.blockedList = blockedList; // sets the current instance of blockedList to the parameter blockedList
        this.invisibleList = invisibleList;
        this.ID = UUID.randomUUID();
    }

    public User(String accountUsername, String password, String email, ArrayList<String> blockedList, ArrayList<String> invisibleList, UUID ID) {
        this.accountUsername = accountUsername; // sets the current instance of accountUsername to the parameter accountUsername
        this.password = password; // sets the current instance of password to the parameter password
        this.email = email; // sets the current instance of email to the parameter email
        this.blockedList = blockedList; // sets the current instance of blockedList to the parameter blockedList
        this.invisibleList = invisibleList;
        this.ID = ID;
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

    // get isBlocked
    public ArrayList<String> getBlockedList() {
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
    public void setBlockedList(ArrayList<String> blockedList) {
        this.blockedList = blockedList;
    }

    public ArrayList<String> getInvisibleList() {
        return invisibleList;
    }

    public void setInvisibleList(ArrayList<String> invisibleList) {
        this.invisibleList = invisibleList;
    }

}
