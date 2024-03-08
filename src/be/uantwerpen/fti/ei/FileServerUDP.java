package be.uantwerpen.fti.ei;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * The FileServerUDP class implements a simple file server than listens on the specified port for incoming packets.
 * Through this same port, it sends packets to a specified port and address.
 * There is no connection establish during this process.
 */
public class FileServerUDP {
    public static void main(String[] args) throws IOException {

        // Define the server port
        int port = 3000;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server started. Listening on Port " + port);

            while (true) {
                // Receive the filename packet
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                // Use a new thread for handle the packet
                new Thread(() -> handlePacket(receivePacket, socket)).start();
            }
        }

    }

    // Handle the packet
    private static void handlePacket(DatagramPacket packet, DatagramSocket socket) {
        try {
            // Get the filename from the packet
            String filename = new String(packet.getData(), 0, packet.getLength());
            System.out.println("filename received: " + filename);

            // Fetch the file
            File fileToSend = new File("./src/ServerFiles" + File.separator + filename);

            // If the file exists, send it
            if (fileToSend.exists()) {
                System.out.println("Sending file: " + filename);

                // This code reads the data from the file, stores it in a buffer and writes it to the packet
                try (FileInputStream fileInputStream = new FileInputStream(fileToSend)){

                    // Buffer to store chunks of file data
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    // if the end of the file is not reached, write the buffer to the packet
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        DatagramPacket sendPacket = new DatagramPacket(buffer, bytesRead, packet.getAddress(), packet.getPort());
                        // Send the packet
                        socket.send(sendPacket);
                    }
                    System.out.println("File sent successfully!");
                }
            } else {
                System.out.println("ERROR File not found: " + filename);
            }
        } catch (Exception e) {
            System.out.println("Error handling packet: " + e.getMessage());
        }
    }

}
