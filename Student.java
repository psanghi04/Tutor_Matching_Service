import java.util.ArrayList;
import java.util.UUID;

public class Student extends User {

    public Student(String message, String accountUsername, String email,
                   String filter, ArrayList<String> filterWordList) {
        super(message, accountUsername, email, filter, filterWordList);
    }

    public Student(String message, String accountUsername, String email,
                   String filter, ArrayList<String> filterWordList, UUID ID) {
        super(message, accountUsername, email, ID, filter, filterWordList);
    }

    @Override
    public String toString() {
        return "Student";
    }
}
