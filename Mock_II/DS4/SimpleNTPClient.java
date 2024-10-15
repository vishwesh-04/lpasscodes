package Mock_II.DS4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SimpleNTPClient {
    public static void main(String[] args) {
        String ntpServer = "time.google.com"; // Use a public NTP server
        try {
            // Prepare NTP request
            byte[] buffer = new byte[48];
            buffer[0] = 0x1B; // NTP version 3, client request
            InetAddress address = InetAddress.getByName(ntpServer);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 123);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);

            // Receive the response
            socket.receive(packet);
            socket.close();

            // Extract time from the response
            long transmitTime = ((buffer[43] & 0xFFL) << 24) |
                                ((buffer[44] & 0xFFL) << 16) |
                                ((buffer[45] & 0xFFL) << 8) |
                                (buffer[46] & 0xFFL);
            transmitTime = transmitTime * 1000L - 2208988800000L; // Convert NTP to Unix time
            System.out.println("Current Time: " + new java.util.Date(transmitTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

