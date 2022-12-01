public class ClientMain {
    public static void main(String[] args) {
        MainPage newR = new MainPage();
        Thread thread1 = new Thread(newR);
        thread1.start();
    }

}
