package be.uantwerpen.fti.ei;
import java.io.IOException;
import java.net.*;

public class TCP_UDP {
    Socket socket = new Socket("127.0.0.1", 5000);

    public TCP_UDP() throws IOException {
    }

    DatagramSocket datagramSocket = new DatagramSocket(1234);

    public void sendUDP() throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);
        System.out.println(packet.getAddress());
    }

}
