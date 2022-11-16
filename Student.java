import java.util.ArrayList;

public class Student extends User {

    public Student(String message, String accountUsername, String email, ArrayList<User> blockedList, ArrayList<User> invisibleList) {
        super(message, accountUsername, email, blockedList, invisibleList);
    }

}
