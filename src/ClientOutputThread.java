import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientOutputThread extends Thread {
    Socket inputSocket;
    DataOutputStream outToServer;

    public ClientOutputThread(Socket inputSocket, DataOutputStream outToServer) {
        this.inputSocket = inputSocket;
        this.outToServer = outToServer;
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
