import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.ArrayList;

public class Message {
//    public void isBlocked(User user, String personBlocked){
//        ArrayList<User> blocked = new ArrayList<User>();
//        blocked = user.getBlockedList();
//        for(int i=0;i<blocked.size();i++){
//            if(blocked.get(i).getAccountUsername().equals(personBlocked)){
//                try{
//                    File f = new File(user.getAccountUsername() + "_" + personBlocked);
//                    BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
//                    bfw.write("This person is blocked");
//                    bfw.close();
//                    break;
//                } catch(IOException e){
//                    System.out.println("This chat does not exist");
//                }
//            }
//        }
//    } // if he is blocked/invisible and correct user

    //    public boolean isInvisible(User user, String personInvisible){
//        for(int i=0;i<user.getInvisibleList().size();i++){
//            if(user.getInvisibleList().get(i).getAccountUsername().equals(personInvisible)){
//                return true;
//            }
//        }
//        return false;
//    }
    public void edit(User user, String otherPerson, String message, String edit) {
        ArrayList<String> conversation = new ArrayList<String>();
        try {
            File f = new File(user.getAccountUsername() + "_" + otherPerson);
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            while (true) {
                String line = bfr.readLine();

                if (line == null) {
                    break;
                }

                conversation.add(line);
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
            for (int i = 0; i < conversation.size(); i++) {

                if (conversation.get(i).contains(message)) {
                    bfw.write(conversation.get(i).replace(message, edit));
                    continue;
                }
                bfw.write(conversation.get(i) + "\n");
            }
            bfr.close();
            bfw.close();
        } catch (IOException e) {
            System.out.println("This chat does not exist");
        }
    }

    public void delete(User user, String otherPerson, String messageDeleted) {
        ArrayList<String> conversation = new ArrayList<String>();
        try {
            File f = new File(user.getAccountUsername() + "_" + otherPerson);
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            while (true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                conversation.add(line);
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
            for (int i = 0; i < conversation.size(); i++) {
                if (conversation.get(i).contains(messageDeleted)) {
                    continue;
                }
                bfw.write(conversation.get(i) + "\n");
            }
            bfw.close();
        } catch (IOException e) {
            System.out.println("This chat does not exist");
        }
    }

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
        String fileName = receiver + "_" + sender;
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(fileName))) {
            for (String msg : messages) {
                bfw.write(msg);
                bfw.newLine();
                bfw.flush();
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

}
