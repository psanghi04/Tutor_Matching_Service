import java.util.ArrayList;
import java.util.UUID;

public class Student extends User {

    String filter;
    ArrayList<String> filterWordList;

    public Student(String message, String accountUsername, String email,
                   ArrayList<String> blockedList, ArrayList<String> invisibleList,
                   String filter, ArrayList<String> filterWordList) {
        super(message, accountUsername, email, blockedList, invisibleList);
        this.filter = filter;
        this.filterWordList = filterWordList;
    }

    public Student(String message, String accountUsername, String email,
                   ArrayList<String> blockedList, ArrayList<String> invisibleList,
                   String filter, ArrayList<String> filterWordList, UUID ID) {
        super(message, accountUsername, email, blockedList, invisibleList, ID);
        this.filter = filter;
        this.filterWordList = filterWordList;
    }

    @Override
    public String toString() {
        return "Student";
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
