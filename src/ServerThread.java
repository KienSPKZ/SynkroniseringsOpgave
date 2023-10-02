import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
    private TCPServer gameServer;

    private Socket inputSocket;
    private BufferedReader inFromClient;
    private String clientMsg;
    private DataOutputStream outToClient;

    public ServerThread(TCPServer gameServer, Socket inputSocket, BufferedReader inFromClient, DataOutputStream outToClient) {
        this.gameServer = gameServer;
        this.inputSocket = inputSocket;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public void run() {
        try {
            while (true) {
                clientMsg = inFromClient.readLine();
                if (clientMsg.equals("null")) {
                    break;
                }
            gameServer.sendMsgToClients(clientMsg);
            }
            inputSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToClient(String msg) throws IOException {
        outToClient.writeBytes(msg + '\n');
    }
}
