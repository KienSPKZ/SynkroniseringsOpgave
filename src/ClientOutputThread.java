import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientOutputThread extends Thread {
    private Socket inputSocket;
    private DataOutputStream outToServer;
    private GUI gui;

    public ClientOutputThread(Socket inputSocket, DataOutputStream outToServer, GUI gui) {
        this.inputSocket = inputSocket;
        this.outToServer = outToServer;
        this.gui = gui;
    }

    public void run() {
        try {
            while (true) {
                //outToServer.writeBytes(inFromUser.readLine() + '\n');
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
