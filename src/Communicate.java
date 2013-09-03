import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.*;
public interface Communicate extends Remote {
        boolean Check() throws RemoteException, NotBoundException;
        boolean JoinServer (String IP, int Port) throws RemoteException;
	boolean LeaveServer (String IP, int Port) throws RemoteException;
	boolean Join (String IP, int Port) throws RemoteException;
	boolean Leave (String IP, int Port) throws RemoteException;
	boolean Subscribe(String IP, int Port, String Article) throws RemoteException;
	boolean Unsubscribe (String IP, int Port, String Article) throws RemoteException;
	boolean Publish (String Article, String IP, int Port) throws RemoteException;
	boolean Ping () throws RemoteException;
        boolean PublishServer (String Article, String IP, int Port) throws RemoteException;
}
