import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientOutputThread extends Thread {
    private Socket inputSocket;
    private DataOutputStream outToServer;
    private String playerName;
    private int xPos;
    private int yPos;

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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
