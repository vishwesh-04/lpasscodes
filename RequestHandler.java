import java.io.*;
import java.net.*;


public class RequestHandler extends Thread {
	
	private Socket connSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public RequestHandler(Socket connSocket) {
		try {
			this.connSocket = connSocket;
			this.out = new PrintWriter(connSocket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			String msg = in.readLine();
			System.out.println( "Server: Received message => " + msg ) ;
			out.println(msg + "(echoed)");
			System.out.println( "Server: Message sent => " + msg ) ;
			out.close();
			in.close();
			connSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
