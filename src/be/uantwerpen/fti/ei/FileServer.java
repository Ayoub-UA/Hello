package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

/**
 * The FileServer class implements a simple file server that listens for incoming client connections
 * and handles each connection in a separate thread. It allows clients to request files, and it sends
 * the requested files to the clients.
 *
 * The server operates on a specified port and serves files from a designated folder.
 * When a client connects, a new thread is created to handle the client's request.
 */

public class FileServer {
    public static void main(String[] args) {
        // Define a path and a folder
        int port = 5000;
        String filePath = "./serverFolder";

        /*
        * This is a try-with-resources statement that declares the serverSocket
        * This statement ensures that the resource is closed at the end of an statement
        * This way there is no need for and explicit declaration of socket.close()
        * source: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        *  */
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("File Server is listening on port " + port);

            while (true) {
                // Listen to connection and accept it
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                // Handle client in a separate thread
                new Thread(() -> handleClient(clientSocket, filePath)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, String filePath) {
        try (
                // OutputStream to send the file and its contents
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                // InputStream to receive the filename
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            // Get the file name
            String fileName = (String) inputStream.readObject();

            File fileToSend = new File(filePath + File.separator + fileName);

            if (fileToSend.exists()) {
                System.out.println("Sending file: " + fileName);

                // This code reads the data from the file, stores it in a buffer and writes it to the outputStream
                try (FileInputStream fileInputStream = new FileInputStream(fileToSend)) {
                    // Buffer to store chunks of file data
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    // if the end of the file is not reached, write the buffer to the outputStream
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    // Send the data immediately
                    outputStream.flush();
                }
                System.out.println("File sent successfully");
            } else {
                System.out.println("File not found: " + fileName);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
