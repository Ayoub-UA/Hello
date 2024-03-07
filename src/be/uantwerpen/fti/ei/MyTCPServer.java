package be.uantwerpen.fti.ei;

import java.net.*;
import java.io.*;
public class MyTCPServer{
    private ServerSocket sSocket;
    private Socket cSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public void receiveMessage(int prt) throws IOException, ClassNotFoundException {


            sSocket= new ServerSocket(prt);
            cSocket = sSocket.accept();
            //System.out.println("Does it work");
            out= new ObjectOutputStream(cSocket.getOutputStream());
            in= new ObjectInputStream(cSocket.getInputStream());


            String input= in.readUTF();
        System.out.println("input:"+input);

            if( input.equals("Succes"))
            {
                out.writeUTF(input+" Hell yeah");

            } else
            {
                out.writeUTF("Fail");

        }
        this.close();
}

    public void close() throws IOException {

        out.close();
        in.close();

        sSocket.close();
        cSocket.close();

        }

    public void receiveFile(int prt ,String fPath) throws IOException {
            sSocket= new ServerSocket(prt);
            cSocket = sSocket.accept();
            out= new ObjectOutputStream(cSocket.getOutputStream());
            in= new ObjectInputStream(cSocket.getInputStream());
            FileOutputStream fos= new FileOutputStream(fPath);
            long length= in.readLong();
            byte[] buf = new byte[(int)length+10];
            int bytes=0;
            while(length>0 && bytes!=-1)
            {
                bytes=in.read(buf,0,(int)length);
                fos.write(buf,0, bytes);
                length = length -bytes;
            }
            fos.close();
            this.close();


        }




   public static void main(String[] args) throws IOException, ClassNotFoundException {

        MyTCPServer server= new MyTCPServer();
        //server.receiveMessage(3579);
        server.receiveFile(8002, "src/be/uantwerpen/fti/ei/ReceivedFile");


    }
}

