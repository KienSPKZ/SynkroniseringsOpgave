import java.io.BufferedReader;

public class ClientInputThread2 extends Thread {
    private BufferedReader inFromServer;
    private GUI2 gui;

    public ClientInputThread2(BufferedReader inFromServer, GUI2 gui) {
        this.inFromServer = inFromServer;
        this.gui = gui;
    }

    public void run() {
        try {
            while (true) {
                String[] input = inFromServer.readLine().split(" ");
                gui.runPlayerMoved(input[0], Integer.parseInt(input[1]), Integer.parseInt(input[2]), input[3]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
