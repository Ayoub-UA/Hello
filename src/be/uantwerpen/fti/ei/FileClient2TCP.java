package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.Socket;

/**
 * The FileClient2TCP class implements a simple client that connects to a file server, sends a request for a specific file,
 * and receives the requested file from the server. The client operates on a specified server address (localhost) and port.

 * This extra class was implemented to showcase the working functionality of multithreading.
 * You can see this by first runnning FileServerTCP and then running concurrentRun.
 */
public class FileClient2TCP {
    public static void main(String[] args) {
        // Define the serverAddress as localHost and the same port
        String serverAddress = "127.0.0.1";
        int serverPort = 5000;
        String fileName = "hello2.txt";

        // Try-with-resources
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server");

            // outputStream to send the file name
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            // inputStream to receive the file and its contents
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Send the file name to the server
            outputStream.writeObject(fileName);

            // Receive the file from the server
            File receivedFile = new File("./src/saveFolder" + File.separator + fileName);

            // This code is similar to the code on the server side but in reverse
            try (FileOutputStream fileOutputStream = new FileOutputStream(receivedFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("File 2 received successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
