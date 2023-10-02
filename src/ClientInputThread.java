import java.io.BufferedReader;
import java.net.Socket;

public class ClientInputThread extends Thread {
    private BufferedReader inFromServer;
    private GUI gui;

    public ClientInputThread(BufferedReader inFromServer, GUI gui) {
        this.inFromServer = inFromServer;
        this.gui = gui;
    }

    public void run() {
        try {
            while (true) {
                String[] input = inFromServer.readLine().split(" ");
                gui.runAnotherPlayerMoved(input[0], Integer.parseInt(input[1]), Integer.parseInt(input[2]), input[3]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
