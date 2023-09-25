import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) throws IOException {

        ServerSocket socket = new ServerSocket(1234);


        Socket connection = socket.accept();
        System.out.println("Connection from: " + connection);

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());

        ServerInputThread sit = new ServerInputThread(connection, inFromClient);
        sit.start();
        ServerOutputThread sot = new ServerOutputThread(connection, outToClient);
        sot.start();
    }
}
