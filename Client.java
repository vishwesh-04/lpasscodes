import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	public static void main(String[] args) {
		
		try {
			Scanner sc = new Scanner(System.in);
			System.out.print("Enter port: ");
			int port = sc.nextInt();
			sc.nextLine();
			while(true)
			{
				System.out.print("Enter msg: ");
				String msg = sc.nextLine();
				Socket connSocket = new Socket("localhost", port);
				PrintWriter out = new PrintWriter(connSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
				out.println(msg);
				String res = in.readLine();
				System.out.println( "Client: Received response: " + res ) ;
				in.close();
				out.close();
				connSocket.close();
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
