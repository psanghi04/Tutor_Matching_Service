import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class Message {
    public void isValid(){} // if he is blocked/invisible and correct user
    public void edit(){}
    public void delete(){}
    public String getTime() {
        Timestamp currentTime = Timestamp.from(Instant.now()); // gets current system time
        return currentTime.toString().substring(0, 19); // returns time with precision as seconds
    }

    public ArrayList<String> readMsg(String sender, String receiver) {
        ArrayList<String> messages = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(sender + "_" + receiver))) {
            String line = bfr.readLine();
            while (line != null) {
                messages.add(line);
                line = bfr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Conversation does not exist!");
        } catch (IOException e) {
            System.out.println("Reading suspended. I/O Exception caught.");
        } catch (Exception e) {
            System.out.println("There was an error. Exception caught.");
        }
        return messages;
    }
    
    public void writeMsg(String sender, String receiver, String content) {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(sender + "_" + receiver, true))) {
            String line = "%s;%s,%s,%s,%s";
            bfw.write(String.format(
                    line,
                    sender,
                    receiver,
                    sender,
                    this.getTime(),
                    content));
            bfw.newLine();
            bfw.flush();
        } catch (Exception e) {
            System.out.println("Writing suspended. Exception caught");
        }
    }
    
    public void export(String sender, String receiver) {
        ArrayList<String> messages = readMsg(sender, receiver);
        String fileName = sender + "_" + receiver;
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(fileName))) {
            for (String msg : messages) {
                bfw.write(msg);
                bfw.flush();
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }
    
    public void existing() {}
}
