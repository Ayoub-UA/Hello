package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 4000; // Server listens on this port
        int clientPort = 4001; // Different from serverPort if running on the same machine !!

        try (DatagramSocket socket = new DatagramSocket(clientPort)) {
            InetAddress serverIPAddress = InetAddress.getByName(serverAddress); // Convert server address to InetAddress

            // Request file
            String filename = "File1";
            String request = "GET " + filename;
            byte[] sendData = request.getBytes(); // Convert request to bytes
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverPort); // Create request packet
            socket.send(sendPacket); // Send request packet to server

            // Prepare to receive the file
            byte[] receiveBuffer = new byte[1024]; // Buffer to store received bytes
            FileOutputStream fileOutputStream = new FileOutputStream("src/ClientFiles/received_" + filename);
            boolean moreData = true; // Flag to check if more data is coming
            while (moreData) { // Loop until all data is received
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length); // Create packet to receive data
                socket.receive(receivePacket); // Receive packet from server
                if (receivePacket.getLength() < 1024) { // Assuming last packet will not be exactly 1024 bytes
                    moreData = false;  // No more data
                }
                // Write received bytes to file
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
            }
            System.out.println("File " + filename + " received!");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
