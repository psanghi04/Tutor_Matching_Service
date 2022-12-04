import java.io.*;
import java.net.*;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            GUI gui = new GUI(reader, writer);
            gui.createMainPage();
        } catch (Exception e) {
            System.out.println("Couldn't print");
        }
    }
}
