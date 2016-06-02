import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


public class WebServer {

    /**
        Steps in completing project:
        Complete the in-class assignment:
            Create a Server socket, accept a connection, set up the input and output.
            Read the "GET" line from the client.
            Use a loop to read all the request headers. Print them to the standard output.
            Parse the "GET" line (e.g., using String.split.)
            Open the requested file.
            Print the required response headers (Content-type and Content-length)
            (You may base the content-type on the file extension.)
            Use a loop to read a block of data from the file then
            Write the data to the socket
           For a text file, you can read and write one line at a time. For a data file
            (like an image), you will want to use the raw read method to read a block of
            data (typically a few Kilobytes), then use the corresponding write method to
            write it to the socket. The read method typically returns the amount of data
            read, which may be less than your buffer size. You can write a text file this
            way also (which means you don't need separate code to handle text and data).
           Finally, close the socket to test your server
            Create a folder named "data" in your project,
            Put some test files in this folder (e.g., a few .html files, a few .txt
            files and a few images),
            Launch your server on some port (e.g., 8080) from your project directory
            (i.e., the one that contains data)
            Point your web browser to "localhost:8080/data/file1.html"
     */

    public static void handleConnection(Socket socket) throws IOException {


        // Created a BufferedReader that can read from the socket
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Create a PrintStream than can write to the socket
        // Passing "true" as the second parameter causes each write to be followed by a flush.
        PrintStream output = new PrintStream(socket.getOutputStream(), true);


        /**
         * Resource data of URL
         */
        String fileType = "";
        String getResource = "";
        String fileName ="";
        String queryString = "";

        /**
         * Reads the server response
         */
        String line;
        while ((line = input.readLine()) != null) {
            if (line.length() == 0) {
                break;
            }

            /**
             * Parse the GET line to receive the correct file and file extension
             */
            if (line.substring(0, 3).equals("GET")) {
                getResource = line.substring(4);
                for (int i = 0; i < getResource.length(); i++) {

                    //Separates the file from the remaining 'GET' string
                    if (getResource.charAt(i) == ' ') {
                        getResource = getResource.substring(0, i);
                    }
                }
                getResource = getResource.substring(1);
                fileName = getResource.substring(10);
                int typeSplit = getResource.indexOf('.');
                fileType = getResource.substring(typeSplit+1);
                if(getResource.contains("?")){
                    queryString = getResource.substring(getResource.charAt('?'));
                    fileType = fileType.substring(0, fileType.charAt('?'));
                }

            }

            /**
             * Prints first section of the server response
             * GET:
             * Host:
             * Connection:
             * etc..
             */
            System.out.println(line);

        }


        /**
         * Load the specified file and send it to the output and display  on
         * the webpage
         */
        File file = new File(getResource);
        if (!file.exists()||fileType.equals("")) {
            output.println("HTTP/1.1 404 NOT FOUND");
            output.println("");
            output.println("FILE NOT FOUND");
        } else {
            InputStream fileIn = null;
            try {

                /**
                 * Handle displaying HTML files
                 */
                if (fileType.equals("html")) {
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html");
                    output.println("Content-Length: " + file.length());
                    output.println("");
                }

                /**
                 * NEED TO HANDLE PL FILES AS WELL
                 */
                if (fileType.equals("pl")) {
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html");
                    output.println("Content-Length: " + file.length());
                    output.println("");
                }

                /**
                 * Handle displaying IMG files
                 */
                String[] imgType = {"png", "jpg", "ico", "jpeg", "bmp", "ico"};
                for (String type : imgType) {
                    if (fileType.equals(type)) {
                        output.println("HTTP/1.1 200 OK");
                        output.println("Content-Type: image/" + fileType);
                        output.println("Content-Length: " + file.length());
                        output.println("");
                    }
                }

                if(!fileType.equals("pl")) {
                    int fileLength = (int) file.length();

                    fileIn = new FileInputStream(file); // Can also pass the constructor a String
                    byte[] buffer = new byte[fileLength];
                    int amount_read = fileIn.read(buffer); // read up to 1024 bytes of raw data
                    output.write(buffer, 0, amount_read); // write data back out to an OutputStream

                }else{
                    Perl perl = new Perl();
                    System.out.println(queryString);
                    output.print(perl.runPerl(getResource, queryString));

                }
            } finally {

                /**
                 * Close the the input, outputs, and socket
                 */
                output.flush();

                input.close();
                output.close();
                socket.close();
            }
        }
        /**
         * Close the the input, outputs, and socket
         */
        output.flush();

        input.close();
        output.close();
        socket.close();
    }



    public static void main(String[] args) throws IOException {
        final int DEFAULT_PORT = 8080; // For security reasons, only root can use ports < 1024.
        int portNumber = args.length > 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        final ServerSocket serverSocket = new ServerSocket(portNumber);
        new Thread(new Runnable() {


            public void run() {
                while (true) {
                    try {
                        handleConnection(serverSocket.accept());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}