/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.*;
import java.net.*;

public class ServerUDPListen implements Runnable{
    
    Thread t;
    ServerUDPListen() throws SocketException{
      t = new Thread(this, "Server Ping Listening Thread");
      t.start(); // Start the thread
      
   }
    
    public void run() {
      try {  
          
          
          
          DatagramSocket UDPListenSocket = new DatagramSocket(9800);
          byte[] buffer = new byte[1024];
          while(true) {
              buffer = new byte[1024]; 
              DatagramPacket Packet = new DatagramPacket(buffer, buffer.length);
              //System.out.println ("Waiting for Ping from Registry Server at 9800..");
              UDPListenSocket.receive(Packet);
              
              String ReceivedString = new String(Packet.getData());
              
              InetAddress IPAddress = Packet.getAddress();
              int port = Packet.getPort(); 
              buffer = ReceivedString.getBytes();
              Packet = new DatagramPacket(buffer, buffer.length, IPAddress, port); 
              //System.out.println("Sending back to server..");
              UDPListenSocket.send(Packet);
               // System.out.println("Pinged Back");
          } 
         
     } catch (Exception e) {
         System.out.println("Server Listening Socket Exception");
     }
     
   }
}
