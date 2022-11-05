import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class Message {
    private Tutor tutor;
    private Student student;
    private String fileName;

    public void isValid(){} // if he is blocked/invisible and correct user
    public void edit(){}
    public void delete(){}
    public String getTime() {
        Timestamp currentTime = Timestamp.from(Instant.now()); // gets current system time
        return currentTime.toString().substring(0, 19); // returns time with precision as seconds
    }

    public ArrayList<String> readMsg() {
        ArrayList<String> messages = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(this.fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                messages.add(line);
                line = bfr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Conversation does not exist!");
        } catch (IOException e) {
            System.out.println("Reading suspended. I/O Exception caught.");
        }

        return messages;
    }
    public void writeMsg(String sender, String content) {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(this.fileName,true))) {
            String line = "%s_%s,%s,%s,%s";
            bfw.write(String.format(
                    line,
                    this.tutor.getName(),
                    this.student.getName(),
                    sender,
                    this.getTime(),
                    content ));
            bfw.newLine();
            bfw.flush();
        } catch (Exception e) {
            System.out.println("Writing suspended. Exception caught");
        }
    }
    public void export(/*<which conversations>*/){}
    public void existing() {}
}
