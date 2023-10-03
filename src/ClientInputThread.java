import java.io.BufferedReader;

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
                if (input[0].equals("move")) {
                    gui.runPlayerMoved(input[1], Integer.parseInt(input[2]), Integer.parseInt(input[3]), input[4]);
                } else if (input[0].equals("newPlayer")) {
                    gui.addNewPlayer(input[1], Integer.parseInt(input[2]), Integer.parseInt(input[3]), input[4]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
