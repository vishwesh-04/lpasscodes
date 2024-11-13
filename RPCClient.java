import java.io.*;
import java.net.*;
import java.util.*;

public class RPCClient {
	public static void main(String args[]) {
		try {
			Socket clientSocket = new Socket("localhost", 5000);
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			Scanner sc = new Scanner(System.in);
			System.out.print("Method: ");
			String method = sc.next();
			int num1, num2;
			System.out.print("Numbers: ");
			num1 = sc.nextInt();
			num2 = sc.nextInt();
			
			out.writeUTF(method);
			out.writeInt(num1);
			out.writeInt(num2);
			out.flush();
			
			int result = in.readInt();
			System.out.println("Method " + method + "-----" + "Result " + result);
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
