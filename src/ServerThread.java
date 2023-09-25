import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    private TCPServer gameServer;

    private Socket inputSocket;
    private BufferedReader inFromClient;
    private String clientMsg;
    private DataOutputStream outToClient;
    private Player player;

    public ServerThread(TCPServer gameServer, Socket inputSocket, BufferedReader inFromClient, DataOutputStream outToClient, Player player) {
        this.gameServer = gameServer;
        this.inputSocket = inputSocket;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        this.player = player;
    }

    public void run() {
        try {
            while (true) {
                outToClient.writeBytes("you " + player.name + " " + player.xpos + " " + player.ypos);
                clientMsg = inFromClient.readLine();
                if (clientMsg.equals("null")) {
                    break;
                }
                System.out.println(clientMsg);
            }
            inputSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
