import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.net.*;
import java.rmi.registry.*;
import java.util.*;
import java.rmi.*;
public class RMIServer {
    public static String GetList(DatagramSocket UDPGetSocket) {
        try {
                    byte[] buffer = new byte[1024];
                    String getlist = "GetList;RMI;"+ Utility.getIP() +";9800";
                    buffer = getlist.getBytes();
                    DatagramPacket Packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("128.101.35.147") , 5105);
                    System.out.println ("Sending GetList String to Registry Server");
                    //DatagramSocket t = new DatagramSocket(9604);
                    UDPGetSocket.send(Packet);
                    buffer = new byte[1024];
                    Packet = new DatagramPacket(buffer, buffer.length);
                    System.out.println ("Waiting for Registry Server..");
                    UDPGetSocket.receive(Packet);
                    String list = new String(Packet.getData());
                    return list;
                    } catch (Exception e) {
                        System.out.println("GetList Error...");
                        System.out.println(e.getStackTrace());
                    }
        return "dfdf";
    }
    public static void main(String[] args) throws  RemoteException, MalformedURLException {
        try {
            String serverIp = Utility.getIP(); 
            System.out.println("In Server Main");
            System.setProperty("java.security.policy","file:./policyfile");
            System.setProperty("java.rmi.server.hostname",serverIp);
            HelloImpl ob;
            Communicate communicate;
            new ServerUDPListen();
            ob = new HelloImpl();
            communicate = ob; 
            new RMIThread(communicate);
            // Trying to register
            DatagramSocket UDPSendSocket = new DatagramSocket(9877);
            byte[] buffer = new byte[1024];
            String register = "Register;RMI;"+serverIp+";9800;AmarRaj;5555";
            // RMI: [“Register;RMI;IP;Port;BindingName;Port(For RMI)”]
            buffer = register.getBytes();
            DatagramPacket Packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("128.101.35.147") , 5105);
            System.out.println ("Sending Register String to Registry Server");
            UDPSendSocket.send(Packet);
            DatagramSocket UDPGetSocket = new DatagramSocket(9600);
            while(true) {
                System.out.println("DeRegister - 1 \n GetList - 2 \n Enter Your Choice : ");
                Scanner reader = new Scanner(System.in);
                switch (reader.nextInt()){  
                    case 1:
                        break;
                    case 2:
                        String list;
                        list = GetList(UDPGetSocket);
                        //list = "23.34.45.2;dsfn;4554;128.101.35.180;AmarRaj;5555";
                        System.out.println(list);
                        String[] result = list.split("[;]",-1);
                        int num = result.length;
                        System.out.println("Number of other Servers "+Integer.toString(num/3));
                        String ip,port,bname;
                        int i = 0;
                        while(i<num){
                            ip = result[i];
                            bname = result[i+1];
                            port = result[i+2];
                            System.out.println("Joining Server "+ip+" "+port+" "+bname);
                            try {
                                float pport  = Float.parseFloat(port);
                                int p = (int)pport;
                                //System.out.println("1");
                                Registry registry = LocateRegistry.getRegistry(ip, p);
                                communicate = (Communicate) registry.lookup(bname);
                                //System.out.println("2");
                                boolean b = communicate.JoinServer(serverIp, 5555 );
                                if(b == true) {
                                    // Add this server to joined_servers 
                                    ob.joined_servers.add( ip+":"+port);
                                    System.out.println("Joined Server "+ip+" "+port+" "+bname+"\n");
                                }
                                } catch(Exception e) {
                                    System.out.println("Could not joinserver to "+ip+" "+port+" "+bname+"\n");
                                    //System.err.println("Client exception: " + e.toString());  
                                    //e.printStackTrace();  
                                }
                            //System.out.println("Trying next one..");
                            Thread.sleep(2000);
                            i=i+3;
                        }
                        break; 
                }
        }
        } catch (Exception e) {
             System.out.println(e.getMessage());
             System.out.println("Error in getting list or RPC to JoinServer.. ");
        }
        }
}

        
