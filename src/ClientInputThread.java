import java.io.BufferedReader;
import java.net.Socket;

public class ClientInputThread extends Thread {
    private Socket inputSocket;
    private BufferedReader inFromServer;
    private String clientMsg;
    private GUI gui;

    public ClientInputThread(Socket inputSocket, BufferedReader inFromServer, GUI gui) {
        this.inputSocket = inputSocket;
        this.inFromServer = inFromServer;
        this.gui = gui;
    }

    public void run() {
        try {
            while (true) {
                clientMsg = inFromServer.readLine();
                System.out.println(clientMsg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
