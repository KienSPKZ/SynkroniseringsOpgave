import java.io.BufferedReader;
import java.net.Socket;

public class ClientInputThread extends Thread {
    private Socket inputSocket;
    private BufferedReader inFromServer;
    private String clientMsg;
    private ClientOutputThread cot;

    public ClientInputThread(Socket inputSocket, BufferedReader inFromServer, ClientOutputThread cot) {
        this.inputSocket = inputSocket;
        this.inFromServer = inFromServer;
        this.cot = cot;
    }

    public void run() {
        boolean playerDataSet = false;
        try {
            while (true) {
                clientMsg = inFromServer.readLine();
                System.out.println(clientMsg);
                if (clientMsg.split(" ")[0] == "you" && !playerDataSet) {
                    cot.setPlayerName(clientMsg.split(" ")[1]);
                    cot.setxPos(Integer.parseInt(clientMsg.split(" ")[2]));
                    cot.setyPos(Integer.parseInt(clientMsg.split(" ")[3]));
                    playerDataSet = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
