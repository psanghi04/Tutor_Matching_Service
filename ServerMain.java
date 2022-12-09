import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain extends Thread {

    Socket client;
    ServerSocket serverSocket;

    public ServerMain(Socket client, ServerSocket socket) {
        this.client = client;
        this.serverSocket = socket;
    }

    public void run() {
        try (DataOutputStream writer = new DataOutputStream(this.client.getOutputStream());
             DataInputStream reader = new DataInputStream(this.client.getInputStream())
        ) {
            Message messageClass = new Message();

            boolean login = false;
            ArrayList<User> userList = new ArrayList<>();
            String userName;
            String password;

            ArrayList<String> blockedUserList = new ArrayList<>();

            try {
                File file = new File("BlockedUsers.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedReader bfr = new BufferedReader(new FileReader(file));

                String line = bfr.readLine();
                while (line != null) {
                    blockedUserList.add(line);
                    line = bfr.readLine();
                }
            } catch (Exception ignored) {}

            ArrayList<String> invisibleList = new ArrayList<>();

            try {
                File file = new File("InvisibleUsers.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedReader bfr = new BufferedReader(new FileReader(file));
                String line = bfr.readLine();
                while (line != null) {
                    invisibleList.add(line);
                    line = bfr.readLine();
                }
            } catch (Exception ignored) {}

            ArrayList<String> filterList = new ArrayList<>();

            File f = new File("UserDetails.txt");

            readUsers(f, filterList, userList);

            User user = null; // currentUser logged in
            boolean signedIn = false;

            while (!login) {
                int optionNum = Integer.parseInt(reader.readUTF());

                switch (optionNum) {
                    case 1:
                        int option = Integer.parseInt(reader.readUTF());
                        userName = reader.readUTF();
                        String email = reader.readUTF();
                        password = reader.readUTF();

                        boolean accountSimilarity = false;

                        for (User person : userList) {
                            if (person.getAccountUsername().equals(userName)) {
                                accountSimilarity = true;
                                break;
                            }

                            if (person.getEmail().equals(email)) {
                                accountSimilarity = true;
                                break;
                            }
                        }

                        if (option == 1) {
                            if (!accountSimilarity) {
                                user = new Student(userName, password, email, "****", filterList);

                                try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                                    pw.write(userName + "," + password + "," + email + "," + "****,," + user + "," + user.getID() + "\n");
                                    pw.flush();

                                    writer.writeUTF("Success");
                                    writer.flush();
                                } catch (IOException e) {
                                    System.out.println("Unable to write file");
                                }
                            } else {
                                writer.writeUTF("Sorry");
                                writer.flush();
                                continue;
                            }
                        } else {
                            if (!accountSimilarity) {
                                writer.writeUTF("Success");
                                writer.flush();

                                String subjects = reader.readUTF();
                                String[] newSubjects = subjects.split(",");

                                double price = Double.parseDouble(reader.readUTF());

                                user = new Tutor(userName, password, email, newSubjects, price, "****", filterList);

                                try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                                    pw.write(userName + "," + password + "," + email + "," + String.join(";", newSubjects) + "," + price + "," + "****,," + user + "," + user.getID() + "\n");
                                    pw.flush();
                                } catch (IOException e) {
                                    System.out.println("Unable to write file");
                                }
                            } else {
                                writer.writeUTF("Sorry");
                                writer.flush();
                                continue;
                            }
                        }

                        userList.add(user);

                        login = true;
                        signedIn = true;

                        break;

                    case 2:
                        while (!login) {
                            userName = reader.readUTF();
                            password = reader.readUTF();

                            for (User acc : userList) {
                                if (acc.getAccountUsername().equals(userName)) {
                                    if (acc.getPassword().equals(password)) {
                                        user = acc;
                                        login = true;
                                        break;
                                    }
                                }
                            }

                            if (login) {
                                writer.writeUTF("Success");
                                writer.writeUTF(user.toString());
                                writer.flush();
                            } else {
                                writer.writeUTF("Failed");
                                String answer = reader.readUTF();
                                if (answer.equals("1")) {
                                    break;
                                }
                            }
                        }

                        signedIn = true;
                        break;

                    case 3:
                        login = true;
                        signedIn = false;
                        break;
                }
            }

            while (signedIn) {
                if (user instanceof Student) {
                    // Student Interface

                    int option = Integer.parseInt(reader.readUTF());

                    boolean deletedAccount = false;

                    switch (option) {

                        case 1:
                            readUsers(f, filterList, userList);
                            ArrayList<User> availableTutors = new ArrayList<>();

                            for (User userEl : userList) {
                                if (userEl instanceof Tutor) {

                                    if (!messageClass.isBlocked(user, userEl) &&
                                            !messageClass.isInvisible(user, userEl)) {
                                        availableTutors.add(userEl);
                                    }
                                }
                            }


                            if (availableTutors.size() == 0) {
                                writer.writeUTF("0");
                                writer.flush();
                            } else {
                                availability(availableTutors, writer);
                            }

                            break;

                        case 2:

                            int index = -1;
                            boolean unableToMessage = false;
                            String person = reader.readUTF();

                            if (userList.size() == 0 || userList.size() == 1) {
                                writer.writeUTF("0");
                                writer.flush();
                                break;
                            }

                            for (int i = 0; i < userList.size(); i++) {

                                if (userList.get(i).getAccountUsername().equals(person)) {
                                    User checkBlock = userList.get(i);

                                    if (messageClass.isBlocked(user, checkBlock)) {
                                        break;
                                    }

                                    if (messageClass.isInvisible(user, checkBlock)) {
                                        break;
                                    }

                                    index = i;

                                    if (userList.get(i) instanceof Tutor) {
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } else {
                                        writer.writeUTF(String.format("Unable to find/message %s\n\n", person));
                                        writer.flush();
                                        break;
                                    }
                                }
                            }

                            if (index == -1) {
                                writer.writeUTF(String.format("Unable to find/message %s\n\n", person));
                                writer.flush();
                                break;
                            }

                            boolean quit = true;

                            while (quit) {
                                int optionMessage = Integer.parseInt(reader.readUTF());
                                quit = messageMenu(optionMessage, writer, reader,
                                        messageClass, user, userList, index, quit);
                            }

                            break;

                        case 3:

                            int optionProfile = Integer.parseInt(reader.readUTF());

                            switch (optionProfile) {
                                case 1:
                                    String newPassword = reader.readUTF();

                                    if (newPassword.equals(user.getPassword())) {
                                        writer.writeUTF("Fail");
                                        writer.flush();
                                        break;
                                    }
                                    user.setPassword(newPassword);

                                    try {
                                        updateFile(userList, f);
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } catch (IOException e) {
                                    }

                                    writer.writeUTF("Fail");
                                    writer.flush();
                                    break;
                                case 2:

                                    String newUsername = reader.readUTF();

                                    boolean sameUsername = false;
                                    for (User users : userList) {
                                        if (users instanceof Tutor) {
                                            if (users.getAccountUsername().equals(newUsername)) {
                                                sameUsername = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!sameUsername) {
                                        user.setAccountUsername(newUsername);
                                    } else {
                                        writer.writeUTF("Fail");
                                        writer.flush();
                                        continue;
                                    }

                                    try {
                                        updateFile(userList, f);
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } catch (IOException e) {
                                    }

                                    writer.writeUTF("Fail");
                                    writer.flush();
                                    break;

                                case 3:
                                    String newEmail = reader.readUTF();

                                    boolean sameEmail = false;
                                    for (User users : userList) {
                                        if (users instanceof Student) {
                                            if (users.getEmail().equals(newEmail)) {
                                                sameEmail = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!sameEmail) {
                                        user.setEmail(newEmail);
                                    } else {
                                        writer.writeUTF("Fail");
                                        writer.flush();
                                        break;
                                    }


                                    try {
                                        updateFile(userList, f);
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } catch (IOException e) {
                                    }

                                    writer.writeUTF("Fail");
                                    writer.flush();
                                    break;

                                case 4:
                                    deletedAccount = true;

                                    // Removing User from ArrayList
                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i).getAccountUsername().equals(user.getAccountUsername())) {
                                            if (userList.get(i).getPassword().equals(user.getPassword())) {
                                                if (userList.get(i).getEmail().equals(user.getEmail())) {
                                                    userList.remove(i);
                                                }
                                            }
                                        }
                                    }

                                    // Rewriting the file again...
                                    try {
                                        updateFile(userList, f);
                                        System.out.println("Account has been deleted");
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }

                                    break;
                                case 5:
                                    String filter = reader.readUTF();
                                    ((Student) user).setFilter(filter);

                                    try {
                                        BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
                                        ArrayList<String> lines = new ArrayList<>();
                                        String line;
                                        while ((line = bfr.readLine()) != null) {
                                            String[] newLine = line.split(",");
                                            if (newLine[0].equals(user.getAccountUsername())) {
                                                newLine[3] = filter;
                                                line = String.join(",", newLine);
                                            }
                                            lines.add(line);
                                        }

                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
                                        for (String det : lines) {
                                            pw.write(det + "\n");
                                            pw.flush();
                                        }
                                    } catch (IOException e) {
                                        System.out.println("Unable to write file");
                                    }
                                    break;
                                case 6:
                                    String answer = reader.readUTF();
                                    switch (answer) {
                                        case "1":
                                            String[] words = reader.readUTF().split(",");
                                            ArrayList<String> pastWords = ((Student) user).getFilterWordList();

                                            Collections.addAll(pastWords, words);

                                            ((Student) user).setFilterWordList(pastWords);

                                            updateCensoredWords(user, pastWords);

                                            break;
                                        case "2":
                                            ArrayList<String> filterWordList = ((Student) user).getFilterWordList();

                                            String[] wordList = reader.readUTF().split(",");

                                            filterWordList.removeAll(List.of(wordList));

                                            ((Student) user).setFilterWordList(filterWordList);

                                            updateCensoredWords(user, filterWordList);
                                            break;
                                        case "3":
                                            break;
                                    }
                                    break;
                                case 7:
                                    String size = String.valueOf(((Student) user).getFilterWordList().size());
                                    writer.writeUTF(size);
                                    writer.flush();

                                    for (String word : ((Student) user).getFilterWordList()) {
                                        writer.writeUTF(word);
                                        writer.flush();
                                    }
                                    break;
                                case 8:
                                    break;
                            }

                            break;

                        case 4:
                            String blockUsername = reader.readUTF();

                            if (blockUsername.equals(user.getAccountUsername())) {
                                writer.writeUTF("Cannot block yourself");
                                writer.flush();
                                break;
                            }

                            int check = 0;
                            for (User users : userList) {
                                if (users.getAccountUsername().equals(blockUsername)) {
                                    blockedUserList.add(user + "," + user.getID() + ";" + users.getID());
                                    block(blockedUserList);
                                    writer.writeUTF("Successfully Blocked User");
                                    writer.flush();
                                    check = 1;
                                    break;
                                }
                            }

                            if (check == 0) {
                                writer.writeUTF("User does not exist");
                                writer.flush();
                            }
                            break;
                        case 5:
                            String unblockUsername = reader.readUTF();

                            if (unblockUsername.equals(user.getAccountUsername())) {
                                writer.writeUTF("Cannot unblock yourself");
                                writer.flush();
                                break;
                            }

                            int i = -1;
                            for (User users : userList) {
                                if (users.getAccountUsername().equals(unblockUsername)) {
                                    i = blockedUserList.indexOf(user + "," + user.getID() + ";" + users.getID());
                                    break;
                                }
                            }

                            if (i != -1) {
                                writer.writeUTF("Success");
                                writer.flush();
                                blockedUserList.remove(i);
                                unblockUser(blockedUserList);
                            } else {
                                writer.writeUTF("There is no blocked user with that name");
                                writer.flush();
                                break;
                            }

                            break;
                        case 6:
                            String invisiblePerson = reader.readUTF();
                            int count = 0;
                            for (User users : userList) {
                                if (users.getAccountUsername().equals(invisiblePerson)) {
                                    invisibleList.add(user + "," + user.getID() + ";" + users.getID());
                                    count++;
                                    break;
                                }
                            }

                            if (count != 0) {
                                setInvisible(invisibleList, writer);
                            } else {
                                writer.writeUTF("User not found");
                                writer.flush();
                            }

                            break;
                        case 7:
                            signedIn = false;
                            break;
                    }

                    if (deletedAccount) {
                        break;
                    }
                } else {
                    // Tutor Interface

                    int option = Integer.parseInt(reader.readUTF());

                    boolean deletedAccount = false;

                    switch (option) {

                        case 1:
                            readUsers(f, filterList, userList);
                            ArrayList<User> availableStudents = new ArrayList<>();

                            for (User userEl : userList) {
                                if (userEl instanceof Student) {

                                    if (!messageClass.isBlocked(user, userEl) &&
                                            !messageClass.isInvisible(user, userEl)) {
                                        availableStudents.add(userEl);
                                    }
                                }
                            }


                            if (availableStudents.size() == 0) {
                                writer.writeUTF("0");
                                writer.flush();
                            } else {
                                availability(availableStudents, writer);
                            }

                            break;

                        case 2:

                            int index = -1;
                            boolean unableToMessage = false;
                            String person = reader.readUTF();

                            if (userList.size() == 0 || userList.size() == 1) {
                                writer.writeUTF("0");
                                writer.flush();
                                break;
                            }

                            for (int i = 0; i < userList.size(); i++) {

                                if (userList.get(i).getAccountUsername().equals(person)) {
                                    User checkBlock = userList.get(i);

                                    if (messageClass.isBlocked(user, checkBlock)) {
                                        break;
                                    }

                                    if (messageClass.isInvisible(user, checkBlock)) {
                                        break;
                                    }

                                    index = i;

                                    if (userList.get(i) instanceof Student) {
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } else {
                                        writer.writeUTF(String.format("Unable to find/message %s\n\n", person));
                                        writer.flush();
                                        break;
                                    }
                                }
                            }

                            if (index == -1) {
                                writer.writeUTF(String.format("Unable to find/message %s\n\n", person));
                                writer.flush();
                                break;
                            }

                            boolean quit = true;
                            while (quit) {
                                int optionMessage = Integer.parseInt(reader.readUTF());
                                quit = messageMenu(optionMessage, writer, reader,
                                        messageClass, user, userList, index, quit);
                            }


                            break;

                        case 3:
                            int optionProfile = Integer.parseInt(reader.readUTF());

                            switch (optionProfile) {
                                case 1:
                                    String newPassword = reader.readUTF();

                                    if (newPassword.equals(user.getPassword())) {
                                        writer.writeUTF("Fail");
                                        writer.flush();
                                        break;
                                    }
                                    user.setPassword(newPassword);

                                    try {
                                        updateFile(userList, f);
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } catch (IOException e) {
                                    }

                                    writer.writeUTF("Fail");
                                    writer.flush();
                                    break;
                                case 2:

                                    String newUsername = reader.readUTF();

                                    boolean sameUsername = false;
                                    for (User users : userList) {
                                        if (users instanceof Student) {
                                            if (users.getAccountUsername().equals(newUsername)) {
                                                sameUsername = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!sameUsername) {
                                        user.setAccountUsername(newUsername);
                                    } else {
                                        writer.writeUTF("Fail");
                                        writer.flush();
                                        continue;
                                    }

                                    try {
                                        updateFile(userList, f);
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } catch (IOException e) {
                                    }

                                    writer.writeUTF("Fail");
                                    writer.flush();
                                    break;

                                case 3:
                                    String newEmail = reader.readUTF();

                                    boolean sameEmail = false;
                                    for (User users : userList) {
                                        if (users instanceof Tutor) {
                                            if (users.getEmail().equals(newEmail)) {
                                                sameEmail = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!sameEmail) {
                                        user.setEmail(newEmail);
                                    } else {
                                        writer.writeUTF("Fail");
                                        writer.flush();
                                        break;
                                    }


                                    try {
                                        updateFile(userList, f);
                                        writer.writeUTF("Success");
                                        writer.flush();
                                        break;
                                    } catch (IOException e) {
                                    }

                                    writer.writeUTF("Fail");
                                    writer.flush();
                                    break;

                                case 4:
                                    deletedAccount = true;

                                    // Removing User from ArrayList
                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i).getAccountUsername().equals(user.getAccountUsername())) {
                                            if (userList.get(i).getPassword().equals(user.getPassword())) {
                                                userList.remove(i);
                                            }
                                        }
                                    }

                                    // Rewriting the file again...
                                    try {
                                        updateFile(userList, f);
                                        System.out.println("Account has been deleted");
                                    } catch (IOException e) {
                                        System.out.println("Can't write to the file!");
                                    }

                                    break;
                                case 5:
                                    String filter = reader.readUTF();
                                    ((Tutor) user).setFilter(filter);

                                    try {
                                        BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
                                        ArrayList<String> lines = new ArrayList<>();
                                        String line;
                                        while ((line = bfr.readLine()) != null) {
                                            String[] newLine = line.split(",");
                                            if (newLine[0].equals(user.getAccountUsername())) {
                                                newLine[5] = filter;
                                                line = String.join(",", newLine);
                                            }
                                            lines.add(line);
                                        }

                                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
                                        for (String det : lines) {
                                            pw.write(det + "\n");
                                            pw.flush();
                                        }
                                    } catch (IOException e) {
                                        System.out.println("Unable to write file");
                                    }

                                    break;
                                case 6:

                                    String answer = reader.readUTF();
                                    switch (answer) {
                                        case "1":
                                            String[] words = reader.readUTF().split(",");
                                            ArrayList<String> pastWords = ((Tutor) user).getFilterWordList();

                                            Collections.addAll(pastWords, words);

                                            ((Tutor) user).setFilterWordList(pastWords);

                                            updateCensorWords(user, pastWords);

                                            break;
                                        case "2":
                                            ArrayList<String> filterWordList = ((Tutor) user).getFilterWordList();

                                            String[] wordList = reader.readUTF().split(",");

                                            filterWordList.removeAll(List.of(wordList));

                                            ((Tutor) user).setFilterWordList(filterWordList);

                                            updateCensorWords(user, filterWordList);
                                            break;
                                        case "3":
                                            break;
                                    }
                                    break;

                                case 7:
                                    String size = String.valueOf(((Tutor) user).getFilterWordList().size());
                                    writer.writeUTF(size);
                                    writer.flush();

                                    for (String word : ((Tutor) user).getFilterWordList()) {
                                        writer.writeUTF(word);
                                        writer.flush();
                                    }
                                    break;
                                case 8:
                                    break;
                            }

                            break;

                        case 4:
                            String blockUsername = reader.readUTF();

                            if (blockUsername.equals(user.getAccountUsername())) {
                                writer.writeUTF("Cannot block yourself");
                                writer.flush();
                                break;
                            }

                            int check = 0;
                            for (User users : userList) {
                                if (users.getAccountUsername().equals(blockUsername)) {
                                    blockedUserList.add(user + "," + user.getID() + ";" + users.getID());
                                    block(blockedUserList);
                                    writer.writeUTF("Successfully Blocked User");
                                    writer.flush();
                                    check = 1;
                                    break;
                                }
                            }

                            if (check == 0) {
                                writer.writeUTF("User does not exist");
                                writer.flush();
                            }
                            break;
                        case 5:
                            String unblockUsername = reader.readUTF();

                            if (unblockUsername.equals(user.getAccountUsername())) {
                                writer.writeUTF("Cannot unblock yourself");
                                writer.flush();
                                break;
                            }

                            int i = -1;
                            for (User users : userList) {
                                if (users.getAccountUsername().equals(unblockUsername)) {
                                    i = blockedUserList.indexOf(user + "," + user.getID() + ";" + users.getID());
                                    break;
                                }
                            }

                            if (i != -1) {
                                writer.writeUTF("Success");
                                writer.flush();
                                blockedUserList.remove(i);
                                unblockUser(blockedUserList);
                            } else {
                                writer.writeUTF("There is no blocked user with that name");
                                writer.flush();
                                break;
                            }

                            break;
                        case 6:
                            String invisiblePerson = reader.readUTF();
                            int count = 0;
                            for (User users : userList) {
                                if (users.getAccountUsername().equals(invisiblePerson)) {
                                    invisibleList.add(user + "," + user.getID() + ";" + users.getID());
                                    count++;
                                    break;
                                }
                            }

                            if (count != 0) {
                                setInvisible(invisibleList, writer);
                            } else {
                                writer.writeUTF("User not found");
                                writer.flush();
                            }

                            break;
                        case 7:
                            signedIn = false;
                            break;
                    }

                    if (deletedAccount) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Socket Closed");
        }
    }

    public static void block(ArrayList<String> blockList) throws IOException {
        File blockedUsers = new File("BlockedUsers.txt");
        if (!blockedUsers.exists()) {
            blockedUsers.createNewFile();
        }

        FileWriter fr = new FileWriter(blockedUsers);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

        for (String s : blockList) {
            pw.println(s);
        }
        pw.flush();
    }

    public static void printMsg(ArrayList<String> messages, User user, DataOutputStream writer) throws IOException {
        String finalMessage = "";
        System.out.println(((Student) user).getFilterWordList());
        System.out.println(((Student) user).getFilterWordList().get(0));
        System.out.println(((Student) user).getFilterWordList().get(0).equals(""));

        if (user instanceof Student) {
            for (String message : messages) {
                if (!((Student) user).getFilterWordList().get(0).equals("")) {
                    for (String filterWord : ((Student) user).getFilterWordList()) {
                        finalMessage = finalMessage + message.replaceAll(String.format("(?i)%s", filterWord), ((Student) user).getFilter()) + ";";
                    }
                } else {
                    finalMessage = finalMessage + message + ";";
                }
            }
            writer.writeUTF(finalMessage);
            writer.flush();

        } else {
            for (String message : messages) {
                if (!((Tutor) user).getFilterWordList().get(0).equals("")) {
                    for (String filterWord : ((Tutor) user).getFilterWordList()) {
                        finalMessage = finalMessage + message.replaceAll(String.format("(?i)%s", filterWord), ((Tutor) user).getFilter()) + ";";
                    }
                } else {
                    finalMessage = finalMessage + message + ";";
                }
            }
            writer.writeUTF(finalMessage);
            writer.flush();
        }
    }

    public static void updateFile(ArrayList<User> userList, File f) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(f));
        String line;
        int count = 0;
        ArrayList<String[]> lines = new ArrayList<>();
        while ((line = bfr.readLine()) != null) {
            if (line.contains(userList.get(count).getAccountUsername()) ||
                    line.contains(userList.get(count).getEmail())) {
                lines.add(line.split(","));
                count++;
            }
        }
        bfr.close();

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i) instanceof Student) {
                pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() + "," +
                        userList.get(i).getEmail() + "," + lines.get(i)[3] + "," + lines.get(i)[4] + "," + "Student," + userList.get(i).getID() + "\n");
            } else {
                pw.write(userList.get(i).getAccountUsername() + "," + userList.get(i).getPassword() +
                        "," + userList.get(i).getEmail() + "," +
                        String.join(";", ((Tutor) userList.get(i)).getSubjects()) +
                        "," + ((Tutor) userList.get(i)).price() + "," + lines.get(i)[5] + "," + lines.get(i)[6] + "," + "Tutor," + userList.get(i).getID() + "\n");
            }
        }

        pw.flush();
    }

    public static void updateCensorWords(User user, ArrayList<String> pastWords) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] newLine = line.split(",");
                if (newLine[0].equals(user.getAccountUsername())) {
                    newLine[6] = String.join(";", pastWords);
                    line = String.join(",", newLine);
                }
                lines.add(line);
            }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
            for (String det : lines) {
                pw.write(det + "\n");
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("Unable to write file");
        }
    }

    public static void setInvisible(ArrayList<String> invisibleList, DataOutputStream writer) throws IOException {
        try {
            File invisibleUsers = new File("InvisibleUsers.txt");
            if (!invisibleUsers.exists()) {
                invisibleUsers.createNewFile();
            }

            PrintWriter pw = new PrintWriter(new FileWriter(invisibleUsers));

            for (String s : invisibleList) {
                pw.println(s);
            }
            pw.flush();
        } catch (IOException e) {
        }

        writer.writeUTF("Successfully Changed Visibility");
        writer.flush();
    }

    public static void unblockUser(ArrayList<String> blockedUserList) {
        try {
            File blockedUsers = new File("BlockedUsers.txt");
            if (!blockedUsers.exists()) {
                blockedUsers.createNewFile();
            }

            FileWriter fr = new FileWriter(blockedUsers);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fr));

            for (String s : blockedUserList) {
                pw.println(s);
            }
        } catch (IOException e) {
        }
    }

    public static void updateCensoredWords(User user, ArrayList<String> pastWords) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("UserDetails.txt"));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] newLine = line.split(",");
                if (newLine[0].equals(user.getAccountUsername())) {
                    newLine[4] = String.join(";", pastWords);
                    line = String.join(",", newLine);
                }
                lines.add(line);
            }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserDetails.txt")));
            for (String det : lines) {
                pw.write(det + "\n");
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("Unable to write file");
        }
    }

    public static void export(DataOutputStream writer, DataInputStream reader, Message messageClass, ArrayList<User> userList, User user, int index) throws IOException {
        try {
            String expFileName = reader.readUTF();

            File exportFile = new File(expFileName + ".csv");

            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            FileWriter fw = new FileWriter(exportFile, false);
            BufferedWriter bfw = new BufferedWriter(fw);

            ArrayList<String> pastMessages = messageClass.readMsg(user, userList.get(index), true);
            for (int i = 0; i < pastMessages.size(); i++) {
                bfw.write(user.getAccountUsername() + ";" + userList.get(index).getAccountUsername() + "," + user.getAccountUsername() + "," + pastMessages.get(i).split(",")[2] + "," + pastMessages.get(i).split(",")[3] + "\n");
            }

            bfw.flush();
            writer.writeUTF("Success");
            writer.flush();
        } catch (IOException e) {
            writer.writeUTF("Fail");
            writer.flush();
        }
    }

    public static void importMessage(Message messageClass, ArrayList<User> userList, User user, int index, String ifileName, ArrayList<String> importMessages, DataOutputStream writer) throws IOException {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(ifileName));

            String line = bfr.readLine();
            while (line != null) {
                importMessages.add(line);
                line = bfr.readLine();
            }

            BufferedWriter bfw = new BufferedWriter(new FileWriter(user.getID() + "_" + userList.get(index).getID(), true));
            for (String importMessage : importMessages) {
                bfw.write(user.getAccountUsername() + ";" + userList.get(index).getAccountUsername() + "," + user.getAccountUsername() + "," + messageClass.getTime() + "," + importMessage + "\n");
            }

            bfr.close();
            bfw.flush();

            messageClass.export(user, userList.get(index));

            writer.writeUTF("Success");
            writer.flush();

        } catch (IOException e) {
            writer.writeUTF("Fail");
            writer.flush();
        }
    }

    public static void availability(ArrayList<User> userList, DataOutputStream writer) throws IOException {
        writer.writeUTF(String.valueOf(userList.size()));
        for (int i = 0; i < userList.size(); i++) {
            writer.writeUTF(userList.get(i).getAccountUsername());
        }
        writer.flush();
        System.out.println();
    }

    public void readUsers(File f, ArrayList<String> filterList, ArrayList<User> userList) {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedReader bfr = new BufferedReader(new FileReader(f));

            userList.clear();
            String userLine = bfr.readLine();
            while (userLine != null) {
                filterList.clear();
                String[] splitLines = userLine.split(",");

                User user;

                if (splitLines[5].equals("Student")) {
//                    System.out.println(splitLines[4].split(";").length);
                    Collections.addAll(filterList, splitLines[4].split(";"));
//                    System.out.println(filterList);
                    user = new Student(splitLines[0], splitLines[1], splitLines[2], splitLines[3], filterList, UUID.fromString(splitLines[6]));
//                    System.out.println(((Student) user).getFilterWordList());
                } else {
//                    System.out.println(splitLines[6].split(";").length);
                    Collections.addAll(filterList, splitLines[6].split(";"));
                    user = new Tutor(splitLines[0], splitLines[1], splitLines[2], splitLines[3].split(";"), Double.parseDouble(splitLines[4]), splitLines[5], filterList, UUID.fromString(splitLines[8]));
//                    System.out.println(((Tutor) user).getFilterWordList());
                }

                userList.add(user);
                userLine = bfr.readLine();
            }

//            System.out.println(userList.size());
//            for (User user : userList) {
//                if (user instanceof Student) {
//                    System.out.println(((Student) user).getFilterWordList());
//                } else {
//                    System.out.println(((Tutor) user).getFilterWordList());
//                }
//            }

            bfr.close();
        } catch (IOException e) {
            System.out.println("Cannot read file!");
        }
    }

    public boolean messageMenu(int optionMessage, DataOutputStream writer,
                               DataInputStream reader, Message messageClass,
                               User user, ArrayList<User> userList, int index, boolean quit) throws IOException {
        switch (optionMessage) {
            case 1:
                ArrayList<String> messages = messageClass.readMsg(user, userList.get(index));

                if (messages.size() == 0) {
                    writer.writeUTF("0");
                    writer.flush();
                    break;
                } else {
                    writer.writeUTF("1");
                    writer.flush();
                }

                printMsg(messages, user, writer);
                break;
            case 2:

                String content = reader.readUTF();
                messageClass.writeMsg(user, userList.get(index), content);
                messageClass.export(user, userList.get(index));

                break;
            case 3:
                String answer = reader.readUTF();

                ArrayList<String> messagesDelete;
                String message = null;
                boolean dMessageExists = false;

                try {
                    int lineNum = Integer.parseInt(answer);
                    messagesDelete = messageClass.readMsg(user, userList.get(index));
                    message = messagesDelete.get(lineNum - 1).split(":")[1];
                    dMessageExists = true;
                } catch (Exception e) {
                    messagesDelete = messageClass.readMsg(user, userList.get(index), true);
                    for (String s : messagesDelete) {
                        if (s.substring(s.lastIndexOf(",") + 1).equals(answer)) {
                            message = answer;
                            dMessageExists = true;
                            break;
                        }
                    }
                }

                if (!dMessageExists) {
                    writer.writeUTF("Message not found");
                    writer.flush();
                    break;
                }

                messageClass.delete(user, userList.get(index), message);
                writer.writeUTF("Message Deleted Successfully");
                writer.flush();

                break;

            case 4:
                ArrayList<String> allMessages = messageClass.readMsg(user, userList.get(index), true);

                writer.writeUTF(String.valueOf(allMessages.size()));
                writer.flush();

                if (allMessages.size() == 0) {
                    break;
                }

                boolean messageExists = false;
                String editedMessage = null;
                String editMessage = reader.readUTF();


                try {
                    int lineNum = Integer.parseInt(editMessage);
                    allMessages = messageClass.readMsg(user, userList.get(index));
                    editedMessage = allMessages.get(lineNum - 1).split(":")[1];
                    messageExists = true;
                } catch (Exception e) {
                    allMessages = messageClass.readMsg(user, userList.get(index), true);
                    for (String messageEl : allMessages) {
                        if (messageEl.substring(messageEl.lastIndexOf(",") + 1).equals(editMessage)) {
                            editedMessage = editMessage;
                            messageExists = true;
                            break;
                        }
                    }
                }

                String newMessage = reader.readUTF();

                if (!messageExists) {
                    writer.writeUTF("Message not found");
                    writer.flush();
                    break;
                }

                messageClass.edit(user, userList.get(index), editedMessage, newMessage + "\n");
                messageClass.export(user, userList.get(index));

                writer.writeUTF("Message Edited Successfully");
                writer.flush();

                break;

            case 5:
                String keyword = reader.readUTF();

                ArrayList<String> searchableMessages = messageClass.readMsg(user, userList.get(index));

                boolean foundMessage = false;

                int counter = 0;
                for (String searchableMessage : searchableMessages) {
                    if (searchableMessage.contains(keyword)) {
                        counter++;
                        foundMessage = true;
                    }
                }

                if (!foundMessage) {
                    writer.writeUTF("Message not found!");
                    writer.flush();
                    break;
                }

                writer.writeUTF("Success");
                writer.flush();

                writer.writeUTF(String.valueOf(counter));
                writer.flush();

                for (String searchableMessage : searchableMessages) {
                    if (searchableMessage.contains(keyword)) {
                        writer.writeUTF(searchableMessage);
                        writer.flush();
                    }
                }
                break;

            case 6:
                String ifileName = reader.readUTF();
                ArrayList<String> importMessages = new ArrayList<>();
                importMessage(messageClass, userList, user, index, ifileName, importMessages, writer);

                break;
            case 7:
                export(writer, reader, messageClass, userList, user, index);
                break;
            case 8:
                quit = false;
                break;
        }

        return quit;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4240);
            while (true) {
                Socket socket = serverSocket.accept();
                (new ServerMain(socket, serverSocket)).start();
            }
        } catch (Exception e) {
            System.out.println("Couldn't Connect");
        }
    }
}
