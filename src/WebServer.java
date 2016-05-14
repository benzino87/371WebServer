import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
	
	//localhost:8080/hi_there.html
	
	public static void handleConnection(Socket socket) throws IOException{
		

	    // Created a BufferedReader that can read from the socket
	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    // Create a PrintStream than can write to the socket
	    // Passing "true" as the second parameter causes each write to be followed by a flush.
	    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
	    
	    String line;
	    while((line = input.readLine())!=null){
	    	if(line.length() == 0){
	    		break;
	    	}
	    	System.out.println(line);
	    }
	    
	    output.println("HTTP/1.1 200 OK");
	    output.println("Content-Type: text/plain");
	    output.println("Content-Length: 70");
	    output.println("Connection: close");
	    output.println("");
	    output.println("This is not the real content because this server is not yet complete.");
	    
	    output.flush();
	    
	    input.close();
	    output.close();
	    socket.close();
	}
	public static void main(String[] args) throws IOException {
		final int DEFAULT_PORT = 8080; // For security reasons, only root can use ports < 1024.
        int portNumber = args.length > 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        new Thread(new Runnable() {
        	
        	
            public void run() {
              try {
                handleConnection(clientSocket);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }).start();
	}
}
