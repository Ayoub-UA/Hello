package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        int port = 4000;
        DatagramSocket socket = new DatagramSocket(port);

        System.out.println("UDP Server started. Listening on Port " + port);

        while (true) {
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            // Use a new thread for handling the request
            new Thread(() -> handlePacket(receivePacket, socket)).start();
        }
    }

    private static void handlePacket(DatagramPacket packet, DatagramSocket socket) {
        try {
            String receivedText = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Request received: " + receivedText);

            if (receivedText.startsWith("GET")) {
                String[] requestParts = receivedText.split(" ", 2);
                if (requestParts.length > 1) {
                    String filename = requestParts[1].trim();
                    sendFile(filename, socket, packet.getAddress(), packet.getPort());
                }
            } else {
                System.out.println("Invalid request");
            }
        } catch (Exception e) {
            System.out.println("Error handling packet: " + e.getMessage());
        }
    }

    private static void sendFile(String filename, DatagramSocket socket, InetAddress clientAddress, int clientPort) {
        File file = new File("./src/ServerFiles" + File.separator + filename);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = fileInputStream.read(buffer)) > 0) {
                    DatagramPacket sendPacket = new DatagramPacket(buffer, count, clientAddress, clientPort);
                    socket.send(sendPacket);
                }
                System.out.println("File sent successfully!");
            } catch (IOException e) {
                System.out.println("ERROR sending file: " + e.getMessage());
            }
        } else {
            System.out.println("ERROR File not found: " + filename);
        }
    }
}
