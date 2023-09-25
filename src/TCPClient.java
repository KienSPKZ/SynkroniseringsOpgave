import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

    public static void main(String[] args) throws IOException {

        Socket connectionToDNSSocket =  new Socket("localhost", 1234);
        BufferedReader inFromDNSServer = new BufferedReader(new InputStreamReader(connectionToDNSSocket.getInputStream()));
        DataOutputStream outTODNSServer = new DataOutputStream(connectionToDNSSocket.getOutputStream());


    }



}
