package be.uantwerpen.fti.ei;

import java.io.*;
import java.net.*;

public class MyTCPClient {
    private Socket cSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void sendMessage(int port,String ip) throws IOException {
        cSocket= new Socket(ip, port);
        in =new ObjectInputStream(cSocket.getInputStream());
        out = new ObjectOutputStream(cSocket.getOutputStream());
        out.writeUTF("Succes");
        out.flush();

        System.out.println(in.readUTF());

    }

    public void close() throws IOException {

        out.close();
        in.close();


        cSocket.close();
    }

    public void sendFile(int port,String ip, String fPath) throws IOException {
        cSocket= new Socket(ip, port);
        in =new ObjectInputStream(cSocket.getInputStream());
        out = new ObjectOutputStream(cSocket.getOutputStream());
        File f= new File(fPath);
        FileInputStream fis= new FileInputStream(f);
        out.writeLong(f.length());
        byte[] buffer= new byte[(int) f.length() +10];
        int bytes=0;
        while (bytes!=-1)
        {
            bytes= fis.read(buffer);
            if(bytes!=-1){
            out.write(buffer,0,bytes);
            out.flush();}
        }

        fis.close();


    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MyTCPClient client= new MyTCPClient();
        //client.sendMessage(3579,"127.0.0.1");
        client.sendFile(8125,"127.0.0.1","src/be/uantwerpen/fti/ei/SendFile" );
        client.close();
    }
}
