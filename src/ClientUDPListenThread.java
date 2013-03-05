/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
          //Thread.currentThread().sleep(1000);
          //System.out.println(tt.size());
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
          }
         /*for(int i = 5; i > 0; i--) {
            System.out.println("Child Thread: " + i);
            // Let the thread sleep for a while.
            Thread.sleep(500);
         }*/
     } catch (Exception e) {
         //System.err.println("Client exception: " + e.toString());  
         //e.printStackTrace(); 
         System.out.println("UDP Listening Socket Exception");
     }
     System.out.println("Exiting child thread.");
   }
}
