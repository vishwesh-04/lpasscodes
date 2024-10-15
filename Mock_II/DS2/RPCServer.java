package Mock_II.DS2;

import java.io.*;
import java.net.*;

public class RPCServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("RPC Server is running...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    String method = in.readUTF();
                    int number = in.readInt();
                    
                    // Process request
                    int result = 0;
                    if ("square".equals(method)) {
                        result = number * number;
                    } else if ("cube".equals(method)) {
                        result = number * number * number;
                    }
                    
                    // Send back the result
                    out.writeInt(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
