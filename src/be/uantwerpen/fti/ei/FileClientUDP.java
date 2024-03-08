package be.uantwerpen.fti.ei;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * The FileClientUDP class implements a simple file client than listens on the specified port for incoming packets.
 * Through this same port, it sends packets to a specified port and address.
 * There is no connection establish during this process.
 */
public class FileClientUDP {
    public static void main(String[] args) {

        String serverAddress = "127.0.0.1";
        int serverPort = 3000; // Server listens on this port
        int clientPort = 3001; // Different from serverPort if running on the same machine

        try (DatagramSocket socket = new DatagramSocket(clientPort)) {

            // Convert server address to InetAddress
            InetAddress serverIPAddress = InetAddress.getByName(serverAddress);

            // Request file
            String filename = "File1";
            byte[] sendData = filename.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverPort);
            socket.send(sendPacket);

            // Receive the file and save it in the ClientFiles folder as received + filename
            try (FileOutputStream fileOutputStream = new FileOutputStream("src/ClientFiles/received_" + filename)) {

                // A buffer to store the file and its contents
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                // receive the packet
                socket.receive(receivePacket);

                // Retrieve the length of the packet
                int bytesRead = receivePacket.getLength();

                // Write the data to the FileOutputStream
                fileOutputStream.write(receiveBuffer, 0, bytesRead);

                System.out.println("File received successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
