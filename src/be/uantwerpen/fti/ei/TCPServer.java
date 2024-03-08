package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main (String[] args) throws IOException {
        int port = 5000; // Server Port
        ServerSocket serverSocket = new ServerSocket(port); // Create server socket
        System.out.println("TCP Server started. Listening on Port " + port);

        while (true) {
            //Accept incoming client connections
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected!");
            //Create new thread
            new Thread(new ClientHandler(clientSocket)).start();
        }

    }

    // Inner class to handle client requests
    private static class ClientHandler implements Runnable {
        public Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Read client request
                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    if (clientMessage.startsWith("GET")) {
                        String[] requestParts = clientMessage.split(" ", 2);
                        if (requestParts.length > 1) {
                            String filename = requestParts[1].trim();
                            sendFile(filename);
                            break; // Exit the loop after sending the file
                        }
                    } else {
                        System.out.println("Invalid request");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendFile(String filename) {
            File file = new File("./src/ServerFiles" + filename);
            if (file.exists()) {
                try {System.out.println("File found");
                    FileInputStream fileInputStream = new FileInputStream(file); // Open file for reading
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);  // Create buffered input stream
                    OutputStream outputStream = clientSocket.getOutputStream();  // Get output stream from client socket

                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = bufferedInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, count);
                    }
                    outputStream.flush();
                    clientSocket.shutdownOutput(); // Indicate file transmission end
                    System.out.println("File sent successfully!");
                } catch (IOException e) {
                    System.out.println("ERROR sending file:" + e.getMessage());
                }
            } else {
                System.out.println("ERROR File not found:" + filename);
            }
        }
    }
}


