import java.net.*;
import java.util.*;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.MalformedURLException; 
public class RMIClient {
    public static void main(String[] args) throws IOException, UnknownHostException, RemoteException, MalformedURLException, NotBoundException {
        //The client should know the location of its group server (IP, port, any RPC/RMI names/interfaces) when it starts up.
        System.out.println("Enter Group Server IP :");
        BufferedReader r;
        r = new BufferedReader(new InputStreamReader(System.in));
        String group_server_ip = r.readLine();
        int group_server_registry_port = 5555;
        String group_server_name = "AmarRaj";
        new ClientUDPListenThread();
        Registry registry = LocateRegistry.getRegistry(group_server_ip, group_server_registry_port);
        Communicate communicate = (Communicate) registry.lookup(group_server_name);
        String myip = Utility.getIP();
        // Menu for CLIENT
        while(true) {
            System.out.println("\n\nJoin Group Server - 1\nSubscribe - 2 \nUnsubscribe - 3\nPing - 4\nPublish - 5\nLeave - 6\nEnter Your Choice : ");
            Scanner reader = new Scanner(System.in);
            switch (reader.nextInt()){  
                case 1:
                    System.out.println("Joining Group..");
                    try {
                        boolean succ = communicate.Join(myip, Utility.client_listen_port);
                        if(succ == true) {
                            System.out.println("Successfully Joined Group Server");
                        } else {
                            System.out.println("Sorry, Client Limit already reached..");
                        }
                    } catch (Exception e) {
                        System.out.println("Join Error !!");
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter Article : ");
                        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
                        String ar = in.readLine();
                        boolean b = communicate.Subscribe(myip, Utility.client_listen_port, ar);
                        if (b == true) {
                            System.out.println("Successfully Subscribed");
                        } else {
                            System.out.println("Unsuccessfull .. ");
                        }
                    } catch (Exception e) {
                        System.out.println("Subscribe Error !!");
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter Article to unsubscribe : ");
                        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
                        String ar = in.readLine();
                        boolean b = communicate.Unsubscribe(myip, Utility.client_listen_port, ar);
                        if (b == true) {
                            System.out.println("Successfully Unsubscribed");
                        } else {
                            System.out.println("Unsuccessfull.. ");
                        }
                    } catch (Exception e) {
                        System.out.println("Unsubscribe Error !!");
                    }
                    break;
                case 4:
                    try {
                        boolean b = communicate.Ping();
                        if (b == true) {
                            System.out.println("Group Server Alive !!");
                        } else {
                            System.out.println("Group Server not responding..");
                        }
                    } catch (Exception e) {
                        System.out.println("Ping not responded by group server !!");
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter Article to Publish : ");
                        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
                        String ar = in.readLine();
                        boolean b = communicate.Publish(ar, myip, Utility.client_listen_port);
                        if (b == true) {
                            System.out.println("Successfully Published");
                        } else {
                            System.out.println("Unsuccessfull..");
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 6:
                    System.out.println("Leaving Group..");
                    try {
                        boolean succ = communicate.Leave(myip, Utility.client_listen_port);
                        if(succ == true) {
                            System.out.println("Successfully Left Group Server");
                        } else {
                            System.out.println("Unsuccessfull");
                        }
                    } catch (Exception e) {
                        System.out.println("Leave Error !!");
                    }
                    break;
                case 7:
                    boolean b = communicate.Check();
                    break;
            }
        } 
}
}
