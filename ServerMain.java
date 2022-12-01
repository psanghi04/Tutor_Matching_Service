import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9648);
        Socket socket = serverSocket.accept();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        File f = new File("usernamePasswords");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedReader bfrFile = new BufferedReader(new FileReader(f));
        PrintWriter pwFile = new PrintWriter(new FileWriter(f));
        int loginOrNewAcc = Integer.parseInt(bfr.readLine()); //bfr not reading
        System.out.println(loginOrNewAcc);
        if (loginOrNewAcc == 1) {
            String username = bfr.readLine();
            System.out.println(username);
            String password = bfr.readLine();
            System.out.println(password);

            bfr.close();

            while (true) {
                String line = bfrFile.readLine();
                if (line == null) {
                    pw.println(2);
                    pw.flush();
                    break;
                }
                if (line.equals(username + password)) {
                    pw.println(1);
                    pw.flush();
                }
            }
        }


    }
}
