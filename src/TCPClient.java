import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

    public static void main(String[] args) throws IOException {

        Socket connectionToSocket =  new Socket("localhost", 1234);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionToSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(connectionToSocket.getOutputStream());

        ClientInputThread cit = new ClientInputThread(connectionToSocket, inFromServer);
        cit.start();
        ClientOutputThread cot = new ClientOutputThread(connectionToSocket, outToServer);
        cot.start();
    }
}
