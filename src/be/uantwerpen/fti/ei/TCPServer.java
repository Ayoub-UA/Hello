package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        int port = 5000;

        // Creating a server socket listening on port 5000
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            // Accept incoming client connections
            Socket clientSocket = serverSocket.accept();

            // Use clientSocket's input and output stream to communicate
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String clientMessage = in.readLine();
            System.out.println("Client says: " + clientMessage);

            // Responding to the client
            out.println("Hello, Client!");

            // Close the streams and client socket
            in.close();
            out.close();
            clientSocket.close();
        }
    }
}
