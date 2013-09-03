import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class RegistryServerComm implements Runnable{
    Thread t;
    RegistryServerComm() {
      t = new Thread(this, "Registry Server Communication Thread");
      t.start(); // Start the thread
   }
    public void run() {
      try {
          System.out.println("Registry Server Communication Thread!!");
          InetAddress thisIp =InetAddress.getLocalHost();
          // Trying to register
          String register = "Regsiter;RMI"+thisIp.getHostAddress()+";9800;AmarRaj;5555";
     } catch (Exception e) {
         System.out.println("Server Listening Socket Exception");
     }
   }
}
