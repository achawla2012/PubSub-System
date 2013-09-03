import java.lang.*;
import java.net.*;
import java.util.*;
public class ClientUDPListenThread implements Runnable{
    Thread t;
    LinkedList<String> tt ;

    ClientUDPListenThread(LinkedList<String> a) {
        tt = a;
    }

    ClientUDPListenThread() {
      t = new Thread(this, "Client Listening Thread");
      t.start(); // Start the thread
    }

    public void run() {
      try {
          DatagramSocket UDPListenSocket = new DatagramSocket(Utility.client_listen_port);
          byte[] buffer = new byte[1024];
          while(true) {
              buffer = new byte[1024]; 
              DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
              System.out.println ("Waiting for Article....Go ahead with you new option input..");
              UDPListenSocket.receive(Packet);
              String ReceivedString = new String(Packet.getData());
              InetAddress IPAddress = Packet.getAddress();
              int port = Packet.getPort(); 
              System.out.println("Received Article from my group server : " + ReceivedString );
              System.out.println("Exiting child thread.");
          }
       } catch (Exception e) {
         System.out.println("UDP Listening Socket Exception");
     }
   }
}
