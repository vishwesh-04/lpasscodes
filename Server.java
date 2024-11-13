import java.io.*;
import java.net.*;
import java.util.*;


public class Server {

	public static void main(String[] args) {
		System.out.print("Enter port num: ");
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt();
		sc.close();
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true)
			{
				Socket connSocket = serverSocket.accept();
				System.out.println( "Server: Request received from => " + connSocket.getInetAddress().toString() ) ; 
				Thread newThread = new RequestHandler(connSocket);
				newThread.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
