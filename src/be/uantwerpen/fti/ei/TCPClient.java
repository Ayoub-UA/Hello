package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        // IP address of the server (localhost here)
        String serverAddress = "127.0.0.1";
        // Port number
        int port = 5000;

        try (
            // Establish a connection to the server
            Socket socket = new Socket(serverAddress, port);
            // Use socket's input and output stream to communicate
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStream in = socket.getInputStream()) {

            // Request a file
            String filename = "File1";
            out.println("GET " + filename);
            String filePath = "src/ClientFiles/received_" + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            // Read the file
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File " + filename + " received!");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
