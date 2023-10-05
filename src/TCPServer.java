

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

        while(true) {
            Socket connection = socket.accept();
            System.out.println("Connection from: " + connection);

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());

            ServerThread st = new ServerThread(gameServer, connection, inFromClient, outToClient);
            st.start();
            gameServer.serverThreads.add(st);
        }
    }
    private ArrayList<ServerThread> serverThreads = new ArrayList<>();

    public synchronized void sendMsgToClients(String msg) throws IOException {
        for (ServerThread st: serverThreads) {
            st.writeToClient(msg);
        }
    }

    public TCPServer() {

    }
}
