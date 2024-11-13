import java.io.*;
import java.net.*;

public class RPCServer {
	
	public static void main(String arg[]) {
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			System.out.println("RPC Server is running...");
			while(true)
			{
			Socket connSocket = serverSocket.accept();
			ObjectInputStream in = new ObjectInputStream(connSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(connSocket.getOutputStream());
			
			String method = in.readUTF();
			int num1 = in.readInt();
			int num2 = in.readInt();
			
			System.out.println(num1 + " " + num2);
//			
//			
//			
			if(method.equals("add"))
				out.writeInt(add(num1, num2));
			else if(method.equals("sub"))
				out.writeInt(sub(num1, num2));
			else if(method.equals("mul"))
				out.writeDouble(multiply(num1, num2));
			else if(method.equals("sub"))
				out.writeDouble(div(num1, num2));
//			

			
//			System.out.println(method + " " + num1);
//			int result = -1;
//			
//			if(method.equals("sq"))
//			{
//				result = square(num1);
//			}
//			else if(method.equals("cube"))
//			{
//				result = cube(num1);
//			}
//			
//			out.writeInt(result);
			out.flush();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int add(int num1, int num2)
	{
		return num1+num2;
	}
	
	private static int sub(int num1, int num2)
	{
		return num1-num2;
	}
	private static double multiply(int num1, int num2)
	{
		return (double) (num1*num2);
	}
	private static double div(int num1, int num2)
	{
		return num1+num2;
	}
	
	private static int square(int num)
	{
		return num*num;
	}
	private static int cube(int num)
	{
		return num*num*num;
	}

}
