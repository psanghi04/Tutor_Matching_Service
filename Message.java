import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Message {
    public boolean isBlocked(User user, User personBlocked) {

        String[] lineArr;
        try (BufferedReader bfr = new BufferedReader(new FileReader("BlockedUsers.txt"))) {
            String line = bfr.readLine();
            while (line != null) {
                lineArr = line.split(";");
                if (UUID.fromString(lineArr[0].split(",")[1]).compareTo(personBlocked.getID()) == 0 &&
                        UUID.fromString(lineArr[1]).compareTo(user.getID()) == 0) {
                    return true;
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    } // if he is blocked/invisible and correct user

    public boolean isInvisible(User user, User personInvisible) {

        String[] lineArr;
        try (BufferedReader bfr = new BufferedReader(new FileReader("InvisibleUsers.txt"))) {
            String line = bfr.readLine();
            while (line != null) {
                lineArr = line.split(";");
                if (UUID.fromString(lineArr[0].split(",")[1]).compareTo(personInvisible.getID()) == 0 &&
                        UUID.fromString(lineArr[1]).compareTo(user.getID()) == 0) {
                    return true;
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void edit(User user, User otherPerson, String message, String edit) {
        ArrayList<String> conversation = new ArrayList<>();
        try {
            File f = new File(user.getID() + "_" + otherPerson.getID());
            if (!f.exists()) {
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

    public void delete(User user, User otherPerson, String messageDeleted) {
        ArrayList<String> conversation = new ArrayList<>();
        try {
            File f = new File(user.getID() + "_" + otherPerson.getID());

            if (f.exists()) {
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

    // remove
//    public ArrayList<String> readMsg(String sender, String receiver) {
//        ArrayList<String> messages = new ArrayList<>();
//
//        try (BufferedReader bfr = new BufferedReader(new FileReader(sender + "_" + receiver))) {
//            String line = bfr.readLine();
//            while (line != null) {
//                String message = line.split(",")[1] + ": " + line.split(",")[3];
//                messages.add(message);
//                line = bfr.readLine();
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Conversation does not exist!");
//        } catch (IOException e) {
//            System.out.println("Reading suspended. I/O Exception caught.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("There was an error. Exception caught.");
//        }
//        return messages;
//    }

    public ArrayList<String> readMsg(User sender, User receiver) {
        ArrayList<String> messages = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(sender.getID() + "_" + receiver.getID()))) {
            String line = bfr.readLine();
            while (line != null) {
                String message = line.split(",")[1] + ": " + line.split(",")[3];
                messages.add(message);
                line = bfr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Conversation does not exist!");
        } catch (IOException e) {
            System.out.println("Reading suspended. I/O Exception caught.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an error. Exception caught.");
        }
        return messages;
    }

    public ArrayList<String> readMsg(User sender, User receiver, boolean ex) {
        ArrayList<String> messages = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(sender.getID() + "_" + receiver.getID()))) {
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
            e.printStackTrace();
            System.out.println("There was an error. Exception caught.");
        }
        return messages;
    }

    // remove
//    public ArrayList<String> readMsg(String sender, String receiver, boolean ex) {
//        ArrayList<String> messages = new ArrayList<>();
//
//        try (BufferedReader bfr = new BufferedReader(new FileReader(sender + "_" + receiver))) {
//            String line = bfr.readLine();
//            while (line != null) {
//                messages.add(line);
//                line = bfr.readLine();
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Conversation does not exist!");
//        } catch (IOException e) {
//            System.out.println("Reading suspended. I/O Exception caught.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("There was an error. Exception caught.");
//        }
//        return messages;
//    }

    // remove
//    public void writeMsg(String sender, String receiver, String content) {
//
//        try {
//            File f = new File(sender + "_" + receiver);
//            if(f.exists()){
//                f.createNewFile();
//            }
//
//            BufferedWriter bfw = new BufferedWriter(new FileWriter(f, true));
//
//            String line = "%s;%s,%s,%s,%s";
//            bfw.write(String.format(
//                    line,
//                    sender,
//                    receiver,
//                    sender,
//                    this.getTime(),
//                    content));
//            bfw.newLine();
//            bfw.flush();
//        } catch (Exception e) {
//            System.out.println("Writing suspended. Exception caught");
//        }
//    }

    public void writeMsg(User sender, User receiver, String content) {

        try {
            File f = new File(sender.getID() + "_" + receiver.getID());
            if (f.exists()) {
                f.createNewFile();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(f, true));

            String line = "%s;%s,%s,%s,%s";
            bfw.write(String.format(
                    line,
                    sender.getAccountUsername(),
                    receiver.getAccountUsername(),
                    sender.getAccountUsername(),
                    this.getTime(),
                    content));
            bfw.newLine();
            bfw.flush();
        } catch (Exception e) {
            System.out.println("Writing suspended. Exception caught");
        }
    }

    // remove
//    public void export(String sender, String receiver) {
//        ArrayList<String> messages = readMsg(sender, receiver, true);
//        String fileName = receiver + "_" + sender;
//        File f = new File(fileName);
//
//        try{
//            if(!f.exists()){
//                f.createNewFile();
//            }
//
//            BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
//
//            for (String msg : messages) {
//                bfw.write(msg);
//                bfw.newLine();
//                bfw.flush();
//            }
//        } catch (IOException e){
//            System.out.println("Error!");
//        }
//    }

    public void export(User sender, User receiver) {
        ArrayList<String> messages = readMsg(sender, receiver, true);
        String fileName = receiver.getID() + "_" + sender.getID();
        File f = new File(fileName);

        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(f));

            for (String msg : messages) {
                bfw.write(msg);
                bfw.newLine();
                bfw.flush();
            }
        } catch (IOException e) {
            System.out.println("Error!");
        }
    }

}
