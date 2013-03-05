/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.*;
import java.util.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;


public class HelloImpl extends UnicastRemoteObject implements Communicate {
    
    String check;
    DatagramSocket UDPSendSocket;
    private final int MAXCLIENTS = Utility.max_clients;
    private int current_client_count;
    private String[] ipport;
    HashMap<String, LinkedList<String>> subscription;
    LinkedList<String> joined_servers;
    LinkedList<String> joined_clients;
    
    public HelloImpl() throws RemoteException, SocketException {
        
        current_client_count = 0;
        //ipport = new String[MAXCLIENTS];
        subscription = new HashMap<String, LinkedList<String>>();
        joined_servers = new LinkedList<String>();
        joined_clients = new LinkedList<String>();
        UDPSendSocket = new DatagramSocket(9880);
    }
    
    public boolean Check() throws RemoteException, NotBoundException {
       
        Registry registry = LocateRegistry.getRegistry("192.168.1.29", 5555 );
        
        Communicate communicate = (Communicate) registry.lookup("AmarRaj");
        communicate.Ping();
        
        
        
        return true;
    }
    public String greeting() throws RemoteException {
        return "Hello !!";
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    
    public boolean Join(String ip, int port) throws RemoteException {
        if(current_client_count == MAXCLIENTS) {
            return false;
        }
        String t =ip + ":" +Integer.toString(port);
        // If client has already joined, do not add again
        if(joined_clients.contains(t)) {
            return true;
        }
        
        joined_clients.add(t);
        //ipport[current_client_count] = t;
        current_client_count++;
        System.out.println("Joined "+t+" to the Joined Clients List");
        return true;
    }
    
    public boolean Leave (String IP, int Port) throws RemoteException {
        
        
        String t = IP + ":" + Integer.toString(Port);
        if (joined_clients.contains(t)) {
            
            joined_clients.remove(t);
            // Iterate the Hash Map and remove client from each list
            Set set = subscription.entrySet();
            Iterator i = set.iterator(); 
            while(i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
                LinkedList<String> tt = (LinkedList<String>)me.getValue();
                tt.remove(t);
            } 
            System.out.println("Removed "+ t);
            return true;
        }
        System.out.println(t + " not joined attempted to leave..");
        return true;
    }
    
    public boolean LeaveServer (String IP, int Port) throws RemoteException {
        /*if(joined_servers.contains(IP+Integer.toString(Port)))  {
            joined_servers.remove(IP+Integer.toString(Port));
            System.out.println("Removed Server " + joined_servers.remove(IP+Integer.toString(Port)); );
        }*/
        
        
        return true;
    }
    
    public boolean Ping() throws RemoteException {
        
        return true;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////
    public boolean Unsubscribe(String IP, int Port, String Article) throws RemoteException {
        if (!(joined_clients.contains(IP+":"+Integer.toString(Port)))) {
            return true;
        }
        String[] result = Article.split("[;]", -1);
        if (result.length != 4) {
            return false;
        }
        if (!(result[3].equals(""))) {
            return false;
        }
        
        if (subscription.containsKey(Article) ) {
            subscription.get(Article).remove(IP+":"+Integer.toString(Port));
            return true;
        }
        else {
            return true;
        }
        
    }
    
    public boolean Subscribe(String IP, int Port, String Article) throws RemoteException {
        
        if (!(joined_clients.contains(IP+":"+Integer.toString(Port)))) {
            return false;
        }
        String[] result = Article.split("[;]", -1);
        if (result.length != 4) {
            return false;
        }
        if (!(result[3].equals(""))) {
            return false;
        }
        if (subscription.containsKey(Article) ) {
            subscription.get(Article).add(IP+":"+Integer.toString(Port));
        }
        else {
            LinkedList l = new LinkedList<String>();
            l.add(IP+":"+Integer.toString(Port));
            subscription.put(Article, l);
        }
        System.out.println("Subscribed "+IP+":"+Integer.toString(Port) +" to " + Article);
        return true; 
    }
//////////////////////////////////////////////////////////////////////////////////////////////////
    
    boolean publish2ownclients(String Article, String IP, int port) {
        
        String[] result = Article.split("[;]", -1);
        String key = result[0] + ";" + result[1] + ";" + result[2] + ";";
        //result[3] needs to be sent to clients
        if(subscription.containsKey(key)) {
            LinkedList<String> li = subscription.get(key);
            Iterator<String> iterator = li.iterator();
            while (iterator.hasNext()) {
                String t = iterator.next();
                
                if(t.equals(IP+":"+Integer.toString(port))) {
                    continue;
                }
                
		String[] client_inf = t.split("[:]", -1 );
                //UDP send (IP Port);
                try {
                    
                    byte[] buffer = new byte[1024];
          
                    buffer = result[3].getBytes();
                    DatagramPacket Packet = new DatagramPacket(buffer, buffer.length,InetAddress.getByName(client_inf[0]),Integer.parseInt(client_inf[1]));
                    
                    UDPSendSocket.send(Packet);
                    System.out.println("Sent Article to "+t);
                    } catch (Exception e) {
                        System.out.println("UDP Server Publish Exception");
                    }
            }
            //return true;
        }
        
        else {
            //return true;   
        }
        return true;
    }
    
     public boolean PublishServer (String Article, String IP, int Port) throws  RemoteException  {
        publish2ownclients(Article, IP, Port);
        return true;
    }

     String search_serverlist_forbname(String list, String ipp, int portt) {
         
         String[] result = list.split("[;]",-1);
                    int num = result.length;
                    String ip,port,bname;
                    int i = 0;
                    while(i<num){
                        ip = result[i];
                        bname = result[i+1];
                        port = result[i+2];
                        
                        if(ipp.equals(ip) && portt == Float.parseFloat(port)) {
                            return bname;
                        }
                        
                        i=i+3;
                    }
                    return "Null";
     }
     
    
    public boolean Publish (String Article, String IP, int Port) throws RemoteException {
        String[] res = Article.split("[;]", -1);
        if (res[3].equals("")) {
            return false;
        }
        publish2ownclients(Article, IP, Port);
        String list = "";
        byte[] buffer = new byte[1024];
        try {
        String getlist = "GetList;RMI;"+ Utility.getIP() +";9800";
        buffer = getlist.getBytes();
        DatagramPacket Packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("128.101.35.147") , 5105);
        System.out.println ("Sending GetList String to Registry Server");
        //DatagramSocket t = new DatagramSocket(9604);
        UDPSendSocket.send(Packet);
        buffer = new byte[1024];
        Packet = new DatagramPacket(buffer, buffer.length);
        System.out.println ("Waiting for GetList..");
        UDPSendSocket.receive(Packet);
        list = new String(Packet.getData()); 
        System.out.println(list);
        } catch (Exception e) {
            System.out.println("Error GetList in Publish");
            return true;
        }
        
        Iterator<String> it = joined_servers.iterator();
        while(it.hasNext()) {
            String[] server_inf = it.next().split("[:]",-1);
            System.out.println("1");
            Registry registry = LocateRegistry.getRegistry( server_inf[0], Integer.parseInt(server_inf[1]));
            System.out.println("1");
            try {
                String bname  = search_serverlist_forbname(list, server_inf[0], Integer.parseInt(server_inf[1]));
                System.out.println("Binding Name = " + bname);
                if (bname.equals("Null")) {
                    continue;
                }
                Communicate communicate = (Communicate) registry.lookup(bname);
                communicate.PublishServer(Article, "dummy", 45);
            } catch(Exception e){
                System.out.println("One of the joined servers not responding to publish server..");
            }
            
        }
        return true;
        
    }
    
    public boolean JoinServer(String ip, int port) throws RemoteException{
        
        if (current_client_count >= MAXCLIENTS) {
            return false;
        }
        if(joined_servers.contains(ip+":"+Integer.toString(port))) {
            //System.out.println("Added " + ip+":"+Integer.toString(port) +" to my joined server list.");
            return false;
        } 
        joined_servers.add(ip+":"+Integer.toString(port));
        System.out.println("Added " + ip+":"+Integer.toString(port) +" to my joined server list.");
        return true;
    }
    
    
}
