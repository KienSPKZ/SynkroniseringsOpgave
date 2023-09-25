import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {

    public static void main(String[] args) throws IOException {
        TCPServer gameServer = new TCPServer();

        ServerSocket socket = new ServerSocket(1234);
        int playerCount = 0;

        while(true) {
            Socket connection = socket.accept();
            System.out.println("Connection from: " + connection);

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());

            Player newPlayer = new Player("Player" + ++playerCount,14,15,"up");
            GUI.players.add(newPlayer);
            GUI.insertHero(newPlayer.xpos, newPlayer.ypos);

            ServerThread st = new ServerThread(gameServer, connection, inFromClient, outToClient, newPlayer);
            st.start();
            gameServer.serverThreads.add(st);
        }
    }
    public ArrayList<ServerThread> serverThreads = new ArrayList<>();

    public TCPServer() {

    }
}
