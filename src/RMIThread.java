/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;



public class RMIThread implements Runnable{
    Communicate communicate;
    Thread t;
    RMIThread(Communicate comm) {
      communicate = comm;
      t = new Thread(this, "Registry Server Communication Thread");
      t.start(); // Start the thread
   }
    
    public void run() {
      try {
        System.out.println("RMI Thread");
        String BindingName = "AmarRaj";
        LocateRegistry.createRegistry(5555);
        Naming.rebind("//"+Utility.getIP()+":5555/"+BindingName, communicate);
        System.out.println("RMI Server is ready at "+Utility.getIP() +":5555");
          
         
     } catch (Exception e) {
         System.out.println("Server Listening Socket Exception");
     }
     
   }
}
