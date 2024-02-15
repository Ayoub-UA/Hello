package be.uantwerpen.fti.ei;

import java.io.IOException;
import java.net.*;

public class UDPExample {
    public static void main(String[] args) throws IOException {
        // Port number for the DatagramSocket
        int port = 1234;

        // Create a DatagramSocket to listen on the specified port
        DatagramSocket datagramSocket = new DatagramSocket(port);

        // Example of receiving a packet
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);

        // Extract data, address, and port from the received packet
        String receivedData = new String(packet.getData(), 0, packet.getLength());
        InetAddress address = packet.getAddress();
        int portFromSender = packet.getPort();

        // Example response
        String responseData = "Received: " + receivedData;
        byte[] responseDataBytes = responseData.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length, address, portFromSender);
        datagramSocket.send(responsePacket);

        // Close the DatagramSocket
        datagramSocket.close();
    }
}
