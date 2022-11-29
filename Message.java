import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;

public class Message {
    public boolean isBlocked(User user, String personBlocked){
//        ArrayList<String> blocked = new ArrayList<String>();
//        for(int i=0;i<user.getBlockedList().size();i++){
//            blocked.add(user.getBlockedList().get(i));
//        }
//        for (String s : blocked) {
//            if (s.equals(personBlocked)) {
//                try {
//                    File f = new File(user.getAccountUsername() + "_" + personBlocked);
//                    BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
//                    bfw.write("This person is blocked");
//                    bfw.close();
//                    return true;
//                    // break;
//                } catch (IOException e) {
//                    System.out.println("This chat does not exist");
//                }
//            }
//        }
//        return false;
        String[] lineArr;
        try (BufferedReader bfr = new BufferedReader(new FileReader("BlockedUsers.txt"))) {
            String line = bfr.readLine();
            System.out.println(user.getAccountUsername());
            while (line != null) {
                lineArr = line.split(";");
                if (lineArr[0].contains(personBlocked) && lineArr[1].equals(user.getAccountUsername())) {
                    System.out.println("bye");
                    return true;
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        System.out.println("Hi");
    return false;
    } // if he is blocked/invisible and correct user

    public boolean blockedStatus(User user, String personBlocked){
        try{
            File f = new File(user.getAccountUsername() + "_" + personBlocked);
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            while(true){
                String line = bfr.readLine();
                if(line == null){
                    break;
                }
                if(line.equals("This person is blocked")){
                    return true;
                }
            }
        } catch(Exception e){
            System.out.println("This chat does not exist");
        }
        return false;
    }

    public boolean isInvisible(User user, String personInvisible){
        for(int i=0;i<user.getInvisibleList().size();i++){
            if(user.getInvisibleList().get(i).equals(personInvisible)){
                return true;
            }
        }
        return false;
    }
    public void edit(User user, String otherPerson, String message, String edit) {
        ArrayList<String> conversation = new ArrayList<String>();
        try {
            File f = new File(user.getAccountUsername() + "_" + otherPerson);
            if(!f.exists()){
                f.createNewFile();
            }
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

            if(f.exists()){
                f.createNewFile();
            }

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

        try {
            File f = new File(sender + "_" + receiver);
            if(f.exists()){
                f.createNewFile();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(f, true));

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
        File f = new File(fileName);

        try{
            if(!f.exists()){
                f.createNewFile();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(f));

            for (String msg : messages) {
                bfw.write(msg);
                bfw.newLine();
                bfw.flush();
            }
        } catch (IOException e){
            System.out.println("Error!");
        }


    }

}
