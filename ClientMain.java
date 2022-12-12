import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4240);
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            GUI gui = new GUI(reader, writer);
            gui.createInterface();
        } catch (Exception e) {
            JFrame frame = new JFrame();
            JPanel errorPanel = new JPanel();
            JLabel error = new JLabel("Couldn't connect");
            errorPanel.add(error);
            frame.add(errorPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(317, 150);
            frame.setVisible(true);
        }
    }
}
