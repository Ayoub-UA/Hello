package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        // IP address of the server (localhost here)
        String serverAddress = "127.0.0.1";
        // Port number (0 to 65535, e.g., HTTP 80)
        int port = 5000;

        // Establishing a connection to the server
        Socket socket = new Socket(serverAddress, port);

        // Use socket's input and output stream to communicate
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("Hello, Server!");

        // Receiving response from the server
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse);

        // Close the streams and socket
        out.close();
        in.close();
        socket.close();
    }
}
