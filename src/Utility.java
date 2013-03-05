/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.net.*;

public class Utility {
    
    
    public static int client_listen_port = 9878; 
    public static int max_clients = 10;
    public static int RMIRegistryPort = 5555;
    
    public static String getIP() throws SocketException{
        
        try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
			
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();

					//System.out.println(inetAddress.getHostAddress().toString());
									
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress())// && inetAddress.isSiteLocalAddress())
					{
						//System.out.println("return");
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			System.out.println(ex.getMessage());
			return null;
		}

		return null;
    }
}