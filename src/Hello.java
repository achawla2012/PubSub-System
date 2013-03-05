/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Hello extends Remote{
    String greeting() throws RemoteException;
    boolean Join(String ip, int port) throws RemoteException;
    boolean JoinServer(String ip, int port) throws RemoteException;
    boolean Ping() throws RemoteException;
    boolean Subscribe(String IP, int Port, String Article) throws RemoteException;
    boolean Unsubscribe(String IP, int Port, String Article) throws RemoteException ;
    boolean Publish (String Article, String IP, int Port) throws RemoteException;
    
}
