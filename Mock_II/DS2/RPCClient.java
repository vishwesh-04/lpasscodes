package Mock_II.DS2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RPCClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // or use the server's IP address
        int serverPort = 5000;

        try (Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            String method = "square"; // or "cube"
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter no to square: ");
            int number = sc.nextInt();

            // Send request
            out.writeUTF(method);
            out.writeInt(number);
            out.flush();

            // Receive response
            int result = in.readInt();
            System.out.println("Result of " + method + "(" + number + ") = " + result);
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

