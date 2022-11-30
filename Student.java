import java.util.ArrayList;

public class Student extends User {

    String filter = "****";
    ArrayList<String> filterWordList = new ArrayList<>();

    public Student(String message, String accountUsername, String email, ArrayList<String> blockedList, ArrayList<String> invisibleList) {
        super(message, accountUsername, email, blockedList, invisibleList);
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
